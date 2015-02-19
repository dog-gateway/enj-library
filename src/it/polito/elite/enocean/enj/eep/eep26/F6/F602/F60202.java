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
package it.polito.elite.enocean.enj.eep.eep26.F6.F602;


/**
 * A class representing devices belonging to the F6-02-02 profile, it is exactly
 * the same as the {@link F60201} class except for the type which changes from
 * 01 to 02.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class F60202 extends F60201
{

	/**
	 * class version number for serialization / de-serialization
	 */
	private static final long serialVersionUID = 1L;

	// the type definition
	public static final byte type = (byte) 0x02;

	// register the type in the EEPProfile even if no instance of this class is
	// created.
	/*static
	{
		EEPRegistry.getInstance().addProfile(
				new EEPIdentifier(F602.rorg, F602.func, F60202.type),
				F60202.class);
	}*/
}
