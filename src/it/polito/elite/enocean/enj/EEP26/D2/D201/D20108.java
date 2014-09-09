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
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import it.polito.elite.enocean.enj.EEP26.EEPIdentifier;
import it.polito.elite.enocean.enj.EEP26.EEPRegistry;
import it.polito.elite.enocean.enj.EEP26.attributes.EEPEnergyMeasurement;
import it.polito.elite.enocean.enj.EEP26.attributes.EEPLocalControl;
import it.polito.elite.enocean.enj.EEP26.attributes.EEPPowerMeasurement;
import it.polito.elite.enocean.enj.EEP26.attributes.EEPSwitching;
import it.polito.elite.enocean.enj.EEP26.attributes.EEPUserInterfaceMode;
import it.polito.elite.enocean.enj.communication.EnJConnection;

import java.io.Serializable;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 * 
 */
public class D20108 extends D201 implements Serializable
{
	/**
	 * class version number for serialization / de-serialization
	 */
	private static final long serialVersionUID = 1L;

	// the type definition
	public static final byte type = (byte) 0x08;

	// the ON state / command
	public static boolean ON = true;

	// the ON command byte
	public static byte ON_BYTE = (byte) 0x64;

	// the OFF state / command
	public static boolean OFF = false;

	// the OFF command byte
	public static byte OFF_BYTE = (byte) 0x00;

	// the byte identifier for all output channels
	public static byte ALL_OUTPUT_CHANNEL = 0x1E;

	// the "data" fields accessible through this eep (and updated upon network
	// data reception)

	// register the type in the EEPProfile even if no instance of this class is
	// created.
	static
	{
		EEPRegistry.getInstance().addProfile(
				new EEPIdentifier(D201.rorg, D201.func, D20108.type),
				D20108.class);
	}

	/**
	 * Builds a new EEPProfile instance of type D2.01.08 as specified in the
	 * EEP2.6 specification.
	 */
	public D20108()
	{
		super("2.6");

		// add the supported functions
		this.addChannelAttribute(1, new EEPSwitching());
		this.addChannelAttribute(1, new EEPEnergyMeasurement());
		this.addChannelAttribute(1, new EEPPowerMeasurement());
		this.addChannelAttribute(1, new EEPLocalControl());
		this.addChannelAttribute(1, new EEPUserInterfaceMode());
	}

	// execution commands
	public void actuatorSetOuput(EnJConnection connection,
			byte[] deviceAddress, boolean command)
	{
		// exec the command by using the D201 general purpose implementation
		super.actuatorSetOutput(connection, deviceAddress,
				D201DimMode.SWITCH_TO_NEW_OUTPUT_VALUE.getCode(),
				D20108.ALL_OUTPUT_CHANNEL, command ? D20108.ON_BYTE
						: D20108.OFF_BYTE);
	}

	public void actuatorSetOuput(EnJConnection connection,
			byte[] deviceAddress, int dimValue, D201DimMode dimMode)
	{
		// check limits
		if (dimValue < 0)
			dimValue = 0;
		else if (dimValue > 100)
			dimValue = 100;

		super.actuatorSetOutput(connection, deviceAddress, dimMode.getCode(),
				D20108.ALL_OUTPUT_CHANNEL, (byte) dimValue);

	}

	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		return new EEPIdentifier(D201.rorg, D201.func, D20108.type);
	}

}
