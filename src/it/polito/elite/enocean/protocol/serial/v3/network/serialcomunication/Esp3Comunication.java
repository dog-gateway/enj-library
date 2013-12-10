/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import java.util.concurrent.Semaphore;
import java.util.PriorityQueue;

import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.ThreadRead;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.ThreadWriteOLD;

/**
 *	CLASSE "CHIAMANTE" DEI DUE THREAD
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Esp3Comunication{

	//prova per la segnalazione
	
	ThreadRead threadLettura;
	ThreadWriteOLD threadScrittura;

}
