package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/*
 * Enables or disables learn mode of controller
 */
public class CoWrLearnmore extends ESP3Packet {
	
	public static byte START_LEARNMODE = 0x01; 
	public static byte END_LEARNMODE = 0x00; 

	/**
	 * @param enable : Start Learn mode = 1 End Learn mode = 0
	 * @param timeout : Time-Out for the learn mode in ms. When time is 0 then default period of 60�000 ms is used
	 * @param channel : 0..0xFD = Channel No. absolute 
	 * 					0xFE = Previous channel relative 
	 * 					0xFF = Next channel relative
	 */
	public CoWrLearnmore(byte enable, int timeout, byte channel) {
		super();
		this.packetType = COMMON_COMMAND;
		this.data = new byte[6];
		//Command code
		this.data[0] = 0x17;
		this.data[1] = enable;
		this.data[2] = (byte) (timeout & 0xff);
		this.data[3] = (byte) ((timeout & 0xff00) >> 8);
		this.data[4] = (byte) ((timeout & 0xff0000) >> 16);
		this.data[5] = (byte) ((timeout & 0xff000000) >> 32);
		this.optData = new byte[1];
		this.optData[0] = channel;
		this.buildPacket();
	}
}