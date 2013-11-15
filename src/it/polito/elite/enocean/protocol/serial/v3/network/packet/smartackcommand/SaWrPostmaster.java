package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrPostmaster extends Packet{
	public SaWrPostmaster(byte mailboxCount){
		//byte header[];
		syncByte = 0x55;
		//dataLenght = 0x02;
		optLenght = 0x00;
		packetType = 0x06;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x08; //Command code;
		data[1] = mailboxCount;
		//this.CRC8D = CRC8.calc(data, dataLenght);
	}
}