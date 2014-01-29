/**
 * 
 */
package testingMain;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand.CoRdVersion;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.*;
import gnu.io.SerialPort;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class TestingMainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
        {
			System.out.println("Sono nel main prova modifiche....");		
			SerialPort serialPort = (new SerialPortFactory()).getPort("/dev/ttyAMA0", 1000);
					
			//Code ad alta priorita
			ConcurrentLinkedQueue<Packet> highPriorityTxQueue = new ConcurrentLinkedQueue<Packet>();
			ConcurrentLinkedQueue<Packet> highPriorityRxQueue = new ConcurrentLinkedQueue<Packet>();
			
			//Code a bassa priorita
			ConcurrentLinkedQueue<ElementQueue> lowPriorityTxQueue = new ConcurrentLinkedQueue<ElementQueue>();
			ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue = new ConcurrentLinkedQueue<ElementQueue>();

			//Invio il comando CO_RD_VERSION
			Semaphore expectedResponse = new Semaphore(1);
			CoRdVersion cmd = new CoRdVersion();
			lowPriorityTxQueue.add( new ElementQueue(cmd,3));
			
			//SerialListener serialListener = new SerialListener(serialPort, highPriorityRxQueue, lowPriorityRxQueue, expectedResponse);
			//serialPort.addEventListener(serialListener);
			
			serialPort.addEventListener(new SerialListener(serialPort, highPriorityRxQueue, lowPriorityRxQueue, expectedResponse));
			serialPort.notifyOnDataAvailable(true);
			
//			(new HighPriorityThread( highPriorityTxQueue, highPriorityRxQueue, expectedResponse)).run();
			
			
			(new ThreadWrite(highPriorityTxQueue, lowPriorityTxQueue, serialPort, expectedResponse)).run();
			
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}