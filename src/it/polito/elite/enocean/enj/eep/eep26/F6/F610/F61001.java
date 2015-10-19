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

import it.polito.elite.enocean.enj.eep.EEPIdentifier;

/**
 * A class representing devices belonging to the F6-10-01 profile, it is exactly
 * the same as the {@link F61000} class except for the type which changes from
 * 00 to 01.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * */
public class F61001 extends F61000
{

	// the type definition
	public static final byte type = (byte) 0x01;

	public F61001()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.eep.eep26.F6.F602.F60201#getEEPIdentifier()
	 */
	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		return new EEPIdentifier(F610.rorg, F610.func, F61001.type);
	}
}
