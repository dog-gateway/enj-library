package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Reads the learnmode state of controller
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdLearnmore extends Packet {
	public CoRdLearnmore() {
		super();
		this.packetType = 0x05;
		// Command code
		this.data[0] = 0x18;
		this.buildPacket();
	}
}