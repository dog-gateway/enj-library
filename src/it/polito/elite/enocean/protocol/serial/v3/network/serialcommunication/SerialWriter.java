/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */




public class SerialWriter {
	/**
	 * @param highPriorityTxQueue
	 * @param lowPriorityTxQueue
	 * @param expectedResponse
	 * @param out
	 */
	public SerialWriter(ConcurrentLinkedQueue<Packet> highPriorityTxQueue, ConcurrentLinkedQueue<ElementQueue> lowPriorityTxQueue, Semaphore expectedResponse, OutputStream out) {
		super();
		this.highPriorityTxQueue = highPriorityTxQueue;
		this.lowPriorityTxQueue = lowPriorityTxQueue;
		this.expectedResponse = expectedResponse;
		this.out = out;
	}

	ConcurrentLinkedQueue<Packet> highPriorityTxQueue;
	ConcurrentLinkedQueue<ElementQueue> lowPriorityTxQueue;
	Semaphore expectedResponse;
	OutputStream out;
	
	public void scrivi(){
		long startTime = 0;

		if (!highPriorityTxQueue.isEmpty()){
			// Prelevo il pacchetto dalla coda ad alta priorita e lo scrivo sulla seriale
			try {
				out.write(this.highPriorityTxQueue.poll().getPacketAsBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Se la coda ad alta priorita e vuota posso mandare dati a bassa priorita
		else{
			// Se non aspetto risposta
			if(this.expectedResponse.availablePermits()>0){ // Equivale a risp attesa = FALSE

				//Se il pacchetto e gia stato inviato correttamente al primo tentativo posso eliminarlo dalla coda
				if((!this.lowPriorityTxQueue.isEmpty())&&(this.lowPriorityTxQueue.peek().counter<3)) this.lowPriorityTxQueue.poll(); // NB ho messo la verifica per verificare che il 
				
				// Se c'e qualcosa nella coda a bassa priorite
				if(!this.lowPriorityTxQueue.isEmpty()){

					
					// Estraggo il pacchetto senza eliminarlo e lo invio
					try {
						// Setto risp attesa = true
						this.expectedResponse.acquire(); //NB quando faccio l'acquire passo da 1 a 0
						//Scrivo sulla seriale
						out.write(this.lowPriorityTxQueue.peek().pkt.getPacketAsBytes());
						System.out.println("Ho scritto sulla seriale");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Setto risp attesa = true


					// Decremento il contatore numero di invii tentati
					this.lowPriorityTxQueue.peek().counter--;

					//Start timer
					startTime = System.currentTimeMillis();

				}//Fine if(!this.lowPriorityQueue.isEmpty())

			} // Fine if(this.expectedResponse.availablePermits()>0)

			// Se sto ancora attendendo la risposta al comando inviato				
			else{
				// Se sono passati piu di 500 ms e non ho ricevuto risposta
				if((System.currentTimeMillis() - startTime)>1000){ //ATTENZIONE metto 1 sec solo per testare

					// Se ho esaurito i tentativi di invio segnalo il fallimento della trasmissione
					if(this.lowPriorityTxQueue.peek().counter==0){ //Segnala un java.lang.NullPointerException perch?
						this.lowPriorityTxQueue.poll();
						System.out.println("Trasmissione fallita dopo 3 tentativi");
					}
					// Altrimenti re invio il pacchetto
					else{

						//Invio il pacchetto
						try {
							out.write(this.lowPriorityTxQueue.peek().pkt.getPacketAsBytes());
							System.out.println("Ho scritto nuovamente sulla seriale");
						} catch (IOException e) {
							e.printStackTrace();
						}

						// Decremento il contatore numero di invii tentati
						this.lowPriorityTxQueue.peek().counter--;

						//Start timer
						startTime = System.currentTimeMillis();
					}
				} // Fine if((System.currentTimeMillis() - startTime)>500)

			} // Fine else if(this.expectedResponse.availablePermits()>0)

		}// Fine else if (!highPriorityQueue.isEmpty())

	}// Fine scrivi()
}
