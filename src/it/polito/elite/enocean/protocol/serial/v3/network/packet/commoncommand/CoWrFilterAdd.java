/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Add filter to fileter list
 */
public class CoWrFilterAdd extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional;
	/*
	 * The byte vector dataValue contains:
	 */
	private static byte[] dataValue;
	public CoWrFilterAdd(byte filterType, int filterValue, byte filterKind){
		super(7,0,(byte) 0x05,dataValue,optional);
		dataValue[0] = 0x0B;
		/*
		 * Filter type: Device ID = 0, R-ORG  = 1, dBm = 2
		 */
		dataValue[1] = filterType;
		/*
		 * Value of filter function
		 */
		dataValue[2] =(byte) (filterValue & 0xff);
		dataValue[3] =(byte) ((filterValue & 0xff00)>>8);
		dataValue[4] =(byte) ((filterValue & 0xff0000)>>16);
		dataValue[5] =(byte) ((filterValue & 0xff000000)>>32);
		/*
		 * Filter kind bloks = 0x00
		 * Filter kind bloks = 0x80
		 */
		dataValue[6] = filterKind;
	}
}