package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Delete filter from filter list
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrFilterDel extends ESP3Packet {
	/**
	 * @param filterType : Device ID = 0, R-ORG = 1, dBm = 2
	 * @param filterValue : Value of filter function �compare�: - device ID - R-ORG - RSSI of radio telegram in dBm
	 */
	public CoWrFilterDel(byte filterType, int filterValue) {
		super();
		this.packetType = COMMON_COMMAND;
		//Command code
		this.data[0] = 0x0C;
		this.data[1] = filterType;
		this.data[2] = (byte) (filterValue & 0xff);
		this.data[3] = (byte) ((filterValue & 0xff00) >> 8);
		this.data[4] = (byte) ((filterValue & 0xff0000) >> 16);
		this.data[5] = (byte) ((filterValue & 0xff000000) >> 32);
	}
}