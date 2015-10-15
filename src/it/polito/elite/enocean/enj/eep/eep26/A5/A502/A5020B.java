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
public class A5020B extends A502
{

	// the type definition
	public static final byte type = (byte) 0x0b;

	// the used channel
	public static int CHANNEL = 0;

	/**
	 * @param version
	 */
	public A5020B()
	{
		super();

		// add attributes A5020B has operative range between 60 and 100 Celsius
		this.addChannelAttribute(A5020B.CHANNEL,
				new EEP26TemperatureInverseLinear(60.0, 100.0));
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
		return new EEPIdentifier(A502.rorg, A502.func, A5020B.type);

	}

}
