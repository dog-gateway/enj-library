package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Sets the gateway transceiver mode.
 * 
 * There are two modes available:
 * - Compatible mode - ERP1 - gateway uses Packet Type 1 to transmit and receive radio telegrams Ð for ASK products
 * - Advanced mode Ð ERP2 - gateway uses Packet Type 10 to transmit and receive radio telegrams Ð for FSK products with advanced protocol
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrMode extends Packet {
	/**
	 * @param mode : 0x00 Ð Compatible mode (default) - ERP1 
	 * 				 0x01 Ð Advanced mode - ERP2
	 */
	public CoWrMode(byte mode) {
		super();
		this.packetType = 0x05;
		this.data[0] = 0x1C;
		this.data[1] = mode;
		this.buildPacket();
	}
}