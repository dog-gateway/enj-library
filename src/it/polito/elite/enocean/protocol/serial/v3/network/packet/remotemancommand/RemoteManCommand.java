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
	private static byte[] optional = null;

	// The byte vector dataValue
	private static byte[] dataValue;
	public RemoteManCommand(int functionNumber, int manufacturerId, byte mexData[], int destinationId, int sourceId, byte dBm, byte sendWithDelay){
		super((byte)0x07, dataValue, optional);
		dataValue[0] = (byte) (functionNumber & 0xff);
		dataValue[1] = (byte) ((functionNumber & 0xff00)>>8);
		dataValue[2] = (byte) (manufacturerId & 0xff);
		dataValue[3] = (byte) ((manufacturerId & 0xff00)>>8);
		for(int i=0 ; i<mexData.length ; i++)
		{
			dataValue[4+i] = mexData[i];			
		}
		dataValue[mexData.length] = (byte) (destinationId & 0xff);
		dataValue[mexData.length + 1] = (byte) ((destinationId & 0xff00)>>8);
		dataValue[mexData.length + 2] = (byte) ((destinationId & 0xff0000)>>16);
		dataValue[mexData.length + 3] = (byte) ((destinationId & 0xff000000)>>32);
		dataValue[mexData.length + 4] = (byte) (sourceId & 0xff);
		dataValue[mexData.length + 5] = (byte) ((sourceId & 0xff00)>>8);
		dataValue[mexData.length + 6] = (byte) ((sourceId & 0xff0000)>>16);
		dataValue[mexData.length + 7] = (byte) ((sourceId & 0xff000000)>>32);
		dataValue[mexData.length + 8] = dBm;
		dataValue[mexData.length + 9] = sendWithDelay;
	}
}