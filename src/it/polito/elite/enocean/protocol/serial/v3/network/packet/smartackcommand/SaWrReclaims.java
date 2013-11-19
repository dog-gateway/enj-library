package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Set the amount of reclaim tries in Smart Ack Client
 * 
 * @author andreabiasi
 *
 */
public class SaWrReclaims extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public SaWrReclaims(byte reclaimCount){
		super(2, 0, (byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x07;
		dataValue[1] = reclaimCount;
	}
}