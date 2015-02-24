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
package it.polito.elite.enocean.enj.eep.eep26.A5.A502;

/**
 * @author bonino
 *
 */
public class A502TemperatureMessage
{
	private int temperature;
	private boolean teachIn;

	/**
	 * 
	 */
	public A502TemperatureMessage(byte data[])
	{
		// temperature data has offset 16 (3rd byte)
		byte temperature = data[2];
		
		// transform into a positive integer
		this.temperature = 0x00FF & temperature; //TODO: check if the conversion is right
		
		// get the teach-in flag (offset 28, 4th bit of the 4th byte)
		byte teachIn = (byte) ((byte)(data[3] & (byte)0x01)>>3);
		
		//check the corresponding boolean value
		if(teachIn == 0)
			this.teachIn = true;
		else
			this.teachIn = false;
	}

	/**
	 * @return the temperature
	 */
	public int getTemperature()
	{
		return temperature;
	}

	/**
	 * @return the teachIn
	 */
	public boolean isTeachIn()
	{
		return teachIn;
	}
	
	

}
