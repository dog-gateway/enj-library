package it.polito.elite.enocean.protocol.serial.v3.network.pkgmain;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.SerialPort;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.SerialPortFactroy;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.ThreadRead;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.ThreadWriteOLD;

/**
 * 
 */

/**
 * @author andreabiasi
 *
 */
public class ProvaProtocollo {

	/**
	 * @param args
	 */
		
	static SerialPortFactroy connessione1 = new SerialPortFactroy();
	
	
	public static void main(String[] args) {
		try
		{
			byte[] buffer = new byte[65536];
			
			SerialPort serialPort = connessione1.connect("ttyAMA0");
			
			InputStream in = serialPort.getInputStream();
			OutputStream out = serialPort.getOutputStream();
			
			ThreadRead lettura = new ThreadRead(in);
			ThreadWriteOLD scrittura = new ThreadWriteOLD(buffer);
			
			//lettura
			buffer = lettura.getSerialInput();
			
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}