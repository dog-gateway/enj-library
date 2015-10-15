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
package it.polito.elite.enocean.enj.eep.eep26.A5.A502;

import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureInverseLinear;

/**
 * @author bonino
 *
 */
public class A50208 extends A502
{

	// the type definition
	public static final byte type = (byte) 0x08;

	// the used channel
	public static int CHANNEL = 0;

	/**
	 * @param version
	 */
	public A50208()
	{
		super();

		// add attributes A50208 has operative range between 30.0 and 70.0
		// Celsius
		this.addChannelAttribute(A50208.CHANNEL,
				new EEP26TemperatureInverseLinear(30.0, 70.0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEP#getEEPIdentifier()
	 */
	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		// return the EEPIdentifier for this profile
		return new EEPIdentifier(A502.rorg, A502.func, A50208.type);

	}

}
