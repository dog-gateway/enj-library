package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Read number of teached in secure devices
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdNumsecuredevice extends ESP3Packet {
	public CoRdNumsecuredevice() {
		super();
		this.packetType = COMMON_COMMAND;
		this.data[0] = 0x1D;
		this.buildPacket();
	}
}