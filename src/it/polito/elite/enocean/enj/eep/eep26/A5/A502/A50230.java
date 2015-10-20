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

import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureInverseLinear;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

/**
 * @author bonino
 *
 */
public class A50230 extends A502
{

	// the type definition
	public static final byte type = (byte) 0x30;

	// the used channel
	public static int CHANNEL = 0;

	/**
	 * @param version
	 */
	public A50230()
	{
		super();

		// add attributes A50230 has operative range between -40.0 and 62.3
		// Celsius
		this.addChannelAttribute(A50230.CHANNEL,
				new EEP26TemperatureInverseLinear(-40.0, 62.3));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEP#getEEPIdentifier()
	 */
	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		// return the EEPIdentifier for this profile
		return new EEPIdentifier(A502.rorg, A502.func, A50230.type);

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
			A502ExtendedTemperatureMessage msg = new A502ExtendedTemperatureMessage(payload);
			
			//update the value of the attribute
			EEP26TemperatureInverseLinear tLinear = (EEP26TemperatureInverseLinear) this.getChannelAttribute(0, EEP26TemperatureInverseLinear.NAME);
			
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
