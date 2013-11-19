package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class 
 * 
 * @author andreabiasi
 *
 */
public class SerialReader implements Runnable{
	InputStream in; 
	byte[] buffer = new byte[65536];

	public SerialReader ( InputStream in)
	{
		this.in = in;
	}

	public void run ()
	{
		byte[] buffer = new byte[1024];
		try
		{
			//Read from in until isn't empty and and stores them into the buffer array buffer.
			while (( this.in.read(buffer)) > -1 )
			{
				//System.out.print(new String(buffer,0,len));
				System.out.println("Sono nel thread di lettura");
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}  
	}

	public byte[] getInput(){
		return buffer;
	}
}