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

import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.link.EnJLink;

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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		// The EnOcean link layer
		EnJLink linkLayer = new EnJLink("/dev/ttyUSB0");
		EnJConnection connection = new EnJConnection(linkLayer, null);
		SimpleDeviceListener listener = new SimpleDeviceListener();
		connection.addEnJDeviceListener(listener);
		
		//connect the link
		linkLayer.connect();
		
		//wait for 60s
		for(int i=0; i<60; i++)
		{
			Thread.sleep(1000);
			//System.out.println("--");
		}

	}

}
