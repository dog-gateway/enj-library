package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Feedback about used addres and lenght of the area and the Smart-Ack table
 */
public class CoRdMemAddress extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;	
	public CoRdMemAddress(byte memoryArea){
		super((byte)0x05, dataValue, optional);
		dataValue[0] = 0x14;
		dataValue[1] = memoryArea;
	}
}