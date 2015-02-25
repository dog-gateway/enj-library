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
 * A class representing a 4BS telegram as defined in the EEP2.6 specification.
 * It provides means for parsing / encoding packets and to extract relevant data
 * encoded as data-payload in packet.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class FourBSTelegram extends EEP26Telegram
{

	/**
	 * Class constructor, builds an instance of {@link FourBSTelegram} given the
	 * {@link ESP3Packet} containing the telegram as payload.
	 * 
	 * @param pkt
	 *            The {@link ESP3Packet} containing the telegram as payload.
	 */
	public FourBSTelegram(ESP3Packet pkt)
	{
		super(EEP26TelegramType.FourBS);

		// store the raw packet wrapped by this VLDPacket instance
		this.rawPacket = pkt;

		// get the raw, un-interpreted data payload,
		// for 4BS packets the payload length is 4 bytes
		byte rawData[] = this.rawPacket.getData();

		// 4 byte payload for all 4BS messages
		this.payload = new byte[4];

		// fill the payload
		int startingOffset = 1;
		for (int i = startingOffset; i < startingOffset + this.payload.length; i++)
		{
			// copy byte by byte
			this.payload[i - startingOffset] = rawData[i];
		}

		// intialize the packet address
		this.address = new byte[4];

		// get the actual address
		startingOffset = 1 + this.payload.length;
		for (int i = startingOffset; i < (startingOffset + this.address.length); i++)
		{
			// not needed
			this.address[i - startingOffset] = rawData[i];
		}

		// build the actual Rorg
		this.rorg = new Rorg(rawData[0]);

		// store the status byte
		this.status = rawData[startingOffset + this.address.length]; // shall be
																		// equal
																		// to
																		// rawData.length-
	}

	/**
	 * Checks if the given {@link ESP3Packet} contains a 4BS packet as payload.
	 * 
	 * @param pkt
	 *            The {@link ESP3Packet} to check
	 * @return true if it contains a 4BS telegram, false otherwise.
	 */
	public static boolean is4BSPacket(ESP3Packet pkt)
	{
		// the packet should be a radio packet with a specific value in the
		// first byte of the data payload (RORG).
		return (pkt.isRadio()) && (pkt.getData()[0] == Rorg.BS4);
	}

}
