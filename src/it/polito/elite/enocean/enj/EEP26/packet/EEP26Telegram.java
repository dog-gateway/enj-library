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


/**
 * @author bonino
 *
 */
public abstract class EEP26Telegram
{
	private TelegramType type;
	
	public EEP26Telegram(TelegramType type)
	{
		this.type = type;
	}
	
	public TelegramType getTelegramType()
	{
		return this.type;
	}
	
	public abstract byte[] getAddress();
}
