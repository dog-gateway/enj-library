package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoWrSecureserviceAdd extends Packet{
	@SuppressWarnings("null")
	public CoWrSecureserviceAdd(byte slf, byte id, byte[] private_key, byte rolling_code){
		byte header[] = null;
		syncByte = 0x55;
		dataLenght[0]=0x00;
		dataLenght[1]=0x19;
		optLenght = 0x00;
		packetType = 0x05;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x19; //Command code
		data[1] = slf;
		//for(i=0 ; i<4 ; i++){
			//this.data[i+2] = id[i];
		//}
		//for(i=0 ; i<16 ; i++){
			//this.data[i+6] = private_key;
		//}
		//for(i=0 ; i<3 ; i++){
			//this.data[i+23] = rolling_code;
		//}
		//this.CRC8D = CRC8.calc(data, dataLenght);
	}
}