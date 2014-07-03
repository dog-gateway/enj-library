/**
 * 
 */
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import it.polito.elite.enocean.enj.EEP26.EEP;
import it.polito.elite.enocean.enj.EEP26.Rorg;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 * 
 */
public abstract class D201 extends EEP
{
	// the EEP26 definition, according to the EEP26 specification
	public static final Rorg rorg = new Rorg((byte)0xd2);
	public static final byte func = (byte)0x01;
	//func must be defined by extending classes
	
	/*
	 * Constant fields for dim value in CMD = 0x01
	 */
	static byte SWITCH_TO_NEW_OUTPUT_VALUE = 0x00;
	static byte DIM_TO_NEW_OUTPUT_VALUE_TIMER1 = 0x01;
	static byte DIM_TO_NEW_OUTPUT_VALUE_TIMER2 = 0x02;
	static byte DIM_TO_NEW_OUTPUT_VALUE_TIMER3 = 0x03;
	static byte STOP_DIMMING = 0x04;
	// -------------------------------------------------

	byte[] dataByte;

	// the class constructor
	public D201(String version)
	{
		//call the superclass constructor
		super(version);
	}
	
	// Metodo per il CMD 1: Actuator Set Output
	byte[] actuatorSetOutput(byte dimValue, byte ioChannel, byte outputValue)
	{ // Visto che outputValue � una percentuale da 0 a 100 la metto in int?
		dataByte = new byte[3];

		// CMD code, the first 4 bit are unused
		dataByte[0] = 0x01;
		// Dim value: bit 7 to 5 - IO channel: bit 4 to 0
		dataByte[1] = (byte) ((dimValue << 5) + ioChannel);
		// Output value bit 6 to 0
		dataByte[2] = outputValue;

		/*
		 * Stampo a video solo per verificare la correttezza dei dati in fase di
		 * debug
		 */
		/*
		 * System.out.println("DB_2 " + String.format("%x", dataByte[0]));
		 * System.out.println("DB_2 " + String.format("%x", dataByte[1]));
		 * System.out.println("DB_2 " + String.format("%x", dataByte[2]));
		 */
		// ----------------------------------------------------------------------------
		return dataByte;
	}
}
