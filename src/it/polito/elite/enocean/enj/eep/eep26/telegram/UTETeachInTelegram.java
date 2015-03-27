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
package it.polito.elite.enocean.enj.eep.eep26.telegram;

import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.Rorg;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * A class representing an UTE Teach-In telegram as defined by the EEP2.6
 * specification.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * 
 */
public class UTETeachInTelegram extends EEP26Telegram
{
	// packet identifiers according to the EnOcean EEP2.6 specification
	public static final byte TEACHIN_REQUEST = (byte) 0x00;
	public static final byte TEACHIN_DELECTION_REQUEST = (byte) 0x01;
	public static final byte TEACHIN_NOT_SPECIFIED = (byte) 0x02;

	// RESPONSE MASK as according to the EnOcean EEP2.6 specification
	public static final byte IS_RESPONSE_MASK = (byte) 0x40;

	// TEACH-IN RESULTS

	// 0x91 = 10010001 ->
	// DB6.BIT_7== 1 ->bidirectional communication
	// DB6.BIT_5..4 == 01 -> request accepted, teach-in successful
	// DB6.BIT3..0 == 001 -> command identifier: teach-in response
	public static final byte BIDIRECTIONAL_TEACH_IN_SUCCESSFUL = (byte) 0x91;

	// 0xb1 = 10110001 ->
	// DB6.BIT_7== 1 ->bidirectional communication
	// DB6.BIT_5..4 == 11 -> request refused, eep not supported
	// DB6.BIT3..0 == 001 -> command identifier: teach-in response
	public static final byte BIDIRECTIONAL_TEACH_IN_REFUSED = (byte) 0xb1;

	// 0xa1 = 10100001 ->
	// DB6.BIT_7== 1 ->bidirectional communication
	// DB6.BIT_5..4 == 10 -> request accepted, teach in canceled
	// DB6.BIT3..0 == 001 -> command identifier: teach-in response
	public static final byte BIDIRECTIONAL_TEACH_IN_DELETION_ACCEPTED = (byte) 0xa1;

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
	private EEPIdentifier eep;

	// the response flag
	protected boolean response;

