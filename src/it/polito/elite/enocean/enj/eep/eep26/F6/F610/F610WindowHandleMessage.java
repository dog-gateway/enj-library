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
package it.polito.elite.enocean.enj.eep.eep26.F6.F610;

import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26HandleRotation;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26HandleRotation.HandlePositions;

/**
 * @author bonino
 *
 */
public class F610WindowHandleMessage
{

	// the handle position indicated by the message
	private EEP26HandleRotation.HandlePositions position;

	// the message validity flag
	private boolean valid;

	/**
	 * Builds a WindowHandle message biven the byte payload of the corresponding
	 * radio message (RPS)
	 */
	public F610WindowHandleMessage(byte data[])
	{
		// initially valid
		this.valid = true;

		// check open 0b11X0XXXX
		if ((data[0] & 0b11010000) == 0b11000000)
			this.position = HandlePositions.OPEN;
		// check close 0b1111XXXX
		else if ((data[0] & 0b11110000) == 0b11110000)
			this.position = HandlePositions.CLOSE;
		// check tilted 0b1101XXXX
		else if ((data[0] & 0b11110000) == 0b11010000)
			this.position = HandlePositions.TILTED;
		else
			this.valid = false;
	}

	/**
	 * Provides the abstract handle position corresponding to the wrapped byte
	 * data
	 * 
	 * @return the position
	 */
	public EEP26HandleRotation.HandlePositions getPosition()
	{
		return position;
	}

	/**
	 * Provides the validity flag value
	 * 
	 * @return true if valid, false otherwise
	 */
	public boolean isValid()
	{
		return valid;
	}

}
