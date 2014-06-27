package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Read x bytes of the flash, ram0, data, idata, xdata
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class CoRdMem extends ESP3Packet {
	/**
	 * @param memoryType : 	Flash 0x00 
	 * 						RAM 0 0x01 
	 * 						data RAM 0x02 
	 * 						idata RAM 0x03 
	 * 						xdata RAM 0x04
	 * @param memoryAddress : Start address to read
	 * @param dataLenght : Length to be read
	 */
	public CoRdMem(byte memoryType, int memoryAddress, int dataLenght) {
		super();
		this.packetType = COMMON_COMMAND;
		//Command code
		this.data[0] = 0x13;
		this.data[1] = memoryType;
		this.data[2] = (byte) (memoryAddress & 0xff);
		this.data[3] = (byte) ((memoryAddress & 0xff00) >> 8);
		this.data[4] = (byte) ((memoryAddress & 0xff0000) >> 16);
		this.data[5] = (byte) ((memoryAddress & 0xff000000) >> 32);
		this.data[6] = (byte) (dataLenght & 0xff);
		this.data[7] = (byte) ((dataLenght & 0xff00) >> 8);
		this.buildPacket();
	}
}