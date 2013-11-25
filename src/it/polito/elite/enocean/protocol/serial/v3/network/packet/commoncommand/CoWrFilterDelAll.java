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
public class CoWrFilterDelAll extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue contains:
	 */
	private static byte[] dataValue;

	public CoWrFilterDelAll() {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x0D;
	}
}