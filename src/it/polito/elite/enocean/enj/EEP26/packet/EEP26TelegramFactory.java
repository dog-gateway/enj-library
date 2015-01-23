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
package it.polito.elite.enocean.enj.EEP26.packet;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author bonino
 * 
 * TODO: exploit reflection or static registration to support other classes
 *
 */
public class EEP26TelegramFactory
{
	public static EEP26Telegram getEEP26Telegram(ESP3Packet pkt)
	{
		EEP26Telegram telegram = null;
		if(UTETeachInTelegram.isUTETeachIn(pkt))
		{
			telegram = new UTETeachInTelegram(pkt);
		}
		else if (VLDTelegram.isVLDPacket(pkt))
		{
			// handle VLD packets, search the destination device
			telegram = new VLDTelegram(pkt);
		}
		else if (RPSTelegram.isRPSPacket(pkt))
		{
			telegram = new RPSTelegram(pkt);
		}
		else if (OneBSTelegram.is1BSPacket(pkt))
		{
			telegram = new OneBSTelegram(pkt);
		}
		else if (FourBSTelegram.is4BSPacket(pkt))
		{
			telegram = new FourBSTelegram(pkt);
		}

		return telegram;
	}
}
