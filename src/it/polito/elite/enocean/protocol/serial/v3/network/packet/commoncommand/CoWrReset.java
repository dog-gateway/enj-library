package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * Order to reset the device
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrReset extends ESP3Packet {
	public CoWrReset() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x02;
		this.buildPacket();
	}
}