package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * Write repeater level OFF, 1, 2;
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class CoWrRepeater extends Packet {
	
	/**
	 * @param repEnable : OFF=0 ON=1
	 * @param repLevel : When repeater OFF must be 0, when ON then 1 for Level-1 , 2 for
		 * Level-2
	 */
	public CoWrRepeater(byte repEnable, byte repLevel) {
		super();
		this.data[0] = 0x09;
		this.data[1] = repEnable;
		this.data[2] = repLevel;
		this.buildPacket();
	}
}