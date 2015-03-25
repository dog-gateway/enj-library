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
package it.polito.elite.enocean.enj.util;

/**
 * A class offering static methods for easily handling operations on bytes and
 * byte arrays
 * 
 * @author bonino
 *
 */
public class ByteUtils
{

	/**
	 * Given a byte array provides back the corresponding string representation
	 * as 0xHHHH. the byte order is assumed to be little endian, i.e., LSB
	 * located at higher positions in the array.
	 * 
	 * @param byteArray
	 * @return The {@link String} representation of the given byte array.
	 */
	public static String toHexString(byte byteArray[])
	{
		// prepare the string buffer for holding the final byte representation
		StringBuffer hexBytes = new StringBuffer();

		// append the hexadecimal notation identifier
		hexBytes.append("0x");

		// convert each byte
		for (int i = 0; i < byteArray.length; i++)
			hexBytes.append(String.format("%02x", byteArray[i]));
		// render the buffer as a string
		return hexBytes.toString();
	}

	/**
	 * Given a byte provides back the corresponding string representation as
	 * 0xHH.
	 * 
	 * @param theByte
	 *            to convert.
	 * @return The {@link String} representation of the given byte.
	 */
	public static String toHexString(byte theByte)
	{
		return String.format("0x%02x", theByte);
	}

}
