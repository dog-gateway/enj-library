package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Read supplies filter
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdFilter extends ESP3Packet {
	public CoRdFilter() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x0F;
		this.buildPacket();
	}
}