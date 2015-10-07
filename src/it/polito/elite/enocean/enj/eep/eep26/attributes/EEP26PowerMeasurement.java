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

import java.nio.ByteBuffer;

/**
 * A class representing the power measurement function associated to some of the
 * EEPs defined in the 2.6 specification. Power values could either be in W or
 * in kW and they are treated as a number-unit couple with no explicit unit of
 * measurement handling.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class EEP26PowerMeasurement extends EEPAttribute<Double>
{
	// the EEPFunction name
	public static final String NAME = "PowerMeasurement";

	// the supported units of measure
	public static final String W = "W";
	public static final String KW = "kW";

	/**
	 * Basic constructor, builds an {@link EEP26PowerMeasurement} instance with
	 * initial value equal to 0 Wh.
	 */
	public EEP26PowerMeasurement()
	{
		// call the super class constructor
		super(EEP26PowerMeasurement.NAME);

		// default value is 0
		this.value = new Double(0);
		this.unit = EEP26PowerMeasurement.W;
	}

	/**
	 * Constructor, builds an {@link EEP26PowerMeasurement} instance with the
	 * given value and unit, if null or empty values are provided default values
	 * will be used.
	 * 
	 * @param value
	 *            The power value, must be a subclass of {@link Number}.
	 * @param unit
	 *            The unit of measure (as a String)
	 */
	public EEP26PowerMeasurement(Double value, String unit)
	{
		// call the super class constructor
		super(EEP26PowerMeasurement.NAME);

		// set the given value
		this.value = value;

		// set the given unit if not null and not empty
		if ((unit != null)
				&& (!unit.isEmpty())
				&& ((unit.equalsIgnoreCase(EEP26PowerMeasurement.W)) || (unit
						.equalsIgnoreCase(EEP26PowerMeasurement.KW))))
		{
			this.unit = unit;
		}
		else
		{
			this.unit = EEP26PowerMeasurement.W;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.EEP26.functions.EEPFunction#setValue(java
	 * .lang.Object)
	 */
	@Override
	public boolean setValue(Double value)
	{
		// store the current value
		this.value = value;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.EEP26.functions.EEPFunction#setUnit(java.
	 * lang.String)
	 */
	@Override
	public boolean setUnit(String unit)
	{
		boolean stored = false;

		// set the given unit if not null and not empty
		if ((unit != null)
				&& (!unit.isEmpty())
				&& ((unit.equalsIgnoreCase(EEP26PowerMeasurement.W)) || (unit
						.equalsIgnoreCase(EEP26PowerMeasurement.KW))))
		{
			this.unit = unit;

			// set the stored flag at true
			stored = true;
		}

		return stored;
	}
	
	@Override
	public byte[] byteValue()
	{
		//it is likely to never be used...
		
		//use byte buffers to ease double encoding / decoding
		
		// a byte buffer wrapping an array of 4 bytes
		ByteBuffer valueAsBytes = ByteBuffer.wrap(new byte[4]);
		
		// store the current value
		valueAsBytes.putDouble(this.value);
		
		// return the value as byte array
		return valueAsBytes.array();
	}

}
