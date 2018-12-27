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
package it.polito.elite.enocean.enj.eep.eep26.A5.A520;

import java.util.ArrayList;

import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.A5.A520.A52001ActuatorInputMessage.FunctionMode;
import it.polito.elite.enocean.enj.eep.eep26.A5.A520.A52001ActuatorInputMessage.SetPointSelectionModeMode;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ActuatorObstructed;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26BatteryCapacity;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26CoverOpen;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26EnergyInputEnabled;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26EnergyStorage;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ServiceOn;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureLinear;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26TemperatureSensorFailure;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26ValvePosition;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26WindowOpen;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

/**
 * @author sbertini
 *
 */
public class A52001 extends A520
{

	// the type definition
	public static final byte type = (byte) 0x01;
	public static int INPUT_CHANNEL = 0;

	/**
	 * @param version
	 */
	public A52001()
	{

		super();

		// add attributes,
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26ValvePosition());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26ServiceOn());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26EnergyInputEnabled());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26EnergyStorage());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26BatteryCapacity());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26CoverOpen());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26TemperatureSensorFailure());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26WindowOpen());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26ActuatorObstructed());
		this.addChannelAttribute(A52001.INPUT_CHANNEL, new EEP26TemperatureLinear(0.0, 40.0));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.polito.elite.enocean.enj.eep.EEP#getEEPIdentifier()
	 */
	@Override
	public EEPIdentifier getEEPIdentifier()
	{
		return new EEPIdentifier(A520.rorg, A520.func, A52001.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.polito.elite.enocean.enj.eep.EEP#handleProfileUpdate(it.polito.elite
	 * .enocean.enj.eep.eep26.telegram.EEP26Telegram)
	 */
	@Override
	public boolean handleProfileUpdate(EEP26Telegram telegram)
	{
		boolean success = false;
		// handle the telegram, as first cast it at the right type (or fail)
		if (telegram.getTelegramType() == EEP26TelegramType.FourBS)
		{
			// cast the telegram to handle to its real type
			FourBSTelegram profileUpdate = (FourBSTelegram) telegram;

			// get the packet payload
			byte[] payload = profileUpdate.getPayload();

			// parse the telegram as an A50701 message
			A52001ActuatorMessage message = new A52001ActuatorMessage(payload);

			// check if its valid
			if (message.isValid())
			{
				// prepare the list of changed attributes (only one)
				ArrayList<EEPAttribute<?>> changedAttributes = new ArrayList<EEPAttribute<?>>();

				// ------- get the attributes

				// valve position
				EEP26ValvePosition valvePosition = (EEP26ValvePosition) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26ValvePosition.NAME);

				if (valvePosition != null)
				{
					// set the valve position value
					valvePosition.setValue(message.getCurrentValue());

					// update the list of changed attributes
					changedAttributes.add(valvePosition);
				}

				// Service On
				EEP26ServiceOn serviceOn = (EEP26ServiceOn) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26ServiceOn.NAME);

				if (serviceOn != null)
				{
					// set the valve position value
					serviceOn.setValue(message.isServiceOn());

					// update the list of changed attributes
					changedAttributes.add(serviceOn);
				}

				// Energy Input Enabled
				EEP26EnergyInputEnabled energyInputEnabled = (EEP26EnergyInputEnabled) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26EnergyInputEnabled.NAME);

				if (energyInputEnabled != null)
				{
					// set the valve position value
					energyInputEnabled.setValue(message.isEnergyInputEnabled());

					// update the list of changed attributes
					changedAttributes.add(energyInputEnabled);
				}

				// Energy Storage
				EEP26EnergyStorage energyStorage = (EEP26EnergyStorage) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26EnergyStorage.NAME);

				if (energyStorage != null)
				{
					// set the valve position value
					energyStorage.setValue(message.isEnergyStorage());

					// update the list of changed attributes
					changedAttributes.add(energyStorage);
				}

				// Battery Capacity
				EEP26BatteryCapacity batteryCapacity = (EEP26BatteryCapacity) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26BatteryCapacity.NAME);

				if (batteryCapacity != null)
				{
					// set the valve position value
					batteryCapacity.setValue(message.isBatteryCapacity());

					// update the list of changed attributes
					changedAttributes.add(batteryCapacity);
				}

				// Cover Open
				EEP26CoverOpen coverOpen = (EEP26CoverOpen) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26CoverOpen.NAME);

				if (coverOpen != null)
				{
					// set the valve position value
					coverOpen.setValue(message.isCoverOpen());

					// update the list of changed attributes
					changedAttributes.add(coverOpen);
				}

				// Temperature Sensor Failure
				EEP26TemperatureSensorFailure temperatureSensorFailure = (EEP26TemperatureSensorFailure) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL,
				                EEP26TemperatureSensorFailure.NAME);

				if (temperatureSensorFailure != null)
				{
					// set the valve position value
					temperatureSensorFailure.setValue(message.isTemperatureSensorFailure());

					// update the list of changed attributes
					changedAttributes.add(temperatureSensorFailure);
				}

				// Window Open
				EEP26WindowOpen windowOpen = (EEP26WindowOpen) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26WindowOpen.NAME);

				if (windowOpen != null)
				{
					// set the valve position value
					windowOpen.setValue(message.isWindowOpen());

					// update the list of changed attributes
					changedAttributes.add(windowOpen);
				}

				// Actuator Obstructed
				EEP26ActuatorObstructed actuatorObstructed = (EEP26ActuatorObstructed) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26ActuatorObstructed.NAME);

				if (actuatorObstructed != null)
				{
					// set the valve position value
					actuatorObstructed.setValue(message.isActuatorObstructed());

					// update the list of changed attributes
					changedAttributes.add(actuatorObstructed);
				}

				// Temperature
				EEP26TemperatureLinear temperatureLinear = (EEP26TemperatureLinear) this
				        .getChannelAttribute(A52001.INPUT_CHANNEL, EEP26TemperatureLinear.NAME);

				if (temperatureLinear != null)
				{
					// set the valve position value
					temperatureLinear.setRawValue(message.getTemperature());

					// update the list of changed attributes
					changedAttributes.add(temperatureLinear);
				}

				// if some attribute changed, notify it to listeners
				if (!changedAttributes.isEmpty())
				{
					// build the dispatching task
					EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
					        changedAttributes, A52001.INPUT_CHANNEL);

					// submit the task for execution
					this.attributeNotificationWorker.submit(dispatcherTask);

					// set success at true
					// TODO check what to do if nothing changes, i.e., with
					// success
					// equal to false.
					success = true;
				}
			}
		}

		return success;
	}

	private void senMessage(EnJConnection connection, byte deviceAddress[],
	        A52001ActuatorInputMessage message)
	{

		// prepare the data payload to host received configuration values
		byte dataByte[] = new byte[5];
		byte payload[] = message.getMessageBytes();

		// add the packet rorg
		dataByte[0] = A520.rorg.getRorgValue();
		dataByte[1] = payload[3];
		dataByte[2] = payload[2];
		dataByte[3] = payload[1];
		dataByte[4] = payload[0];
		// System.arraycopy(payload, 0, dataByte, 1, 4);

		// data payload is now ready, send the data
		connection.sendRadioCommand(deviceAddress, dataByte);
	}

	public void sendValvePositionMessage(EnJConnection connection, byte deviceAddress[],
	        int valvePosition)
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setValvePosition(valvePosition);
		message.setSetPointSelectionMode(SetPointSelectionModeMode.VALVE_POSITION);

		senMessage(connection, deviceAddress, message);
	}

	public void sendValveTemperatureMessage(EnJConnection connection, byte deviceAddress[],
	        double temperature)
	{
		sendValveTemperatureMessage(connection, deviceAddress, temperature, false);
	}

	public void sendValveTemperatureInverseMessage(EnJConnection connection, byte deviceAddress[],
	        double temperature)
	{
		sendValveTemperatureMessage(connection, deviceAddress, temperature, true);
	}

	public void sendValveTemperatureMessage(EnJConnection connection, byte deviceAddress[],
	        double temperature, boolean setPointInverse)
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		int valvePosition = 0;
		message.setValvePosition(valvePosition);
		message.setSetpointInverse(setPointInverse);
		message.setSetPointSelectionMode(SetPointSelectionModeMode.TEMPERATURE);
		senMessage(connection, deviceAddress, message);
	}

	public void sendValveOpenMessage(EnJConnection connection, byte deviceAddress[])
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setValveOpen(true);
		senMessage(connection, deviceAddress, message);
	}

	public void sendValveCloseMessage(EnJConnection connection, byte deviceAddress[])
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setValveClosed(true);
		senMessage(connection, deviceAddress, message);
	}

	public void sendSummerModeMessage(EnJConnection connection, byte deviceAddress[])
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setSummerMode(true);
		senMessage(connection, deviceAddress, message);
	}

	public void sendRunInitSequenceMessage(EnJConnection connection, byte deviceAddress[])
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setRunInitSequence(true);
		senMessage(connection, deviceAddress, message);
	}

	public void sendliftSetMessage(EnJConnection connection, byte deviceAddress[])
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setLiftSet(true);
		senMessage(connection, deviceAddress, message);
	}

	public void sendSelectFunctionMessage(EnJConnection connection, byte deviceAddress[],
	        FunctionMode functionMode)
	{
		A52001ActuatorInputMessage message = new A52001ActuatorInputMessage();
		message.setSelectFunction(functionMode);
		senMessage(connection, deviceAddress, message);
	}
}
