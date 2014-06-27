package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Read mailbox information at the postmaster device, about all learned Smart Ack clients
 * 
 * @author andreabiasi
 *
 */
public class SaRdLearnedclients extends ESP3Packet{
	public SaRdLearnedclients(){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x06;
		this.buildPacket();
	}
}
