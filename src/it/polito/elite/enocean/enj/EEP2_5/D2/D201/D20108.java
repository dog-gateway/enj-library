/**
 * 
 */
package it.polito.elite.enocean.enj.EEP2_5.D2.D201;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class D20108 extends D201{
	public static boolean ON = true;
	public static boolean OFF = false;
	
	public static byte ALL_OUTPUT_CHANNEL = 0x1E;
	
	public D20108(){
		super();
	}
	
	public byte[] actuatorSetOutput(boolean outputCommand){
		byte outputValue;

		if (outputCommand == ON){
			outputValue = (byte) 0x64;
		}
		else{
			outputValue = (byte) 0x00;
		}
		return super.actuatorSetOutput(SWITCH_TO_NEW_OUTPUT_VALUE, ALL_OUTPUT_CHANNEL, outputValue);
	}
}
