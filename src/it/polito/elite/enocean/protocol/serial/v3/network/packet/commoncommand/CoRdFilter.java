package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Read supplies filter
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdFilter extends Packet {
	public CoRdFilter() {
		super();
		this.packetType = 0x05;
		// Command code
		this.data[0] = 0x0F;
		this.buildPacket();
	}
}