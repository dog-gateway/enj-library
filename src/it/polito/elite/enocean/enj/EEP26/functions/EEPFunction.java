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
package it.polito.elite.enocean.enj.EEP26.functions;

import java.util.HashSet;
import java.util.Set;

import it.polito.elite.enocean.enj.EEP26.EEP;
import it.polito.elite.enocean.enj.EEP26.EEPIdentifier;

/**
 * The superclass for all functions supported by EEPs. Allows keeping track of
 * the current value associated to the function, i.e., of the current state of a
 * device, with reference to the specific EEP.
 * 
 * @author bonino
 * @param <T>
 *            The value type.
 *
 */
public abstract class EEPFunction<T>
{
	// the function name
	protected String name;

	// the supported EEPs, i.e., the EEPs having this function
	protected Set<String> supportedEEP;

	/**
	 * The class constructor, initializes the data structures shared between all
	 * EEPFunctions, i.e., the name and the set of supported EEPs (storing their
	 * identifiers).
	 */
	public EEPFunction(String name)
	{
		// store the name
		this.name = name;

		// initialize the EEP set
		this.supportedEEP = new HashSet<String>();
	}

	/**
	 * Get the function name, should be one, unique and matching the EEP
	 * specification
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set the function name, should be one, unique and matching the EEP
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
	 * Get a live reference to the set of {@link EEPIdentifier}s of the
	 * {@link EEP}s to which such a function is applicable.
	 * 
	 * @return the supportedEEPs
	 */
	public Set<String> getSupportedEEP()
	{
		return supportedEEP;
	}

	/**
	 * Stores the {@link EEPIdentifier}s of the {@link EEP}s to which such a
	 * function is applicable.
	 * 
	 * @param supportedEEP
	 *            the supportedEEP to set
	 */
	public void setSupportedEEP(Set<String> supportedEEP)
	{
		this.supportedEEP = supportedEEP;
	}

	public abstract T getValue();

	public abstract void setValue(T value);

	public abstract String getUnit();

	public abstract void setUnit();

}
