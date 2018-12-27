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
package it.polito.elite.enocean.examples;

import java.util.logging.Logger;

import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeListener;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ActuatorObstructed;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26BatteryCapacity;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26CoverOpen;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26EnergyInputEnabled;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26EnergyStorage;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ServiceOn;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureSensorFailure;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ValvePosition;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26WindowOpen;

/**
 * @author bonino
 *
 */
public class SimpleValveListener implements EEPAttributeChangeListener
{
	private Logger logger;

	/**
	 * 
	 */
	public SimpleValveListener()
	{
		this.logger = Logger.getLogger(SimpleValveListener.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEPAttributeChangeListener#
	 * handleAttributeChange(int, it.polito.elite.enocean.enj.eep.EEPAttribute)
	 */
	@Override
	public void handleAttributeChange(int channelId, EEPAttribute<?> attribute)
	{
		if (attribute instanceof EEP26ValvePosition)
		{
			this.logger
			        .info("Received valve position: " + ((EEP26ValvePosition) attribute).getValue()
			                + " " + ((EEP26ValvePosition) attribute).getUnit());
		}
		if (attribute instanceof EEP26ServiceOn)
		{
			this.logger.info("Received service on: " + ((EEP26ServiceOn) attribute).getValue());
		}
		if (attribute instanceof EEP26EnergyInputEnabled)
		{
			this.logger.info("Received energy input enabled: "
			        + ((EEP26EnergyInputEnabled) attribute).getValue());
		}
		if (attribute instanceof EEP26EnergyStorage)
		{
			this.logger.info(
			        "Received energy storage: " + ((EEP26EnergyStorage) attribute).getValue());
		}
		if (attribute instanceof EEP26BatteryCapacity)
		{
			this.logger.info(
			        "Received battery capacity: " + ((EEP26BatteryCapacity) attribute).getValue());
		}
		if (attribute instanceof EEP26CoverOpen)
		{
			this.logger.info("Received cover open: " + ((EEP26CoverOpen) attribute).getValue());
		}
		if (attribute instanceof EEP26TemperatureSensorFailure)
		{
			this.logger.info("Received temperature sensor failure: "
			        + ((EEP26TemperatureSensorFailure) attribute).getValue());
		}
		if (attribute instanceof EEP26WindowOpen)
		{
			this.logger.info("Received window open: " + ((EEP26WindowOpen) attribute).getValue());
		}
		if (attribute instanceof EEP26ActuatorObstructed)
		{
			this.logger.info("Received actuator obstructed: "
			        + ((EEP26ActuatorObstructed) attribute).getValue());
		}
		if (attribute instanceof EEP26TemperatureLinear)
		{
			this.logger.info(
			        "Received temperature: " + ((EEP26TemperatureLinear) attribute).getValue());
		}

	}
}
