package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class CoWrLearnmore extends Packet{
	@SuppressWarnings("null")
	public CoWrLearnmore(byte enable, byte[] timeout, byte channel){
		byte header[] = null;
		syncByte = 0x55;
		dataLenght[0]=0x00;
		dataLenght[1]=0x06;
		optLenght = 0x01; // <-- ATTENZIONE ERRORE NEL DATASHEET
		packetType = 0x05;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x17; //Command code
		data[1] = enable;
		//for(i=0 ; i<4 ; i++){
			//this.data[i+2] = timeout[i];
		//}
		optData[0] = channel;
		//this.CRC8D = CRC8.calc(data + optData, dataLenght);
	}
}