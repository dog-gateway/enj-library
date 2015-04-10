/*
 * EnJ - EnOcean Java API
 * 
 * Copyright 2015 Dario Bonino 
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
import it.polito.elite.enocean.enj.eep.EEP;
import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.Rorg;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26DimLevel;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26EnergyMeasurement;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ErrorLevel;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26LocalControl;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26OverCurrentSwitchOff;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PowerFailure;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PowerFailureDetection;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PowerMeasurement;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26Switching;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.VLDTelegram;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class representing the D2-01 family of EnOcean Equipment Profiles. Cannot
 * be directly istantiated, instead specific classes matching the real device
 * EEP must be used.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * 
 */
public abstract class D201 extends EEP
{
	// the EEP26 definition, according to the EEP26 specification
	public static final Rorg rorg = new Rorg((byte) 0xd2);
	public static final byte func = (byte) 0x01;

	// func must be defined by extending classes

	// Executor Thread Pool for handling attribute updates
	private volatile ExecutorService attributeNotificationWorker;

	// -------------------------------------------------
	// Parameters defined by this EEP, which
	// might change depending on the network
	// activity.
	// --------------------------------------------------

	// -------------------------------------------------

	// the class constructor
	public D201()
	{
		// call the superclass constructor
		super("2.6");

		// build the attribute dispatching worker
		this.attributeNotificationWorker = Executors.newFixedThreadPool(1);
	}

	/**
	 * D2.01 CMD 0x1 Implements the CMD 0x1 of the D2.01 EnOcean Equipment
	 * Profile, which allows setting the output level of a D2.01 device (On, Off
	 * and Dimming are supported).
	 * 
	 * @param connection
	 *            The {@EnJConnection} link to be used for
	 *            sending the command, packet encapsulation will be performed at
	 *            such level.
	 * @param dimValue
	 *            The required dimming value (low-level, in byte).
	 * @param ioChannel
	 *            The output channel to be controlled (each device may have more
	 *            than one channel, each controllable independently from the
	 *            others).
	 * @param outputValue
	 *            The required ouput value. A byte representing a percentage
	 *            between 0 (0x00) and 100 (0x64).
	 */
	public void actuatorSetOutput(EnJConnection connection,
			byte[] deviceAddress, byte dimValue, byte ioChannel,
			byte outputValue)
	{
		// prepare the data payload to host "desired" values
		byte dataByte[] = new byte[4];

		// add the packet rorg
		dataByte[0] = D201.rorg.getRorgValue();

		// CMD code (0x01), the first 4 bits are not used
		dataByte[1] = (byte) 0x01;

		// Dim value: bit 7 to 5 - IO channel: bit 4 to 0
		dataByte[2] = (byte) ((dimValue << 5) + ioChannel);

		// Output value bit 6 to 0
		dataByte[3] = outputValue;

		// send the payload for connection-layer encapsulation
		connection.sendRadioCommand(deviceAddress, dataByte);
	}

