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
package it.polito.elite.enocean.enj.eep.eep26.A5.A520;

import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.A5.A502.A502TemperatureMessage;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

/**
 * @author bonino
 *
 */
public class A52001 extends A520
{
	// the type definition
	public static final byte type = (byte) 0x01;
	public static int CHANNEL = 0;

	/**
	 * 
	 */
	public A52001()
	{
		// call the superclass constructor
		super();

		// add attributes
		this.addChannelAttribute(A52001.CHANNEL,
				new EEP26TemperatureLinear(new Double(0), new Double(40.0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEP#getEEPIdentifier()
	 */
	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		return new EEPIdentifier(A520.rorg, A520.func, A52001.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.eep.EEP#handleProfileUpdate(it.polito.elite.
	 * enocean.enj.eep.eep26.telegram.EEP26Telegram)
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

			// wrap the message into a temperature message
			A502TemperatureMessage setPointMessage = new A502TemperatureMessage(
					payload);

			// update the value of the attribute
			EEP26TemperatureLinear tLinear = (EEP26TemperatureLinear) this
					.getChannelAttribute(0, EEP26TemperatureLinear.NAME);

			// check not null
			if (tLinear != null)
			{
				int rawT = setPointMessage.getTemperature();

				// check range
				if ((rawT >= 0)
						&& (rawT <= EEP26TemperatureLinear.MAX_VALID_RAW))
				{
					// update the attribute value
					tLinear.setRawValue(rawT);

					// build the dispatching task
					EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
							tLinear, A52001.CHANNEL);

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
