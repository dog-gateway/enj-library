/*
 * EnJ - EnOcean Java API
 * 
 * Copyright 2014 Andrea Biasi, Dario Bonino 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package it.polito.elite.enocean.enj.eep.eep26.telegram;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author bonino
 *
 */
public class FourBSTeachInTelegram extends FourBSTelegram
{
	private byte func;
	private byte type;
	private byte[] manId;
	private boolean withEEP;
	private boolean isQuery;
	// the response flag
	protected boolean response;

	// TEACH-IN RESULTS

	// 0xf0 = 11110000 ->
	// DB0.BIT_7 == 1 ->bidirectional communication
	// DB0.BIT_6 == 1 -> EEP Result: EEP supported
	// DB0.BIT_5 == 1 -> Sender ID stored
	// DB0.BIT_4 == 1 -> LRN Status = Response
	// DB0.BIT_3 == 0 -> LRN Bit = Teach-in telegram
	// DB0.BIT_2..0 == 000
	public static final byte BIDIRECTIONAL_TEACH_IN_SUCCESSFUL = (byte) 0x70;
	public static final byte BIDIRECTIONAL_TEACH_IN_SUCCESSFUL_WITH_EEP = (byte) 0xf0;

	// 0xf0 = 10010000 ->
	// DB0.BIT_7 == 1 ->bidirectional communication
	// DB0.BIT_6 == 0 -> EEP Result: EEP not supported
	// DB0.BIT_5 == 0 -> Sender ID not stored
	// DB0.BIT_4 == 1 -> LRN Status = Response
	// DB0.BIT_3 == 0 -> LRN Bit = Teach-in telegram
	// DB0.BIT_2..0 == 000
	public static final byte BIDIRECTIONAL_TEACH_IN_REFUSED = (byte) 0x90;

	/**
	 * @param pkt
	 */
	public FourBSTeachInTelegram(ESP3Packet pkt)
	{
		super(pkt);
		this.init();
	}

	public FourBSTeachInTelegram(FourBSTelegram telegram)
	{
		super(telegram.rawPacket);
		this.init();
	}

	private void init()
	{
		// get the function byte
		this.func = (byte) ((this.payload[0] >> 2) & (byte) 0x3F);

		// get the type byte (spans across 2 bytes of the payload)
		byte hiBits = (byte) (this.payload[0] & (byte) 0x03);
		byte loBits = (byte) ((this.payload[1] >> 3) & (byte) 0x1F);
		this.type = (byte) ((hiBits << 5) | loBits);

		// get the manufacturer id
		this.manId = new byte[2];

		// Consider only DB_6.BIT2 , DB_6.BIT1 , DB_6.BIT0 by performing a
		// bitwise AND with the 00000111 mask.
		this.manId[0] = (byte) (this.payload[1] & (0x07));
		this.manId[1] = this.payload[2];

		// get the learn type
		// 1000 0000
		byte learnTypeByte = (byte) (this.payload[3] & (byte) 0x80);

		if (learnTypeByte != 0)
			this.withEEP = true;
		else
			this.withEEP = false;

		// get the learn status
		byte learnStatus = (byte) (this.payload[3] & (byte) 0x10);

		if (learnStatus == 0)
			this.isQuery = true;
		else
			this.isQuery = false;

	}

	/**
	 * @return the func
	 */
	public byte getEEPFunc()
	{
		return func;
	}

	/**
	 * @return the type
	 */
	public byte getEEPType()
	{
		return type;
	}

	/**
	 * @return the withEEP
	 */
	public boolean isWithEEP()
	{
		return withEEP;
	}

	/**
	 * 
	 * @return if the packet is a query or not
	 */
	public boolean isQuery()
	{
		return isQuery;
	}

	/**
	 * @return the manId
	 */
	public byte[] getManId()
	{
		return manId;
	}

	public boolean isResponse()
	{
		return response;
	}

	public void setResponse(boolean response)
	{
		this.response = response;
	}

	public FourBSTelegram buildResponse(byte response)
	{
		byte[] payloadResp = new byte[10];

		// Rorg 4BS
		payloadResp[0] = (byte) 0xA5;

		// Inverted order of bytes in the transmission
		payloadResp[1] = this.payload[0];
		payloadResp[2] = this.payload[1];
		payloadResp[3] = this.payload[2];
		payloadResp[4] = response;

		// address
		payloadResp[5] = (byte) 0x00;
		payloadResp[6] = (byte) 0xFF;
		payloadResp[7] = (byte) 0xFF;
		payloadResp[8] = (byte) 0xFF;

		// status
		payloadResp[9] = (byte) 0x00;

		// optional data
		byte[] opt = new byte[7];
		opt[0] = (byte) 0x03;
		opt[1] = this.address[0];// (byte) 0x00;
		opt[2] = this.address[1];// (byte) 0x81;
		opt[3] = this.address[2];// (byte) 0x2A;
		opt[4] = this.address[3];// (byte) 0x90;
		opt[5] = (byte) 0xFF;
		opt[6] = (byte) 0x00;

		// the low level packet
		ESP3Packet fourBSTeachInResponse = new ESP3Packet(ESP3Packet.RADIO,
				payloadResp, opt);
		// the 4BS teach in packet
		FourBSTeachInTelegram responsePacket = new FourBSTeachInTelegram(
				fourBSTeachInResponse);

		// set the response flag
		responsePacket.setResponse(true);

		// the FourBSTeachInPacket
		return responsePacket;
	}

	public static boolean isTeachIn(FourBSTelegram telegram)
	{
		boolean teachIn = false;
		// get the payload
		byte data[] = telegram.getPayload();

		// get the teach-in flag (offset 28, 4th bit of the 4th byte)
		byte teachInByte = (byte) ((byte) (data[3] & (byte) 0x08) >> 3);

		// check the corresponding boolean value
		if (teachInByte == 0)
			teachIn = true;

		return teachIn;

	}
}
