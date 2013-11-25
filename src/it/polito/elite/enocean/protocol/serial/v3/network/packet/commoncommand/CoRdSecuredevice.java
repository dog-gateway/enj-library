package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Read secure device
 */
public class CoRdSecuredevice extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoRdSecuredevice(byte index) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x1B;
		dataValue[1] = index;
	}
}