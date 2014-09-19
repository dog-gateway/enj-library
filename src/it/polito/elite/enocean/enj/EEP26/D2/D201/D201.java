/**
 * 
 */
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import it.polito.elite.enocean.enj.EEP26.EEP;
import it.polito.elite.enocean.enj.EEP26.Rorg;
import it.polito.elite.enocean.enj.communication.EnJConnection;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
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
		dataByte[1]= (byte)((overCurrentShutDown << 7)+(resetOverCurrentShutDown<<6)+(localControl<<5)+channelId);

	}
}