	/**
	 * D2.01 CMD 0x02, Implements the CMD 0x02 of the D2.01 EnOcean Equipment
	 * Profile. It allows to configure one or all channels of a single actuator.
	 * No response timing is defined / required.
	 * 
	 * Recommendation: In case the device implements an inner order for dim
	 * timers, this order should be from "dim timer1"(fast) to
	 * "dim timer3"(slow). the configured time shall always be interpreted for a
	 * full range (0 to 100%) dimming.
	 * 
	 * @param connection
	 *            The {@EnJConnection} link to be used for
	 *            sending the command, packet encapsulation will be performed at
	 *            such level.
	 * @param deviceAddress
	 *            The low level address of the device to which the configuration
	 *            command must be sent.
	 * @param channelId
	 *            The id of the channel to be configured.
	 * @param localControl
	 *            Enables / disables the local control.
	 * @param overCurrentShutDown
	 *            Configures the behavior of the device in case of over-current
	 *            shutdown.
	 * @param resetOverCurrentShutDown
	 *            Configures the device behavior when resetting after an
	 *            over-current shutdown.
	 * @param userInterfaceIndication
	 *            Sets the user interface mode, from day to night.
	 * @param powerFailure
	 *            Sets the device behavior upon a power failure event.
	 * @param defaultState
	 *            Sets the default device state, to be restored when the device
	 *            powers up.
	 * @param dimTime1
	 *            The dim timer 1.
	 * @param dimTime2
	 *            The dim timer 2.
	 * @param dimTime3
	 *            The dim timer 3.
	 */
	public void actuatorSetLocal(EnJConnection connection,
			byte[] deviceAddress, byte channelId, byte localControl,
			byte overCurrentShutDown, byte resetOverCurrentShutDown,
			byte userInterfaceIndication, byte powerFailure, byte defaultState,
			D201DimTime dimTime1, D201DimTime dimTime2, D201DimTime dimTime3)
	{
		// prepare the data payload to host received configuration values
		byte dataByte[] = new byte[5];

		// add the packet rorg
		dataByte[0] = D201.rorg.getRorgValue();

		// CMD code (0x02), the first bit relates to taught-in devices, not
		// handled at the moment, the other 3 bits are not used, in binary
		// notation this means 10000002 (1 enable taught-in devices, 000 not
		// used, 0002 command code
		dataByte[1] = (byte) ((0x08 << 4) + 0x02); // byte is signed therefore
													// 0x82 would require a
													// cast)

		// bit 7 -> over current shutdown
		// bit 6 -> over current shutdown reset
		// bit 5 -> local control
		// bit 4 to 0 -> channelId
		dataByte[2] = (byte) ((overCurrentShutDown << 7)
				+ (resetOverCurrentShutDown << 6) + (localControl << 5) + channelId);

		// first 4 bits, dim timer 2, remaining 4 bits, dim timer 3
		dataByte[3] = (byte) ((dimTime2.getCode() << 4) + dimTime3.getCode());

		// bit 7 -> user interface mode
		// bit 6 -> power failure
		// bit 4-5 -> default state
		// bit 0-4 -> dim timer 1
		dataByte[4] = (byte) ((userInterfaceIndication << 7)
				+ (powerFailure << 6) + (defaultState << 4) + dimTime1
				.getCode());

		// data payload is now ready, send the data
		connection.sendRadioCommand(deviceAddress, dataByte);
	}

	/**
	 * D2.01 CMD 0x03, Implements the CMD 0x03 of the D2.01 EnOcean Equipment
	 * Profile. It requests the status of one or all channels of an actuator.
	 * 
	 * Response timing: An actuator status response message shall be received
	 * within a maximum of 300ms from the time of transmission of this message.
	 * In case no such response is received within this time frame the action
	 * shall be treated as completed without results.
	 * 
	 * @param connection
	 *            The {@EnJConnection} link to be used for
	 *            sending the command, packet encapsulation will be performed at
	 *            such level.
	 * @param deviceAddress
	 *            The low level address of the device to which the status query
	 *            command must be sent.
	 * @param channelId
	 *            The id of the channel to be queried.
	 * @param connection
	 * @param deviceAddress
	 * @param channelId
	 */
	public void actuatorStatusQuery(EnJConnection connection,
			byte[] deviceAddress, byte channelId)
	{
		byte dataByte[] = new byte[3];

		// add the packet rorg
		dataByte[0] = D201.rorg.getRorgValue();

		// first byte
		// bit 0-3 -> command id
		dataByte[1] = 0x03;

		// second byte
		// bit 0-4 -> channel id
		dataByte[2] = channelId;

		// send the command
		connection.sendRadioCommand(deviceAddress, dataByte);
	}

