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
import it.polito.elite.enocean.enj.link.serial.SerialPortFactory;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

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
	private SerialPort serialPort;

	// The high priority RX and TX queues used by the link layer
	private ConcurrentLinkedQueue<PacketQueueItem> highPriorityTxQueue;
	private ConcurrentLinkedQueue<PacketQueueItem> highPriorityRxQueue;

	// The low priority RX and TX queues used by the link layer
	private ConcurrentLinkedQueue<PacketQueueItem> lowPriorityTxQueue;
	private ConcurrentLinkedQueue<PacketQueueItem> lowPriorityRxQueue;

	// the semaphore for synchronizing reception and transmission
	private Semaphore expectedResponse;

	// the transmitter
	private PacketTransmitter transmitter;

	// the receiver
	private PacketReceiver receiver;

	// the packet delivery process
	private PacketDelivery pktDeliveryProcess;

	/**
	 * Builds a new instance of the EnJ link layer, which handles low-level
	 * communication with any physical transceiver connected to the serial port
	 * identified by given serial port id.
	 */
	public EnJLink(String serialPortId) throws Exception
	{
		super();

		// build transmission and reception queues
		// TODO: check if it is better to adopt circular buffers in order to
		// avoid possible memory leaks.
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
					this.lowPriorityTxQueue, this.serialPort,
					this.expectedResponse);

			// build (and start) the packet receiver
			this.receiver = new PacketReceiver(this.highPriorityRxQueue,
					this.lowPriorityRxQueue, this.serialPort,
					this.expectedResponse);

			// build the packet delivery process
			this.pktDeliveryProcess = new PacketDelivery(
					this.lowPriorityRxQueue);
		}
	}

	/**
	 * Establishes the connection to the EnOcean physical transceiver. Starts
	 * the processes responsible for receiving and sending messages to the
	 * transceiver, and then to the EnOcean network.
	 */
	public void connect()
	{
		if ((this.transmitter != null) && (this.receiver != null))
		{
			// --------------- TX ------------------

			// create the TX thread
			Thread transmitterThread = new Thread(this.transmitter);

			// enable the TX thread
			this.transmitter.setRunnable(true);

			// start the Thread
			transmitterThread.start();

			// add the receiver as a listener
			try
			{
				this.serialPort.addEventListener(this.receiver);
			}
			catch (TooManyListenersException e)
			{
				// TODO: handle exception, add a logging system here
				e.printStackTrace();
			}

			// enable asynchronous data handling on the serial port
			this.serialPort.notifyOnDataAvailable(true);

			// create the packet delivery process thread
			Thread packetDeliveryThread = new Thread(this.pktDeliveryProcess);

			// enable the packet delivery process
			this.pktDeliveryProcess.setRunnable(true);

			// start packet delivery
			packetDeliveryThread.start();
		}
	}

	/**
	 * Disconnects from the physical transceiver. Stops the transmission
	 * processes and removes the asynchronous handling of RX data.
	 */
	public void disconnect()
	{
		if ((this.transmitter != null) && (this.receiver != null))
		{
			// disable notification
			this.serialPort.notifyOnDataAvailable(false);

			// remove the event listener
			this.serialPort.removeEventListener();

			// stop the transmission thread
			this.transmitter.setRunnable(false);
		}
	}

	/**
	 * Adds a packet listener to the set of listeners notified of new packets in
	 * the RX queue (low priority)
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addPacketListener(PacketListener listener)
	{
		if (this.pktDeliveryProcess != null)
			this.pktDeliveryProcess.addPacketListener(listener);
	}

	/**
	 * Removes a packet listener from the set of listeners notified of new
	 * packets in the RX queue (low priority)
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	public void removePacketListener(PacketListener listener)
	{
		if (this.pktDeliveryProcess != null)
			this.pktDeliveryProcess.removePacketListener(listener);
	}

	/**
	 * Sends a single {@link ESP3Packet} to the transceiver
	 * 
	 * @param pkt
	 *            The packet to send.
	 */
	public void send(ESP3Packet pkt)
	{
		this.send(pkt, false);
	}

	/**
	 * Sends a single {@link ESP3Packet} to the transceiver using normal or high
	 * priority
	 * 
	 * @param pkt
	 *            the packet to send
	 * @param isHighPriority
	 *            true if the packet should be sent with high priority (typical
	 *            for responses), false otherwise.
	 */
	public void send(ESP3Packet pkt, boolean isHighPriority)
	{
		if (!isHighPriority)
			this.lowPriorityTxQueue.add(new PacketQueueItem(pkt));
		else
			this.highPriorityTxQueue.add(new PacketQueueItem(pkt));
	}

	/**
	 * Return the first unread packet from the reception queue. Might not be
	 * exactly the last if the packet delivery process is running and listeners
	 * are registered.
	 * 
	 * @return The first unread {@link ESP3Packet}
	 */
	public ESP3Packet receive()
	{
		ESP3Packet pkt = null;

		// get the current RX queue size
		int size = lowPriorityRxQueue.size();

		// debug, TODO: add a logging system here
		System.out.println("Packets in queue: " + size);

		// check that the queue is not empty
		if (!this.lowPriorityRxQueue.isEmpty())
			pkt = this.lowPriorityRxQueue.poll().getPkt();

		// return the read packet or null;
		return pkt;
	}
}