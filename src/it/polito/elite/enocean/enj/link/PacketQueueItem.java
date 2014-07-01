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

/**
 * A class modeling a queue item containing an EnOcean {@link ESP3Packet}, and a
 * (re)transmission counter. It is mainly used in transmission queues to account
 * for possible re-transmissions. Defines a default retransmission counter,
 * which can be redefined programmatically, or overridden by using the proper
 * constructor.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @authr <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi </a>
 * 
 */
public class PacketQueueItem
{
	// the default (re)transmission counter
	public static int MAX_RETRANSMISSION = 3;

	// The ESP (EnOcean Serial Protocol) packet
	private ESP3Packet pkt;

	// Count the number of attempts to send packet
	private int counter;

	/**
	 * Create a new Queue item containing an ESP packet, and the default
	 * re-transmission counter {@link PacketQueueItem.MAX_RETRANSMISSION}.
	 * 
	 * @param pkt
	 *            The ESP3 packet represented by the just create queue item.
	 */
	public PacketQueueItem(ESP3Packet pkt)
	{
		this.pkt = pkt;
		this.counter = PacketQueueItem.MAX_RETRANSMISSION;
	}

	/**
	 * Get the ESP packet contained by this {@link PacketQueueItem}.
	 * 
	 * @return The ESP packet.
	 */
	public ESP3Packet getPkt()
	{
		return pkt;
	}

	/**
	 * Set the ESP packet contained by this {@link PacketQueueItem}.
	 * 
	 * @param pkt
	 *            The ESP3 packet to set.
	 */
	public void setPkt(ESP3Packet pkt)
	{
		this.pkt = pkt;
	}

	/**
	 * Get the current re-transmission counter associated to this queue item
	 * 
	 * @return The current value of the re-trasmission counter, typically <=
	 *         initial_value.
	 */
	public int getRetransmissionCounter()
	{
		return counter;
	}

	/**
	 * Set the re-transmission counter associated to this queue item.
	 * 
	 * @param counter
	 *            The initial value of the counter.
	 */
	public void setRetransmissionCounter(int counter)
	{
		this.counter = counter;
	}
	
	/**
	 * Decreases by one the retransmission counter.
	 */
	public void decreaseRetransmissionCounter()
	{
		this.counter--;
	}

	/**
	 * Decrease the re-transmission counter.
	 * 
	 * @return the updated counter
	 */
	public int decreaseReTransmissionCount()
	{
		return --this.counter;
	}
}