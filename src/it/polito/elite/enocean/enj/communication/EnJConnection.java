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
package it.polito.elite.enocean.enj.communication;

import it.polito.elite.enocean.enj.EEP26.EEPRegistry;
import it.polito.elite.enocean.enj.EEP26.D2.D201.D20108;
import it.polito.elite.enocean.enj.EEP26.packet.UTETeachInPacket;
import it.polito.elite.enocean.enj.communication.timing.tasks.CancelTeachInTask;
import it.polito.elite.enocean.enj.link.EnJLink;
import it.polito.elite.enocean.enj.link.PacketListener;
import it.polito.elite.enocean.enj.model.EnOceanDevice;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.radio.Radio;

import java.util.Timer;

/**
 * The EnOcean for Java (EnJ) connection layer. It decouples link-level
 * communication and protocol management issues from the application logic.
 * Defines standard and "easy to use" methods for writing / reading data from an
 * EnOcean network.
 * 
 * It is typically built on top of an EnJLink instance, e.g.:
 * 
 * <pre>
 * String serialId = &quot;/dev/tty0&quot;;
 * 
 * EnJLink link = new EnJLink(serialId);
 * 
 * EnJConnection connection = new EnJConnection(link);
 * </pre>
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * @authr <a href="mailto:biasiandrea04@gmail.com">Andrea Biasi </a>
 * 
 */
public class EnJConnection implements PacketListener
{
	// the wrapped link layer
	private EnJLink linkLayer;

	// the default teach-in timeout in milliseconds
	public static final int TEACH_IN_TIME = 20000;

	// the teach-in flag
	private boolean teachIn;

	// the teach in timer
	private Timer teachInTimer;

	// the teach in disabling task
	private CancelTeachInTask teachInResetTask;

	// -------- check if needed -------------
	private EnOceanDevice device;

	/**
	 * Build a connection layer instance on top of the given link layer
	 * instance.
	 * 
	 * @param linkLayer
	 *            The {@link EnJLink} instance upon which basing the connection
	 *            layer.
	 */
	public EnJConnection(EnJLink linkLayer)
	{
		// initialize the teachIn flag at false
		this.teachIn = false;

		// initialize the teach in timer
		this.teachInTimer = new Timer();

		// intialize the teach in reset task
		this.teachInResetTask = new CancelTeachInTask(this);

		// store a reference to the link layer
		this.linkLayer = linkLayer;

		// add this connection layer as listener for incoming events
		this.linkLayer.addPacketListener(this);
	}

	/**
	 * Enables the teach in procedure, it forces the connection layer to listen
	 * for teach-in requests coming from the physical network. whenever a new
	 * teach-in request is detected, a device recognition process is started
	 * enabling access to the newly discovered device.
	 * 
	 * New devices are transfered to the next layer by means of a listener
	 * mechanism.
	 * 
	 * The teach in procedure lasts for a time equal to the default
	 * <code>EnJConnection.TEACH_IN_TIME</code>
	 */
	public void enableTeachIn()
	{
		// start reset timer
		this.enableTeachIn(EnJConnection.TEACH_IN_TIME);
	}

	/**
	 * Enables the teach in procedure, it forces the connection layer to listen
	 * for teach-in requests coming from the physical network. whenever a new
	 * teach-in request is detected, a device recognition process is started
	 * enabling access to the newly discovered device.
	 * 
	 * New devices are transfered to the next layer by means of a listener
	 * mechanism.
	 * 
	 * @param teachInTime
	 *            the maximum time for which the connection layer will accept
	 *            teach in requests.
	 */
	public void enableTeachIn(int teachInTime)
	{
		if (!this.teachIn)
		{
			// enable teach in
			this.teachIn = true;

			// start the teach in reset timer
			this.teachInTimer.schedule(this.teachInResetTask, teachInTime);
		}
	}

	/**
	 * Checks if the connection layer is currently accepting teach-in requests
	 * or not
	 * 
	 * @return the teachIn true if the connection layer is accepting teach-in
	 *         requests, false otherwise.
	 */
	public boolean isTeachInEnabled()
	{
		return teachIn;
	}

	/**
	 * Disables the teach mode on the connection layer. Teach-in requests are
	 * ignored.
	 */
	public void disableTeachIn()
	{
		// stop any pending timer
		this.teachInTimer.cancel();
		this.teachInTimer.purge();

		// disable the teach in procedure
		this.teachIn = false;
	}

	// TODO: change this to abstract the link layer packet composition!!
	public void sendRadioCommand(byte[] address, byte[] payload)
	{
		// build the link-layer packet
		Radio enjLinkPacket = Radio.getRadioToSend(address, payload);

		//send the packet
		this.linkLayer.send(enjLinkPacket);
	}

	@Override
	/**
	 * Handles packets received at the link layer
	 */
	public void handlePacket(ESP3Packet pkt)
	{
		if ((this.teachIn) && (UTETeachInPacket.isUTETeachIn(pkt)))
			this.handleUTETeachIn(new UTETeachInPacket(pkt));

	}

	/**
	 * Handles the UTE teach-in process, it can either result in a new device
	 * being added, in a tech-in procedure disabling or it could just refuse the
	 * physical teach-in request if not supported.
	 * 
	 * @param pkt
	 *            The teach-in request packet
	 */
	private void handleUTETeachIn(UTETeachInPacket pkt)
	{
		// the possible response to send back to the transceiver
		UTETeachInPacket response = null;

		// check the packet type
		if (pkt.isTeachInRequest())
		{
			// check the eep
			if (EEPRegistry.getInstance().isEEPSupported(pkt.getEEP()))

				// build the response packet
				response = pkt
						.buildResponse(UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_SUCCESSFUL);
			else
				// build the response packet
				response = pkt
						.buildResponse(UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_REFUSED);
		}
		else if (pkt.isTeachInDeletionRequest())
		{
			// stop the learning process
			this.disableTeachIn();

			// check if response is required
			// build the response packet
			response = pkt
					.buildResponse(UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_DELETION_ACCEPTED);
		}
		else if (pkt.isNotSpecifiedTeachIn())
		{
			// currently not supported
			response = pkt
					.buildResponse(UTETeachInPacket.BIDIRECTIONAL_TEACH_IN_REFUSED);
		}

		if ((pkt.isResponseRequired()) && (response != null)
				&& (response.isResponse()))
		{
			// send the packet back to the transceiver, with high
			// priority as a maximum 500ms latency is allowed.
			this.linkLayer.send(response.getRawPacket(), true);
		}
	}

}