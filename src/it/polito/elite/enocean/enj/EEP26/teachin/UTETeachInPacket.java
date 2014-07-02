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
package it.polito.elite.enocean.enj.EEP26.teachin;

import it.polito.elite.enocean.enj.EEP2_5.primitives.EnoceanEquipmentProfile;
import it.polito.elite.enocean.enj.EEP2_5.primitives.Rorg;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author bonino
 * 
 */
public class UTETeachInPacket
{
	//packet identifiers as per the EnOcean EEP2.6
	public static byte TEACHIN_REQUEST = (byte) 0x00;
	public static byte TEACHIN_DELECTION_REQUEST = (byte) 0x01;
	public static byte TEACHIN_NOT_SPECIFIED = (byte) 0x02;
	
	// the raw (link layer) packet wrapped by this instance
	private ESP3Packet rawPacket;

	// the data payload
	private byte payload[];

	// the device address
	private byte address[];

	// the manufacturer id
	private byte manId[];

	// the packet RORG
	private Rorg rorg;

	// the packet EEP
	private EnoceanEquipmentProfile eep;

	/**
	 * 
	 */
	public UTETeachInPacket(ESP3Packet pkt)
	{
		// store the packet reference
		this.rawPacket = pkt;

		// initialize the packet payload
		this.payload = new byte[7];

		// fill the payload

		// get the raw, un-interpreted data payload
		byte rawData[] = this.rawPacket.getData();

		// get the actual payload
		for (int i = 1; i < 8; i++)
		{
			// reverse fill
			this.payload[this.payload.length - i] = rawData[i];
		}

		// intialize the packet address
		this.address = new byte[4];

		// get the actual address
		for (int i = 8; i < 12; i++)
		{
			this.address[this.address.length - i] = rawData[i];
		}

		// get the manufacturer id
		this.manId = new byte[2];

		// Consider only DB_6.BIT2 , DB_6.BIT1 , DB_6.BIT0 by performing a
		// bitwise AND with the 00000111 mask.
		this.manId[0] = (byte) (this.payload[3] & (0x07));
		this.manId[1] = this.payload[4];

		// build the rorg
		this.rorg = new Rorg(this.payload[0]);

		// build the equipment profile
		this.eep = new EnoceanEquipmentProfile(this.rorg, this.payload[1],
				this.payload[2]);
	}

	/**
	 * Gets the EnOcean RORG of this teach-in packet
	 * 
	 * @return
	 */
	public Rorg getRorg()
	{
		return this.rorg;
	}

	/**
	 * Gets the EnOcean Equipment Profile of the device who has sent this teach
	 * in packet.
	 * 
	 * @return
	 */
	public EnoceanEquipmentProfile getEEP()
	{
		return this.eep;
	}

	/**
	 * Checks if this {@link UTETeachInPacket} instance is a teach-in request
	 * @return
	 */
	public boolean isTeachInRequest()
	{
		return ((this.payload[6] & (0x30)) == UTETeachInPacket.TEACHIN_DELECTION_REQUEST)
				|| ((this.payload[6] & (0x30)) == (byte) 0x20);
	}
	
	public boolean isTeachInDeletionRequest()
	{
		return ((payload[6] & (0x30)) == UTETeachInPacket.TEACHIN_DELECTION_REQUEST);
	}
	
	
	/**
	 * Checks if the given packet is a UTE TeachIn packet
	 * 
	 * @param pkt
	 *            the packet to check
	 * @return true if it is a UTE Teach In, false otherwise.
	 */
	public static boolean isUTETeachIn(ESP3Packet pkt)
	{
		// the packet should be a radio packet with a specific value in the
		// first byte of the data payload (RORG).
		return (pkt.isRadio()) && (pkt.getData()[0] == Rorg.UTE);
	}

}
