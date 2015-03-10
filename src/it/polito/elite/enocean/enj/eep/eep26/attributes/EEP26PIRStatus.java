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
package it.polito.elite.enocean.enj.eep.eep26.attributes;

import it.polito.elite.enocean.enj.eep.EEPAttribute;

/**
 * A class representing the PIR detection status associated to occupancy
/** sensors
 * @author bonino
 *
 */
public class EEP26PIRStatus extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "PIRStatus";

	// the human readable values
	public static final boolean ON = true;
	public static final boolean OFF = false;

	/**
	 * @param name
	 */
	public EEP26PIRStatus()
	{
		// call the super class constructor
		super(EEP26PIRStatus.NAME);

		// by default the function starts at OFF
		this.value = EEP26PIRStatus.OFF;
	}

	/**
	 * Constructor, builds a new {@link EEP26PIRStatus} instance with the given
	 * value (true == on, false == off).
	 * 
	 * @param value
	 */
	public EEP26PIRStatus(boolean value)
	{
		// call the super class constructor
		super(EEP26PIRStatus.NAME);

		// by default the function starts at OFF
		this.value = value;
	}

	@Override
	public byte[] byteValue()
	{
		// by default is disabled
		byte value = 0x00;

		// if value is true than the local control is enabled and the value
		// should be 0b1 == 0x01
		if (this.value == EEP26PIRStatus.ON)
			value = 0x01;

		return new byte[]{value};
	}
}
