package it.polito.elite.enocean.protocol.serial.v3.network.packet.response;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

//TYPE 2
public class Response extends Packet{
	public Response(byte data){ //Piu che data andrebbe messo un tipo risposta
		//byte[] header;
		this.syncByte = 0x55;
		//dataLenght = 0x01;
		optLenght = 0x00;
		packetType = 0x02;
		//header[0] = dataLenght;
		//header[1] = optLenght;
		//header[2] = packetType;
		//this.CRC8H = CRC8.calc(header, 4);
		this.data[0] = data;
		//this.optData ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
		//this.CRC8D = CRC8.calc(data, dataLenght);		
	}

	// Metodi che restituiscono TRUE o FALSE per stabilire velocemente di che risposta si stratta

	boolean RET_OK(){
		return this.data[0] == 0x00;
	}

	boolean RET_ERROR(){
		return this.data[0] == 0x01;
	}

	boolean RET_NOT_SUPPORTED(){
		return this.data[0] == 0x02;
	}

	boolean RET_WRONG_PARAM(){
		return this.data[0] == 0x03;
	}

	boolean RET_OPERATION_DENIED(){
		return this.data[0] == 0x04;
	}

	/*
		Se >128 usati per comandi con informazioni di ritorno speciali 

		boolean ALTRO(){
			return this.data[0] > 0x80;
		}
	 */	
}