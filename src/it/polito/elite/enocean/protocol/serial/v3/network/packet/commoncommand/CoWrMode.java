package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Sets the gateway transceiver mode.
 * 
 * There are two modes available:
 * - Compatible mode - ERP1 - gateway uses Packet Type 1 to transmit and receive radio telegrams � for ASK products
 * - Advanced mode � ERP2 - gateway uses Packet Type 10 to transmit and receive radio telegrams � for FSK products with advanced protocol
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrMode extends ESP3Packet {
	/**
	 * @param mode : 0x00 � Compatible mode (default) - ERP1 
	 * 				 0x01 � Advanced mode - ERP2
	 */
	public CoWrMode(byte mode) {
		super();
		this.packetType = COMMON_COMMAND;
		this.data[0] = 0x1C;
		this.data[1] = mode;
		this.buildPacket();
	}
}