package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoRdSecurity extends Packet{
	@SuppressWarnings("null")
	public CoRdSecurity(){	
		byte header[] = null;
		syncByte = 0x55;
		dataLenght[0]=0x00;
		dataLenght[1]=0x01;
		optLenght = 0x00;
		packetType = 0x05;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x15; //Command code
		//this.CRC8D = CRC8.calc(data, dataLenght);		
	}
}