package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaRdLearnmode extends Packet{
	public SaRdLearnmode(){
		//byte header[];
		//this.dataLenght = 0x0001;
		this.optLenght = 0x00;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//this.CRC8H = CRC8.calc(header, header.length);
		this.data[0] = 0x02;
		//this.CRC8D = CRC8.calc(data, dataLenght); 
	}
}