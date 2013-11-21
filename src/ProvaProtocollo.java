import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.SerialPort;

import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.SerialConnection;


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
		
	static SerialConnection connessione1 = new SerialConnection();
	
	InputStream in = serialPort.getInputStream();
	OutputStream out = serialPort.getOutputStream();
	
	public static void main(String[] args) {
		try{
			
			connessione1.connect("ttyAMA0");
			ThreadRead lettura = new ThreadRead(in);
			ThreadRead scrittura = new ThreadWrite(buffer);
				
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}