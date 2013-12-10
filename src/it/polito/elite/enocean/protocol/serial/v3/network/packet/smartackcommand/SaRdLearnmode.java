package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Reads the learnmode state of Smart Ack Controller.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class SaRdLearnmode extends Packet{
	public SaRdLearnmode(){
		super();
		this.packetType = 0x06;
		//Smart ack code
		this.data[0] = 0x02; 
		this.buildPacket();
	}
}