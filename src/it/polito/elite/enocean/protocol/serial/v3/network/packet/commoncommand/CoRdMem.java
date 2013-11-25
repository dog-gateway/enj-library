package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Read x bytes of the flash, ram0, data, idata, xdata
 */
public class CoRdMem extends Packet {
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue
	 */
	private static byte[] dataValue;

	public CoRdMem(byte memoryType, int memoryAddress, int dataLenght) {
		super((byte) 0x05, dataValue, optional);
		dataValue[0] = 0x13;
		dataValue[1] = memoryType;
		dataValue[2] = (byte) (memoryAddress & 0xff);
		dataValue[3] = (byte) ((memoryAddress & 0xff00) >> 8);
		dataValue[4] = (byte) ((memoryAddress & 0xff0000) >> 16);
		dataValue[5] = (byte) ((memoryAddress & 0xff000000) >> 32);
		dataValue[6] = (byte) (dataLenght & 0xff);
		dataValue[7] = (byte) ((dataLenght & 0xff00) >> 8);
	}
}