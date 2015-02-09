/*
 * EnJ - EnOcean Java API
 * 
 * Copyright 2014 Dario Bonino 
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
 * A class representing the capability of a device to be locally controlled.
 * Such a capability may either be enabled or disabled depending on
 * configuration and application needs.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEP26LocalControl extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "LocalControl";

	// the possible values
	public static final boolean ENABLED = true;
	public static final boolean DISABLED = false;

	public EEP26LocalControl()
	{
		// call the superclass constructor
		super(EEP26LocalControl.NAME);

		// set the default value
		this.value = EEP26LocalControl.DISABLED;
	}

	public EEP26LocalControl(Boolean value)
	{
		// call the superclass constructor
		super(EEP26LocalControl.NAME);

		// set the value
		this.value = value;
	}

	@Override
	public byte[] byteValue()
	{
		// by default is disabled
		byte value = 0x00;

		// if value is true than the local control is enabled and the value
		// should be 0b1 == 0x01
		if (this.value)
			value = 0x01;

		return new byte[]{value};
	}

}
