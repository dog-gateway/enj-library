package it.polito.elite.enocean.protocol.serial.v3.network.packet.remotemancommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Packet type 7: remote management send or receive message
 * 
 * @author andreabiasi
 *
 */
public class RemoteManCommand extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public RemoteManCommand(int functionNumber, int manufacturerId, byte mexData[], int destinationId, int sourceId, byte dBm, byte sendWithDelay){
		super(4+mexData.length, 10, (byte)0x07, dataValue, optional);
		dataValue[0] = (byte) (functionNumber & 0xff);
		dataValue[1] = (byte) ((functionNumber & 0xff00)>>8);
		dataValue[2] = (byte) (manufacturerId & 0xff);
		dataValue[3] = (byte) ((manufacturerId & 0xff00)>>8);
		for(int i=0 ; i<mexData.length ; i++)
		{
			dataValue[4+i] = mexData[i];			
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
		optional[9] = sendWithDelay;
	}
}