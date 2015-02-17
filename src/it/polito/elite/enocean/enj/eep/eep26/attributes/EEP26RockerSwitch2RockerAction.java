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
package it.polito.elite.enocean.enj.eep.eep26.attributes;

import it.polito.elite.enocean.enj.eep.EEPAttribute;

import java.util.Arrays;

/**
 * A class representing the payload of a rocker switch message for profile types
 * 01 and 02.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEP26RockerSwitch2RockerAction extends EEPAttribute<Boolean[]>
{
	public static int AI = 0;
	public static int AO = 1;
	public static int BI = 2;
	public static int BO = 3;

	public static String NAME = "RockerSwitch2Rocker";

	/**
	 * Class constructor, builds a default
	 * {@link EEP26RockerSwitch2RockerAction} instance, with all button values
	 * at false.
	 */
	public EEP26RockerSwitch2RockerAction()
	{
		super(EEP26RockerSwitch2RockerAction.NAME);

		// build the initial value
		this.value = new Boolean[] { false, false, false, false };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.eep.EEPAttribute#setValue(java.lang.Object)
	 */
	@Override
	public boolean setValue(Boolean[] value)
	{
		// deep copy
		this.value = Arrays.copyOf(value, value.length);

		// success
		return true;
	}

	/**
	 * Sets the current value of one of the RockerSwitch buttons
	 * 
	 * @param buttonId
	 *            The button id (static public variables in
	 *            {@link EEP26RockerSwitch2RockerAction}).
	 * @param value
	 *            The boolean value to set (true = pressed, false = released)
	 */
	public void setButtonValue(int buttonId, boolean value)
	{
		this.value[buttonId] = value;
	}

	/**
	 * Gets the current value of one of the RockerSwitch buttons.
	 * 
	 * @param buttonId
	 *            The id of the button (static public variables in
	 *            {@link EEP26RockerSwitch2RockerAction}) for which a value must
	 *            be retrieved.
	 * @return The value as a boolean (true = pressed, false= released)
	 */
	public boolean getButtonValue(int buttonId)
	{
		return this.value[buttonId];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEPAttribute#byteValue()
	 */
	@Override
	public byte[] byteValue()
	{
		// TODO Auto-generated method stub
		// not used for rocker switches
		return null;
	}

}
