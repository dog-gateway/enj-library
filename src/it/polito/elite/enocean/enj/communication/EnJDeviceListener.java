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
package it.polito.elite.enocean.enj.communication;

import it.polito.elite.enocean.enj.model.EnOceanDevice;

/**
 * EnOcean for Java Device Listener interface, allows to attach and handle all
 * events related to EnOcean Devices including creation, deletion or
 * modification.
 * 
 * @author bonino
 * 
 */
public interface EnJDeviceListener
{
	/**
	 * Called when a new {@link EnOceanDevice} has been added, can either be
	 * exploited at the connection or at the application layer.
	 * 
	 * @param device
	 *            The just added device;
	 */
	public void addedEnOceanDevice(EnOceanDevice device);

	/**
	 * Called when a {@link EnOceanDevice} definition has changed, e.g., because
	 * of addition of new EEPs.
	 * 
	 * @param changedDevice
	 *            The changed device.
	 */
	public void modifiedEnOceanDevice(EnOceanDevice changedDevice);

	/**
	 * Called when a {@link EnOceanDevice} has been remove from the physical
	 * layer, e.g., moved in another place not reachable by the same EnOcean
	 * network.
	 * 
	 * @param changedDevice
	 */
	public void removedEnOceanDevice(EnOceanDevice changedDevice);
}
