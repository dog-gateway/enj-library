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
package it.polito.elite.enocean.enj.EEP26.attributes;

/**
 * A class representing the capability of a device to detect power failures. It
 * might either be enabled or disabled.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEPPowerFailure extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "PowerFailure";

	// the possible values
	public static final boolean DISABLED = false;
	public static final boolean ENABLED = true;
	
	public EEPPowerFailure()
	{
		//call the superclass constructor
		super(EEPPowerFailure.NAME);
		
		//set the default value at disabled
		this.value = EEPPowerFailure.DISABLED;
	}
	
	public EEPPowerFailure(Boolean value)
	{
		//call the superclass constructor
		super(EEPPowerFailure.NAME);
		
		//set the given value
		this.value = value;
	}
	
	@Override
	public byte[] byteValue()
	{
		// by default is disabled
		byte value = 0x00;

		// if value is true than the local control is enabled and the value
		// should be 0b1 == 0x01
		if (this.value == EEPPowerFailure.ENABLED)
			value = 0x01;

		return new byte[]{value};
	}
}
