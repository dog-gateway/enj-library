package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Enables or disables learn mode of Smart Ack Controller.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class SaWrLearnmode extends ESP3Packet{
	/**
	 * @param enable Start Learnmode = 1 
	 * 				 End Learnmode = 0
	 * @param extended 	Simple Learnmode = 0 
	 * 					Advance Learnmode = 1 
	 * 					Advance Learnmode select Rep. = 2
	 * @param timeout	Time-Out for the learn mode in ms. 
	 * 					When time is 0 then default period of 60�000 ms is used
	 */
	public SaWrLearnmode(byte enable, byte extended, int timeout){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x01;
		//Enable
		this.data[1] = enable;
		//Extended
		this.data[2] = extended;
		//Timeout
		this.data[3] = (byte) (timeout & 0xff);
		this.data[4] = (byte) ((timeout & 0xff00)>>8);
		this.data[5] = (byte) ((timeout & 0xff0000)>>16);
		this.data[6] = (byte) ((timeout & 0xff000000)>>32);
		this.buildPacket();
	}
}