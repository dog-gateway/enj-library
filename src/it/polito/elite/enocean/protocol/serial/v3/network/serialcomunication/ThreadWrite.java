/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import gnu.io.SerialPort;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class ThreadWrite extends Thread{

	// Coda di messaggi ad alta prioritˆ (Risposte da inviare)
	DataQueue highPriorityQueue;

	// Coda di messaggi a bassa prioritˆ
	DataRxQueue lowPriorityQueue;

	// Elemento estratto dalla coda a bassa priotitˆ, contiene pacchetto e un contatore
	ElementQueue element;

	// Pacchetto ESP3
	Packet pkt;

	// Porta seriale
	SerialPort serialPort;

	// Stream dati in uscita
	OutputStream out;

	/*
	 * Strutture comuni
	 */
	// Flag per risposta attesa
	boolean expectedResponse;
	//Semaforo per le risposte
	Semaphore mutexHpTxQueue;


	public ThreadWrite(DataQueue highPriorityQueue, boolean expectedResponse, Semaphore mutexHpTxQueue) {
		super();
		this.highPriorityQueue = highPriorityQueue;
		this.expectedResponse = expectedResponse;
		this.mutexHpTxQueue = mutexHpTxQueue;
	}


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

		while(canRun){
			// Se la coda ad alta prioritˆ non  vuota, invia prima i dati ad alta prioritˆ
			if (!highPriorityQueue.isEmpty()){
				try {						
					synchronized (highPriorityQueue){
						// Provo ad acquisire il controllo della coda
						while (!this.mutexHpTxQueue.tryAcquire()) // Forse andrebbe bene anche un if 
						{
							//Il semaforo mi dice che non  disponnibile quindi il thread corrente aspetta finche un altro thread non chiama notify
							highPriorityQueue.wait();
						}
						// Prelevo il pacchetto dalla coda e lo scrivo sulla seriale
						out.write(this.highPriorityQueue.poll().getBytes());
						// Risveglio l'altro thread che utilizza highPriorityQueue
						highPriorityQueue.notify();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				if(!this.expectedResponse){
					// Se c' qualcosa nella coda a bassa prioritˆ
					if(!this.lowPriorityQueue.isEmpty()){
						
						// Estraggo il pacchetto senza eliminarlo
						element = this.lowPriorityQueue.peek();
						pkt = element.pkt;						
						
						while(this.element.counter>0){

							// Scrivo il pacchetto
							try {
								out.write(pkt.getBytes());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							// Lo "start" del cronometro
							long startTime = System.currentTimeMillis();

							this.expectedResponse = true; // Sarˆ sinchronized?

							// Tempo di esecuzione dell'operazione
							long opTime;
							
							//rimango nel ciclo finche o ricevo una risposta o raggiungo il timeout 
							while(this.expectedResponse||((opTime=System.currentTimeMillis()-startTime)>500)){}

							//Se ho ottenuto una risp in RX
							if(!this.expectedResponse){
								// Elimino l'elemento dalla coda
								this.lowPriorityQueue.poll();

								//Setto il contatore a zero cos“ esco dal while
								this.element.counter = 0;
							}
							else{
								if((opTime>500)&&(this.element.counter==0)){
									System.out.println("Trasmissione fallita dopo 3 tentativi");
									break;
								}
								else this.element.counter--;
							}
						} // FINE while(this.element.counter>0)
						// Metto in sleep il thread
						try {
							sleep(5); // E' un tempo arbitrario per ora, bisogna dimensionarlo meglio
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}