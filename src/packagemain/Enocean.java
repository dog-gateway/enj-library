/**
 * 
 */
package packagemain;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.polito.elite.enocean.enj.EEP2_5.D2.D201.D20108;
import it.polito.elite.enocean.enj.EEP2_5.communication.EnjCommunicator;
import it.polito.elite.enocean.enj.EEP2_5.primitives.Device;
import it.polito.elite.enocean.enj.knowndevices.EnjDevices;
import it.polito.elite.enocean.protocol.serial.v3.network.link.EnJLink;
import it.polito.elite.enocean.protocol.serial.v3.network.link.PacketQueueItem;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.radio.Radio;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.radiomessage.RadioMessage;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Enocean {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String lettura = null;
		EnjDevices devices = new EnjDevices();

		// Reload devices
		   devices.load(); // C'e exception perch� non c'� una minchia da caricare
		// end reload

		
		System.out.println("Select operation:");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			lettura = in.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue = new ConcurrentLinkedQueue<PacketQueueItem>();
		EnJLink connection = new EnJLink(lowPriorityRxQueue);
		EnjCommunicator communicator = new EnjCommunicator(connection,lowPriorityRxQueue);
		
		try {
			connection.startConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lettura = "command";
		//int size = lowPriorityRxQueue.size();
		if(lettura.equals("teach")){
			Device newDevice = communicator.teach();
			devices.add(newDevice);
			devices.save();
		}
		else{
			if(lettura.equals("command")){
				
				D20108 plug = new D20108();
				byte[] address = devices.peekDevice().getAddress();
				byte[] payloadToSend = new byte[1 + plug.actuatorSetOutput(D20108.ON).length + 4 + 1];
				
				// RORG
				//payloadToSend[0] = (byte)0xA6;
				
				// RORG
				payloadToSend[0] = (byte)0xD2;
				
				System.out.println("Type of command (on or off):");
				BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));

				try {
					lettura = in1.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(lettura.equals("on")){
				//Command ON
				payloadToSend[1] = plug.actuatorSetOutput(D20108.ON)[0];
				payloadToSend[2] = plug.actuatorSetOutput(D20108.ON)[1];
				payloadToSend[3] = plug.actuatorSetOutput(D20108.ON)[2];
				}
				else{
					//Command OFF
					payloadToSend[1] = plug.actuatorSetOutput(D20108.OFF)[0];
					payloadToSend[2] = plug.actuatorSetOutput(D20108.OFF)[1];
					payloadToSend[3] = plug.actuatorSetOutput(D20108.OFF)[2];
				}
				
				
				//Address sender ( IO )
				payloadToSend[4] = (byte)0xFF;
				payloadToSend[5] = (byte)0x00;
				payloadToSend[6] = (byte)0xFF;
				payloadToSend[7] = (byte)0xFF;
				
				//Status
				payloadToSend[8] = (byte)0x00;

				
				//							  byte data[],   byte subTelNum,byte destinationId , byte dBm, byte securityLevel
				Radio radioToSend = new Radio(payloadToSend, (byte)0x03, address, (byte)0xFF, (byte)0x00);

											   				
				connection.send(radioToSend);
				
				//communicator.sendCommand();
				/*
				 * 
				 * COMMUNICATION
				 * 
				 */
			}
		}
		//Alla fine salva i dispositivi --> devices.save
	}
}