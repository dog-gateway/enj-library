package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiomessage;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Packet type 9 : The radio message (payload data without any radio telegram contents) is embedded into the ESP3 packet
 * 
 * @author andreabiasi
 *
 */
public class RadioMessage extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public RadioMessage(byte messageRorg, byte mexData[], int destinationId, int sourceId, byte dBm, byte sendWithDelay){
		super(1+mexData.length, 9, (byte)0x07, dataValue, optional);
		dataValue[0] = messageRorg;
		for(int i=0 ; i<mexData.length ; i++)
		{
			dataValue[1+i] = mexData[i];			
		}
		optional[0] = (byte) (destinationId & 0xff);
		optional[1] = (byte) ((destinationId & 0xff00)>>8);
		optional[2] = (byte) ((destinationId & 0xff0000)>>16);
		optional[3] = (byte) ((destinationId & 0xff000000)>>32);
		optional[4] = (byte) (sourceId & 0xff);
		optional[5] = (byte) ((sourceId & 0xff00)>>8);
		optional[6] = (byte) ((sourceId & 0xff0000)>>16);
		optional[7] = (byte) ((sourceId & 0xff000000)>>32);
		optional[8] = dBm;
	}
}