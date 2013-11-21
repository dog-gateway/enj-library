/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import java.io.InputStream;

/**
 * @author andreabiasi
 *
 */
public class ThreadRead extends Thread{
	InputStream in;
	byte buffer[];
	//COSTRUTTORE
	public ThreadRead(InputStream in){
		this.in=in;
	}
	public void run(){
		try{
			while(in.read(buffer)>-1);
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getSerialInput(){
		return buffer;
	}
}