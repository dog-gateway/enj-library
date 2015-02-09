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
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getAddress()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
