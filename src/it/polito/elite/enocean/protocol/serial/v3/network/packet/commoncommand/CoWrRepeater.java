/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Write repeater level OFF, 1, 2;
 */
public class CoWrRepeater extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional;
	/*
	 * The byte vector dataValue contains the command code
	 */
	private static byte[] dataValue;
	public CoWrRepeater(byte repEnable, byte repLevel){
		super(3,0,(byte) 0x05,dataValue,optional);
		dataValue[0] = 0x09;
		/*
		 * repEnable: OFF=0 ON=1
		 */
		dataValue[1] = repEnable;
		/*
		 * When repeater OFF must be 0, when ON then 1 for Level-1 , 2 for Level-2
		 */
		dataValue[2] = repLevel;
	}
}