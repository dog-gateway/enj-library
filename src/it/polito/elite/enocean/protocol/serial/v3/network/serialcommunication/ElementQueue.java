/**
 * Class which define the element in the queue.
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class ElementQueue {
	/**
	 * @param pkt
	 * @param counter
	 */
	public ElementQueue(Packet pkt, int counter) {
		super();
		this.pkt = pkt;
		this.counter = counter;
	}

	//The ESP packet
	Packet pkt;
	
	//Count the number of attempts to send packet
	int counter;
	
	/**
	 * @param pkt : the ESP3 packet
	 */
	public ElementQueue(Packet pkt){
		this.pkt = pkt;
	}
	
	public Packet getPkt() {
		return pkt;
	}

	public void setPkt(Packet pkt) {
		this.pkt = pkt;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public int decreaseCoun(){
		this.counter--;
		return counter;
	}
}