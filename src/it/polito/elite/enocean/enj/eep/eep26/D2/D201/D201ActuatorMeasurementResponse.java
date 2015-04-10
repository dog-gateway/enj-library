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

import java.nio.ByteBuffer;

/**
 * @author bonino
 *
 */
public class D201ActuatorMeasurementResponse
{
	// the command id, always 7
	private int commandId;

	// the channel id
	private int channelId;

	// the input / output flag
	private boolean output;

	// the measure
	private byte measure[];

	// the measure unit
	private D201UnitOfMeasure unit;

	public D201ActuatorMeasurementResponse(byte commandId, byte channelId,
			byte[] measureValue, byte unit)
	{
		// store the command id
		this.commandId = (int) commandId;

		// store the channel id
		this.channelId = (byte) channelId;

		// set the input/output flag
		this.output = (this.channelId < 126); // leaves space for
												// "not used values" to be
												// classified as input
		// store the measure as a byte and the offer means to retrieve his value
		// as int, double or whatever. Performs an array deep copy.
		this.measure = new byte[measureValue.length];
		for (int i = 0; i < measureValue.length; i++)
			this.measure[i] = measureValue[i];

		// store the unit of measure
		this.unit = D201UnitOfMeasure.valueOf(unit);

	}

	/**
	 * @return the commandId
	 */
	public int getCommandId()
	{
		return commandId;
	}

	/**
	 * @return the channelId
	 */
	public int getChannelId()
	{
		return channelId;
	}

	/**
	 * @return the output
	 */
	public boolean isOutput()
	{
		return output;
	}

	/**
	 * @return the measure
	 */
	public byte[] getMeasure()
	{
		return measure;
	}

	/**
	 * @return the unit
	 */
	public D201UnitOfMeasure getUnit()
	{
		return unit;
	}

	/**
	 * Returns the measure value as a double TODO: check measure encoding as it
	 * is not specified in the EEP documentation
	 * 
	 * @return
	 */
	public double getMeasureAsDouble()
	{
		ByteBuffer buffer = ByteBuffer.wrap(measure);

		return (double)buffer.getInt();
	}

	/**
	 * Returns the measure value as an integer.
	 * 
	 * @return
	 */
	public int getMeasureAsInt()
	{
		ByteBuffer buffer = ByteBuffer.wrap(measure);

		return buffer.getInt();
	}

}
