/**
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Write security information (level, keys). This functions does not support the actual security concept should not be used any more
 */
public class CoWrSecurity extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoWrSecurity(byte secLevel, int key, int rollingCode) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x16;
		dataValue[1] = secLevel;
		dataValue[2] = (byte) (key & 0xff);
		dataValue[3] = (byte) ((key & 0xff00) >> 8);
		dataValue[4] = (byte) ((key & 0xff0000) >> 16);
		dataValue[5] = (byte) ((key & 0xff000000) >> 32);
		dataValue[6] = (byte) (rollingCode & 0xff);
		dataValue[7] = (byte) ((rollingCode & 0xff00) >> 8);
		dataValue[8] = (byte) ((rollingCode & 0xff0000) >> 16);
		dataValue[9] = (byte) ((rollingCode & 0xff000000) >> 32);
	}
}
