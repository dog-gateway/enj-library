package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * Delete all filter from filter list
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrFilterDelAll extends ESP3Packet {
	public CoWrFilterDelAll() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x0D;
		this.buildPacket();
	}
}