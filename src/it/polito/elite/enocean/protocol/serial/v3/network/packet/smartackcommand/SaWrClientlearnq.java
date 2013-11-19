package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Code 4 : Sends smart ack learn request telegram to smart ack controller. This function will only be in a Smart Ack Client
 * 
 * @author andreabiasi
 *
 */
public class SaWrClientlearnq extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public SaWrClientlearnq(byte MsbManufactorId, byte LsbManufactorId, int EEP){
		super(12, 0, (byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x04; 
		dataValue[1] = MsbManufactorId;
		dataValue[2] = LsbManufactorId;
		dataValue[4] = (byte) (EEP & 0xff);
		dataValue[5] = (byte) ((EEP & 0xff00)>>8);
		dataValue[6] = (byte) ((EEP & 0xff0000)>>16); 
	}
}