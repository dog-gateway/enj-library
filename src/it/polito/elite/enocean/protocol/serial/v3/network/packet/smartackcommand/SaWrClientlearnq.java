package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrClientlearnq extends Packet{
	public SaWrClientlearnq(byte campo_sconosciuto, byte manufactorID, byte[] EEP){
		@SuppressWarnings("unused")
		byte header[];
		syncByte = 0x55;
		//dataLenght = 0x06;
		optLenght = 0x00;
		packetType = 0x06;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x04; //Command code
		data[1] = campo_sconosciuto; // <-- Cosa fa questo campo?
		data[2] = manufactorID;
		for(int i=0 ; i<3 ; i++){
			this.data[i+8] = EEP[i];
		}
		//this.CRC8D = CRC8.calc(data, dataLenght);
	}
}