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
 * An enumeration representing valid dimming times as defined in the EnOcean
 * Equipment Profile specification, version 2.6. It offers dimming time between
 * 0.5 and 7.5s at steps of 0.5s.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public enum D201DimTime
{
	DIM05((byte) 0x01), DIM10((byte) 0x02), DIM15((byte) 0x03), DIM20(
			(byte) 0x04), DIM25((byte) 0x05), DIM30((byte) 0x06), DIM35(
			(byte) 0x07), DIM40((byte) 0x08), DIM45((byte) 0x09), DIM50(
			(byte) 0x0a), DIM55((byte) 0x0b), DIM60((byte) 0x0c), DIM65(
			(byte) 0x0d), DIM70((byte) 0x0e), DIM75((byte) 0x0f);

	private final byte code;

	private D201DimTime(byte modCode)
	{
		this.code = modCode;
	}

	/**
	 * Returns the hexadecimal code associated to this enumeration instance
	 * 
	 * @return the hexadecimal code as a byte.
	 */
	public byte getCode()
	{
		return this.code;
	}

	/**
	 * Gets the time in milliseconds represented by this enumeration instance
	 * 
	 * @return The time in milliseconds, as an integer number
	 */
	public long getTimeMillis()
	{
		return ((long) this.code * 500);
	}
}
