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
 * A factory class for genetaring proper {@link EEP26Telegram} instances given a
 * low level {@link ESP3Packet} containing a telegram as payload. While it
 * currently works only on the fixed set of telegrams defined in the eEP2.6
 * specification, it will be extended to automatically include newly added
 * telegram types.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * 
 *         TODO: exploit reflection or static registration to support other
 *         classes (the Reflections library might be a good candidate)
 *
 */
public class EEP26TelegramFactory
{
	/**
	 * Provides the right instance of {@link EEP26Telegram} given an
	 * {@link ESP3Packet} containing the telegram as payload.
	 * 
	 * @param pkt
	 *            The origin {@link ESP3Packet}.
	 * @return The {@link EEP26Telegram} instance representing the
	 *         {@link ESP3Packet} payload or null if no telegram can be suitably
	 *         mapped to the payload.
	 */
	public static EEP26Telegram getEEP26Telegram(ESP3Packet pkt)
	{
		EEP26Telegram telegram = null;

		// handle UTE Teach-In telegrams
		if (UTETeachInTelegram.isUTETeachIn(pkt))
		{
			telegram = new UTETeachInTelegram(pkt);
		}

		// handle VLD telegrams
		else if (VLDTelegram.isVLDPacket(pkt))
		{
			// handle VLD packets, search the destination device
			telegram = new VLDTelegram(pkt);
		}

		// handle RPS telegrams
		else if (RPSTelegram.isRPSPacket(pkt))
		{
			telegram = new RPSTelegram(pkt);
		}

		// handle 1BS telegrams
		else if (OneBSTelegram.is1BSPacket(pkt))
		{
			telegram = new OneBSTelegram(pkt);
		}

		// handle 4BS telegrams
		else if (FourBSTelegram.is4BSPacket(pkt))
		{
			telegram = new FourBSTelegram(pkt);
		}

		return telegram;
	}
}
