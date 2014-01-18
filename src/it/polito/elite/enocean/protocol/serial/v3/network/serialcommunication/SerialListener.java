/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class SerialListener implements SerialPortEventListener{
	public SerialListener(SerialPort serialPort,
			ConcurrentLinkedQueue<Packet> highPriorityQueue,
			ConcurrentLinkedQueue<ElementQueue> lowPriorityQueue,
			Semaphore expectedResponse) {
		super();
		this.serialPort = serialPort;
		this.highPriorityRxQueue = highPriorityQueue;
		this.lowPriorityRxQueue = lowPriorityQueue;
		this.expectedResponse = expectedResponse;
	}

	// Porta seriale
	SerialPort serialPort;

	// Pacchetto ricevuto
	Packet pkt;

	// flusso di dati in ingresso alla porta seriale
	InputStream in;	

	// Buffer di byte per immagazzinare i byte in arrivo dalla seriale
	byte[] buffer = new byte[45]; //Attenzione ho inizializzato per poter andare avanti nel debug

	// Coda di messaggi ad alta priorita
	ConcurrentLinkedQueue<Packet> highPriorityRxQueue;

	// Coda di messaggi a bassa priorita
	ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue;

	// Flag per risposta attesa
	Semaphore expectedResponse;

	/*
	 * 
	 * Va fatta dalla classe che inizializza la porta che quindi richiama il listener seriale:
	 * 
	 * Registra come ascoltatore un oggetto di tipo SerialPortEventListener, in questo caso questa classe
	 * serialPort.addEventListener(this);
	 * 
	 * Notifica la presenza di dati in ingresso all'oggetto di tipo SerialPortEventListener inpostato con addEventListener
	 * serialPort.notifyOnDataAvailable(true);
	 * 
	 */

	public void serialEvent(SerialPortEvent event) {
		
		try {
			in = serialPort.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Sono in SerialListener");
		
		// Se il tipo di evento e: dati disponibili sulla seriale
		if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE){

			// Flag di stato per segnalre fine della lettura dalla seriale
			//int readstatus = 0;
			//int i = 0;

			
			try {
				in.read(this.buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Finche ho qualocosa leggo
			//while( readstatus > -1){
				//Faccio partire il cronometro per i 100 ms
				//long startTime = System.currentTimeMillis();
				//try {
					//Leggo solo un byte per poter controllare che tra un byte e l'altro non passino piu di 100 ms, restituisce -1 se non c'e piu nulla da leggere
					//readstatus = in.read(this.buffer, i, 1);
				//} catch (IOException e) {
					//e.printStackTrace();
				//}
				/*
				try {
					readstatus = in.read(this.buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				//Tempo trascorso per l'operazione di lettura
				//long opTime = System.currentTimeMillis() - startTime;
				//i++;

				//Se il tempo per la lettura e maggiore al timeout di 100 ms
				/*if(opTime > 100) {
					// Setto readstatus per uscire dal while
					readstatus = -1; //Bisognera gestire le eccezioni con il throw

					System.out.println("Attenzione superati i 100 ms di timeout tra due dati ricevuti sulla seriale"); //Per ora mando solo un messaggio a video ma bisognera agire di conseguenza 
				}*/
			}

			System.out.println("Ho letto i dati");
			System.out.println("");
			System.out.println(new String(buffer,0,buffer.length));
			System.out.println("");
			/*
			 * Per il debug stampo a video cio che ricevo
			 */
			for(int i1=0 ; i1<buffer.length ; i1++){
				System.out.print("byte: "+i1+": ");
				System.out.println((byte)buffer[i1]); //Sara giusto per stampare un vettore di HEX ?
			}
			
			// Inpacchetta il vettore di byte in un oggetto pkt di tipo Packet
			pkt.parsePacket(this.buffer); // ATTENZIONE parsepacket non funziona perch gli elementi de buffer di dati ricevuti sono scritti in indici sbagliat 

			
			//Se il pacchetto ricevuto e una risposta
			if(pkt.isResponse()){
				System.out.println("Il pacchetto  una risposta");

				// Libero il flag risposta attesa
				this.expectedResponse.release();
				
				//Aggiungo il paccketto alla coda dati a bassa prioritˆ
				this.lowPriorityRxQueue.add(new ElementQueue(pkt, 3));
			}
			else{
				if(pkt.requireResponse()){
					this.highPriorityRxQueue.add(pkt);
				}
				else{
					//Aggiungo il paccketto alla coda dati a bassa prioritˆ
					this.lowPriorityRxQueue.add(new ElementQueue(pkt, 3));
				}
			} // Fine isResponse
		}
	//}
}