	/**
	 * D2.01 CMD 0x05, Implements the CMD 0x05 of the D2.01 EnOcean Equipment
	 * Profile. It configures the energy and power measurement of one or all
	 * channels of an actuator.
	 * 
	 * @param connection
	 *            The {@link EnJConnection object which enables command
	 *            transmission over the physical network.}
	 * @param deviceAddress
	 *            The address of the destination device.
	 * @param reportMeasurement
	 *            0b0 query only, 0b1 auto reporting and query.
	 * @param resetMeasurement
	 *            0b0 not active, 0b1 trigger signal.
	 * @param measurementMode
	 *            0b0 energy measurement, 0b1 power measurement.
	 * @param channelId
	 *            Output channel from 0x00 to 0x1D.
	 * @param measurementDeltaLSB
	 *            Measurement delta to be reported (LSB) 0-4095.
	 * @param unit
	 *            Unit of measure, 0x00 Energy (Ws), 0x01 Energy (Wh), 0x02
	 *            Energy (kWh), 0x03 Power (W), 0x04 Power (kW), 0x05..0x07 not
	 *            used.
	 * @param measurementDeltaMSB
	 *            Measurement delta to be reported (MSB) 0-4095.
	 * @param mat
	 *            Maximum time between to subsequent actuator messages
	 *            (10-2550s).
	 * @param mit
	 *            Minimum time between to subsequent actuator messages (0-255s).
	 */
	public void actuatorSetMeasurement(EnJConnection connection,
			byte deviceAddress[], byte reportMeasurement,
			byte resetMeasurement, byte measurementMode, byte channelId,
			byte measurementDeltaLSB, byte unit, byte measurementDeltaMSB,
			byte mat, byte mit)
	{
		// the array of bytes containing the data payload
		byte dataByte[] = new byte[7];

		// add the packet rorg
		dataByte[0] = D201.rorg.getRorgValue();

		// first byte -> lower 4 bits for the command code
		dataByte[1] = 0x05;

		// second byte:
		// first bit -> report measurement
		// second bit -> reset measurement
		// third bit -> measurement mode
		// last 5 bits -> channel id
		dataByte[2] = (byte) ((reportMeasurement << 7)
				+ (resetMeasurement << 6) + (measurementMode << 5) + channelId);

		// third byte
		// first 4 bits -> measurementDeltaLSB
		// 5th bit -> not used
		// last 3 bits -> unit of measure
		dataByte[3] = (byte) ((measurementDeltaLSB << 4) + (0x00 << 3) + (unit));

		// fourth byte
		// 8 bit of measurementDeltaMSB
		dataByte[4] = measurementDeltaMSB;

		// fifth byte
		// maximum time between subsequent actuator messages
		dataByte[5] = mat;

		// sixth byte
		// minimum time between subsequent actuator messages
		dataByte[6] = mit;

		// send the radio packet
		connection.sendRadioCommand(deviceAddress, dataByte);

	}

	/**
	 * D2.01 CMD 0x06, Implements the CMD 0x06 of the D2.01 EnOcean Equipment
	 * Profile. Asks for specific measurement values (energy or power) on a
	 * given channel of an EnOcean actuator.
	 * 
	 * @param connection
	 *            The {@link EnJConnection} object for sending the radio packet
	 *            on the physical network.
	 * @param deviceAddress
	 *            The address of the device to which the message must be sent.
	 * @param queryType
	 *            The query type (0b0 Energy, 0b1 Power).
	 * @param channelId
	 *            The id of the actuator channel to be queried.
	 */
	public void actuatorMeasurementQuery(EnJConnection connection,
			byte deviceAddress[], byte queryType, byte channelId)
	{
		// the array of bytes containing the data payload
		byte dataByte[] = new byte[3];

		// add the packet rorg
		dataByte[0] = D201.rorg.getRorgValue();

		// first byte -> lower 4 bits for the command code
		dataByte[1] = 0x06;

		// second byte
		// first 2 bits are not used;
		// third bit -> query mode (0b0 pEnergy, 0b1 Power)
		// last 5 bits -> channelId
		dataByte[2] = (byte) (((0x01 & queryType) << 5) + channelId);

		// send the radio packet
		connection.sendRadioCommand(deviceAddress, dataByte);
	}

