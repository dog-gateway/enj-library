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
package it.polito.elite.enocean.protocol.serial.v3.network.link;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * A class implementing the reception tier of the Java EnOcean Serial Protocol
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
	// within a maximum time frame of 5ms.
	private ConcurrentLinkedQueue<Packet> highPriorityRxQueue;

	// LowPriority message queue, holds messages not needing any response.
	private ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue;

	// Semaphore for "waiting" responses for those packets needing a response
	// from the transceiver. Typically, a message is sent by the
	// PacketTransmitter over the serial connection and no other operations
	// could be performed until a response is received.
	private Semaphore expectedResponse;

	/**
	 * Create a {@link PacketReceiver} instance, attached to the given serial
	 * port, and using the given reception queues and response semaphore.
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
	public PacketReceiver(SerialPort serialPort,
			ConcurrentLinkedQueue<Packet> highPriorityRxQueue,
			ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue,
			Semaphore expectedResponse)
	{
		super();
		this.serialPort = serialPort;
		this.highPriorityRxQueue = highPriorityRxQueue;
		this.lowPriorityRxQueue = lowPriorityRxQueue;
		this.expectedResponse = expectedResponse;
	}

	@Override
	public void serialEvent(SerialPortEvent event)
	{
		try
		{
			// Input Stream of the serial port
			InputStream serialInputStream = this.serialPort.getInputStream();

			// Input byte buffer as ArrayList (check if needed)
			Vector<Byte> buffer = new Vector<Byte>();

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
					if (readedByteValue == Packet.SYNC_BYTE)
						this.readPacket(buffer);
					
					//clear the buffer
					buffer.clear();

					// Store the current byte in the packet buffer (always
					// starts with a sync byte)
					buffer.add(Byte.valueOf(readedByteValue));

					// debug, TODO use a logging system here
					System.out.println(""
							+ String.format("%x", readedByteValue));

				}

				// try to read the last packet when transmission ends and no
				// sync bytes can be exploited as packet delimiter
				this.readPacket(buffer);

				//clear the buffer
				buffer.clear();
				
				//debug
				//TODO: add logging here
				System.out.println("Data read");
			}

		}
		catch (IOException e)
		{
			// TODO add logging here
			e.printStackTrace();
		}
	}

	private void readPacket(Vector<Byte> buffer)
	{
		// Input byte buffer to use for packet construction
		byte[] receivedBytes;

		// Prepare a Packet instance for holding the just received data
		Packet pkt = new Packet();

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

		// place the packet in the right queue
		putInQueue(pkt);
	}

	private void putInQueue(Packet pkt)
	{
		// Se il pacchetto ricevuto e una risposta
		if (pkt.isResponse())
		{
			System.out.println("Il pacchetto � una risposta");

			// Libero il flag risposta attesa
			this.expectedResponse.release();

			// Aggiungo il paccketto alla coda dati a bassa priorit�
			this.lowPriorityRxQueue.add(new PacketQueueItem(pkt, 3));
		}
		else
		{
			if (pkt.requireResponse())
			{
				this.highPriorityRxQueue.add(pkt);
			}
			else
			{
				// Aggiungo il paccketto alla coda dati a bassa priorit�
				this.lowPriorityRxQueue.add(new PacketQueueItem(pkt, 3));
			}
		} // Fine isResponse
	}

}