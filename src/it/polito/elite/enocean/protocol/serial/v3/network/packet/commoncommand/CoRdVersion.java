package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Read the device SW version / HW version, chip-ID, etc.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdVersion extends Packet {
	public CoRdVersion() {
		super();
		// Command code
		this.data[0] = 0x03;
		this.buildPacket();
	}
}