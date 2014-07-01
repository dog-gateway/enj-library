/**
 * 
 */
package it.polito.elite.enocean.enj.EEP2_5.receiveEvent;


import it.polito.elite.enocean.enj.link.EnJLink;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class PacketReceiverListener implements ReceivedDataEventListener{

	ESP3Packet pkt;
	EnJLink connection;
	boolean received = false;
	
	public PacketReceiverListener(EnJLink connection){
		this.connection = connection;
		received=false;
	}
	
	public void handleReceiveData(ReceiveDataEvent event) {
		System.out.println("Sono in handleReceiveData");
		pkt = this.connection.receive();
		received = true;
	}
	
	public ESP3Packet getPacket(){
		while(!received){
			//System.out.println("Non ho ancora ricevuto");
		}
		return pkt;
	}
	
}
