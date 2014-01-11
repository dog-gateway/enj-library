/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class SerialPortFactory {
	
	public SerialPortFactory() {
		super();
	}
	SerialPort serialPort;
	public SerialPort getPort( String portName, int timeout ) throws Exception {   
		
		/* 
		 * Attenzione : potrebbe essere necessario inserire questa riga per settare il nome della porta 
		 * 
		 * String SerialPortID = "/dev/ttyAMA0";
		 * System.setProperty("gnu.io.rxtx.SerialPorts", SerialPortID); 
		 * Fonte: https://www.java.net//forum/topic/jdk/java-se-snapshots-project-feedback/rxtx-or-what-about-serial-interfaces
		 *
		 */
		
		System.setProperty("gnu.io.rxtx.SerialPorts", portName); 
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
		}
		else
		{
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);
			if ( commPort instanceof SerialPort )
			{
				serialPort = (SerialPort) commPort;
				//Setto i parametri della UART Baudarate: 57600 bps, 8 bit di dato, 1 bit di STOP, No paritˆ 
				serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			}
			else
			{
				System.out.println("Error");
			}
		}
		return serialPort;
	}
}