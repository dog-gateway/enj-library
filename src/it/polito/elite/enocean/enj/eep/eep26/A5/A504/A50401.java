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
package it.polito.elite.enocean.enj.eep.eep26.A5.A504;

import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26HumidityLinear;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;

/**
 * @author bonino
 *
 */
public class A50401 extends A504
{
	// the type definition
	public static final byte type = (byte) 0x01;

	// the used channel
	public static int CHANNEL = 0;

	/**
	 * 
	 */
	public A50401()
	{
		super();

		// add attributes A50204 has operative range between 0.0 and 40 Celsius
		this.addChannelAttribute(A50401.CHANNEL, new EEP26TemperatureLinear(
				0.0, 40.0));
		// and between 0 and 100% humidity
		this.addChannelAttribute(A50401.CHANNEL, new EEP26HumidityLinear(0.0,
				100.0));
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
		return new EEPIdentifier(A504.rorg, A504.func, A50401.type);
	}

}
