package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiomessage;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * The radio message (payload data without any radio telegram contents) is embedded into the ESP3 packet
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

/*
 * NB Questo pacchetto non � supportato dal TCM310
 * 
 */


public class RadioMessage extends ESP3Packet{
	/**
	 * @param messageRorg : RORG
	 * @param mexData : Message Data Content
	 * @param destinationId : Destination ID Broadcast ID: FF FF FF FF
	 * @param sourceId : Receive case: Source ID of the sender Send case: 0x00000000
	 * @param dBm : Send case: 0xFF 
	 * 				Receive case: Best RSSI value of all received sub telegrams (value decimal without minus)
	 */
	//public RadioMessage(byte messageRorg, byte mexData[], int destinationId, int sourceId, byte dBm){
	public RadioMessage(byte messageRorg, byte mexData[], byte[] destinationId, byte[] sourceId, byte dBm){
		super();
		this.packetType = RADIO_MESSAGE;
		data = new byte[1+mexData.length];
		this.data[0] = messageRorg;
		for(int i=0 ; i<mexData.length ; i++)
		{
			this.data[1+i] = mexData[i];			
		}
		optData = new byte[8];
		this.optData[0] = destinationId[0];
		this.optData[1] = destinationId[1];
		this.optData[2] = destinationId[2];
		this.optData[3] = destinationId[3];
		
		this.optData[4] = sourceId[0];
		this.optData[5] = sourceId[1];
		this.optData[6] = sourceId[2];
		this.optData[7] = sourceId[3];
		
		/*
		this.optData[0] = (byte) (destinationId & 0xff);
		this.optData[1] = (byte) ((destinationId & 0xff00)>>8);
		this.optData[2] = (byte) ((destinationId & 0xff0000)>>16);
		this.optData[3] = (byte) ((destinationId & 0xff000000)>>32);
		this.optData[4] = (byte) (sourceId & 0xff);
		this.optData[5] = (byte) ((sourceId & 0xff00)>>8);
		this.optData[6] = (byte) ((sourceId & 0xff0000)>>16);
		this.optData[7] = (byte) ((sourceId & 0xff000000)>>32);
		this.optData[8] = dBm;
		*/
		this.buildPacket();
	}
}