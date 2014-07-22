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
 * The superclass for all attributes supported by EEPs. Allows keeping track of
 * the current value associated to the attribute, i.e., of the current state of a
 * device, with reference to the specific EEP.
 * 
 * @author bonino
 */
public abstract class EEPAttribute<T>
{
	// the attribute name
	protected String name;

	// the attribute value
	// e.g., power measurement.
	protected T value;

	// The unit of measure for the value, if any.
	protected String unit;

	/**
	 * The class constructor, initializes the data structures shared between all
	 * EEPFunctions, i.e., the name and the set of supported EEPs (storing their
	 * identifiers).
	 */
	public EEPAttribute(String name)
	{
		// store the name
		this.name = name;

		// null unit
		this.unit = null;
	}

	/**
	 * Get the attribute name, should be one, unique and matching the EEP
	 * specification
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set the attribute name, should be one, unique and matching the EEP
	 * specification
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Get the current value of this EEP attribute
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public T getValue()
	{
		return this.value;
	}

	/**
	 * Set the current value of this EEP attribute
	 * 
	 * @param <T>
	 * 
	 * @param value
	 * @return
	 */
	public boolean setValue(T value)
	{
		this.value = value;
		return true;
	}

	/**
	 * Get the unit of measure associated to the value of this attribute, if
	 * available or null otherwise.
	 * 
	 * @return
	 */
	public String getUnit()
	{
		return this.unit;
	}

	/**
	 * Set the unit of measure associated to the attribute value.
	 */
	public boolean setUnit(String unit)
	{
		this.unit = unit;
		return true;
	}
}
