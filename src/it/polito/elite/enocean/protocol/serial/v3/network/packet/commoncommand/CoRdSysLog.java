/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Read system log from device databank
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdSysLog extends ESP3Packet {
	public CoRdSysLog() {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x04;
		this.buildPacket();
	}
}