	/**
	 * Class constructor, builds an instance of {@link UTETeachInTelegram} given
	 * the {@link ESP3Packet} containing the telegram as payload.
	 * 
	 * @param The
	 *            {@link ESP3Packet} containing the telegram as payload.
	 */
	public UTETeachInTelegram(ESP3Packet pkt)
	{
		// call the superclass constructor
		super(EEP26TelegramType.UTETeachIn);

		// by default the packet is not a response
		this.response = false;

		// store the packet reference
		this.rawPacket = pkt;

		// initialize the packet payload
		this.payload = new byte[7];

		// fill the payload

		// get the raw, un-interpreted data payload
		byte rawData[] = this.rawPacket.getData();

		// get the actual payload
		int startingOffset = 1;
		for (int i = startingOffset; i < (startingOffset + this.payload.length); i++)
		{
			// reverse fill
			this.payload[(startingOffset + this.payload.length)-(i+1)] = rawData[i];
		}

		// intialize the packet address
		startingOffset = 8;
		this.address = new byte[4];

		// get the actual address
		for (int i = startingOffset; i < (startingOffset + this.address.length); i++)
		{
			// normal order
			this.address[i - startingOffset] = rawData[i];
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
		this.eep = new EEPIdentifier(this.rorg, this.payload[1],
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
	public EEPIdentifier getEEP()
	{
		return this.eep;
	}

	/**
	 * Checks if this {@link UTETeachInTelegram} instance is a teach-in request
	 * 
	 * @return true if it is a teach-in request, false otherwise
	 */
	public boolean isTeachInRequest()
	{
		return ((this.payload[6] & (0x30)) == UTETeachInTelegram.TEACHIN_REQUEST)
				|| ((this.payload[6] & (0x30)) == (byte) 0x20);
	}

	/**
	 * Checks if this {@link UTETeachInTelegram} instance is a teach-in deletion
	 * request
	 * 
	 * @return true if it is a teach-in deletion request, false otherwise
	 */
	public boolean isTeachInDeletionRequest()
	{
		return ((payload[6] & (0x30)) == UTETeachInTelegram.TEACHIN_DELECTION_REQUEST);
	}

	/**
	 * Checks if this {@link UTETeachInTelegram} instance is a not specified
	 * teach-in
	 * 
	 * @return true if it is a not-specified teach-in packet, false otherwise.
	 */
	public boolean isNotSpecifiedTeachIn()
	{
		return ((payload[6] & (0x30)) == UTETeachInTelegram.TEACHIN_NOT_SPECIFIED);
	}

	/**
	 * Checks if this packet requires a response
	 * 
	 * @return true if the packet requires a response within 500ms, false
	 *         otherwise
	 */
	public boolean isResponseRequired()
	{
		return ((payload[6] & UTETeachInTelegram.IS_RESPONSE_MASK) == 0x00);
	}

	/**
	 * Checks if the packet represents a teach-in response or not
	 * 
	 * @return the response true if it is a response, false otherwise
	 */
	public boolean isResponse()
	{
		return response;
	}

	/**
	 * Sets the response flag, can only be called by extending classes
	 * 
	 * @param response
	 *            the response flag
	 */
	protected void setResponse(boolean response)
	{
		this.response = response;
	}

	/**
	 * Gets the raw {@link ESP3Packet} instance corresponding to this teach-in
	 * high-level packet
	 * 
	 * @return the rawPacket
	 */
	public ESP3Packet getRawPacket()
	{
		return rawPacket;
	}

	/**
	 * Get the address of the device who sent this packet
	 * 
	 * @return the address as a byte array
	 * 
	 *         TODO: integer would be better?
	 */
	@Override
	public byte[] getAddress()
	{
		return address;
	}

	/**
	 * Get the manufacturer id of the device who sent this packet
	 * 
	 * @return the manId as a byte array
	 * 
	 *         TODO: integer would be better?
	 */
	public byte[] getManId()
	{
		return manId;
	}

	/**
	 * Build the response to this packet instance, response could be one of
	 * <ul>
	 * <li><code>UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_SUCCESSFUL</code></li>
	 * <li><code>UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_REFUSED</code></li>
	 * <li>
	 * <code>UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_DELETION_ACCEPTED</code></li>
	 * </ul>
	 * <p>
	 * the typical usage is as follows:<br/>
	 * 
	 * <pre>
	 * //The teach-in request
	 * UTETeachInPacket uteRequest = new UTETeachInPacket(rawPacket);
	 * 
	 * //check if a response is required
	 * if(uteRequest.isResponseRequired())
	 * {
	 * 		//teach-in request
	 * 		if(uteRequest.isTeachInRequest())
	 * 		{
	 * 			//check if the eep is available
	 * 		if(this.isEEPAvailable(uteRequest.getEEP())
	 * 		{
	 * 			//build the accept response
	 * 			UTETeachInPacket response = uteRequest.buildResponse(UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_SUCCESSFUL);
	 * 			
	 * 			//send the response
	 * 			
	 * 		}
	 * 		}
	 * }
	 * </pre>
	 * 
	 * @param response
	 *            The response to send
	 * @return the {@link UTETeachInTelegram} response packet.
	 * 
	 * 
	 */
	public UTETeachInTelegram buildResponse(byte response)
	{

		// build the response packet
		byte[] payloadResp = new byte[13];

		// Rorg UTE
		payloadResp[0] = (byte) 0xD4;

		// Inverted order of bytes in the transmission
		payloadResp[1] = response;

		payloadResp[2] = this.payload[5];
		payloadResp[3] = this.payload[4];
		payloadResp[4] = this.payload[3];
		payloadResp[5] = this.payload[2];
		payloadResp[6] = this.payload[1];
		payloadResp[7] = this.payload[0];

		// address
		payloadResp[8] = (byte) 0x00;
		payloadResp[9] = (byte) 0xFF;
		payloadResp[10] = (byte) 0xFF;
		payloadResp[11] = (byte) 0xFF;

		// status
		payloadResp[12] = (byte) 0x00;

		// optional data
		byte[] opt = new byte[7];
		opt[0] = (byte) 0x03;
		opt[1] = this.address[0];//(byte) 0x00;
		opt[2] = this.address[1];//(byte) 0x81;
		opt[3] = this.address[2];//(byte) 0x2A;
		opt[4] = this.address[3];//(byte) 0x90;
		opt[5] = (byte) 0xFF;
		opt[6] = (byte) 0x00;

		// the low level packet
		ESP3Packet uteTeachInresponse = new ESP3Packet(ESP3Packet.RADIO,
				payloadResp, opt);
		// the UTE teach in packet
		UTETeachInTelegram responsePacket = new UTETeachInTelegram(
				uteTeachInresponse);

		// set the response flag
		responsePacket.setResponse(true);

		// the UTETeachinPacket
		return responsePacket;
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
