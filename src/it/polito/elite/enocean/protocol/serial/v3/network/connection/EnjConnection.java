/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.connection;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import it.polito.elite.enocean.enj.EEP2_5.receiveEvent.PacketEventSender;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.ElementQueue;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.SerialListener;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.SerialPortFactory;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.ThreadWrite;
import gnu.io.SerialPort;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class EnjConnection {

	SerialPort serialPort;

	//Code ad alta priorita
	ConcurrentLinkedQueue<Packet> highPriorityTxQueue = new ConcurrentLinkedQueue<Packet>();
	ConcurrentLinkedQueue<Packet> highPriorityRxQueue = new ConcurrentLinkedQueue<Packet>();

	//Code a bassa priorita
	ConcurrentLinkedQueue<ElementQueue> lowPriorityTxQueue = new ConcurrentLinkedQueue<ElementQueue>();
	ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue = new ConcurrentLinkedQueue<ElementQueue>();

	Semaphore expectedResponse = new Semaphore(1);

	ThreadWrite threadWrite;


	public PacketEventSender packetSender = new PacketEventSender(this.lowPriorityRxQueue);

	/**
	 * 
	 */
	public EnjConnection() {
		super();
	}

	public void startConnection() throws Exception{

		serialPort = (new SerialPortFactory()).getPort("/dev/ttyAMA0", 1000);

		//(new HighPriorityThread( highPriorityTxQueue, highPriorityRxQueue, expectedResponse)).run();

		SerialListener serialListener = new SerialListener(serialPort, highPriorityRxQueue, lowPriorityRxQueue, expectedResponse);
		serialPort.addEventListener(serialListener);
		serialPort.notifyOnDataAvailable(true);
		
		//PacketEventSender packetSender = new PacketEventSender(this.lowPriorityRxQueue);
		packetSender.start();

		threadWrite = new ThreadWrite(highPriorityTxQueue, lowPriorityTxQueue, serialPort, expectedResponse);
		threadWrite.start();

	}
	
	

	public void send(Packet pkt){
		//System.out.println("Sono in send packet");
		lowPriorityTxQueue.add( new ElementQueue(pkt,3));
	}

	/*
	 * Non serve perchè c'è il listener
	 */
	public Packet receive(){
//		return this.lowPriorityRxQueue.peek().getPkt();
		return this.lowPriorityRxQueue.poll().getPkt();
	}
}