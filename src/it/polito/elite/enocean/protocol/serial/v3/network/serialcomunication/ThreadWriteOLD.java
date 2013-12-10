package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import gnu.io.SerialPort;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.response.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 
 * @author andreabiasi
 *
 */
public class ThreadWriteOLD extends Thread{
	//Struttura dati comune ai due thread per immagazzinare le risposte
	Set<Response> responseSet; // NON E DETTO CHE VADA BENE COME STRUTTURA DATI --> VEDERE QUALE STRUTTURA PIU IDONEA

	//Semaforo per le risposte
	Semaphore mutexResponseSemaphore;

	//Semaforo per la coda dei dati
	Semaphore mutexDataQueueSemaphore;

	//Messaggi ricevuti
	Response respReceived;

	// Dati in uscita
	OutputStream out;

	//Array che contiene i byte da inviare alla seriale
	byte[] buffer;

	// Generico pacchetto del ESP3
	Packet pkt;

	// Porta seriale
	SerialPort serialPort;

	// Struttura comune ai due thread per la ricezione dei comandi
	MessageQueue queue; // Non  detto che ci voglia una coda --> vedere quale struttura pu˜ essere la migliore
	
	//Coda di comandi da inviare
	MessageQueue internalQueue;

	// Costructor
	public ThreadWriteOLD(SerialPort serialPort, MessageQueue queue, MessageQueue internalQueue, Semaphore mutexDataQueueSemaphore, Semaphore mutexResponseSemaphore, Set<Response> responseSet) throws IOException{
		this.serialPort = serialPort;
		this.queue = queue;
		this.internalQueue = internalQueue;
		this.mutexDataQueueSemaphore = mutexDataQueueSemaphore;
		this.mutexResponseSemaphore = mutexResponseSemaphore;
	}

	public void run(){
		while(true){
			try {
				out = serialPort.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*
			 * Ricezione eventi asincroni
			 * 
			 * Se ho qualcosa in coda (ma poi sarˆ giusta la coda?) lo prelevo e lo processo
			 */

			if(!queue.isEmpty()){ // Va bene controllare cos“ la presenza di dati o predisporre una struttura tipo "flag"?
				synchronized (queue) {
					//Finch il semaforo mi dice che la risorsa non  disponibile aspetto
					while (!mutexDataQueueSemaphore.tryAcquire()) {
						try {
							//Il semaforo mi dice che non  disponnibile quindi il thread corrente aspetta finche un altro thread non chiama notify
							queue.wait();
						} catch (InterruptedException e) {
							return;
						}
					}
					//Prendo il controllo della coda e setto il semaforo
					try {
						mutexDataQueueSemaphore.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

					//prelevo il pacchetto dalla coda
					pkt = (Packet) queue.poll();

					//Rilascio il semaforo e do la possibilitˆ di accedere alla coda ad un altro thread
					mutexDataQueueSemaphore.release();

					//Sblocco l'altro thread
					queue.notify();
				}
			}
			else{

				/*
				 * Invio comandi sincroni e aspetto risposta
				 */

				//scrivo il pacchetto sull'output stream usando il metodo getBytes() che "spacchetta" il pacchetto e lo mette in un vettore di bytes
				try {
					out.write(pkt.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				boolean receivedResponse = false;
				// Faccio partire il cronometro
				long startTime = System.currentTimeMillis();	
				
				//Finche non ricevo risposta controllo che nella struttura
				while(!receivedResponse){
					//Se ho la risposta la prelevo e setto il flag per notificare la ricezione della risposta
					if(!responseSet.isEmpty()){
						//ricevi la risposta
						responseSet.remove(respReceived); //Non sono sicuro sia la struttura giusta con i metodi giusti
						receivedResponse = true;
					}
					// Durata dell'operazione
					long opTime = System.currentTimeMillis() - startTime;
					
					if ((opTime>500)&&!receivedResponse){
						/*
						 * Se la risposta non arriva entro 500 ms agisco di conseguenza
						 */
					}
				}
			}
		}
	} 
}