	/**
	 * D2.01 CMD 0x04, Implements the CMD 0x04 of the D2.01 EnOcean Equipment
	 * Profile. Represents a response to the Actuator Status Query message
	 * (D2.01.04) and contains information about the current status of an
	 * EnOcean Actuator.
	 * 
	 * @param dataPayload
	 *            The low level data payload to "wrap";
	 * @return The wrapped payload.
	 */
	public D201ActuatorStatusResponse parseActuatorStatusResponse(
			byte[] dataPayload)
	{
		// dataPayload[0] --> MSB
		// dataPayload[dataPayload.length] --> LSB

		// power failure 0 -> disabled, 1->enabled
		byte powerFailure = (byte) ((dataPayload[0] & (byte) 0x80) >> 7);

		// power failure detection 0->power failure not detected / supported
		// /enabled, 1-> power failure detected
		byte powerFailureDetection = (byte) ((dataPayload[0] & (byte) 0x40) >> 3);

		// the command id, should be 0x04
		byte commandId = (byte) (dataPayload[0] & (byte) 0x0F);

		// the status of the overcurrent switch off function
		// 0 -> ready / not supported
		// 1 -> executed
		byte overCurrentSwitchOff = (byte) ((dataPayload[1] & (byte) 0x80) >> 7);

		// the current error level
		// 0 -> hardware ok
		// 1 -> hardware warning
		// 2 -> hardware failure
		// 3 -> not supported
		byte errorLevel = (byte) ((dataPayload[1] & (byte) 0x60) >> 5);

		// the channel id
		// 0x00..0x1D -> Output Channel
		// 0x1E -> not applicable / do not use
		// 0x1F -> input channel (from mains supply)
		byte channelId = (byte) (dataPayload[1] & (byte) 0x1F);

		// local control
		// 0 -> disabled / not supported
		// 1 -> enabled
		byte localControl = (byte) ((dataPayload[2] & (byte) 0x80) >> 7);

		// output value
		// 0x00 -> 0% or OFF
		// 0x01 - 0x64 -> 1% -100% (or ON)
		// 0x65 - 0x7E -> Not used
		// 0x7F -> Output not valid / not set
		byte outputValue = (byte) ((dataPayload[2] & (byte) 0x7F));

		return new D201ActuatorStatusResponse(powerFailure,
				powerFailureDetection, commandId, overCurrentSwitchOff,
				errorLevel, channelId, localControl, outputValue);
	}

	public D201ActuatorMeasurementResponse parseActuatorMeasurementResponse(
			byte[] dataPayload)
	{
		// dataPayload[0] --> MSB
		// dataPayload[dataPayload.length] --> LSB

		// the command id, should be 0x07
		byte commandId = (byte) (dataPayload[0] & (byte) 0x0F);

		// the unit
		// 0x00 --> Energy [Ws]
		// 0x01 --> Energy [Wh]
		// 0x02 --> Energy [kWh]
		// 0x03 --> Power [W]
		// 0x04 --> Power [kW]
		// 0x05..0x07 --> Not used
		byte unit = (byte) ((dataPayload[1] & (byte) 0xE0) >> 5);

		// the channel id
		// 0x00..0x1D -> Output Channel
		// 0x1E -> not applicable / do not use
		// 0x1F -> input channel (from mains supply)
		byte channelId = (byte) (dataPayload[1] & (byte) 0x1F);

		byte measureValue[] = new byte[4];
		measureValue[0] = dataPayload[2];
		measureValue[1] = dataPayload[3];
		measureValue[2] = dataPayload[4];
		measureValue[3] = dataPayload[5];

		return new D201ActuatorMeasurementResponse(commandId, channelId,
				measureValue, unit);
	}

