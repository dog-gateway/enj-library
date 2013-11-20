package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiosubtel;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Packet type 3 : Radio Sub Tel
 * 
 * @author andreabiasi
 *
 */
public class RadioSubTel extends Packet{

	// Optional data
	private static byte[] optional;

	public RadioSubTel(byte data[], byte subTelNum, int destinationId, byte dBm, byte securityLevel, byte timeStamp, byte tickSubTel, byte dBmSubTel, byte statusSubTel){
		super((byte)0x03, data, optional);
		optional[0] = subTelNum;
		optional[1] = (byte) (destinationId & 0xff);
		optional[2] = (byte) ((destinationId & 0xff00)>>8);
		optional[3] = (byte) ((destinationId & 0xff0000)>>16);
		optional[4] = (byte) ((destinationId & 0xff000000)>>32);
		optional[5] = dBm;
		optional[6] = securityLevel;
		optional[7] = (byte) (timeStamp & 0xff);
		optional[8] = (byte) ((timeStamp & 0xff00)>>8);	
		optional[9] = tickSubTel;
		optional[10] = dBmSubTel;
		optional[11] = statusSubTel;
	}
}