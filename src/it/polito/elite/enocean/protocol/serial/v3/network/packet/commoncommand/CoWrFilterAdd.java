package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Add filter to fileter list
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrFilterAdd extends ESP3Packet {
	/**
	 * @param filterType : Device ID = 0, R-ORG = 1, dBm = 2
	 * @param filterValue : Value of filter function
	 * @param filterKind : Filter kind bloks = 0x00 Filter kind bloks = 0x80
	 */
	public CoWrFilterAdd(byte filterType, int filterValue, byte filterKind) {
		super();
		this.packetType = COMMON_COMMAND;
		this.data[0] = 0x0B;
		this.data[1] = filterType;
		this.data[2] = (byte) (filterValue & 0xff);
		this.data[3] = (byte) ((filterValue & 0xff00) >> 8);
		this.data[4] = (byte) ((filterValue & 0xff0000) >> 16);
		this.data[5] = (byte) ((filterValue & 0xff000000) >> 32);
		this.data[6] = filterKind;
		this.buildPacket();
	}
}