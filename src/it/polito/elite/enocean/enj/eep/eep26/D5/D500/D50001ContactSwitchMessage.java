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
package it.polito.elite.enocean.enj.eep.eep26.D5.D500;

/**
 * @author bonino
 *
 */
public class D50001ContactSwitchMessage
{
	private boolean contactClosed;
	private boolean teachIn;
	private boolean valid;

	/**
	 * 
	 */
	public D50001ContactSwitchMessage(byte data[])
	{
		// initially not valid
		this.valid = false;

		if (data.length == 1)
		{
			//------- single byte data ----------
			
			// decode the teach in status
			byte teachInAsByte = (byte) ((byte) (data[0] & (byte) 0x08) >> 3);
			
			//convert to boolean
			if(teachInAsByte != 0)
				this.teachIn = true;
			else
				this.teachIn = false;
			
			//decode the contact status
			byte contactAsByte = (byte)(data[0] & (byte)0x01);
			
			//convert to boolean
			if(contactAsByte != 0)
				this.contactClosed = true;
			else
				this.contactClosed = false;
			
			
			//everything fine, the message is valid
			this.valid = true;
		}
	}

	/**
	 * @return the contactClosed
	 */
	public boolean isContactClosed()
	{
		return contactClosed;
	}

	/**
	 * @return the teachIn
	 */
	public boolean isTeachIn()
	{
		return teachIn;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid()
	{
		return valid;
	}
	
	

}
