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
 * A byte-valued enumeration representing the possible default states for a
 * switching device.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public enum D201DefaultStateValue
{
	OFF((byte) 0x00), ON((byte) 0x01), PREVIOUS_STATE((byte) 0x02), NOT_USED(
			(byte) 0x03);

	private final byte code;

	private D201DefaultStateValue(byte modCode)
	{
		this.code = modCode;
	}

	public byte getCode()
	{
		return this.code;
	}
}
