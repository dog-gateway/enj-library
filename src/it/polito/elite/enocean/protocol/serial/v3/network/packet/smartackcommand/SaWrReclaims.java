package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrReclaims extends Packet{
	public SaWrReclaims(byte reclaimCount){
		//byte[] header;
		syncByte = 0x55;
		//this.dataLenght = data.length;
		this.optLenght = 0x00;
		this.packetType = 0x06;
		//header[0] = data.length;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 4);
		data[0] = 0x07;
		data[1] = reclaimCount;
		//this.CRC8D = CRC8.calc(data, dataLenght);	
	}
}