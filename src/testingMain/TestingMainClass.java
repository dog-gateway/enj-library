/**
 * 
 */
package testingMain;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand.CoRdVersion;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.*;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

//Corretto il 14/01

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class TestingMainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
        {
			System.out.println("Sono nel main ....");		
			SerialPort serialPort = (new SerialPortFactory()).getPort("/home/utente/com1", 1000);
			
			/*
			InputStream in = serialPort.getInputStream();
			OutputStream out = serialPort.getOutputStream();

			(new Thread(new SerialReader(in))).start();        
			(new Thread(new SerialWriter(out))).start();
			*/
			
			
			//Code ad alta priorita
			ConcurrentLinkedQueue<Packet> highPriorityTxQueue = new ConcurrentLinkedQueue<Packet>();
			ConcurrentLinkedQueue<Packet> highPriorityRxQueue = new ConcurrentLinkedQueue<Packet>();
			
			//Code a bassa priorita
			ConcurrentLinkedQueue<ElementQueue> lowPriorityTxQueue = new ConcurrentLinkedQueue<ElementQueue>();
			ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue = new ConcurrentLinkedQueue<ElementQueue>();

			//Invio il comando CO_RD_VERSION
			Semaphore expectedResponse = new Semaphore(1);
			CoRdVersion cmd = new CoRdVersion();
			lowPriorityTxQueue.add( new ElementQueue(cmd,3) );
			
//			(new HighPriorityThread( highPriorityTxQueue, highPriorityRxQueue, expectedResponse)).run();
			(new ThreadWrite(highPriorityTxQueue, lowPriorityTxQueue, serialPort, expectedResponse)).run();
			SerialListener serialListener = new SerialListener(serialPort, highPriorityRxQueue, lowPriorityRxQueue, expectedResponse);

			serialPort.addEventListener(serialListener);
			serialPort.notifyOnDataAvailable(true);
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}


/*
class SerialWriter implements Runnable 
{
	OutputStream out;
	byte buffer[] = new byte[8];

	public SerialWriter ( OutputStream out )
	{
		this.out = out;
	}

	public void run ()
	{
		try
		{  
			// Invio un comando CO_RD_VERSION
			
			System.out.println("Inizio ad inviare i byte");
			buffer[0] = (byte) 0x55; // Header
			System.out.println("Scondo byte");
			buffer[1] = (byte) 0x00; // Data Lenght
			System.out.println("Terzo byte");			
			buffer[2] = (byte) 0x01; // Data Lenght
			buffer[3] = (byte) 0x00; // Optional lenght
			buffer[4] = (byte) 0x05; // Packet Type

			buffer[5] = (byte) 0x70; // CRC8H 

			buffer[6] = (byte) 0x03; // COMMAND CODE
			buffer[7] = (byte) 0x09; // CRC8D
			System.out.println("Sono prima del write");
			this.out.write(buffer); 
			System.out.println("Ho inviato il pacchetto radio sulla seriale");
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}            
	}
}

class SerialReader implements Runnable 
{
	InputStream in; 

	public SerialReader ( InputStream in )  //COSTRUTTORE passa all'oggetto la variabile di tipo InputStream
	{
		this.in = in;
	}

	public void run ()
	{
		byte[] buffer = new byte[1024];
		int len = -1;

		try
		{
			System.out.println("Dati ricevuti :  ");
			while ( ( len = in.read(buffer)) > -1 )
			
			{
				System.out.println("Sono nel while");
				System.out.print(new String(buffer,0,len));
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}            
	}
}*/