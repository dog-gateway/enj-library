/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Code 5 : Send reset command to a Smart Ack Client
 * 
 * @author andreabiasi
 *
 */
public class SaWrReset extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public SaWrReset(int deviceId){
		super(1, 0, (byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x05;
		dataValue[1] = (byte) (deviceId & 0xff);
		dataValue[2] = (byte) ((deviceId & 0xff00)>>8);
		dataValue[3] = (byte) ((deviceId & 0xff0000)>>16);
		dataValue[4] = (byte) ((deviceId & 0xff000000)>>32);
	}
}
