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
		super();
		this.packetType = RADIO;
		this.data = new byte[data.length];
		this.data=data;
		this.optData = new byte[7];
		this.optData[0] = subTelNum;
		this.optData[1] = (byte) (destinationId & 0xff);
		this.optData[2] = (byte) ((destinationId & 0xff00)>>8);
		this.optData[3] = (byte) ((destinationId & 0xff0000)>>16);
		this.optData[4] = (byte) ((destinationId & 0xff000000)>>32);
		this.optData[5] = dBm;
		this.optData[6] = securityLevel;
		this.buildPacket();
		
	}
}