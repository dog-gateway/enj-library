package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiomessage;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * The radio message (payload data without any radio telegram contents) is embedded into the ESP3 packet
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class RadioMessage extends Packet{
	/**
	 * @param messageRorg : RORG
	 * @param mexData : Message Data Content
	 * @param destinationId : Destination ID Broadcast ID: FF FF FF FF
	 * @param sourceId : Receive case: Source ID of the sender Send case: 0x00000000
	 * @param dBm : Send case: 0xFF 
	 * 				Receive case: Best RSSI value of all received sub telegrams (value decimal without minus)
	 */
	public RadioMessage(byte messageRorg, byte mexData[], int destinationId, int sourceId, byte dBm){	
		super();
		this.packetType = RADIO_MESSAGE;
		this.data[0] = messageRorg;
		for(int i=0 ; i<mexData.length ; i++)
		{
			this.data[1+i] = mexData[i];			
		}
		this.optData[0] = (byte) (destinationId & 0xff);
		this.optData[1] = (byte) ((destinationId & 0xff00)>>8);
		this.optData[2] = (byte) ((destinationId & 0xff0000)>>16);
		this.optData[3] = (byte) ((destinationId & 0xff000000)>>32);
		this.optData[4] = (byte) (sourceId & 0xff);
		this.optData[5] = (byte) ((sourceId & 0xff00)>>8);
		this.optData[6] = (byte) ((sourceId & 0xff0000)>>16);
		this.optData[7] = (byte) ((sourceId & 0xff000000)>>32);
		this.optData[8] = dBm;
		this.buildPacket();
	}
}