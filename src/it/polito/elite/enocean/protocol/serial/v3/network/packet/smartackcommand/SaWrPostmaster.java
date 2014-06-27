package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Enables/Disables postmaster function of device
 * 
 * @author andreabiasi
 *
 */
public class SaWrPostmaster extends ESP3Packet{
	/**
	 * @param mailboxCount :Enables/Disables postmaster function of device.
	 */
	public SaWrPostmaster(byte mailboxCount){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x08;
		this.data[1] = mailboxCount;
		this.buildPacket();
	}
}