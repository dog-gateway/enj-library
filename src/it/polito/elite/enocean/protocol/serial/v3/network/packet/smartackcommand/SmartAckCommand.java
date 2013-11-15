package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

//TYPE 6
public class SmartAckCommand extends Packet {
	public SmartAckCommand(){
		this.syncByte = 0x55;
		this.packetType = 0x06;
	}

	public class SaWrReset extends Packet{
		public SaWrReset(byte SmartAckCientID[]){
			//byte header[];
			syncByte = 0x55;
			//dataLenght = 0x05;
			optLenght = 0x00;
			packetType = 0x06;
			//header[0] = dataLenght;
			//header[1] = optLenght;
			//header[2] = packetType;
			//this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x05; //SmartAck code;
			for(int i=0 ; i<4 ; i++){
				this.data[i+1] = SmartAckCientID[i];
			}
			//this.CRC8D = CRC8.calc(data, dataLenght);
		}
	}
}