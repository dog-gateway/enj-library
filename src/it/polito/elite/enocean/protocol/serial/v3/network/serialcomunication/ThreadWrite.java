/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author andreabiasi
 *
 */
public class ThreadWrite extends Thread{
	OutputStream out;
	byte[] buffer;

	// Costructor

	public ThreadWrite(byte buffer[]){
		this.buffer = buffer;
	}
	public void run(){
		try 
		{
			out.write(buffer);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public OutputStream writeOutput(){
		return  out;
	}
}