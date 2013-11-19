package it.polito.elite.enocean.protocol.serial.v3.network.packet.radioadvanced;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Packet type 10 Radio Advanced: the advanced radio protocol telegram (raw data without LEN and CRC) is embedded into the ESP3 packet
 * 
 * @author andreabiasi
 *
 */
public class RadioAdvanced extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	public RadioAdvanced(byte rawData[], byte subTelNum, byte dBm){
		super(rawData.length, 2, (byte)0x07, rawData, optional);

		optional[0] = subTelNum;
		optional[1] = dBm;
	}
}