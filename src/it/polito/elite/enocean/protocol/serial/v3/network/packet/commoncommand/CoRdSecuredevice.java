package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
/**
 * Read secure device
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdSecuredevice extends Packet {
/**
 * @param index : Index of secure device to read, starting with 1É255
 */
public CoRdSecuredevice(byte index) {
		super();
		this.packetType = 0x05;
		this.data[0] = 0x1B;
		this.data[1] = index;
		this.buildPacket();
	}
}