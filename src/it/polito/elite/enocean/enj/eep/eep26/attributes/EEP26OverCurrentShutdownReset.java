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
 * A class representing the behavior of a device upon an over current shutdown.
 * It can either stay turned off until manual switching is performed or it can
 * be reset to the original state through a trigger signal.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEP26OverCurrentShutdownReset extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "ResetOverCurrentShutdown";

	// the possible values
	public static final boolean NOT_ACTIVE = false;
	public static final boolean TRIGGER_SIGNAL = true;

	public EEP26OverCurrentShutdownReset()
	{
		// call the superclass constructor
		super(EEP26OverCurrentShutdownReset.NAME);

		// set the default value at NOT_ACTIVE
		this.value = NOT_ACTIVE;
	}

	public EEP26OverCurrentShutdownReset(Boolean value)
	{
		// call the super class constructor
		super(EEP26OverCurrentShutdownReset.NAME);

		// set the given value
		this.value = value;

	}
	
	@Override
	public byte[] byteValue()
	{
		// by default is disabled
		byte value = 0x00;

		// if value is true than the local control is enabled and the value
		// should be 0b1 == 0x01
		if (this.value == EEP26OverCurrentShutdownReset.TRIGGER_SIGNAL)
			value = 0x01;

		return new byte[]{value};
	}
}
