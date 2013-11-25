package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Add secure device to coltroller. It is possible to add ony one or more rocker with this function
 */
public class CoWrSecureserviceAdd extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoWrSecureserviceAdd(byte slf, int id, byte[] privateKey,
			int rollingCode) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x19;
		dataValue[1] = slf;

		dataValue[2] = (byte) (id & 0xff);
		dataValue[3] = (byte) ((id & 0xff00) >> 8);
		dataValue[4] = (byte) ((id & 0xff0000) >> 16);
		dataValue[5] = (byte) ((id & 0xff000000) >> 32);

		for (int i = 0; i < privateKey.length; i++) {
			dataValue[6 + i] = privateKey[i];
		}
		dataValue[6 + privateKey.length] = (byte) (rollingCode & 0xff00);
		dataValue[6 + privateKey.length + 1] = (byte) ((rollingCode & 0xff00) >> 8);
		dataValue[6 + privateKey.length + 2] = (byte) ((rollingCode & 0xff0000) >> 32);
	}

}