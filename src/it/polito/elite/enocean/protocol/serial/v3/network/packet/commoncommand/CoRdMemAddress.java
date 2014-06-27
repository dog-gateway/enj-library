package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Feedback about used addres and lenght of the area and the Smart-Ack table
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdMemAddress extends ESP3Packet {
	/**
	 * @param memoryArea : 	Config area = 0 
	 * 						Smart Ack Table = 1 
	 * 						System error log = 2
	 */
	public CoRdMemAddress(byte memoryArea) {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x14;
		this.data[1] = memoryArea;
		this.buildPacket();
	}
}