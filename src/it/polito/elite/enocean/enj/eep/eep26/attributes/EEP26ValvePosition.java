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

/**
 * @author bonino
 *
 */
public class EEP26ValvePosition extends EEPAttribute<Integer>
{
	// the EEPFunction name
	public static final String NAME = "ValvePosition";

	// the allowed range
	private Integer min;
	private Integer max;

	/**
	 * @param name
	 */
	public EEP26ValvePosition()
	{
		super(EEP26ValvePosition.NAME);

		// set the default value
		this.value = 0;
		this.unit = "%";
		this.min = 0;
		this.max = 100;
	}

	public EEP26ValvePosition(Integer value, String unit)
	{
		super(EEP26ValvePosition.NAME);

		if ((unit != null)

		        && (!unit.isEmpty()) && ((unit.equalsIgnoreCase("%"))))
		{
			// store the value
			this.value = value;

			// store the unit
			this.unit = unit;

			// set the maximum range
			this.min = 0;
			this.max = 100;
		}

		else
		{
			throw new NumberFormatException(
			        "Wrong unit or null value for supply voltage in Volt (V)");
		}

	}

	public EEP26ValvePosition(Integer minV, Integer max)
	{
		super(EEP26ValvePosition.NAME);

		// default value 0%
		this.value = 0;
		this.unit = "V";
		this.min = minV;
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public int getMin()
	{
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(int min)
	{
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public int getMaxV()
	{
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMaxV(int max)
	{
		this.max = max;
	}

	/*
	 * @see it.polito.elite.enocean.enj.eep.EEPAttribute#setValue
	 */
	@Override
	public boolean setValue(Integer value)
	{
		boolean stored = false;

		if (value instanceof Number)
		{
			// store the current value
			this.value = value;

			// updated the operation result
			stored = true;
		}

		return stored;
	}

	/*
	 * @see it.polito.elite.enocean.enj.eep.EEPAttribute#setUnit
	 */
	@Override
	public boolean setUnit(String unit)
	{
		boolean stored = false;

		if ((unit != null)

		        && (!unit.isEmpty()) && ((unit.equalsIgnoreCase("%"))))
		{

			// store the unit
			this.unit = unit;

			// set the stored flag at true
			stored = true;
		}

		return stored;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEPAttribute#byteValue()
	 */
	@Override
	public byte[] byteValue()
	{
		return new byte[] { this.value.byteValue() };
	}

	/**
	 * Checks if the current attribute represents a value in the declared valid
	 * range or not.
	 * 
	 * @return
	 */
	public boolean isValid()
	{
		return ((this.value >= this.min) && (this.value <= this.max));
	}

}
