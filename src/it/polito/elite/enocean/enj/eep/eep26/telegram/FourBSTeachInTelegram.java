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
		byte learnTypeByte = (byte) (this.payload[3] & (byte)0x80);
		
		if(learnTypeByte != 0)
			this.withEEP = true;
		else
			this.withEEP = false;

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
	 * @return the manId
	 */
	public byte[] getManId()
	{
		return manId;
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
