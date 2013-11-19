/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Read mailbox information at the postmaster device, about all learned Smart Ack clients
 * 
 * @author andreabiasi
 *
 */
public class SaRdLearnedclients extends Packet{
	// The byte vector optional may contains the optional data, in this packet type is empty
	private static byte[] optional;

	// The byte vector dataValue
	private static byte[] dataValue;
	public SaRdLearnedclients(){
		super(1, 0, (byte)0x06, dataValue, optional);
		//Smart ack code
		dataValue[0] = 0x06; 		
	}
}
