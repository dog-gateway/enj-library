package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Order to reset the device
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrReset extends Packet {
	public CoWrReset() {
		super();
		// Command code
		this.data[0] = 0x02;
		this.buildPacket();
	}
}