package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Perform flash BIST operation (Built-in-self-test)
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrBist extends Packet {
	public CoWrBist() {
		super();
		// Command code
		this.data[0] = 0x06;
		this.buildPacket();
	}
}