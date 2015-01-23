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
 */
public class RPSTelegram extends EEP26Telegram
{

	public RPSTelegram(ESP3Packet pkt)
	{
		super(TelegramType.RPS);
		// TODO Auto-generated constructor stub
	}

	public static boolean isRPSPacket(ESP3Packet pkt)
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
