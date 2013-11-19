package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrLearnmode extends Packet{
	
	 // The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;
	
	// The byte vector dataValue
	private static byte[] dataValue;
	public SaWrLearnmode(byte enable, byte extended, int timeout){
		super(7, 0, (byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x01;
		//Enable
		dataValue[1] = enable;
		//Extended
		dataValue[2] = extended;
		//Timeout
		dataValue[3] = (byte) (timeout & 0xff);
		dataValue[4] = (byte) ((timeout & 0xff00)>>8);
		dataValue[5] = (byte) ((timeout & 0xff0000)>>16);
		dataValue[6] = (byte) ((timeout & 0xff000000)>>32);
	}
}