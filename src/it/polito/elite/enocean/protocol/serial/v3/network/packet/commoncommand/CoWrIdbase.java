package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Write ID range base number
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrIdbase extends ESP3Packet {
	public CoWrIdbase(int baseId) {
		super();
		this.packetType = COMMON_COMMAND;
		this.data[0] = 0x07;
		this.data[1] = (byte) (baseId & 0xff00);
		this.data[2] = (byte) ((baseId & 0xff00) >> 8);
		this.data[3] = (byte) ((baseId & 0xff0000) >> 16);
		this.data[4] = (byte) 0xff;
		this.buildPacket();
	}
}