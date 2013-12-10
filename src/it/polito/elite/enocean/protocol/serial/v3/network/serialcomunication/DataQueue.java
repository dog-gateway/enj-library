/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.util.Vector;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class DataQueue {
	
	Vector<Packet> v;
	
	/**
	 * Return true is the queue is empty
	 */
	public boolean isEmpty() {
		return v.isEmpty();
	}

	/**
	 * Inserts the specified element into this queue if it is possible to do so immediately without violating capacity restrictions, returning true upon success and throwing an IllegalStateException if no space is currently available.
	 */
	public boolean add(Packet e) {		
		return v.add(e);
	}
	
	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
	 */
	public Packet peek() {
		return v.firstElement();
	}

	/**
	 * Retrieves and removes the head of this queue, or returns null if this queue is empty.
	 */
	public Packet poll() {
		return v.remove(0);
	}
}
