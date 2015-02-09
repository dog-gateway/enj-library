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
package it.polito.elite.enocean.enj.eep.eep26.D2.D201;

/**
 * @author bonino
 *
 */
public enum D201ErrorLevel
{
	HARDWARE_OK ((byte)0x00),
	HARDWARE_WARNING ((byte)0x01),
	HARDWARE_FAILURE ((byte)0x02),
	NOT_SUPPORTED ((byte)0x03);
	

	private final byte code;
	
	private D201ErrorLevel(byte modCode)
	{
		this.code = modCode;
	}
	
	public byte getCode()
	{
		return this.code;
	}
	
	public static D201ErrorLevel valueOf(byte value)
	{
		D201ErrorLevel parsedLevel = null;
		for(D201ErrorLevel level : D201ErrorLevel.values())
		{
			if(level.getCode()==value)
			{
				parsedLevel = level;
				break;
			}
		}
		
		if(parsedLevel==null)
			throw new IllegalArgumentException();
		
		return parsedLevel;
	}
}
