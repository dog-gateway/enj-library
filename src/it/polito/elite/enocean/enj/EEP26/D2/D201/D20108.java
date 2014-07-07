/**
 * 
 */
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import java.io.Serializable;

import it.polito.elite.enocean.enj.EEP26.EEPIdentifier;
import it.polito.elite.enocean.enj.EEP26.EEPRegistry;
import it.polito.elite.enocean.enj.communication.EnJConnection;

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
	}

	// execution commands
	public void actuatorSetOuput(EnJConnection connection, byte[] deviceAddress, boolean command)
	{
		// exec the command by using the D201 general purpose implementation
		super.actuatorSetOutput(connection, deviceAddress, D201.SWITCH_TO_NEW_OUTPUT_VALUE,
				D20108.ALL_OUTPUT_CHANNEL, command ? D20108.ON_BYTE
						: D20108.OFF_BYTE);
	}

}
