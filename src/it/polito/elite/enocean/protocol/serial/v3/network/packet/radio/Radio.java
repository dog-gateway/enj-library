package it.polito.elite.enocean.protocol.serial.v3.network.packet.radio;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;


//TYPE 1
public class Radio extends Packet{
	@SuppressWarnings("null")
	public Radio(byte[] dataLenght, byte data[], byte SubTelNum, byte[] DestinationID , byte dBm, byte Securitylevel){
		byte[] header = null;
		int i;
		syncByte = 0x55;
		this.dataLenght[0] = dataLenght[0];
		this.dataLenght[1] = dataLenght[1];
		optLenght = 0x07;
		packetType = 0x01;
		header[0] = dataLenght[0];
		header[1] = dataLenght[1];
		header[2] = optLenght;
		header[3] = packetType;
		//this.CRC8H = CRC8.calc(header, 4);
		this.data = data;
		optData[0] = SubTelNum;
		for(i=0 ; i<4 ; i++) {
			optData[i+1] = DestinationID[i]; // Mettere int o vettore di 4 byte?
		}
		optData[5] = dBm;
		optData[6] = Securitylevel;			
		//Come concatenare due vettori? Usare la classe vector?
		//this.CRC8D = CRC8.calc(data+optData, dataLenght+7);		
	}
}