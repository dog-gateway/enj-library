package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrLearnmode extends Packet{
	public SaWrLearnmode(byte enable, byte extended, byte[] timeout){
		int i;
		//byte header[];
		//this.dataLenght = 0x0007;
		this.optLenght = 0x00;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header , 3);
		this.data[0] = 0x01;
		this.data[1] = enable;
		this.data[2] = extended;
		for(i=0 ; i<4 ; i++){
			this.data[3+i] = timeout[i];
		}			
		//this.CRC8D = CRC8.calc(data, dataLenght); 
	}
}