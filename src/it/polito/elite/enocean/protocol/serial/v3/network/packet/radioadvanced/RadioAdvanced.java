package it.polito.elite.enocean.protocol.serial.v3.network.packet.radioadvanced;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

//TYPE 10
public class RadioAdvanced extends Packet{
	public RadioAdvanced(byte data, byte SubTelNum, byte dBm){
		this.syncByte = 0x55;
		//this.dataLenght = data.length;
		this.packetType  = 0x0A;
		//this.CRC8H = CRC8.calc(header, 4);
		//this.data = data;
		this.optData[0] = SubTelNum; 
		this.optData[1] = dBm;
		//this.CRC8D = CRC8.calc(data, data.lenght+2);
	}
}