package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Send reset command to a Smart Ack Client
 * 
 * @author andreabiasi
 *
 */
public class SaWrReset extends ESP3Packet{
	/**
	 * @param deviceId : Device ID of the Smart Ack Client
	 */
	public SaWrReset(int deviceId){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x05;
		this.data[1] = (byte) (deviceId & 0xff);
		this.data[2] = (byte) ((deviceId & 0xff00)>>8);
		this.data[3] = (byte) ((deviceId & 0xff0000)>>16);
		this.data[4] = (byte) ((deviceId & 0xff000000)>>32);
		this.buildPacket();
	}
}