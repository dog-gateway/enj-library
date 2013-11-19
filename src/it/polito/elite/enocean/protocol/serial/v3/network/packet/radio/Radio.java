package it.polito.elite.enocean.protocol.serial.v3.network.packet.radio;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Packet type 1 : Radio
 * 
 * @author andreabiasi
 *
 */
public class Radio extends Packet{
	// Optional data
	private static byte[] optional;
	public Radio(byte data[], byte subTelNum, int destinationId , byte dBm, byte securityLevel){
		super(data.length, 7, (byte)0x01, data, optional);
		optional[0] = subTelNum;
		optional[1] = (byte) (destinationId & 0xff);
		optional[2] = (byte) ((destinationId & 0xff00)>>8);
		optional[3] = (byte) ((destinationId & 0xff0000)>>16);
		optional[4] = (byte) ((destinationId & 0xff000000)>>32);
		optional[5] = dBm;
		optional[6] = securityLevel;
	}
}