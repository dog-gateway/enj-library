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
package it.polito.elite.enocean.enj.eep.eep26.A5.A504;

import it.polito.elite.enocean.enj.eep.EEP;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.Rorg;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26HumidityLinear;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class representing the A5-04 family of EnOcean Equipment Profiles
 * (Temperature and Humidity sensors).
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public abstract class A504 extends EEP
{
	// the EEP26 definition, according to the EEP26 specification
	public static final Rorg rorg = new Rorg((byte) 0xa5);
	public static final byte func = (byte) 0x04;

	// func must be defined by extending classes

	// Executor Thread Pool for handling attribute updates
	protected volatile ExecutorService attributeNotificationWorker;

	// -------------------------------------------------
	// Parameters defined by this EEP, which
	// might change depending on the network
	// activity.
	// --------------------------------------------------

	// -------------------------------------------------

	/**
	 * The class constructor
	 */
	public A504()
	{
		// call the superclass constructor
		super("2.6");

		// build the attribute dispatching worker
		this.attributeNotificationWorker = Executors.newFixedThreadPool(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.eep.EEP#handleProfileUpdate(it.polito.elite
	 * .enocean.enj.eep.eep26.telegram.EEP26Telegram)
	 */
	@Override
	public boolean handleProfileUpdate(EEP26Telegram telegram)
	{
		// success flag, initially false
		boolean success = false;

		// handle the telegram, as first cast it at the right type (or fail)
		if (telegram.getTelegramType() == EEP26TelegramType.FourBS)
		{
			// cast the telegram to handle to its real type
			FourBSTelegram profileUpdate = (FourBSTelegram) telegram;

			// get the packet payload
			byte[] payload = profileUpdate.getPayload();

			// wrap the payload as a temperature and humidity message
			A504TemperatureAndHumidityMessage msg = new A504TemperatureAndHumidityMessage(
					payload);

			// ----- handle temperature values

			// update the value of the attribute
			EEP26TemperatureLinear tLinear = (EEP26TemperatureLinear) this
					.getChannelAttribute(0, EEP26TemperatureLinear.NAME);

			// check not null
			if (tLinear != null)
			{
				int rawT = msg.getTemperature();

				// check range
				if ((rawT >= 0)
						&& (rawT <= EEP26TemperatureLinear.MAX_VALID_RAW))
				{
					// update the attribute value
					tLinear.setRawValue(rawT);

					// build the dispatching task
					EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
							tLinear, 1);

					// submit the task for execution
					this.attributeNotificationWorker.submit(dispatcherTask);

					// update the success flag
					success = true;
				}
			}

			// ----- handle temperature values

			// update the value of the attribute
			EEP26HumidityLinear hLinear = (EEP26HumidityLinear) this
					.getChannelAttribute(0, EEP26HumidityLinear.NAME);

			// check not null
			if (hLinear != null)
			{
				int rawH = msg.getHumidity();

				// check range
				if ((rawH >= 0) && (rawH <= EEP26HumidityLinear.MAX_VALID_RAW))
				{
					// update the attribute value
					hLinear.setRawValue(rawH);

					// build the dispatching task
					EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
							hLinear, 1);

					// submit the task for execution
					this.attributeNotificationWorker.submit(dispatcherTask);

					// update the success flag
					success = true;
				}
			}

		}

		return success;
	}

}
