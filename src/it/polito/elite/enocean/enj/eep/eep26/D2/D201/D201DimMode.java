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
public enum D201DimMode
{
	SWITCH_TO_NEW_OUTPUT_VALUE((byte)0x00),
	TIMED_DIM_1((byte)0x01),
	TIMED_DIM_2((byte)0x02),
	TIMED_DIM_3((byte)0x03),
	STOP_DIMMING((byte)0x04);
	
	private final byte code;
	
	private D201DimMode(byte modCode)
	{
		this.code = modCode;
	}
	
	public byte getCode()
	{
		return this.code;
	}
}
