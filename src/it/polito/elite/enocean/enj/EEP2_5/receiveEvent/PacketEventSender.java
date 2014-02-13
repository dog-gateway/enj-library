/**
 * 
 */
package it.polito.elite.enocean.enj.EEP2_5.receiveEvent;

import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.ElementQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class PacketEventSender extends Thread{

	List<PacketReceiverListener> _listeners = new ArrayList<PacketReceiverListener>();
	ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue;

	public PacketEventSender() {
		super();
	}

	public PacketEventSender( ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue ) {
		super();
		this.lowPriorityRxQueue = lowPriorityRxQueue;
	}

	public void run() {
		boolean canRun = true;
		while(canRun){
			/*
			//Se c'è qualcosa in coda
			if(!this.lowPriorityRxQueue.isEmpty()){
				//System.out.println("Sono in Event Sender");
				int size = this.lowPriorityRxQueue.size();
				// Richiamo l'evento
				Thread.yield();
				this.fireEvent();
			}
			else{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
		}
	}

	public void addEventListener(PacketReceiverListener listener)  {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(PacketReceiverListener listener)   {
		_listeners.remove(listener);
	}

	private synchronized void fireEvent() {

		ReceiveDataEvent event = new ReceiveDataEvent(this);
		Iterator<PacketReceiverListener> i = _listeners.iterator();

		while( i.hasNext() ) {
			( i.next() ).handleReceiveData(event);
		}
	}
}
