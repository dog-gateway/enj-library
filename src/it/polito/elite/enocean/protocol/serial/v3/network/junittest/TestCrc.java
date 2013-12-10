/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.junittest;

import static org.junit.Assert.*;
import it.polito.elite.enocean.protocol.serial.v3.network.crc8.Crc8;

import org.junit.Test;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class TestCrc {
	/**
	 * Test method for {@link it.polito.elite.enocean.protocol.serial.v3.network.crc8.Crc8#calc(byte[])}.
	 */
	@Test
	public void testCalc() {
		//Crc8 crc = new Crc8();
		
		byte data[] = new byte[4];
		data[0] = 0x03;
		data[1] = 0x04;
		data[2] = 0x01;
		data[3] = 0x01;
		
		assertEquals(0x83,Crc8.calc(data));
		
		/*
		 * La funzione è giusta ma restituisce i valori in SIGNED per cui per il caso:
		 * 
		 * assertEquals(0x83,(byte)Crc8.calc(data)) restuisce -125 (10000011)
		 * 
		 */
		
	}

}
