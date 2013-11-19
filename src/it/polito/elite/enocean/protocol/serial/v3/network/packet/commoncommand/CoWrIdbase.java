/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Write ID range base number
 */
public class CoWrIdbase extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional;
	/*
	 * The byte vector dataValue contains an integer number in range between 0xFF800000 and 0xFFFFFF80
	 */
	private static byte[] dataValue;
	public CoWrIdbase(int baseId){
		super(5,0,(byte) 0x05,dataValue,optional);
		dataValue[0] = 0x07;
		dataValue[1] = (byte) (baseId & 0xff00);
		dataValue[2] = (byte) ((baseId & 0xff00)>>8);
		dataValue[3] = (byte) ((baseId & 0xff0000)>>16);
		dataValue[4] = (byte) 0xff;
	}
}