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

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class provides a utility for sampling a given queue containing
 * {@link ESP3Packet}s encapsulated into {@link PacketQueueItem}s, and to
 * deliver such packets to registered {@link PacketListener}s. It allows to
 * decouple reception timing from event handling times, thus acting as "buffer"
 * layer between low level events and high level packet handling.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @authr <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi </a>
 * 
 */
public class PacketDelivery implements Runnable
{
	// the time between two consecutive packet delivery cycles
	private static final int DEFAULT_PACKET_DELIVERY_TIME = 10;

	// a reference to the queue from which extracting Packets to deliver
	private ConcurrentLinkedQueue<PacketQueueItem> theQueue;

	// the set of listeners registered with the link layer
	private HashSet<PacketListener> listeners;

	// the instance-level delivery time
	private int deliveryTime;

	// the runnable flag
	private boolean runnable;

	/**
	 * Builds a {@link PacketDelivery} instance, initialized with the default
	 * delivery time (i.e.,
	 * <code>Packetdelivery.DEFAULT_PACKET_DELIVERY_TIME</code>).
	 */
	public PacketDelivery(ConcurrentLinkedQueue<PacketQueueItem> queue)
	{
		// store a reference to the queue from which events must be delivered
		this.theQueue = queue;

		// initialize the set of listeners to deliver to
		this.listeners = new HashSet<>();

		// default delivery time
		this.deliveryTime = PacketDelivery.DEFAULT_PACKET_DELIVERY_TIME;

		// initially runnable
		this.runnable = true;
	}

	/**
	 * Adds a packet listener to the set of listeners notified of new packets in
	 * the managed queue (low priority)
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addPacketListener(PacketListener listener)
	{
		if (this.listeners != null)
			this.listeners.add(listener);
	}

	/**
	 * Removes a packet listener from the set of listeners notified of new
	 * packets in the managed queue (low priority)
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	public void removePacketListener(PacketListener listener)
	{
		if (this.listeners != null)
			this.listeners.remove(listener);
	}

	/**
	 * Get the state of this {@link PacketDelivery} instance
	 * 
	 * @return true if the delivery process can run (or is running), false
	 *         otherwise.
	 */
	public boolean isRunnable()
	{
		return runnable;
	}

	/**
	 * Set the state of this {@link PacketDelivery} instance.
	 * 
	 * @param runnable
	 *            true if the packet delivery process can run, false otherwise
	 *            (setting the runnable flag at false is the suggested method
	 *            for stopping the packet delivery process).
	 */
	public void setRunnable(boolean runnable)
	{
		this.runnable = runnable;
	}

	/**
	 * Get the delivery time currently set for this packet delivery process, may
	 * be changed while running.
	 * 
	 * @return the deliveryTime in milliseconds.
	 */
	public int getDeliveryTime()
	{
		return deliveryTime;
	}

	/**
	 * Set the delivery time for this packet delivery process, may be changed
	 * while running.
	 * 
	 * @param deliveryTime
	 *            the deliveryTime to set in milliseconds
	 */
	public void setDeliveryTime(int deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	@Override
	public void run()
	{
		// main delivery loop, packets are consumed only if at least one
		// listener is
		// registered, possible leaks here, check if it is better to:
		// 1) use a circular buffer as queue
		// 2) always poll data from the queue
		while (this.runnable)
		{
			// check that there are listeners registered for the packet delivery
			if ((this.listeners != null) && (!this.listeners.isEmpty()))
			{
				if (this.theQueue.size() > 0)
				{
					// poll the current queue item
					ESP3Packet pkt = this.theQueue.poll().getPkt();

					// deliver the current packet to the set of registered
					// listeners
					// this migh be improved using a thread pool of executors if
					// timing becomes critical
					for (PacketListener listener : this.listeners)
					{
						// deliver the packet
						listener.handlePacket(pkt);
					}
				}
			}

			// sleep for the current delivery time
			try
			{
				Thread.sleep(this.deliveryTime);
			}
			catch (InterruptedException e)
			{
				// TODO add logging system here
				e.printStackTrace();

				// stop the thread
				this.runnable = false;
			}
		}

	}
}
