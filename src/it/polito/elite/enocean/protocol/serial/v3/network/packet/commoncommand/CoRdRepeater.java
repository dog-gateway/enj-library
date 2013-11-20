/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Read repeater level OFF, 1, 2
 */
public class CoRdRepeater extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue contains the command code
	 */
	private static byte[] dataValue;
	public CoRdRepeater(){			
		super((byte) 0x05,dataValue,optional);
		dataValue[0] = 0x0A;
	}
}