package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Read repeater level OFF, 1, 2
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdRepeater extends ESP3Packet {
	public CoRdRepeater() {
		super();
		this.packetType = COMMON_COMMAND;
		// Event type
		this.data[0] = 0x0A;
		this.buildPacket();
	}
}