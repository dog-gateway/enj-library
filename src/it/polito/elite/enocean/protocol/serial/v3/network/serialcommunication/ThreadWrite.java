/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication;

import gnu.io.SerialPort;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

public class ThreadWrite extends Thread{

	/**
	 * @param highPriorityQueue
	 * @param lowPriorityQueue
	 * @param serialPort
	 * @param expectedResponse
	 */
	public ThreadWrite(ConcurrentLinkedQueue<Packet> highPriorityQueue,
			ConcurrentLinkedQueue<ElementQueue> lowPriorityQueue,
			SerialPort serialPort, Semaphore expectedResponse) {
		super();
		this.highPriorityTxQueue = highPriorityQueue;
		this.lowPriorityTxQueue = lowPriorityQueue;
		this.serialPort = serialPort;
		this.expectedResponse = expectedResponse;
	}

	// Coda di messaggi ad alta priorita (Risposte da inviare)
	ConcurrentLinkedQueue<Packet> highPriorityTxQueue;

	// Coda di messaggi a bassa priorita
	ConcurrentLinkedQueue<ElementQueue> lowPriorityTxQueue;

	// Elemento estratto dalla coda a bassa priotita, contiene pacchetto e un contatore
	ElementQueue element;

	// Pacchetto ESP3
	Packet pkt;

	// Porta seriale
	SerialPort serialPort;

	// Stream dati in uscita
	OutputStream out;

	// Flag per risposta attesa
	Semaphore expectedResponse;

	
	public void run(){
		
		
		try {
			// Ottieni lo stream di dati in uscita
			out = serialPort.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Flag per segnalare continuazione del thread
		boolean canRun = true;

		//SerialWriter serialWriter = new SerialWriter(highPriorityTxQueue, lowPriorityTxQueue, expectedResponse, out);


		while(canRun){
			//System.out.println("Sono in thread write");

			// Se la coda ad alta priorita non e vuota, invia prima i dati ad alta priorita
			//System.out.println("Prima del wait");

			if(!lowPriorityTxQueue.isEmpty()){
			    ThreadWrite.yield(); //cerca di eseguire alla massima velocità possibile (100% cpu) ma lasciando spazio agli altri thread
				this.scrivi();
			}
			else{
			    try {
					Thread.sleep(10); //dorme per 10ms prima di svegliarsi e fare un altro giro
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //dorme per 10ms prima di svegliarsi e fare un altro giro
			}
			
		}// Fine while(canRun)
	}
	

	private void scrivi(){
		long startTime = 0;

		System.out.println("Sono in scrivi");
		
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
				if((System.currentTimeMillis() - startTime)>500){

					// Se ho esaurito i tentativi di invio segnalo il fallimento della trasmissione
					if(this.lowPriorityTxQueue.peek().counter==0){ //Segnala un java.lang.NullPointerException perchè?
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

		//Sleep per 100 ms
		try {
			sleep(200); // C'è un problema nei tempi, se non metto uno sleep così elevato invia e riceve 3 volte
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// Fine scrivi()
}