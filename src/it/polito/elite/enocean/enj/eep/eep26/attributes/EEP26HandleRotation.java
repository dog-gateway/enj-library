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
 * @author bonino
 *
 */
public class EEP26HandleRotation extends
		EEPAttribute<EEP26HandleRotation.HandlePositions>
{

	// the EEPFunction name
	public static final String NAME = "WindowHandle";

	// the Movement status as a public inner enum, this will probably be changed
	// in the future.
	public enum HandlePositions
	{
		OPEN, CLOSE, TILTED,
	}

	/**
	 * @param name
	 */
	public EEP26HandleRotation()
	{
		// call the super class constructor
		super(EEP26HandleRotation.NAME);

		// set the default value at closed
		this.value = EEP26HandleRotation.HandlePositions.CLOSE;
	}

	public EEP26HandleRotation(EEP26HandleRotation.HandlePositions position)
	{
		// call the super class constructo
		super(EEP26HandleRotation.NAME);

		// set the inner value at the given one
		this.value = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEPAttribute#byteValue()
	 */
	@Override
	public byte[] byteValue()
	{
		// by default is undefined
		byte value = 0x00;

		// define the byte value depending on the currently stored value
		switch (this.value)
		{
			case CLOSE:
			{
				// stems from the EnOcean specification where XX have been set
				// at 0
				value = (byte) 0b11110000;
				break;
			}

			case OPEN:
			{
				// stems from the EnOcean specification where XX have been set
				// at 0
				value = (byte) 0b11000000;
				break;
			}
			case TILTED:
			{
				// stems from the EnOcean specification where XX have been set
				// at 0
				value = (byte) 0b11010000;
				break;
			}
		}

		return new byte[] { value };
	}

}
