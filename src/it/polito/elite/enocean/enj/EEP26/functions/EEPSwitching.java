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

import it.polito.elite.enocean.enj.EEP26.EEP;
import it.polito.elite.enocean.enj.EEP26.EEPIdentifier;
import it.polito.elite.enocean.enj.EEP26.D2.D201.D201;
import it.polito.elite.enocean.enj.EEP26.D2.D201.D20108;

/**
 * @author bonino
 *
 */
public class EEPSwitching extends EEPFunction<Boolean>
{

	public static final String NAME = "Switching";
	
	public EEPSwitching()
	{
		//call the super class constructor
		super(EEPSwitching.NAME);
		
		//store supported EEPs
		this.supportedEEP.add(D201.class.getSimpleName());
		this.supportedEEP.add(D20108.class.getSimpleName());
	}

	@Override
	public Boolean getValue()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(Boolean value)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUnit()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUnit()
	{
		// TODO Auto-generated method stub
		
	}

	

}
