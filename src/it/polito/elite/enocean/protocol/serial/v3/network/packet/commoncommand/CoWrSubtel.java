/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoWrSubtel extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;
	public CoWrSubtel(byte enable){
		super(2, 0, (byte)0x05, dataValue, optional);
		dataValue[0] = 0x11;
		dataValue[1] = enable;
	}
}