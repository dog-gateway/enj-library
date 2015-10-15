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
package it.polito.elite.enocean.enj.eep.eep26.D2.D201;

import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26DefaultState;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26EnergyMeasurement;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ErrorLevel;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26LocalControl;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26OverCurrentShutdown;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26OverCurrentShutdownReset;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26OverCurrentSwitchOff;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PowerFailure;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PowerMeasurement;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26Switching;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26UserInterfaceMode;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 * 
 */
public class D20108 extends D201
{

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

	// the used channel
	public static int CHANNEL = 0;

	// the "data" fields accessible through this eep (and updated upon network
	// data reception)

	/**
	 * Builds a new EEPProfile instance of type D2.01.08 as specified in the
	 * EEP2.6 specification.
	 */
	public D20108()
	{
		super();

		// add the supported functions
		this.addChannelAttribute(D20108.CHANNEL, new EEP26Switching());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26LocalControl());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26UserInterfaceMode());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26OverCurrentShutdown());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26OverCurrentShutdownReset());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26OverCurrentSwitchOff());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26EnergyMeasurement());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26PowerMeasurement());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26DefaultState());
		this.addChannelAttribute(D20108.CHANNEL, new EEP26ErrorLevel());
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

	/**
	 * Updates the configuration of the physical actuator having this EEP
	 * profile with values from the given set of attributes. Attributes not
	 * being part of the acceptable configuration parameters will be simply
	 * ignored.
	 * 
	 * @param connection
	 *            The {@link EnJConnection} object enabling physical layer
	 *            communication
	 * @param deviceAddress
	 *            The physical layer address of the device
	 * @param attributes
	 *            The configuration attributes to set
	 */
	public void actuatorSetLocal(EnJConnection connection,
			byte[] deviceAddress, int channelId,
			EEPAttribute<? extends Object>[] attributes, D201DimTime dimTime1,
			D201DimTime dimTime2, D201DimTime dimTime3)
	{
		// the over current shutdown settings (enabled / disabled), disabled by
		// default
		byte overCurrentShutDown = 0x00;

		// the reset behavior in overcurrent shutdown cases
		byte resetOverCurrentShutDown = 0x00;

		// the local control enabling flag
		byte localControl = 0x00;

		// the user interface mode (either day or night)
		byte userInterfaceIndication = 0x00;

		// the power failure flag
		byte powerFailure = 0x00;

		// the default state to set when the actuator is powered
		byte defaultState = 0x00;

		// extract the attributes
		// TODO: find a better way to perform such operations, if possible
		for (EEPAttribute<? extends Object> attribute : attributes)
		{
			if (attribute instanceof EEP26LocalControl)
				localControl = attribute.byteValue()[0];
			else if (attribute instanceof EEP26OverCurrentShutdown)
				overCurrentShutDown = attribute.byteValue()[0];
			else if (attribute instanceof EEP26OverCurrentShutdownReset)
				resetOverCurrentShutDown = attribute.byteValue()[0];
			else if (attribute instanceof EEP26UserInterfaceMode)
				userInterfaceIndication = attribute.byteValue()[0];
			else if (attribute instanceof EEP26PowerFailure)
				powerFailure = attribute.byteValue()[0];
			else if (attribute instanceof EEP26DefaultState)
				defaultState = attribute.byteValue()[0];
		}

		// call the superclass method for setting the device configuration
		super.actuatorSetLocal(connection, deviceAddress, (byte) channelId,
				localControl, overCurrentShutDown, resetOverCurrentShutDown,
				userInterfaceIndication, powerFailure, defaultState, dimTime1,
				dimTime2, dimTime3);
	}

	public void actuatorSetMeasurement(EnJConnection connection,
			byte[] deviceAddress, boolean autoReportMesurement,
			boolean signalResetMeasurement, boolean powerMode, int channelId,
			int measurementDeltaToBeReported, D201UnitOfMeasure unitOfMeasure,
			int maximumTimeBetweenActuatorMessages,
			int minimumTimeBetweenActuatorMessages)
	{
		if ((maximumTimeBetweenActuatorMessages >= 0)
				&& (minimumTimeBetweenActuatorMessages >= 0))
		{
			byte reportMeasurementAsByte = autoReportMesurement ? (byte) 0x01
					: (byte) 0x00;
			byte signalResetMeasurementAsByte = signalResetMeasurement ? (byte) 0x01
					: (byte) 0x00;
			byte powerModeAsByte = powerMode ? (byte) 0x01 : (byte) 0x00;

			// get the measurement delta LSB, and with all 0 apart from the last
			// 4 bits
			byte measurementDeltaLSB = (byte) ((measurementDeltaToBeReported) & (0x000F));

			// get the measurement delta MSB, shift right by 8 bits and set at 0
			// the 8 leading bits
			byte measurementDeltaMSB = (byte) ((measurementDeltaToBeReported >> 8) & (0x00FF));
			byte maximumTimeAsByte = (byte) Math.min(
					(maximumTimeBetweenActuatorMessages / 10), 255);
			byte minimumTimeAsByte = (byte) Math.min(
					minimumTimeBetweenActuatorMessages, 255);

			// call the superclass method
			super.actuatorSetMeasurement(connection, deviceAddress,
					reportMeasurementAsByte, signalResetMeasurementAsByte,
					powerModeAsByte, (byte) channelId, measurementDeltaLSB,
					unitOfMeasure.getCode(), measurementDeltaMSB,
					maximumTimeAsByte, minimumTimeAsByte);
		}
		else
			throw new NumberFormatException(
					"Only positive numbers allowed for time values");
	}

	/**
	 * Asks for the current power or energy measurement on a given channel Id of
	 * a given EnOcean actuator
	 * 
	 * @param connection
	 * @param deviceAddress
	 * @param powerMode
	 * @param channelId
	 */
	public void actuatorMeasurementQuery(EnJConnection connection,
			byte[] deviceAddress, boolean powerMode, int channelId)
	{
		// get the measurement mode as a byte value
		byte powerModeAsByte = powerMode ? (byte) 0x01 : (byte) 0x00;

		// call the superclass method
		super.actuatorMeasurementQuery(connection, deviceAddress,
				powerModeAsByte, (byte) channelId);
	}

	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		return new EEPIdentifier(D201.rorg, D201.func, D20108.type);
	}
}