	@Override
	public boolean handleProfileUpdate(EEP26Telegram telegram)
	{
		boolean success = false;
		// handle the telegram, as first cast it at the right type (or fail)
		if (telegram.getTelegramType() == EEP26TelegramType.VLD)
		{
			// prepare the list of changed attributes (only one)
			ArrayList<EEPAttribute<?>> changedAttributes = new ArrayList<EEPAttribute<?>>();

			// cast the telegram to the right type
			VLDTelegram profileUpdate = (VLDTelegram) telegram;

			// check the telegram type:actuator status response (CMD_ID = 0x04),
			// Actuator Measurement Response (CMD_ID = 0x07)
			byte dataPayload[] = profileUpdate.getPayload();

			// get the command id
			byte commandId = (byte) (dataPayload[0] & (byte) 0x0F);

			// the channel id
			int channelId = -1;

			if (commandId == (byte) 0x04)
			{
				// parse actuator status response
				D201ActuatorStatusResponse response = this
						.parseActuatorStatusResponse(dataPayload);

				// update the channelId
				channelId = response.getChannelId();

				// updates all the attributes associated to the status change
				// message
				changedAttributes.addAll(this.updateStatusAttributes(response));

			}
			else if (commandId == (byte) 0x07)
			{
				// parse the actuator measurement response
				D201ActuatorMeasurementResponse response = this
						.parseActuatorMeasurementResponse(dataPayload);

				// update the channelId
				channelId = response.getChannelId();

				// get the channel to attribute to update, can either be Energy
				// or Power measurement, detect it depending on the unit of
				// measure.
				D201UnitOfMeasure uom = response.getUnit();

				if (uom.isEnergy())
				{
					// handle energy attribute update
					EEPAttribute<?> energyAttribute = this
							.updateEnergyAttribute(response);

					// add the attribute to the list of changed ones
					changedAttributes.add(energyAttribute);

				}
				else if (uom.isPower())
				{
					// handle power attribute update
					EEPAttribute<?> powerAttribute = this
							.updatePowerAttribute(response);

					changedAttributes.add(powerAttribute);
				}
			}

			if ((!changedAttributes.isEmpty()) && (channelId >= 0))
			{
				// build the dispatching task
				EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
						changedAttributes, channelId);

				// submit the task for execution
				this.attributeNotificationWorker.submit(dispatcherTask);

				// set success at true
				// TODO check what to do if nothing changes, i.e., with success
				// equal to false.
				success = true;
			}
		}
		return success;
	}

	private ArrayList<EEPAttribute<?>> updateStatusAttributes(
			D201ActuatorStatusResponse response)
	{
		// the set of changed attributes
		ArrayList<EEPAttribute<?>> changedAttributes = new ArrayList<EEPAttribute<?>>();

		// -------- update the power failure settings
		EEP26PowerFailure powerFailureAttribute = (EEP26PowerFailure) this
				.getChannelAttribute(response.getChannelId(),
						EEP26PowerFailure.NAME);

		// check not null
		if (powerFailureAttribute != null)
		{
			if (powerFailureAttribute.getValue() != response
					.isPowerFailureEnabled())
			{
				// update the attribute
				powerFailureAttribute
						.setValue(response.isPowerFailureEnabled());

				// add the attribute to the set of changed ones
				changedAttributes.add(powerFailureAttribute);
			}
		}

		// -------- update the power failure detection status
		EEP26PowerFailureDetection powerFailureDetectionAttribute = (EEP26PowerFailureDetection) this
				.getChannelAttribute(response.getChannelId(),
						EEP26PowerFailureDetection.NAME);

		// check not null
		if (powerFailureDetectionAttribute != null)
		{
			if (powerFailureDetectionAttribute.getValue() != response
					.isPowerFailureDetected())
			{
				// update the attribute
				powerFailureDetectionAttribute.setValue(response
						.isPowerFailureDetected());

				// add the attribute to the set of changed attributes
				changedAttributes.add(powerFailureDetectionAttribute);
			}
		}

		// ------ update the overcurrent switch-off attribute
		EEP26OverCurrentSwitchOff overCurrentSwitchOffAttribute = (EEP26OverCurrentSwitchOff) this
				.getChannelAttribute(response.getChannelId(),
						EEP26OverCurrentSwitchOff.NAME);

		// check not null
		if (overCurrentSwitchOffAttribute != null)
		{
			if (overCurrentSwitchOffAttribute.getValue() != response
					.isOverCurrentSwitchOffExecuted())
			{
				// update the attribute
				overCurrentSwitchOffAttribute.setValue(response
						.isOverCurrentSwitchOffExecuted());

				// add the attribute to the set of changed attributes
				changedAttributes.add(overCurrentSwitchOffAttribute);
			}
		}

		// ----- update the error level attribute
		EEP26ErrorLevel errorLevelAttribute = (EEP26ErrorLevel) this
				.getChannelAttribute(response.getChannelId(),
						EEP26ErrorLevel.NAME);

		// check not null (attribute exists)
		if (errorLevelAttribute != null)
		{
			// check for a new value
			if (errorLevelAttribute.getValue() != response.getErrorLevel())
			{
				// update the attribute
				errorLevelAttribute.setValue(response.getErrorLevel());

				// add the attribute to the set of changed attributes
				changedAttributes.add(errorLevelAttribute);
			}
		}

		// ----- update the local control value
		EEP26LocalControl localControlAttribute = (EEP26LocalControl) this
				.getChannelAttribute(response.getChannelId(),
						EEP26LocalControl.NAME);

		// check not null
		if (localControlAttribute != null)
		{
			// check for a new value
			if (localControlAttribute.getValue() != response
					.isLocalControlEnabled())
			{
				// update the attribute
				localControlAttribute
						.setValue(response.isLocalControlEnabled());

				// add the attribute to the set of changed attributes
				changedAttributes.add(localControlAttribute);
			}
		}

		// ------ update the ouput level value
		EEP26Switching switchingAttribute = (EEP26Switching) this
				.getChannelAttribute(response.getChannelId(),
						EEP26Switching.NAME);

		EEP26DimLevel dimLevelAttribute = (EEP26DimLevel) this
				.getChannelAttribute(response.getChannelId(),
						EEP26DimLevel.NAME);

		// check that at least one of the two is not null
		if ((switchingAttribute != null) || (dimLevelAttribute != null))
		{
			// get the current output level
			int outputLevel = response.getOutputValue();

			// check valid
			if (outputLevel <= 100)
			{

				// update switching
				if ((switchingAttribute != null)
						&& (switchingAttribute.getValue() != (outputLevel > 0)))
				{
					// update the switching attribute
					switchingAttribute.setValue(outputLevel > 0);

					// add the attribute to the set of changed attributes
					changedAttributes.add(switchingAttribute);
				}

				// update dimming
				if ((dimLevelAttribute != null)
						&& (dimLevelAttribute.getValue() != outputLevel))
				{
					// update the dim level
					dimLevelAttribute.setValue(outputLevel);

					// add the attribute to the set of changed attributes
					changedAttributes.add(dimLevelAttribute);
				}
			}
		}

		// return changed attributes
		return changedAttributes;

	}

	/**
	 * Given an ActuatorMeasurementResponse, updates the right attibute
	 * (channel-specific)
	 * 
	 * @param response
	 *            The response carrying updated power data.
	 */
	private EEPAttribute<?> updatePowerAttribute(
			D201ActuatorMeasurementResponse response)
	{
		// get the right channel attribute and update the current power figure
		EEP26PowerMeasurement powerMeasurementAttribute = (EEP26PowerMeasurement) this
				.getChannelAttribute(response.getChannelId(),
						EEP26PowerMeasurement.NAME);

		powerMeasurementAttribute.setValue(response.getMeasureAsDouble());
		powerMeasurementAttribute.setUnit(response.getUnit().name());

		// return the changed attribute
		return powerMeasurementAttribute;
	}

	/**
	 * Given an ActuatorMeasurementResponse, updates the right attribute
	 * (channel-specific)
	 * 
	 * @param response
	 *            The response carrying updated energy data.
	 */
	private EEPAttribute<?> updateEnergyAttribute(
			D201ActuatorMeasurementResponse response)
	{
		// get the right channel attribute and update the current energy figure
		EEP26EnergyMeasurement energyMeasurementAttribute = (EEP26EnergyMeasurement) this
				.getChannelAttribute(response.getChannelId(),
						EEP26EnergyMeasurement.NAME);

		energyMeasurementAttribute.setValue(response.getMeasureAsDouble());
		energyMeasurementAttribute.setUnit(response.getUnit().name());

		return energyMeasurementAttribute;
	}
}
