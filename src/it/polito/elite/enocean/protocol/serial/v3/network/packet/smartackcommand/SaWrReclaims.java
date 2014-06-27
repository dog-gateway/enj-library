package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Set the amount of reclaim tries in Smart Ack Client
 * 
 * @author andreabiasi
 *
 */
public class SaWrReclaims extends ESP3Packet{
	/**
	 * @param reclaimCount : Presetting for the number of required reclaim tries
	 */
	public SaWrReclaims(byte reclaimCount){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x07;
		this.data[1] = reclaimCount;
		this.buildPacket();
	}
}