package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Code 2 : Reads the learnmode state of Smart Ack Controller
 * 
 * @author andreabiasi
 *
 */
public class SaRdLearnmode extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional = null;

	// The byte vector dataValue
	private static byte[] dataValue;
	public SaRdLearnmode(){
		super((byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x02; 
	}
}