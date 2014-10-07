/**
 * 
 */
package it.polito.elite.enocean.test;

import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import gnu.io.SerialPort;
import it.polito.elite.enocean.enj.EEP26.EEPIdentifier;
import it.polito.elite.enocean.enj.EEP26.Rorg;
import it.polito.elite.enocean.enj.application.devices.EnJPersistentDeviceSet;
import it.polito.elite.enocean.enj.link.PacketQueueItem;
import it.polito.elite.enocean.enj.link.PacketReceiver;
import it.polito.elite.enocean.enj.link.PacketTransmitter;
import it.polito.elite.enocean.enj.link.serial.SerialPortFactory;
import it.polito.elite.enocean.enj.model.EnOceanDevice;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand.CoRdVersion;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class TestingMainClass {

	/**
	 * @param args
	 * @return 
	 */
	public static void main(String[] args) {
/*
		try {
			System.out.println("Sono nel main prova....");		


			SerialPort serialPort = (new SerialPortFactory()).getPort("/dev/ttyAMA0", 1000);


			//Code ad alta priorita
			ConcurrentLinkedQueue<ESP3Packet> highPriorityTxQueue = new ConcurrentLinkedQueue<ESP3Packet>();
			ConcurrentLinkedQueue<ESP3Packet> highPriorityRxQueue = new ConcurrentLinkedQueue<ESP3Packet>();

			//Code a bassa priorita
			ConcurrentLinkedQueue<PacketQueueItem> lowPriorityTxQueue = new ConcurrentLinkedQueue<PacketQueueItem>();
			ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue = new ConcurrentLinkedQueue<PacketQueueItem>();

			//Invio il comando CO_RD_VERSION
			Semaphore expectedResponse = new Semaphore(1);
			CoRdVersion cmd = new CoRdVersion();
			lowPriorityTxQueue.add( new PacketQueueItem(cmd,3));

			PacketReceiver serialListener = new PacketReceiver(serialPort, highPriorityRxQueue, lowPriorityRxQueue, expectedResponse);

			serialPort.addEventListener(serialListener);

			serialPort.notifyOnDataAvailable(true);
			//			(new HighPriorityThread( highPriorityTxQueue, highPriorityRxQueue, expectedResponse)).run();
			PacketTransmitter threadwrite = new PacketTransmitter(highPriorityTxQueue, lowPriorityTxQueue, serialPort, expectedResponse);
			threadwrite.run();

			//(new ThreadWrite(highPriorityTxQueue, lowPriorityTxQueue, serialPort, expectedResponse)).run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		byte[] address = new byte[4];
		address[0] = (byte) 0xff;
		address[1] = (byte) 0xff;
		address[2] = (byte) 0xff;
		address[3] = (byte) 0xff;

		byte[] manId = new byte[3];
		manId[2] = (byte) 0x11;
		manId[1] = (byte) 0x11;	
		manId[0] = (byte) 0x11;

		Device device = new Device(new EnoceanEquipmentProfile(new Rorg((byte)0xD2), (byte)0x01, (byte)0x02), address, manId);
		EnjDevices devices = new EnjDevices();
		devices.add(device);
		devices.save();

		EnjDevices devices2 = new EnjDevices();
		devices2.load();

		System.out.println("RORG id:" + String.format("%x", devices2.peekDevice().getEEP().getRorg().getRorgValue()));
		System.out.println("Funcion:" + String.format("%x", devices.peekDevice().getEEP().getFunction()));
		System.out.println("Type:" + String.format("%x", devices.peekDevice().getEEP().getType()));
		System.out.print("Address: " + String.format("%x", devices.peekDevice().getAddress()[3]));
		System.out.print("" + String.format("%x", devices.peekDevice().getAddress()[2]));
		System.out.print("" + String.format("%x", devices.peekDevice().getAddress()[1]));
		System.out.println("" + String.format("%x", devices.peekDevice().getAddress()[0]));
		System.out.print("ManufacturerId: " + String.format("%x", devices.peekDevice().getManufacturerId()[1] ));
		System.out.println("" + String.format("%x", devices.peekDevice().getManufacturerId()[0]));
		 */
	}
}