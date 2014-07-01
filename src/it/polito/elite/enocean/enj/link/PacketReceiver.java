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
package it.polito.elite.enocean.enj.link;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * A class implementing the receiver tier of the Java EnOcean Serial Protocol
 * API, EnJ. It listens for new incoming packets on the attached serial port,
 * and routes packets depending on their nature, accounting for responses, when
 * needed.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @authr <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi </a>
 * 
 */
public class PacketReceiver implements SerialPortEventListener
{
	// The serial port from which packets are received (i.e., read)
	private SerialPort serialPort;

	// HighPriority message queue, will hold incoming messages who need
	// response.
	// within a maximum time frame of 500ms. TODO check the actual time frame on
	// the ESP3 specs
	private ConcurrentLinkedQueue<PacketQueueItem> highPriorityRxQueue;

	// LowPriority message queue, holds messages not needing any response.
	private ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue;

	// Semaphore for "waiting" responses for those packets needing a response
	// from the transceiver. Typically, a message is sent by the
	// PacketTransmitter over the serial connection and no other operations
	// could be performed until a response is received.
	private Semaphore expectedResponse;

	/**
	 * Create a {@link PacketReceiver} instance, attached to the given serial
	 * port, and using the given message queues and response semaphore.
	 * 
	 * @param serialPort
	 *            The serial port upon which receiving packets.
	 * @param highPriorityRxQueue
	 *            The High Priority queue to which deliver messages needing a
	 *            response.
	 * @param lowPriorityRxQueue
	 *            The Low Priority queue to which deliver all the other
	 *            messages.
	 * @param expectedResponse
	 *            The semaphore signaling if a response is needed.
	 */
	public PacketReceiver(
			ConcurrentLinkedQueue<PacketQueueItem> highPriorityRxQueue,
			ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue,
			SerialPort serialPort, Semaphore expectedResponse)
	{
		super();

		// store the serial port
		this.serialPort = serialPort;
	
		// store the high-priority queue reference
		this.highPriorityRxQueue = highPriorityRxQueue;

		// store the low-priority queue reference
		this.lowPriorityRxQueue = lowPriorityRxQueue;

		// store a reference to the expected response semaphore
		this.expectedResponse = expectedResponse;
	}

	/**
	 * @see {@link SerialPortEventListener}
	 * 
	 *      Listens for events on the attached serial port and handles incoming
	 *      data, parsing ESP3 packets and putting them in the right message
	 *      queue.
	 */
	@Override
	public void serialEvent(SerialPortEvent event)
	{
		try
		{
			// Input Stream of the serial port
			InputStream serialInputStream = this.serialPort.getInputStream();

			// Input byte buffer as Vector (check if needed)
			ArrayList<Byte> buffer = new ArrayList<Byte>();

			// check if data is available
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			{
				// read the incoming packet if data is available

				// Clean the input buffer
				if (buffer.size() > 0)
					buffer.clear();

				// byte placeholder for data reading
				int readedIntValue = 0;

				// Read until there is data on the serial port
				while ((serialInputStream.available()) > 0)
				{
					// read the next byte of data from the input stream
					readedIntValue = serialInputStream.read();

					// convert read data into a real byte
					byte readedByteValue = (byte) (readedIntValue & 0xff);

					// check for the packet sync byte
					if (readedByteValue == ESP3Packet.SYNC_BYTE)
					{

						// parse the Packet and enqueue it in the right
						// place
						ESP3Packet pkt = this.parsePacket(buffer);

						// check not null
						if (pkt != null)
						{
							// place the packet in the right queue
							putInQueue(pkt);

							// clear the buffer
							buffer.clear();
						}
					}

					// Store the current byte in the packet buffer (always
					// starts with a sync byte)
					buffer.add(Byte.valueOf(readedByteValue));

					// debug, TODO use a logging system here
					System.out.println(""
							+ String.format("%x", readedByteValue));

				}

				// try to read the last packet when transmission ends and no
				// sync bytes can be exploited as packet delimiter

				// parse the Packet and enqueue it in the right place
				ESP3Packet pkt = this.parsePacket(buffer);

				// check not null
				if (pkt != null)
				{
					// place the packet in the right queue
					putInQueue(pkt);

					// clear the buffer
					buffer.clear();
				}

				// debug, TODO use a logging system here
				System.out.println("Data read");
			}

		}
		catch (IOException e)
		{
			// TODO use a logging system here
			e.printStackTrace();
		}
	}

	/**
	 * Given a buffer of bytes as an {@link ArrayList} instance, parses the
	 * buffer into an ESP3 Packet, if possible.
	 * 
	 * @param buffer
	 *            The byte buffer to parse.
	 * @return The corresponding ESP3 {@link ESP3Packet} if the parsing process
	 *         was successful, null otherwise.
	 */
	private ESP3Packet parsePacket(ArrayList<Byte> buffer)
	{
		ESP3Packet pkt = null;

		if (buffer.size() > 0)
		{
			// Input byte buffer to use for packet construction
			byte[] receivedBytes;

			// Prepare a Packet instance for holding the just received data
			pkt = new ESP3Packet();

			// create a new byte array of the size of the read
			// packet
			receivedBytes = new byte[buffer.size()];

			// fill the byte array
			for (int i = 0; i < buffer.size(); i++)
			{
				receivedBytes[i] = buffer.get(i).byteValue();
			}
			// Build the Packet instance
			pkt.parsePacket(receivedBytes);
		}
		return pkt;
	}

	private void putInQueue(ESP3Packet pkt)
	{
		// If the packet is a response to a previously sent packet, then the
		// expected semaphore should be freed.
		if (pkt.isResponse())
		{
			// debug, TODO use a logging system here
			System.out.println("Received response packet");

			// free the expected response semaphore
			this.expectedResponse.release();

			// Add the packet to the low priority queue
			this.lowPriorityRxQueue.add(new PacketQueueItem(pkt));
		}
		else
		{
			// if the packet requires a response, than specific timings must be
			// respected (response in less than 500ms, TODO check the actual
			// time frame on the ESP3 specs), and therefore the packet should be
			// inserted into an
			// high priority message queue.
			if (pkt.requiresResponse())
			{
				this.highPriorityRxQueue.add(new PacketQueueItem(pkt));
			}
			else
			{
				// simple packet not requiring any response, should be treated
				// at normal speed.
				this.lowPriorityRxQueue.add(new PacketQueueItem(pkt));
			}
		}
	}

}