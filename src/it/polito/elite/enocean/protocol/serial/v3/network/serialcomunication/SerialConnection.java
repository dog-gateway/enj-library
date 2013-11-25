/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import it.polito.elite.enocean.protocol.serial.v3.network.serialcomunication.*;

/**
 * @author andreabiasi
 *
 */
public class SerialConnection {

	public SerialPort connect ( String portName ) throws Exception //Preso dall'esempio su RXTX
	{      
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
		}
		else
		{
			CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
			if ( commPort instanceof SerialPort )
			{
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			}
			else
			{
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
		return serialPort;
	}
}