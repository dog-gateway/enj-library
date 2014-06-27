package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Enable/Disable transmitting additional subtelegram info.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrSubtel extends ESP3Packet {
	/**
	 * @param enable : Enable = 1 Disable = 0
	 */
	public CoWrSubtel(byte enable) {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x11;
		this.data[1] = enable;
		this.buildPacket();
	}
}