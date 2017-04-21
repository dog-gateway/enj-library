/*
 * EnJ - EnOcean Java API
 * 
 * Copyright 2014 Andrea Biasi, Dario Bonino 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package it.polito.elite.enocean.enj.link.serial;

import com.fazecast.jSerialComm.SerialPort;

/**
 * A utility factory for getting a reference to the serial port used for
 * communicating with the physical EnOcean transceiver, initialized with the
 * correct parameters.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @author <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi </a>
 * 
 */
public class SerialPortFactory
{

	/**
	 * Provides a correctly configured serial port connection, given a port
	 * identifier and a transmission timeout.
	 * 
	 * @param portName
	 *            The name of the port to connect to (e.g., COM1, /dev/tty0,
	 *            ...)
	 * @param timeout
	 *            The connection timeout
	 * @return a {@link SerialPort} instance representing the port identified by
	 *         the given data, if existing, or null otherwise.
	 * @throws Exception
	 */
	public static SerialPort getPort(String portName, int timeout) throws Exception
	{
		// the serial port reference, initially null
		SerialPort serialPort = SerialPort.getCommPort(portName);
		if (serialPort.getSystemPortName().equalsIgnoreCase("Bad Port")) {
			throw new Exception("Error: Unrecognised port name " + portName);
		}
		serialPort.setComPortParameters(57600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0 ,0);
		return serialPort;
	}
}