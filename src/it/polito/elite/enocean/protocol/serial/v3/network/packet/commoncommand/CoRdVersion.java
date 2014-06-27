package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * Read the device SW version / HW version, chip-ID, etc.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdVersion extends ESP3Packet {
	public CoRdVersion() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data = new byte[1];
		this.optData = new byte[0];
		this.data[0] = 0x03;
		this.buildPacket();
	}
}