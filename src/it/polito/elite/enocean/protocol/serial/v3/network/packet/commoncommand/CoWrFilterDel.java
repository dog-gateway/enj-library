/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Delete filter from filter list
 */
public class CoWrFilterDel extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue contains:
	 */
	private static byte[] dataValue;

	public CoWrFilterDel(byte filterType, int filterValue) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x0C;
		dataValue[1] = filterType;
		dataValue[2] = (byte) (filterValue & 0xff);
		dataValue[3] = (byte) ((filterValue & 0xff00) >> 8);
		dataValue[4] = (byte) ((filterValue & 0xff0000) >> 16);
		dataValue[5] = (byte) ((filterValue & 0xff000000) >> 32);
	}
}