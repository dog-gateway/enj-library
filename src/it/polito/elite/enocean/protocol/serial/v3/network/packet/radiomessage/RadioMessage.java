package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiomessage;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class RadioMessage extends Packet{
	public RadioMessage(byte data[], byte optData[]){
		@SuppressWarnings("unused")
		byte[] header;
		@SuppressWarnings("unused")
		byte[] d;
		this.syncByte = 0x55;
		//this.dataLenght = data.length;
		this.optLenght = 0x0A;
		this.packetType = 0x09;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 4);
		this.data = data;
		this.optData = optData;
		// d : concatenare in d data+optData
		//this.CRC8D = CRC8.calc(d, dataLenght);
	}
}