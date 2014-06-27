package it.polito.elite.enocean.protocol.serial.v3.network.link;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.response.Response;

/**
 * This class look if there are high priority message received (ones which needs response), process them and put eventually response into the high priority message in transmission thread
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class HighPriorityThread extends Thread {

	/**
	 * @param highPriorityTxQueue
	 * @param highPriorityRxQueue
	 * @param expectedResponse
	 */
	public HighPriorityThread(
			ConcurrentLinkedQueue<ESP3Packet> highPriorityTxQueue,
			ConcurrentLinkedQueue<ESP3Packet> highPriorityRxQueue,
			Semaphore expectedResponse) {
		super();
		this.highPriorityTxQueue = highPriorityTxQueue;
		this.highPriorityRxQueue = highPriorityRxQueue;
		this.expectedResponse = expectedResponse;
	}

	// Coda di messaggi ad alta priorit� in trasmissione (Risposte da trasmettere)
	ConcurrentLinkedQueue<ESP3Packet> highPriorityTxQueue;

	// Coda di messaggi ad alta priorit� in ricezione (Messaggi che necessitano di risposta)
	ConcurrentLinkedQueue<ESP3Packet> highPriorityRxQueue;

	// Pacchetto ESP3
	ESP3Packet pkt;

	// Flag per risposta attesa
	Semaphore expectedResponse;

	public void run(){
		boolean canRun = true;
		while(canRun){
			// Prende i pacchetti dalla coda RX e li mette nella coda in TX
			if (!this.highPriorityRxQueue.isEmpty()){
				
				//Preleva il pacchetto dalla coda ad alta priorit� in ingresso
				pkt = this.highPriorityRxQueue.poll();
				
				// Setta risposta attesa
				try {
					this.expectedResponse.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/*
				 * 
				 * 
				 *	BISOGNA PROCESSARE LA RISPOSTA DA METTERE	
				 * 
				 * 
				 */
				
				// Metto la risposta della coda ad alta priorit� in scrittura
				this.highPriorityTxQueue.add(new Response((byte)0x00));
				
			} // Fine if
			
		} // Fine while(canRun)
	
	}// Fine run
	
}// Fine HighPriorityThread