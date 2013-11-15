package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrLearnconfirm extends Packet{
	public SaWrLearnconfirm (byte[] response_time, byte confirm_code, byte[] postmaster_candidate_ID, byte sartack_client_ID){
		@SuppressWarnings("unused")
		byte header[];
		syncByte = 0x55;
		//dataLenght = 0x0C;
		optLenght = 0x00;
		packetType = 0x06;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 3);
		data[0] = 0x03; //Command code
		for(int i=0 ; i<3 ; i++){
			this.data[i+2] = response_time[i];
		}
		data[3] = confirm_code;
		//for(int i=0 ; i<4 ; i++){
			//this.data[i+4] = postmaster_candidate_ID;
		//}
		//for(int i=0 ; i<4 ; i++){
			//this.data[i+8] = smartack_client_ID;
		//}
		//this.CRC8D = CRC8.calc(data, dataLenght);
	}
}