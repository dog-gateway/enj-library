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
 * A class representing the capability of a device to be locally controlled.
 * Such a capability may either be enabled or disabled depending on
 * configuration and application needs.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEPLocalControl extends EEPAttribute<Boolean>
{
	// the EEPFunction name
	public static final String NAME = "LocalControl";

	// the possible values
	public static final boolean ENABLED = true;
	public static final boolean DISABLED = false;

	public EEPLocalControl()
	{
		// call the superclass constructor
		super(EEPLocalControl.NAME);

		// set the default value
		this.value = EEPLocalControl.DISABLED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.EEP26.attributes.EEPAttribute#setValue(java
	 * .lang.Object)
	 */
	@Override
	public boolean setValue(Boolean value)
	{
		this.value = value;

		return true;
	}

}
