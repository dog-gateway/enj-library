/*
 * EnJ - EnOcean Java API
 * 
 * Copyright 2014-2015 Andrea Biasi, Dario Bonino 
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
package it.polito.elite.enocean.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.eep.eep26.D2.D201.D20109;
import it.polito.elite.enocean.enj.eep.eep26.D2.D201.D201UnitOfMeasure;
import it.polito.elite.enocean.enj.link.EnJLink;
import it.polito.elite.enocean.enj.model.EnOceanDevice;
import it.polito.elite.enocean.examples.util.Options;

/**
 * @author bonino
 *
 */
public class TestApp
{
	// supported test modes
	protected static final String DEMO_MODE = "demo";
	protected static final String INTERACTIVE_MODE = "interactive";
	protected static final String HELP = "help";
	protected static final String DEMO = "demo";

	/**
	 * 
	 */
	public TestApp()
	{
		// empty
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		// a utility object for managing command line arguments...
		Options opt = new Options(
				new String[] {
						"-p port",
						"The serial port to which the EnOcean dongle / adapter is connected",
						"-f persistent-device-file",
						"The file on which persisting devices", "-m mode",
						"the testmode, either [interactive,demo], default is demo" },
				"java TestApp", args);

		// create an instance of TestApp
		TestApp app = new TestApp();

		// check mandatory args
		if ((opt.getValue('p') != null) && (!opt.getValue('p').isEmpty()))
		{
			// The EnOcean link layer
			try
			{
				// create the lowest link layer
				EnJLink linkLayer = new EnJLink(opt.getValue('p'));

				// create a device listener for handling device updates
				SimpleDeviceListener listener = new SimpleDeviceListener();

				// get the persistent file name
				String persistentFileName = opt.getValue('f');

				// create the connection layer
				EnJConnection connection = new EnJConnection(linkLayer,
						((persistentFileName != null) && (!persistentFileName
								.isEmpty())) ? persistentFileName : null,
						listener);

				// connect the link
				linkLayer.connect();

				// check the app-mode
				String mode = TestApp.DEMO_MODE;

				// get the app-mode from options
				String optMode = opt.getValue('m');

				// change operating mode only if specified
				if ((optMode != null) && (!optMode.isEmpty()))
					if (optMode.equalsIgnoreCase(TestApp.INTERACTIVE_MODE))
						mode = TestApp.INTERACTIVE_MODE;

				// handle plain old demo mode
				// TODO: generalize the demo mode to work in generic usage
				// scenarios
				if (mode.equals(TestApp.DEMO_MODE))
				{
					app.performDemo(connection);
				}
				else if (mode.equals(TestApp.INTERACTIVE_MODE))
				{
					// handle the interactive mode
					app.interactiveDemo(connection);

					// disconnect from the adapter
					linkLayer.disconnect();

					// stop the application
					System.exit(0);
				}

			}
			catch (Exception e)
			{
				System.err
						.println("The given port does not exist or no device is plugged in"
								+ e);
			}
		}
		else
		{
			opt.usage(System.out, "TestApp");
		}
	}

	public void performDemo(EnJConnection connection)
			throws InterruptedException
	{
		// ---------- Explicit teach-in ---------
		// the device to learn
		// System.out.println("Enabling explicit teach-in for 018a781f");
		// connection.enableTeachIn("018a781f", "A5-02-05", 10000);

		// Thread.sleep(11000);

		// 0187ae92
		// System.out.println("Enabling explicit teach-in for 0187ae92");
		// connection.enableTeachIn("0187ae92", "A5-07-01", 10000);

		// Thread.sleep(11000);

		// ---------- Smart teach-in -------------

		// teach-in for 2s
		System.out.println("Enabling smart teach-in for 2s");
		connection.setSmartTeachIn(true);
		System.out.println("SmartTeachIn: "
				+ connection.isSmartTeachInEnabled());
		connection.enableTeachIn(12000);
		System.out.println("SmartTeachIn: "
				+ connection.isSmartTeachInEnabled());

		Thread.sleep(12000);

		connection.setSmartTeachIn(false);
		System.out.println("SmartTeachIn: "
				+ connection.isSmartTeachInEnabled());

		Thread.sleep(2000);

		// ----------- actuation test ------------

		// get the device by high-level uid
		EnOceanDevice device = connection.getDevice(25673502);

		// check not null
		if (device != null)
		{

			// get the device eep
			D20109 eep = (D20109) device.getEEP();

			eep.actuatorSetMeasurement(connection, device.getAddress(), true,
					true, true, 0, 0, D201UnitOfMeasure.kW, 10, 1);

			for (int i = 0; i < 10; i++)
			{
				System.out.println("Sending command");

				// if the device is not null, toggle its status
				if (device != null)
				{

					// toggle the status
					eep.actuatorSetOuput(connection, device.getAddress(),
							((i % 2) == 0) ? true : false);

					Thread.sleep(3000);
				}
			}
		}
	}

	public void interactiveDemo(EnJConnection connection) throws IOException,
			InterruptedException
	{
		// handle interactive mode

		// prompt:
		String prompt = "enj-interactive cmd > ";

		// the string holding the command line
		String cmdLine = "";

		// prepare the input reader
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(System.in));

		// the commaand cycle
		while (!(cmdLine.equalsIgnoreCase("exit")))
		{
			// print the first prompt
			System.out.print(prompt);

			// read the line
			cmdLine = bfr.readLine();

			// switch commands
			switch (cmdLine)
			{
				case TestApp.HELP:
				{
					// print the command help
					System.out.println(TestApp.class.getSimpleName()
							+ " command help:");

					// help command
					System.out
							.println("help - provides information about available commands");

					// demo
					System.out
							.println("demo - execute the hardcoded demo mode, for internal usage only");

					break;
				}

				case TestApp.DEMO:
				{
					// perform the demo
					this.performDemo(connection);

					break;
				}
			}
		}
	}

}
