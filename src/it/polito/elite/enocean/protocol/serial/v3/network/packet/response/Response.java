package it.polito.elite.enocean.protocol.serial.v3.network.packet.response;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Packet type 2 : Response
 * 
 * @author andreabiasi
 *
 */
public class Response extends Packet{
	// Optional data
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public Response(byte returnCode){ //Piu che data andrebbe messo un tipo risposta
		super(1, 0, (byte)0x02, dataValue, optional);
		dataValue[0] = returnCode;
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