package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Reset the system log from device databank
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrSysLog extends Packet {
	public CoWrSysLog() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x01;
		this.buildPacket();
	}
}