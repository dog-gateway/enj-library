package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Read security information (level, keys). This function does not support the actual security concept and should not be used any more
 */
public class CoRdSecurity extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;
	public CoRdSecurity(){	
		super((byte)0x05, dataValue, optional);
		dataValue[0] = 0x15;
	}
}