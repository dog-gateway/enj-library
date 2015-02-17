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
package it.polito.elite.enocean.enj.eep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bonino
 *
 */
public class EEPAttributeChangeDispatcher implements Runnable
{
	// The lis of attributes that must be dispatched as "changed"
	private List<EEPAttribute<?>> attributesToDispatch;

	// The channel to which the list of attributes refer
	private int channelId;

	/**
	 * 
	 */
	public EEPAttributeChangeDispatcher(
			List<EEPAttribute<?>> attributesToDispatch, int channelId)
	{
		// store the list of attributes
		this.attributesToDispatch = attributesToDispatch;

		// store the channel id
		this.channelId = channelId;
	}
	
	public EEPAttributeChangeDispatcher(EEPAttribute<?> attributeToDispatch, int channelId)
	{
		this.attributesToDispatch = new ArrayList<EEPAttribute<?>>();
		this.attributesToDispatch.add(attributeToDispatch);
		
		this.channelId = channelId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		for (EEPAttribute<?> attribute : this.attributesToDispatch)
		{
			attribute.notifyAttributeListeners(this.channelId);
			// sleeps for 1ms between each attribute delivery
			// in order to not lock the CPU, yield() may
			// also be used here.
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				// do nothing and exit
				break;
			}
		}

	}

}
