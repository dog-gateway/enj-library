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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import it.polito.elite.enocean.enj.EEP2_5.receiveEvent.PacketEventSender;
import it.polito.elite.enocean.protocol.serial.v3.network.link.serial.SerialPortFactory;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
import gnu.io.SerialPort;

/**
 * The link-level EnOcean connection, handles packet transmission and reception.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @authr <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi </a>
 * 
 */
public class EnJLink
{
	// The default serial port timeout
	public static int SERIAL_TIMEOUT;

	// The serial port to which the link layer is attached
	SerialPort serialPort;

	// The high priority RX and TX queues used by the link layer
	ConcurrentLinkedQueue<PacketQueueItem> highPriorityTxQueue;
	ConcurrentLinkedQueue<PacketQueueItem> highPriorityRxQueue;

	// The low priority RX and TX queues used by the link layer
	ConcurrentLinkedQueue<PacketQueueItem> lowPriorityTxQueue;
	ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue;

	// the semaphore for synchronizing reception and transmission
	Semaphore expectedResponse;

	// the transmitter
	PacketTransmitter transmitter;

	// the receiver
	PacketReceiver receiver;

	/*
	 * public PacketEventSender packetSender = new PacketEventSender(
	 * this.lowPriorityRxQueue);
	 */// TODO: check if can be removed.

	/**
	 * 
	 */
	public EnJLink(String serialPortId)
	{
		super();

		// build transmission and reception queues
		this.highPriorityRxQueue = new ConcurrentLinkedQueue<>();
		this.highPriorityTxQueue = new ConcurrentLinkedQueue<>();
		this.lowPriorityRxQueue = new ConcurrentLinkedQueue<>();
		this.lowPriorityTxQueue = new ConcurrentLinkedQueue<>();

		// build the response semaphore
		this.expectedResponse = new Semaphore(1);

		// get the serial port
		this.serialPort = SerialPortFactory.getPort(serialPortId,
				EnJLink.SERIAL_TIMEOUT);

		// check not null
		if (this.serialPort != null)
		{
			// build the packet transmitter
			this.transmitter = new PacketTransmitter(this.highPriorityTxQueue,
					lowPriorityTxQueue, serialPort, expectedResponse);

			// build the packet receiver
			this.receiver = new PacketReceiver(serialPort, highPriorityRxQueue,
					lowPriorityRxQueue, expectedResponse);
		}
	}

	public void startConnection() throws Exception
	{

		serialPort = (new SerialPortFactory()).getPort("/dev/ttyAMA0", 1000);

		// (new HighPriorityThread( highPriorityTxQueue, highPriorityRxQueue,
		// expectedResponse)).run();

		PacketReceiver serialListener = new PacketReceiver(serialPort,
				highPriorityRxQueue, lowPriorityRxQueue, expectedResponse);
		serialPort.addEventListener(serialListener);
		serialPort.notifyOnDataAvailable(true);

		// PacketEventSender packetSender = new
		// PacketEventSender(this.lowPriorityRxQueue);
		packetSender.start();

		threadWrite = new PacketTransmitter(highPriorityTxQueue,
				lowPriorityTxQueue, serialPort, expectedResponse);
		threadWrite.start();
		System.out.println("");
	}

	public void send(ESP3Packet pkt)
	{
		// System.out.println("Sono in send packet");
		lowPriorityTxQueue.add(new PacketQueueItem(pkt, 3));
	}

	public ESP3Packet receive()
	{
		// return this.lowPriorityRxQueue.peek().getPkt();
		int size = lowPriorityRxQueue.size();
		System.out.println("Elementi in coda: " + size);
		if (size != 0)
		{
			ESP3Packet pkt = this.lowPriorityRxQueue.poll().getPkt();
			return pkt;
		}
		// return pkt;
		return null;
	}
}