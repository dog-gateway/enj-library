/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class SerialListener implements SerialPortEventListener{
	// Porta seriale
	SerialPort serialPort;
	
	// Pacchetto ricevuto
	Packet pkt;
	
	// flusso di dati in ingresso alla porta seriale
	InputStream in;	

	// Buffer di byte per immagazzinare i byte in arrivo dalla seriale
	byte[] buffer;

	// Coda di messaggi ricevuti a bassa priorità
	DataQueue lowPriorityQueue;

	// Coda di messaggi ricevuti ad alta priorità ( per es risposte )
	DataQueue highPriorityQueue;
	
	// Flag risposta attesa
	boolean flagResponse;

	public SerialListener(SerialPort serialPort, DataQueue lowPriorityQueue, DataQueue highPriorityQueue, boolean flagResponse) {
		super();
		this.serialPort = serialPort;
		this.lowPriorityQueue = lowPriorityQueue;
		this.highPriorityQueue = highPriorityQueue;
		this.flagResponse = flagResponse;
	}

	public void run() throws TooManyListenersException{
		/*
		 * 
		 * Ho messo questo metodo run per avere anche l'inizializzazione della porta in questa classe.
		 * entrambe le dichiarazioni: serialPort.addEventListener(this) e serialPort.notifyOnDataAvailable(true)
		 * possono essere fatte in un altra classe, specificando come ascoltatore questa classe: serialPort.addEventListener(SerialListener)
		 * 
		 */

		//Registra come ascoltatore un oggetto di tipo SerialPortEventListener, in questo caso questa classe
		serialPort.addEventListener(this);
		
		//Notifica la presenza di dati in ingresso all'oggetto di tipo SerialPortEventListener inpostato con addEventListener
		serialPort.notifyOnDataAvailable(true);
	}

	public void serialEvent(SerialPortEvent event) {
		
		// Se il tipo di evento è: dati disponibili sulla seriale
		if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE){
			
			// Flag di stato per segnalre fine della lettura dalla seriale
			int readstatus = 0;
			
			int i = 0;
			
			//Finchè ho qualocosa leggo
			while( readstatus > -1){
				//Faccio partire il cronometro per i 100 ms
				long startTime = System.currentTimeMillis();

				try {
					//Leggo solo un byte per poter controllare che tra un byte e l'altro non passino piu di 100 ms, restituisce -1 se non c'è più nulla da leggere
					readstatus = in.read(this.buffer, i, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}

				//Tempo trascorso per l'operazione di lettura
				long opTime = System.currentTimeMillis() - startTime;
				i++;
				
				//Se il tempo per la lettura è maggiore al timeout di 100 ms
				if(opTime > 100) {
					
					// Setto readstatus per uscire dal while
					readstatus = -1;
					
					System.out.println("Attenzione superati i 100 ms di timeout tra due dati ricevuti sulla seriale"); //Per ora mando solo un messaggio a video ma bisognerà agire di conseguenza 
				}

				// Inpacchetta il vettore di byte in un oggetto pkt di tipo Packet
				pkt.parsePacket(this.buffer);

				//Se mi aspetto una risposta e ricevo una risposta
				if((pkt.isResponse())&&(this.flagResponse==true)){
					
					// Libero il flag risposta attesa
					flagResponse = false;
					
					//Aggiungo il paccketto alla coda dati ad alta priorità
					highPriorityQueue.add(pkt);
				}
				else{
					
					//Se il pacchetto richiede risposta cioè se è un evento ed è di tipo 1,2 o 3
					if((pkt.getPacketType()==0x04) && ((pkt.getData()[0]==0x01)||(pkt.getData()[0]==0x02)||(pkt.getData()[0]==0x03)))
					{
						// Aggiungo elemento in high priority queue
						highPriorityQueue.add(pkt);
				
						// Setto risposta attesa perchè devo ricevere una risposta
						flagResponse = true;
					}
					else{
						//Store packet in lowPriority queue
						this.lowPriorityQueue.add(pkt);
						
						// Il messaggio ricevuto non richiede risposta
						flagResponse = false;
					}
				}
			}	
		}
	}
}