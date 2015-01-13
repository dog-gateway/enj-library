/**
 * 
 */
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import it.polito.elite.enocean.enj.EEP26.EEP;
import it.polito.elite.enocean.enj.EEP26.Rorg;
import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.radio.Radio;

/**
 * @author <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi</a>, <a
 *         href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @param <T>
 * 
 */
public abstract class D201 extends EEP
{
	// the EEP26 definition, according to the EEP26 specification
	public static final Rorg rorg = new Rorg((byte) 0xd2);
	public static final byte func = (byte) 0x01;

	// func must be defined by extending classes

	// -------------------------------------------------
	// Parameters defined by this EEP, which
	// might change depending on the network
	// activity.
	// --------------------------------------------------

	// -------------------------------------------------

	// the class constructor
	public D201(String version)
	{
		// call the superclass constructor
		super(version);
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
		byte dataByte[] = new byte[3];

		// CMD code (0x01), the first 4 bits are not used
		dataByte[0] = 0x01;

		// Dim value: bit 7 to 5 - IO channel: bit 4 to 0
		dataByte[1] = (byte) ((dimValue << 5) + ioChannel);

		// Output value bit 6 to 0
		dataByte[2] = outputValue;

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
		byte dataByte[] = new byte[4];

		// CMD code (0x02), the first bit relates to taught-in devices, not
		// handled at the moment, the other 3 bits are not used, in binary
		// notation this means 10000002 (1 enable taught-in devices, 000 not
		// used, 0002 command code
		dataByte[0] = (byte) ((0x08 << 4) + 0x02); // byte is signed therefore
													// 0x82 would require a
													// cast)

		// bit 7 -> over current shutdown
		// bit 6 -> over current shutdown reset
		// bit 5 -> local control
		// bit 4 to 0 -> channelId
		dataByte[1] = (byte) ((overCurrentShutDown << 7)
				+ (resetOverCurrentShutDown << 6) + (localControl << 5) + channelId);

		// first 4 bits, dim timer 2, remaining 4 bits, dim timer 3
		dataByte[2] = (byte) ((dimTime2.getCode() << 4) + dimTime3.getCode());

		// bit 7 -> user interface mode
		// bit 6 -> power failure
		// bit 4-5 -> default state
		// bit 0-4 -> dim timer 1
		dataByte[3] = (byte) ((userInterfaceIndication << 7)
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
		byte dataByte[] = new byte[2];

		// first byte
		// bit 0-3 -> command id
		dataByte[0] = 0x03;

		// second byte
		// bit 0-4 -> channel id
		dataByte[1] = channelId;

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
		byte dataByte[] = new byte[6];

		// first byte -> lower 4 bits for the command code
		dataByte[0] = 0x05;

		// second byte:
		// first bit -> report measurement
		// second bit -> reset measurement
		// third bit -> measurement mode
		// last 5 bits -> channel id
		dataByte[1] = (byte) ((reportMeasurement << 7)
				+ (resetMeasurement << 6) + (measurementMode << 5) + channelId);

		// third byte
		// first 4 bits -> measurementDeltaLSB
		// 5th bit -> not used
		// last 3 bits -> unit of measure
		dataByte[2] = (byte) ((measurementDeltaLSB << 4) + (0x00 << 3) + (unit));

		// fourth byte
		// 8 bit of measurementDeltaMSB
		dataByte[3] = measurementDeltaMSB;

		// fifth byte
		// maximum time between subsequent actuator messages
		dataByte[4] = mat;

		// sixth byte
		// minimum time between subsequent actuator messages
		dataByte[5] = mit;

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
		byte dataByte[] = new byte[2];

		// first byte -> lower 4 bits for the command code
		dataByte[0] = 0x06;

		// second byte
		// first 2 bits are not used;
		// third bit -> query mode (0b0 pEnergy, 0b1 Power)
		// last 5 bits -> channelId
		dataByte[1] = (byte) (((0x01 & queryType) << 5) + channelId);

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
		byte powerFailure = (byte) (dataPayload[0] & (byte) 0x80);

		// power failure detection 0->power failure not detected / supported
		// /enabled, 1-> power failure detected
		byte powerFailureDetection = (byte) (dataPayload[0] & (byte) 0x40);

		// the command id, should be 0x04
		byte commandId = (byte) (dataPayload[0] & (byte) 0x0F);

		// the status of the overcurrent switch off function
		// 0 -> ready / not supported
		// 1 -> executed
		byte overCurrentSwitchOff = (byte) (dataPayload[1] & (byte) 0x80);

		// the current error level
		// 0 -> hardware ok
		// 1 -> hardware warning
		// 2 -> hardware failure
		// 3 -> not supported
		byte errorLevel = (byte) (dataPayload[1] & (byte) 0x60);

		// the channel id
		// 0x00..0x1D -> Output Channel
		// 0x1E -> not applicable / do not use
		// 0x1F -> input channel (from mains supply)
		byte channelId = (byte) (dataPayload[1] & (byte) 0x1F);

		// local control
		// 0 -> disabled / not supported
		// 1 -> enabled
		byte localControl = (byte) (dataPayload[2] & (byte) 0x80);

		// output value
		// 0x00 -> 0% or OFF
		// 0x01 - 0x64 -> 1% -100% (or ON)
		// 0x65 - 0x7E -> Not used
		// 0x7F -> Output not valid / not set
		byte outputValue = (byte) (dataPayload[2] & (byte) 0x7F);

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
		byte unit = (byte) (dataPayload[1] & (byte) 0xE0);

		// the channel id
		// 0x00..0x1D -> Output Channel
		// 0x1E -> not applicable / do not use
		// 0x1F -> input channel (from mains supply)
		byte channelId = (byte) (dataPayload[1] & (byte) 0x1F);
		
		byte measureValue[] = new byte[4];
		measureValue[0] = dataPayload[2];
		measureValue[1] =  dataPayload[3];
		measureValue[2] = dataPayload[4];
		measureValue[3]	= dataPayload[5];

		return new D201ActuatorMeasurementResponse(commandId, channelId, measureValue, unit);
	}
	
	public void handleProfileUpdate(Radio pkt)
	{
		
	}
}
