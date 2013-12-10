/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import gnu.io.*;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.response.Response;

import java.io.InputStream;

import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.Timer;
/**
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class ThreadRead extends Thread implements SerialPortEventListener{

	/**
	 * @param serialPort
	 * @param queue
	 * @param mutexDataQueueSemaphore
	 * @param mutexResponseSemaphore
	 * @param responseSet
	 */
	public ThreadRead(SerialPort serialPort, MessageQueue queue, Semaphore mutexDataQueueSemaphore, Semaphore mutexResponseSemaphore, Set<Response> responseSet){
		this.serialPort = serialPort;
		this.queue = queue;
		this.mutexDataQueueSemaphore = mutexDataQueueSemaphore;
		this.mutexResponseSemaphore = mutexResponseSemaphore;
		this.responseSet = responseSet;
		this.in = serialPort.getInputStream(); //Si può mettere fuori dal while?
		this.flagResponse = flagResponse;
	}

	//Struttura dati comune ai due thread per immagazzinare le risposte
	Set<Response> responseSet;
	
	//Semaforo per le risposte
	Semaphore mutexResponseSemaphore;

	//Semaforo per la coda dei dati
	Semaphore mutexDataQueueSemaphore;

	// Coda di dati condivisa
	MessageQueue queue;

	// flusso di dati in ingresso alla porta seriale
	InputStream in;	

	//porta seriale
	SerialPort serialPort;

	//buffer di byte in ingresso
	byte[] buffer = new byte[65536];

	//Pacchetto
	Packet pkt;
	
	boolean flagResponse;
	
	Timer timeoutTimer;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		try{
			
			// Ciclo infinito per tenere "aperto" il thread
			canRun=true
			while(canRun){
				//Leggo dalla seriale
				
				//Registra come ascoltatore un oggetto di tipo SerialPortEventListener, in questo caso questa classe
				serialPort.addEventListener(this);
				
				//Notifica la presenza di dati in ingresso all'oggetto di tipo SerialPortEventListener inpostato con addEventListener
				serialPort.notifyOnDataAvailable(true);
			}
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 *
	 * Metodo in cui metto cosa devo fare quando mi si presenta un evento
	 */
	public void serialEvent(SerialPortEvent event){
		// L'evento è: dato disponibile sulla seriale
		if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE){
			try{
				//ATTENZIONE BISOGNA CONTROLLARE CHE TRA UN BYTE E L'ALTRO NON PASSINO PIU DI 100 ms altrimenti bisogna bloccare e segnalare
				//PROBABILMENTE BISOGNA LEGGERE UN BYTE ALLA VOLTA INVECE DELL'INTERO VETTORE				
				int readstatus = 0;
				int i = 0;
				//Finchè ho qualocosa leggo
				while( readstatus > -1){
					//Faccio partire il cronometro per i 100 ms
					long startTime = System.currentTimeMillis();
					
					//Leggo solo un byte per poter controllare che tra un byte e l'altro non passino piu di 100 ms, restituisce -1 se non c'è più nulla da leggere
					readstatus = in.read(this.buffer, i, 1);
					
					//Tempo trascorso per l'operazione di lettura
					long opTime = System.currentTimeMillis() - startTime;
					i++;
					//Se il tempo per la lettura è maggiore al timeout di 100 ms
					if(opTime > 100) {
						// Setto readstatus per uscire dal while
						readstatus = -1;
						System.out.println("Attenzione superati i 100 ms di timeout tra due dati ricevuti sulla seriale"); //Per ora mando solo un messaggio a video ma bisognerà agire di conseguenza 
					}
				}
				
				// Inpacchetta il vettore di byte in un oggetto pkt di tipo Packet
				pkt.parsePacket(this.buffer);

				//Se il pacchetto è una risposta non la metto nella coda ma la metto in una struttura apposita, non è detto sia un S
				if(pkt.isResponse()) {
					synchronized (responseSet) {
						//Finchè il semaforo mi dice che la risorsa non è disponibile aspetto
						while (!mutexResponseSemaphore.tryAcquire()) {
							try {
								//Il semaforo mi dice che non è disponnibile quindi il thread corrente aspetta finche un altro thread non chiama notify
								responseSet.wait();
							} catch (InterruptedException e) {
								return;
							}
						}
						//Prendo il controllo della coda e setto il semaforo
						mutexResponseSemaphore.acquire(); // Non dovrebbe esserci bisogno dell' acquire() perchè il tryAcquire() se restituisce TRUE (quindi esce dal while) chiama anche l'acquire
						
						//Metto nella struttura condivisa la risposta
						responseSet.add((Response) pkt);

						//Rilascio il semaforo e do la possibilità di accedere alla struttura dati ad un altro thread
						mutexResponseSemaphore.release();

						//Sblocco l'altro thread
						responseSet.notify();
					}
				}
				else{
					//Se il pacchetto è qualcos'altro
					synchronized (queue) {
						//Finchè il semaforo mi dice che la risorsa non è disponibile aspetto
						while (!mutexDataQueueSemaphore.tryAcquire()) {
							try {
								//Il semaforo mi dice che non è disponnibile quindi il thread corrente aspetta finche un altro thread non chiama notify
								queue.wait();
							} catch (InterruptedException e) {
								return;
							}
						}
						//Prendo il controllo della coda e setto il semaforo
						mutexDataQueueSemaphore.acquire();	//non ce n'è bisogno forse perchè già quando esce dal while e il tryAcquire() restituisce TRUE, fa un acuiore()				

						//Metto il pacchetto nella coda
						queue.add(pkt);

						//Rilascio il semaforo e do la possibilità di accedere alla coda ad un altro thread
						mutexDataQueueSemaphore.release();
						
						//Sblocco l'altro thread
						queue.notify();
					}
				}
			}
			catch ( Exception e ){ e.printStackTrace(); }
		}
	}
}