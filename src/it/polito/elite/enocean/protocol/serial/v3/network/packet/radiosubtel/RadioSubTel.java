package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiosubtel;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Packet type 3 : Radio Sub Tel
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class RadioSubTel extends ESP3Packet{
	/**
	 * @param data : radio telegram without checksum
	 * @param subTelNum : actual sequence number of subtelegrams 
	 * @param destinationId : Broadcast radio FF FF FF FF
	 * @param dBm : Send case: FF
	 * 				Receive case : best RSSI value of all received subtelegrams (value decimal without minus)	
	 * @param securityLevel : 0 = telegram unencrypted
	 * 						  n = type of encryption (not supported any more)
	 * @param timeStamp : Timestamp of 1st subtelegram is the system timer tick [ms] (2 bytes lower address)
	 * @param tickSubTel : Relative time [ms] of each subtelegram
	 * @param dBmSubTel : RSSI value of each subtelegram
	 * @param statusSubTel : Telegram control bits of each subtelegram - used in case of repeating, switch telegram encapsulation, checksum type identification
	 */
	public RadioSubTel(byte data[], byte subTelNum, int destinationId, byte dBm, byte securityLevel, byte timeStamp, byte tickSubTel, byte dBmSubTel, byte statusSubTel){
		super();
		this.packetType = RADIO_SUB_TEL;
		this.optData[0] = subTelNum;
		this.optData[1] = (byte) (destinationId & 0xff);
		this.optData[2] = (byte) ((destinationId & 0xff00)>>8);
		this.optData[3] = (byte) ((destinationId & 0xff0000)>>16);
		this.optData[4] = (byte) ((destinationId & 0xff000000)>>32);
		this.optData[5] = dBm;
		this.optData[6] = securityLevel;
		this.optData[7] = (byte) (timeStamp & 0xff);
		this.optData[8] = (byte) ((timeStamp & 0xff00)>>8);	
		this.optData[9] = tickSubTel;
		this.optData[10] = dBmSubTel;
		this.optData[11] = statusSubTel;
		this.buildPacket();
	}
}