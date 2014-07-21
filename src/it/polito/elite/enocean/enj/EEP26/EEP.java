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
package it.polito.elite.enocean.enj.EEP26;

import it.polito.elite.enocean.enj.EEP26.functions.EEPFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author bonino
 * @param <T>
 * @param <T>
 * 
 */
public abstract class EEP
{

	// the EnOcean Equipment Profile version
	// TODO: handle this as a real version
	private String version;

	// the set of functions associated to this profile
	protected HashMap<String, Set<EEPFunction>> functions;

	/**
	 * 
	 */
	public EEP(String version)
	{
		// store the version number
		this.version = new String(version);

		// initialize the functions map
		this.functions = new HashMap<String, Set<EEPFunction>>();
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * Lists the unique names of all functions associated to this EEP
	 * 
	 * @return
	 */
	public Set<String> listAllFunctionNames()
	{
		return this.functions.keySet();
	}

	/**
	 * Lists all functions associated to this EEP, typically the {@link Set}
	 * contained in the returned {@link Collection} contain one element only,
	 * except for cases in which same functions might have more instances
	 * (multiple channel deviceS).
	 * 
	 * @return
	 */
	public Collection<Set<EEPFunction>> getAllFunctions()
	{
		return this.functions.values();
	}

	/**
	 * Get the function instances associated to the given function name.
	 * 
	 * @param functionName
	 *            The function name (typically obtained through reflection:
	 *            EEPFunctionClass.class.getSimpleName()).
	 * @return The set of {@link EEPFunction}s associated to given function name.
	 */
	public Set<EEPFunction> getFunction(String functionName)
	{
		return functionName != null ? this.functions.get(functionName) : null;
	}

	/**
	 * Add a function to the set of functions assoxiated to this EEP.
	 * @param function
	 */
	public void addFunction(EEPFunction function)
	{
		// get the function name
		String functionName = function.getClass().getSimpleName();

		// prepare a variable for holding the functionset associated to the
		// given function (type)
		Set<EEPFunction> functionSet = null;
		if (this.functions.containsKey(functionName))
		{
			// get the set if exists
			functionSet = this.functions.get(functionName);
		}
		else
		{
			// create a new set
			functionSet = new HashSet<EEPFunction>();

			// store the set into the EEP map of functions
			this.functions.put(functionName, functionSet);
		}

		// add the function
		functionSet.add(function);
	}

	/**
	 * Return the eep identifier associated to this EEP
	 * 
	 * @return
	 */
	public abstract EEPIdentifier getEEPIdentifier();
}
