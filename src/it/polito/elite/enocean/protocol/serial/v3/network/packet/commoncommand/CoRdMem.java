package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoRdMem extends Packet{
	@SuppressWarnings("null")
	public CoRdMem(byte memory_type, byte[] memory_address, byte[] dataLenght){
		byte header[] = null;
		this.syncByte = 0x55;
		dataLenght[0]=0x00;
		dataLenght[1]=0x08;
		optLenght = 0x00;
		packetType = 0x05;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x13; //Command code
		//data[1] = memory_type
				for(int i=0 ; i<4 ; i++){
					this.data[i+2] = memory_address[i];
				}
		for(int i=0 ; i<2 ; i++){
			this.data[i+6] = dataLenght[i];
		}
		//this.optData ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
		//this.CRC8D = CRC8.calc(data, dataLenght);			
	}
}