package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Write security information (level, keys). This functions does not support the actual security concept should not be used any more
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrSecurity extends ESP3Packet {	
	/**
	 * @param secLevel : Type no. of encryption
	 * @param key : Security key
	 * @param rollingCode : Reserved
	 */
	public CoWrSecurity(byte secLevel, int key, int rollingCode) {
		super();
		this.packetType = COMMON_COMMAND;
		// Command code
		this.data[0] = 0x16;
		this.data[1] = secLevel;
		this.data[2] = (byte) (key & 0xff);
		this.data[3] = (byte) ((key & 0xff00) >> 8);
		this.data[4] = (byte) ((key & 0xff0000) >> 16);
		this.data[5] = (byte) ((key & 0xff000000) >> 32);
		this.data[6] = (byte) (rollingCode & 0xff);
		this.data[7] = (byte) ((rollingCode & 0xff00) >> 8);
		this.data[8] = (byte) ((rollingCode & 0xff0000) >> 16);
		this.data[9] = (byte) ((rollingCode & 0xff000000) >> 32);
		this.buildPacket();
	}
}
