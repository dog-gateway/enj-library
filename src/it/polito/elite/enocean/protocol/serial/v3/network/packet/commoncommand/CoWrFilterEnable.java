/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Enable/Disable all supplied filters
 */
public class CoWrFilterEnable extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional;
	/*
	 * The byte vector dataValue contains:
	 */
	private static byte[] dataValue;
	public CoWrFilterEnable(byte filterOnoff, byte filterOperator){
		super(3,0,(byte) 0x05,dataValue,optional);
		dataValue[0] = 0x0E;
		dataValue[1] = filterOnoff;
		dataValue[2] = filterOperator;
	}
}