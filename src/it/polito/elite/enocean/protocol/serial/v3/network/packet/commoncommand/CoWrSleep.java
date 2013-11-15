package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoWrSleep extends Packet{
	@SuppressWarnings("null")
	public CoWrSleep(int deep_sleep_period[]){
		//deep_sleep_period in int? poi bisogna mettere l'int a gruppi di 1 byte alla volta in data, perchè data è di tipo byte
		byte header[] = null;
		syncByte = 0x55;
		this.dataLenght[0] = 0x00;
		this.dataLenght[1] = 0x05;
		optLenght = 0x00;
		packetType = 0x05;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.crc8h = CRC8.calc(header, 3);
		this.data[0] = 0x01; //Command code
		
		//for(i=0 ; i<5 ; i++){
			//this.data[i+1] = deep_sleep_period[i]; //Convertire l'intero
		//}
		
		//this.optData ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
		//this.crc8d = CRC8.calc(data, dataLenght);
	}
}