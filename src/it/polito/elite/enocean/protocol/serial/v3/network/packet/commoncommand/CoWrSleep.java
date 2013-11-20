/**
 * 
 * @author andreabiasi
 *
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/*
 * Order to enter the energy saving mode.
 */
public class CoWrSleep extends Packet{
	/*
	 * The byte vector optional may contains the optional data, in this packet type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue contains the sleep period
	 */
	private static byte[] dataValue;
	
	/*
	 * The sleep period is expressed in 10ms value.
	 * 00000000 = default
	 * Max value = 00FFFFFF(~46h)
	 */
	public CoWrSleep(int deepSleepPeriod){
		super((byte) 0x05,dataValue,optional); //NB Non ho optional value e non passo nulla
		dataValue[0] = (byte) (deepSleepPeriod & 0xff); //Command code
		dataValue[1] = (byte) ((deepSleepPeriod & 0xff00)>>8);
		dataValue[2] = (byte) ((deepSleepPeriod & 0xff0000)>>16);
		dataValue[3] = 0x00;
	}
}