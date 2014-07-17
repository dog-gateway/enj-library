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
package it.polito.elite.enocean.enj.communication.timing.tasks;

import java.util.Set;

import it.polito.elite.enocean.enj.communication.EnJDeviceChangeType;
import it.polito.elite.enocean.enj.communication.EnJDeviceListener;
import it.polito.elite.enocean.enj.model.EnOceanDevice;

/**
 * @author bonino
 *
 */
public class EnJDeviceChangeDeliveryTask implements Runnable
{
	private EnOceanDevice changedDevice;
	private EnJDeviceChangeType typeOfChange;
	private Set<EnJDeviceListener> listeners;

	/**
	 * Builds a device update delivery task having as subject the given
	 * {@link EnOceanDevice} instance and as type of change the given
	 * {@link EnJDeviceChangeType} value.
	 * 
	 * @param changedDevice
	 *            The changed device.
	 * @param typeOfChange
	 *            The type of change (CREATED, MODIFIED or DELETED)
	 * @param listeners
	 *            The {@link EnJDeviceListener}s to be notified.
	 */
	public EnJDeviceChangeDeliveryTask(EnOceanDevice changedDevice,
			EnJDeviceChangeType typeOfChange, Set<EnJDeviceListener> listeners)
	{
		this.changedDevice = changedDevice;
		this.typeOfChange = typeOfChange;
		this.listeners = listeners;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		// deliver to all listeners
		for (EnJDeviceListener listener : this.listeners)
		{
			// deliver the device update notification according to the change
			// type
			switch (this.typeOfChange)
			{
				case CREATED:
				{
					listener.addedEnOceanDevice(changedDevice);
					break;
				}
				case MODIFIED:
				{
					listener.modifiedEnOceanDevice(changedDevice);
					break;
				}
				case DELETED:
				{
					listener.removedEnOceanDevice(changedDevice);
					break;
				}
			}
		}

	}

}
