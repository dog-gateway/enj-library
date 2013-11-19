package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

public class SaWrLearnconfirm extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public SaWrLearnconfirm (int responseTime, byte confirmCode, byte[] postmaster_candidate_ID, byte sartack_client_ID){
		super(12, 0, (byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x03; 
		dataValue[1] = (byte) (responseTime & 0xff);
		dataValue[2] = (byte) ((responseTime & 0xff00)>>8);
		dataValue[3] = confirmCode;
		dataValue[4] = (byte) (responseTime & 0xff);
		dataValue[5] = (byte) ((responseTime & 0xff00)>>8);
		dataValue[6] = (byte) ((responseTime & 0xff0000)>>16); 
		dataValue[7] = (byte) ((responseTime & 0xff000000)>>32);
	}
}