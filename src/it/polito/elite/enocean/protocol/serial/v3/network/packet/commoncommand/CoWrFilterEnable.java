package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoWrFilterEnable extends Packet{
	@SuppressWarnings("null")
	public CoWrFilterEnable(byte filter_onoff, byte filter_operator){
		byte header[] = null;
		syncByte = 0x55;
		dataLenght[0]=0x00;
		dataLenght[1]=0x03;
		optLenght = 0x00;
		packetType = 0x05;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x0E; //Command code
		data[1] = filter_onoff;
		data[2] = filter_operator;
		//this.optData ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
		//this.CRC8D = CRC8.calc(data, dataLenght);
	}
}