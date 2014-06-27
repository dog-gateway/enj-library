package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Perform flash BIST operation (Built-in-self-test)
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrBist extends ESP3Packet {
	public CoWrBist() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x06;
		this.buildPacket();
	}
}