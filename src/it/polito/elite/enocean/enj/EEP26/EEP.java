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

/**
 * @author bonino
 * 
 */
public abstract class EEP
{

	// the EnOcean Equipment Profile version
	// TODO: handle this as a real version
	private String version;

	/**
	 * 
	 */
	public EEP(String version)
	{
		// store the version number
		this.version = new String(version);
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

}
