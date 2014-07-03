/**
 * 
 */
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import it.polito.elite.enocean.enj.EEP26.EEPIdentifier;
import it.polito.elite.enocean.enj.EEP26.EEPRegistry;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 * 
 */
public class D20108 extends D201
{
	//the type definition
	public static final byte type = (byte)0x08;
	
	public static boolean ON = true;
	public static boolean OFF = false;

	public static byte ALL_OUTPUT_CHANNEL = 0x1E;
	
	//register the type
	static
	{
		EEPRegistry.getInstance().addProfile(new EEPIdentifier(D201.rorg, D201.func, D20108.type), D20108.class);
	}

	public D20108()
	{
		super("2.6");
	}

	public byte[] actuatorSetOutput(boolean outputCommand)
	{
		byte outputValue;

		if (outputCommand == ON)
		{
			outputValue = (byte) 0x64;
		}
		else
		{
			outputValue = (byte) 0x00;
		}
		return super.actuatorSetOutput(SWITCH_TO_NEW_OUTPUT_VALUE,
				ALL_OUTPUT_CHANNEL, outputValue);
	}
}
