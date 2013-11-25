package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Sets the gateway transceiver mode
 */
public class CoWrMode extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoWrMode(byte mode) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x1C;
		dataValue[1] = mode;
	}
}