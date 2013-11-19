package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/*
 * Enables or disables learn mode of controller
 */
public class CoWrLearnmore extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;
	public CoWrLearnmore(byte enable, int timeout, byte channel){
		super(6, 1, (byte)0x05, dataValue, optional);
		dataValue[0] = 0x17;
		dataValue[1] = enable;
		dataValue[2] = (byte) (timeout & 0xff);
		dataValue[3] = (byte) ((timeout & 0xff00)>>8);
		dataValue[4] = (byte) ((timeout & 0xff0000)>>16);
		dataValue[5] = (byte) ((timeout & 0xff000000)>>32);
		optional[0] = channel;
	}
}