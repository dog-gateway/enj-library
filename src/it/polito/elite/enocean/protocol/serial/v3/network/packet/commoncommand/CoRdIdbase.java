/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Read ID range base number
 */
public class CoRdIdbase extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue contains the command code
	 */
	private static byte[] dataValue;

	public CoRdIdbase() {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x08;
	}
}