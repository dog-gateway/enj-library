package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Write x bytes of the Flash, RAM0, DATA, IDATA, XDATA.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoWrMem extends ESP3Packet {
	/**
	 * @param memoryType : 	Flash 0x00 
	 * 					   	RAM 0 0x01 
	 * 						data RAM 0x02 
	 * 						idata RAM 0x03 
	 * 						xdata RAM 0x04
	 * @param memoryAddress : Start address to write
	 * @param memoryData : Data content to write
	 */
	public CoWrMem(byte memoryType, int memoryAddress, byte[] memoryData) {
		super();
		this.packetType = COMMON_COMMAND;
		//Command code
		this.data[0] = 0x12;
		this.data[1] = memoryType;
		this.data[2] = (byte) (memoryAddress & 0xff);
		this.data[3] = (byte) ((memoryAddress & 0xff00) >> 8);
		this.data[4] = (byte) ((memoryAddress & 0xff0000) >> 16);
		this.data[5] = (byte) ((memoryAddress & 0xff000000) >> 32);
		for (int i = 6; i < memoryData.length + 6; i++) {
			this.data[i] = memoryData[i];
		}
		this.buildPacket();
	}
}