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

import it.polito.elite.enocean.enj.eep.Rorg;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * This class epresents a data telegram transferred between two EnOcean devices,
 * one of which is typically a gateway. According to the EnOcean Equipment
 * Profile specification, telegrams can be of 5 different types: 1BS, 4BS, RBS,
 * UTE and VLD. For each type a subclass of this {@link EEP26Telegram} abstract
 * class is defined.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public abstract class EEP26Telegram
{
	// the telegram type
	private EEP26TelegramType type;

	// the raw (link layer) packet wrapped by this instance
	protected ESP3Packet rawPacket;

	// the data payload
	protected byte payload[];

	// the device address
	protected byte address[];

	// the status byte
	protected byte status;

	// the packet Rorg
	protected Rorg rorg;

	/**
	 * Create a new {@link EEP26Telegram} of the given {@link EEP26TelegramType}
	 * .
	 * 
	 * @param type
	 *            The telegram type, either VLD, UTE, RBS, OneBS or FourBS
	 */
	public EEP26Telegram(EEP26TelegramType type)
	{
		this.type = type;
	}

	/**
	 * Get the type of this {@link EEP26Telegram} instance.
	 * 
	 * @return The telegram type as {@link EEP26TelegramType}.
	 */
	public EEP26TelegramType getTelegramType()
	{
		return this.type;
	}

	/**
	 * @return the type
	 */
	public EEP26TelegramType getType()
	{
		return type;
	}

	/**
	 * @return the payload
	 */
	public byte[] getPayload()
	{
		return payload;
	}

	/**
	 * @return the address
	 */
	public byte[] getAddress()
	{
		return address;
	}

	/**
	 * @return the status
	 */
	public byte getStatus()
	{
		return status;
	}

	/**
	 * @return the rorg
	 */
	public Rorg getRorg()
	{
		return rorg;
	}
}
