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
 * A class for representing the current user interface lighting mode, can either
 * assume the DAY or NIGHT values.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEP26UserInterfaceMode extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "UserInterfaceMode";

	// the possible values
	public static final boolean NIGHT = true;
	public static final boolean DAY = false;

	public EEP26UserInterfaceMode()
	{
		super(EEP26UserInterfaceMode.NAME);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.EEP26.attributes.EEPAttribute#setValue(java
	 * .lang.Object)
	 */
	@Override
	public boolean setValue(Boolean value)
	{
		this.value = value;

		return true;
	}

	@Override
	public byte[] byteValue()
	{
		// by default is disabled
		byte value = 0x00;

		// if value is true than the local control is enabled and the value
		// should be 0b1 == 0x01
		if (this.value == EEP26UserInterfaceMode.NIGHT)
			value = 0x01;

		return new byte[]{value};
	}
}
