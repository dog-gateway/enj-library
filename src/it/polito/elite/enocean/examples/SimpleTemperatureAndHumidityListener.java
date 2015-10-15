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

import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeListener;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26HumidityLinear;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;

import java.util.logging.Logger;

/**
 * @author bonino
 *
 */
public class SimpleTemperatureAndHumidityListener implements
		EEPAttributeChangeListener
{
	private Logger logger;
	
	/**
	 * 
	 */
	public SimpleTemperatureAndHumidityListener()
	{
		this.logger = Logger.getLogger(SimpleTemperatureListener.class.getName());
	}

	/* (non-Javadoc)
	 * @see it.polito.elite.enocean.enj.eep.EEPAttributeChangeListener#handleAttributeChange(int, it.polito.elite.enocean.enj.eep.EEPAttribute)
	 */
	@Override
	public void handleAttributeChange(int channelId, EEPAttribute<?> attribute)
	{
		if (attribute instanceof EEP26TemperatureLinear)
		{
			this.logger.info("Received temperature: "
					+ ((EEP26TemperatureLinear) attribute).getValue() + " "
					+ ((EEP26TemperatureLinear) attribute).getUnit());
		}
		else if (attribute instanceof EEP26HumidityLinear)
		{
			this.logger.info("Received humidity: "
					+ ((EEP26HumidityLinear) attribute).getValue() + " "
					+ ((EEP26HumidityLinear) attribute).getUnit());
		}

	}

}
