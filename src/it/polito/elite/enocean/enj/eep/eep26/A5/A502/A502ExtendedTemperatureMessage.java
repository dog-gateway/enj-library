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
 * A class representing the temperature measurement message sent by a sensor
 * belonging to the A502 profile set. Basically, every message set by devices
 * having a profile stemming from A502 use the same structure and the only
 * difference is given the temperature linear range.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class A502ExtendedTemperatureMessage
{
	private int temperature;
	private boolean teachIn;

	/**
	 * Class constructor, builds a message instance given the raw byte payload
	 * of the corresponding 4BS telegram.
	 */
	public A502ExtendedTemperatureMessage(byte data[])
	{
		// temperature data takes 10bits and has offset 14 (2nd+3rd byte)
		byte temperatureHigh = (byte) (data[1] & (byte)0x03);
		byte temperatureLow = data[2];

		// transform into a positive integer
		// uses logic or as shitf towards left fills number with 0s
		this.temperature = (temperatureHigh << 8) | (0x00FF & temperatureLow); // TODO: check if the
													// conversion is right

		// get the teach-in flag (offset 28, 4th bit of the 4th byte)
		byte teachIn = (byte) ((byte) (data[3] & (byte) 0x08) >> 3);

		// check the corresponding boolean value
		if (teachIn == 0)
			this.teachIn = true;
		else
			this.teachIn = false;
	}

	/**
	 * Get the temperature value "transferred" by means of this messageas an integer between 0 and 255.
	 * @return the temperature as an integer, between 0 and 255
	 */
	public int getTemperature()
	{
		return temperature;
	}

	/**
	 * Get the teach-in status 
	 * @return the teachIn, true if teach-in is active, false otherwise.
	 */
	public boolean isTeachIn()
	{
		return teachIn;
	}

}
