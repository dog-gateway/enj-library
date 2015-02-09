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
public class EEP26DimLevel extends EEPAttribute<Integer>
{
	public static final String NAME = "DimLevel";

	/**
	 * Class constructor, builds an {@link EEP26DimLevel} instance initialized
	 * at the default dimming level of 0%
	 */
	public EEP26DimLevel()
	{
		super(EEP26DimLevel.NAME);

		// default value 0%
		this.value = 0;
	}

	public EEP26DimLevel(int value)
	{
		super(EEP26DimLevel.NAME);

		// store the value
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEPAttribute#byteValue()
	 */
	@Override
	public byte[] byteValue()
	{
		// the value is only one byte
		// TODO check result here (working with binary data could provide
		// "unexpected" results
		return new byte[] { this.value.byteValue() };
	}

}
