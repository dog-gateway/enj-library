/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Waiting till end of maturity time before received radio telegrams will transit
 */
public class CoWrWaitMaturity extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoWrWaitMaturity(byte waitEndMaturity) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x10;
		dataValue[1] = waitEndMaturity;
	}
}