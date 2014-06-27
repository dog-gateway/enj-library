package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * Waiting till end of maturity time before received radio telegrams will transit
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrWaitMaturity extends ESP3Packet {
	/**
	 * @param waitEndMaturity : 0: Radio telegrams are send immediately 1: Radio telegrams are send after the maturity time is elapsed
	 */
	public CoWrWaitMaturity(byte waitEndMaturity) {
		super();
		this.packetType = COMMON_COMMAND;
		this.data[0] = 0x10;
		this.data[1] = waitEndMaturity;
		this.buildPacket();
	}
}