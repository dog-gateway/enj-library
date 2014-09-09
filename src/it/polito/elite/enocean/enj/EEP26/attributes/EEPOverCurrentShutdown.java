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
 * A class representing the capability of a switching device to act upon over
 * current detection. Allowed actions include turning statically to the off
 * state with manual reset (STATIC_OFF) or automatically restarting the actuator
 * (AUTOMATIC_RESTART).
 * 
 * @author bonino <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEPOverCurrentShutdown extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "OverCurrentShutdown";

	// the possible values
	public static final boolean STATIC_OFF = false;
	public static final boolean AUTOMATIC_RESTART = true;

	public EEPOverCurrentShutdown()
	{
		// call the super class constructor
		super(EEPOverCurrentShutdown.NAME);

		// the default value is set at STATIC_OFF,
		// TODO: check if the assumption is valid or not
		this.value = EEPOverCurrentShutdown.STATIC_OFF;
	}
	
	public EEPOverCurrentShutdown(Boolean value)
	{
		//call the super class constructor
		super(EEPOverCurrentShutdown.NAME);
		
		//set the given value
		this.value = value;
	}
}
