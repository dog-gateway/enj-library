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
package it.polito.elite.enocean.enj.EEP26.packet;

import it.polito.elite.enocean.enj.EEP26.Rorg;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author bonino
 *
 */
public class VLDPacket
{
	// the raw (link layer) packet wrapped by this instance
	private ESP3Packet rawPacket;

	// the data payload
	private byte payload[];

	// the device address
	private byte address[];
	
	// the packet Rorg
	private Rorg rorg;

	/**
	 * @param pkt
	 * 
	 */
	public VLDPacket(ESP3Packet pkt)
	{
		// store the raw packet wrapped by this VLDPacket instance
		this.rawPacket = pkt;

		// get the raw, un-interpreted data payload,
		// for VLD packets the payload may have a variable length
		// therefore data shall be accessed accordingly
		byte rawData[] = this.rawPacket.getData();

		// compute the payload length: data.length - 1byte RORG - 4 byte
		// senderId - 1 byte status
		int payloadLength = rawData.length - 6;

		// initialize the packet payload
		this.payload = new byte[payloadLength];

		// read the payload in order
		// TODO: check if reversing is needed, apparently it is not.
		// skip RORG (first byte)
		int startingOffset = 1;
		for (int i = startingOffset; i < (startingOffset+payloadLength); i++)
		{
			// reverse fill
			this.payload[this.payload.length - i] = rawData[i];
		}

		// intialize the packet address
		this.address = new byte[4];

		// get the actual address
		startingOffset = rawData.length-(1+this.payload.length);
		for (int i = startingOffset; i < (startingOffset+this.address.length); i++)
		{
			// reverse fill, TODO check if works!
			this.address[this.address.length - (i - 8)] = rawData[i];
		}
		
		// build the actual Rorg
		this.rorg = new Rorg(rawData[0]);
		
		// no func nor type definitions are provided in these packets...
	}
	
	

	/**
	 * @return the rawPacket
	 */
	public ESP3Packet getRawPacket()
	{
		return rawPacket;
	}



	/**
	 * @return the payload
	 */
	public byte[] getPayload()
	{
		return payload;
	}



	/**
	 * @return the address
	 */
	public byte[] getAddress()
	{
		return address;
	}



	/**
	 * @return the rorg
	 */
	public Rorg getRorg()
	{
		return rorg;
	}



	public static boolean isVLDPacket(ESP3Packet pkt)
	{
		// the packet should be a radio packet with a specific value in the
		// first byte of the data payload (RORG).
		return (pkt.isRadio()) && (pkt.getData()[0] == Rorg.VLD);
	}

}
