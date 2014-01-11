package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Read ID range base number
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class CoRdIdbase extends Packet {
	public CoRdIdbase() {
		super();
		this.packetType = COMMON_COMMAND;
		// Event code
		this.data[0] = 0x08;
		this.buildPacket();
	}
}