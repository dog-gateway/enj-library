/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.enj;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class D201 {
	/*
	 * 	Constant fields for dim value in CMD = 0x01 
	 */
	static byte SWITCH_TO_NEW_OUTPUT_VALUE = 0x00;
	static byte DIM_TO_NEW_OUTPUT_VALUE_TIMER1 = 0x01;
	static byte DIM_TO_NEW_OUTPUT_VALUE_TIMER2 = 0x02;
	static byte DIM_TO_NEW_OUTPUT_VALUE_TIMER3 = 0x03;
	static byte STOP_DIMMING = 0x04;
	//-------------------------------------------------
	
	byte[] dataByte;
	public void actuatorSetOutput(byte dimValue, byte ioChannel, byte outputValue){
		dataByte = new byte[3];
		// CMD code, the first 4 bit are unused
		dataByte[0] = 0x01;
		// Dim value: bit 7 to 5  - IO channel: bit 4 to 0
		dataByte[1] = (byte) ( (dimValue<<5) + ioChannel);
		// Output value bit 6 to 0
		dataByte[2] = outputValue;
		
		/*
		 * Stampo a video solo per verificare la correttezza dei dati in fase di debug
		 */
		System.out.println(dataByte[0]); //Stampare in hex
		System.out.println("DB_2 " + String.format("%x", dataByte[0]));
		System.out.println("DB_2 " + String.format("%x", dataByte[1]));
		System.out.println("DB_2 " + String.format("%x", dataByte[2]));
	}
}
