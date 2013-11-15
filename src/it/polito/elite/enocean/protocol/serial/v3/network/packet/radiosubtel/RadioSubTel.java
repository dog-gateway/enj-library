package it.polito.elite.enocean.protocol.serial.v3.network.packet.radiosubtel;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

//TYPE 3
public class RadioSubTel extends Packet{
	public RadioSubTel(short dataLenght, byte optLenght, byte data[], byte optData[]){
		byte[] header;
		syncByte = 0x55;
		this.dataLenght = dataLenght;
		optLenght = 0x07;
		packetType = 0x03;
		header[0] = dataLenght;
		header[1] = optLenght;
		header[2] = packetType;
		this.CRC8H = CRC8.calc(header, 4);
		this.data = data;
		this.optData = optData;
		this.CRC8D = CRC8.calc(data+optdata, data.length+optData.length);		
	}
}