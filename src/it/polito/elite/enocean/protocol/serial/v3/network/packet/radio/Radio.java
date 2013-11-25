package it.polito.elite.enocean.protocol.serial.v3.network.packet.radio;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Radio extends Packet{
	/**
	 * Optional data: Byte vector which contain all optional data passed with the constructor
	 */
	private static byte[] optional;
	
	/**
	 * Constructor to initialize data and optional data in the radio packet
	 * 
	 * @param data : data to sent, in a byte vector
	 * @param subTelNum : number of subtelegram; Send: 3 / receive: 1 ... y
	 * @param destinationId : Broadcast radio: FF FF FF FF 
	 * 						  ADT radio: Destination ID (=address)
	 * @param dBm : Send case: FF
	 * 				Receive case: best RSSI value of all received subtelegrams (value decimal without minus)
	 * @param securityLevel : 0 = telegram unencrypted
	 * 						  n = type of encryption (not supported any more)
	 */
	public Radio(byte data[], byte subTelNum, int destinationId , byte dBm, byte securityLevel){
		super((byte)0x01, data, optional);
		optional[0] = subTelNum;
		optional[1] = (byte) (destinationId & 0xff);
		optional[2] = (byte) ((destinationId & 0xff00)>>8);
		optional[3] = (byte) ((destinationId & 0xff0000)>>16);
		optional[4] = (byte) ((destinationId & 0xff000000)>>32);
		optional[5] = dBm;
		optional[6] = securityLevel;
	}
}