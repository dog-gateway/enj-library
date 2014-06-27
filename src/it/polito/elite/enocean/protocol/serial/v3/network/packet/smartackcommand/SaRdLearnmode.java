package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * Reads the learnmode state of Smart Ack Controller.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class SaRdLearnmode extends ESP3Packet{
	public SaRdLearnmode(){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x02; 
		this.buildPacket();
	}
}