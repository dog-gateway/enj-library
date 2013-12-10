/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import java.util.Vector;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class DataRxQueue {
	
	Vector<ElementQueue> v;
	
	/**
	 * Return true is the queue is empty
	 */
	public boolean isEmpty() {
		return v.isEmpty();
	}

	/**
	 * Inserts the specified element into this queue if it is possible to do so immediately without violating capacity restrictions, returning true upon success and throwing an IllegalStateException if no space is currently available.
	 */
	public boolean add(ElementQueue e) {		
		return v.add(e);
	}
	
	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
	 */
	public ElementQueue peek() {
		return v.firstElement();
	}

	/**
	 * Retrieves and removes the head of this queue, or returns null if this queue is empty.
	 */
	public ElementQueue poll() {
		return v.remove(0);
	}
}
