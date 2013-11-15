package it.polito.elite.enocean.protocol.serial.v3.network.packet.remotemancommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

//TYPE 7
public class RemoteManCommand extends Packet{
	
	public RemoteManCommand(byte data[]){
		super(0x55,);
		@SuppressWarnings("unused")
		byte[] header;
		this.syncByte = 0x55;
		//this.dataLenght = data.length;
		this.optLenght = 0x00;
		this.packetType = 0x07;
		//header[0] = data.length;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 4);
		this.data = data;
		//this.CRC8D = CRC8.calc(data, dataLenght);
	}
}