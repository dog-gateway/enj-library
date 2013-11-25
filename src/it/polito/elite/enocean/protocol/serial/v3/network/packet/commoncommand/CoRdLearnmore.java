package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Reads the learnmode state of controller
 */
public class CoRdLearnmore extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoRdLearnmore() {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x18;
	}
}