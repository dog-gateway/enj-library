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

public  class SerialWriter implements Runnable 
{
	OutputStream out;
	byte[] buffer = new byte[65636];

	public SerialWriter ( OutputStream out , byte[] buffer)
	{
		this.out = out;
		this.buffer = buffer;
	}

	public void run ()
	{
		try
		{                
			//Write all buffer vector in OutputStream out file
			for(int i=0 ; i<buffer.length ; i++)
			{
				this.out.write(buffer[i]);
			}                
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}            
	}
}