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

import it.polito.elite.enocean.enj.eep.EEP;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.Rorg;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class representing the A5-02 family of EnOcean Equipment Profiles
 * (Temperature sensors).
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public abstract class A502 extends EEP
{
	// the EEP26 definition, according to the EEP26 specification
	public static final Rorg rorg = new Rorg((byte) 0xa5);
	public static final byte func = (byte) 0x02;

	// func must be defined by extending classes

	// Executor Thread Pool for handling attribute updates
	protected volatile ExecutorService attributeNotificationWorker;

	// -------------------------------------------------
	// Parameters defined by this EEP, which
	// might change depending on the network
	// activity.
	// --------------------------------------------------

	// --------------------------------------------------

	/**
	 *The class constructor
	 */
	public A502()
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
		boolean success = false;
		// handle the telegram, as first cast it at the right type (or fail)
		if (telegram.getTelegramType() == EEP26TelegramType.FourBS)
		{
			// cast the telegram to handle to its real type
			FourBSTelegram profileUpdate = (FourBSTelegram) telegram;

			// get the packet payload
			byte[] payload = profileUpdate.getPayload();
			
			
			//wrap the payload as a temperature message
			A502TemperatureMessage msg = new A502TemperatureMessage(payload);
			
			//update the value of the attribute
			EEP26TemperatureLinear tLinear = (EEP26TemperatureLinear) this.getChannelAttribute(0, EEP26TemperatureLinear.NAME);
			
			//check not null
			if(tLinear!=null)
			{
				int rawT = msg.getTemperature();
				
				//check range
				if((rawT >= 0)&&(rawT<=255))
				{
					//update the attribute value
					tLinear.setRawValue(rawT);
					
					// build the dispatching task
					EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
							tLinear, 1);

					// submit the task for execution
					this.attributeNotificationWorker.submit(dispatcherTask);
					
					//update the success flag
					success = true;
				}
			}
			
		}
		
		return success;
	}

}
