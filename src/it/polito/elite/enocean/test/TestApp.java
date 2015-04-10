/*
 * EnJ - EnOcean Java API
 * 
 * Copyright 2014-2015 Andrea Biasi, Dario Bonino 
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

import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.eep.eep26.D2.D201.D20109;
import it.polito.elite.enocean.enj.eep.eep26.D2.D201.D201UnitOfMeasure;
import it.polito.elite.enocean.enj.link.EnJLink;
import it.polito.elite.enocean.enj.model.EnOceanDevice;

/**
 * @author bonino
 *
 */
public class TestApp
{

	/**
	 * 
	 */
	public TestApp()
	{
		// empty
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		// The EnOcean link layer
		try
		{
			//create the lowest link layer
			EnJLink linkLayer = new EnJLink("/dev/ttyUSB0");
			
			//create a device listener for handling device updates
			SimpleDeviceListener listener = new SimpleDeviceListener();
			
			//create the connection layer
			EnJConnection connection = new EnJConnection(linkLayer,
					"/home/bonino/Temp/devices.dat", listener);

			// connect the link
			linkLayer.connect();

			// ---------- Explicit teach-in ---------
			// the device to learn
			//System.out.println("Enabling explicit teach-in for 018a781f");
			//connection.enableTeachIn("018a781f", "A5-02-05", 10000);

			//Thread.sleep(11000);

			// 0187ae92
			// System.out.println("Enabling explicit teach-in for 0187ae92");
			// connection.enableTeachIn("0187ae92", "A5-07-01", 10000);

			// Thread.sleep(11000);

			// ---------- Smart teach-in -------------
			
			// teach-in for 2s
			System.out.println("Enabling smart teach-in for 2s");
			connection.setSmartTeachIn(true);
			System.out.println("SmartTeachIn: "
					+ connection.isSmartTeachInEnabled());
			connection.enableTeachIn(2000);
			System.out.println("SmartTeachIn: "
					+ connection.isSmartTeachInEnabled());

			Thread.sleep(2000);

			connection.setSmartTeachIn(false);
			System.out.println("SmartTeachIn: "
					+ connection.isSmartTeachInEnabled());
			
			Thread.sleep(2000);

			// ----------- actuation test ------------
			
			//get the device by high-level uid
			EnOceanDevice device = connection.getDevice(25672741);
			
			//get the device eep
			D20109 eep = (D20109) device.getEEP();
			
			eep.actuatorSetMeasurement(connection, device.getAddress(), true, true, true, 0, 0, D201UnitOfMeasure.kW, 10, 1);
			
			for (int i = 0; i < 10; i++)
			{
				System.out.println("Sending command");
				
				
				
				//if the device is not null, toggle its status
				if (device != null)
				{
								
					//toggle the status
					eep.actuatorSetOuput(connection, device.getAddress(),
							((i % 2) == 0) ? true : false);
					
					Thread.sleep(3000);
				}
			}

		}
		catch (Exception e)
		{
			System.err
					.println("The given port does not exist or no device is plugged in"
							+ e);
		}
	}

}
