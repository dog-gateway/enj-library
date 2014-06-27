package it.polito.elite.enocean.protocol.serial.v3.network.packet.radioadvanced;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * The advanced radio protocol telegram (raw data without LEN and CRC) is embedded into the ESP3 packet
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class RadioAdvanced extends ESP3Packet{
	/**
	 * @param rawData : Advanced radio protocol telegram without the first Length byte. For sending the advanced protocol CRC8 byte can be set to any value. x = Data Length
	 * @param subTelNum : Number of sub telegram; Send: 3 / receive: 1 ... y
	 * @param dBm : Send case: FF Receive case: best RSSI value of all received sub telegrams (value decimal without minus)
	 */
	public RadioAdvanced(byte rawData[], byte subTelNum, byte dBm){
		super();
		this.packetType = RADIO_ADVANCED;
		this.data=rawData;
		this.optData[0] = subTelNum;
		this.optData[1] = dBm;
		this.buildPacket();
	}
}