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

import static org.junit.Assert.*;
import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.communication.EnJDeviceListener;
import it.polito.elite.enocean.enj.link.EnJLink;
import it.polito.elite.enocean.enj.model.EnOceanDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author bonino
 *
 */
public class EnJConnectionTest implements EnJDeviceListener
{

	private EnJLink link;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.link = new EnJLink("/dev/ttyUSB0");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link it.polito.elite.enocean.enj.communication.EnJConnection#EnJConnection(it.polito.elite.enocean.enj.link.EnJLink, java.lang.String)}.
	 */
	@Test
	public void testEnJConnection()
	{
		EnJConnection connection = new EnJConnection(this.link, "test");
		assertNotNull(connection);
	}

	/**
	 * Test method for {@link it.polito.elite.enocean.enj.communication.EnJConnection#addEnJDeviceListener(it.polito.elite.enocean.enj.communication.EnJDeviceListener)}.
	 */
	@Test
	public void testAddEnJDeviceListener()
	{
		EnJConnection connection = new EnJConnection(this.link, "test");
		assertNotNull(connection);
		connection.addEnJDeviceListener(this);
	}

	/**
	 * Test method for {@link it.polito.elite.enocean.enj.communication.EnJConnection#handlePacket(it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet)}.
	 */
	@Test
	public void testHandlePacket()
	{
		fail("Not yet implemented");
	}

	@Override
	public void addedEnOceanDevice(EnOceanDevice device)
	{
		// TODO Auto-generated method stub
		System.out.println("Added device:"+device.getDeviceUID()+" - low-address: "+device.getAddress());
	}

	@Override
	public void modifiedEnOceanDevice(EnOceanDevice changedDevice)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedEnOceanDevice(EnOceanDevice changedDevice)
	{
		// TODO Auto-generated method stub
		
	}

}
