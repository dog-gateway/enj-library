package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Send smart ack learn answer to modify mailbox at postmaster.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class SaWrLearnconfirm extends ESP3Packet{
	/**
	 * @param responseTime : Response time for sensor in ms in which the controller can prepare the data and send it to the postmaster. Only actual, if learn return code is Learn IN. 
	 * @param confirmCode : Learn IN: 0x00 
	 * 						Learn OUT: 0x20
	 * @param postmaster_candidate_ID : Device ID of the used Post master
	 * @param sartack_client_ID : Device ID of the learned IN/OUT Smart Ack Client
	 */
	public SaWrLearnconfirm (int responseTime, byte confirmCode, byte[] postmaster_candidate_ID, byte sartack_client_ID){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x03; 
		this.data[1] = (byte) (responseTime & 0xff);
		this.data[2] = (byte) ((responseTime & 0xff00)>>8);
		this.data[3] = confirmCode;
		this.data[4] = (byte) (responseTime & 0xff);
		this.data[5] = (byte) ((responseTime & 0xff00)>>8);
		this.data[6] = (byte) ((responseTime & 0xff0000)>>16); 
		this.data[7] = (byte) ((responseTime & 0xff000000)>>32);
		this.buildPacket();
	}
}