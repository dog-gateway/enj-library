package it.polito.elite.enocean.protocol.serial.v3.network.packet.remotemancommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Remote management send or receive message
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class RemoteManCommand extends ESP3Packet{
	
	/**
	 * @param functionNumber : Range: 0x0000 ... 0x0FFF
	 * @param manufacturerId : Range: 0x0000 ... 0x07FF
	 * @param mexData : 0 ... 511 bytes
	 * @param destinationId : Destination ID Broadcast ID: FF FF FF FF
	 * @param sourceId : Receive case: Source ID of the sender Send case: 0x00000000
	 * @param dBm : Send case: 0xFF Receive case: Best RSSI value of all received sub telegrams (value decimal without minus)
	 * @param sendWithDelay : 1: if the first message has to be sent with random delay. When answering to broadcast message this has to be 1, otherwise 0. Default: 0
	 */
	
	public RemoteManCommand(int functionNumber, int manufacturerId, byte mexData[], int destinationId, int sourceId, byte dBm, byte sendWithDelay){
		super();
		this.packetType = REMOTE_MAN_COMMAND;
		this.data[0] = (byte) (functionNumber & 0xff);
		this.data[1] = (byte) ((functionNumber & 0xff00)>>8);
		this.data[2] = (byte) (manufacturerId & 0xff);
		this.data[3] = (byte) ((manufacturerId & 0xff00)>>8);
		for(int i=0 ; i<mexData.length ; i++)
		{
			this.data[4+i] = mexData[i];			
		}
		this.data[mexData.length] = (byte) (destinationId & 0xff);
		this.data[mexData.length + 1] = (byte) ((destinationId & 0xff00)>>8);
		this.data[mexData.length + 2] = (byte) ((destinationId & 0xff0000)>>16);
		this.data[mexData.length + 3] = (byte) ((destinationId & 0xff000000)>>32);
		this.data[mexData.length + 4] = (byte) (sourceId & 0xff);
		this.data[mexData.length + 5] = (byte) ((sourceId & 0xff00)>>8);
		this.data[mexData.length + 6] = (byte) ((sourceId & 0xff0000)>>16);
		this.data[mexData.length + 7] = (byte) ((sourceId & 0xff000000)>>32);
		this.data[mexData.length + 8] = dBm;
		this.data[mexData.length + 9] = sendWithDelay;
	}
}