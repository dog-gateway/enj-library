package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Delete secure device from controller. It is only possible to delete ALL rockets of a secure device.
 */
public class CoWrSecuredeviceDel extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;	
	
	public CoWrSecuredeviceDel(int id){
		super((byte)0x05, dataValue, optional);
		dataValue[0] = 0x1A;
		dataValue[1] = (byte) (id & 0xff);
		dataValue[2] = (byte) ((id & 0xff00)>>8);
		dataValue[3] = (byte) ((id & 0xff0000)>>16);
		dataValue[4] = (byte) ((id & 0xff000000)>>32); 
	}
}