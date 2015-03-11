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
package it.polito.elite.enocean.test;

import it.polito.elite.enocean.enj.communication.EnJDeviceListener;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PIRStatus;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26RockerSwitch2RockerAction;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26SupplyVoltage;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26SupplyVoltageAvailability;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26Switching;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;
import it.polito.elite.enocean.enj.model.EnOceanDevice;

/**
 * @author bonino
 *
 */
public class SimpleDeviceListener implements EnJDeviceListener
{

	/**
	 * 
	 */
	public SimpleDeviceListener()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.communication.EnJDeviceListener#
	 * addedEnOceanDevice(it.polito.elite.enocean.enj.model.EnOceanDevice)
	 */
	@Override
	public void addedEnOceanDevice(EnOceanDevice device)
	{
		System.out.println("Added device:" + device.getDeviceUID()
				+ " - low-address: " + device.getAddress());
		
		SimpleMovementListener movementListener = new SimpleMovementListener();

		// handle device types
		if(device.getEEP().getChannelAttribute(1,EEP26RockerSwitch2RockerAction.NAME)!=null)
			device.getEEP().addEEP26AttributeListener(1,
					EEP26RockerSwitch2RockerAction.NAME,
					new SimpleRockerSwitchListener());
		else if (device.getEEP().getChannelAttribute(1, EEP26TemperatureLinear.NAME)!=null)
			device.getEEP().addEEP26AttributeListener(1,
					EEP26TemperatureLinear.NAME,
					new SimpleTemperatureListener());
		else if (device.getEEP().getChannelAttribute(1, EEP26Switching.NAME)!=null)
			device.getEEP().addEEP26AttributeListener(1,
					EEP26Switching.NAME,
					new SimpleContactSwitchListener());
		else if (device.getEEP().getChannelAttribute(1, EEP26PIRStatus.NAME)!=null)
			device.getEEP().addEEP26AttributeListener(1, EEP26PIRStatus.NAME, movementListener);
		else if (device.getEEP().getChannelAttribute(1, EEP26SupplyVoltage.NAME)!=null)
			device.getEEP().addEEP26AttributeListener(1, EEP26SupplyVoltage.NAME, movementListener);
		else if (device.getEEP().getChannelAttribute(1, EEP26SupplyVoltageAvailability.NAME)!=null)
			device.getEEP().addEEP26AttributeListener(1, EEP26SupplyVoltageAvailability.NAME, movementListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.communication.EnJDeviceListener#
	 * modifiedEnOceanDevice(it.polito.elite.enocean.enj.model.EnOceanDevice)
	 */
	@Override
	public void modifiedEnOceanDevice(EnOceanDevice device)
	{
		System.out.println("Modified device:" + device.getDeviceUID()
				+ " - low-address: " + device.getAddress());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.communication.EnJDeviceListener#
	 * removedEnOceanDevice(it.polito.elite.enocean.enj.model.EnOceanDevice)
	 */
	@Override
	public void removedEnOceanDevice(EnOceanDevice device)
	{
		System.out.println("Removed device:" + device.getDeviceUID()
				+ " - low-address: " + device.getAddress());
	}

}
