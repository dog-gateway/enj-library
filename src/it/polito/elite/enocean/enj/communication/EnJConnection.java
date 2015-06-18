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

import it.polito.elite.enocean.enj.application.devices.EnJPersistentDeviceSet;
import it.polito.elite.enocean.enj.communication.timing.tasks.CancelTeachInTask;
import it.polito.elite.enocean.enj.communication.timing.tasks.EnJDeviceChangeDeliveryTask;
import it.polito.elite.enocean.enj.eep.EEP;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.EEPRegistry;
import it.polito.elite.enocean.enj.eep.eep26.D5.D500.D500;
import it.polito.elite.enocean.enj.eep.eep26.D5.D500.D50001;
import it.polito.elite.enocean.enj.eep.eep26.F6.F602.F602;
import it.polito.elite.enocean.enj.eep.eep26.F6.F602.F60201;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramFactory;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTeachInTelegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.OneBSTelegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.RPSTelegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.UTETeachInTelegram;
import it.polito.elite.enocean.enj.link.EnJLink;
import it.polito.elite.enocean.enj.link.PacketListener;
import it.polito.elite.enocean.enj.model.EnOceanDevice;
import it.polito.elite.enocean.enj.util.ByteUtils;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.event.Event;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.radio.Radio;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.response.Response;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

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
	// the class logger
	private Logger logger;

	// the wrapped link layer
	private EnJLink linkLayer;

	// the set of device listeners to keep updated about device events
	private Set<EnJDeviceListener> deviceListeners;
	
	private Set<EnJTeachInListener> teachInListeners;

	// the executor service to run device update tasks
	private ExecutorService deviceUpdateDeliveryExecutor;

	// ------------- TEACH IN -----------------

	// the default teach-in timeout in milliseconds
	public static final int TEACH_IN_TIME = 20000;

	// the teach-in flag
	private boolean teachIn;

	// the teach in timer
	private Timer teachInTimer;

	// the device to teach-in
	private EnOceanDevice deviceToTeachIn;

	// The set of known devices
	private EnJPersistentDeviceSet knownDevices;

	// The EEP registry
	private EEPRegistry registry;

	// a flag for enabling / disabling smart (non-standard) teach-in for a
	// selected subset of telegrams (RPS and 1BS)
	private boolean smartTeachIn;

	/**
	 * Build a connection layer instance on top of the given link layer
	 * instance.
	 * 
	 * @param linkLayer
	 *            The {@link EnJLink} instance upon which basing the connection
	 *            layer.
	 * 
	 * @param peristentDeviceStorageFilename
	 *            The name of the persistent storage to use (path on the file
	 *            system), can be null for in-memory storage.
	 */
	public EnJConnection(EnJLink linkLayer,
			String peristentDeviceStorageFilename)
	{
		this.initCommon(linkLayer, peristentDeviceStorageFilename, null);
	}

	/**
	 * 
	 * @param linkLayer
	 *            The {@link EnJLink} instance upon which basing the connection
	 *            layer.
	 * 
	 * @param peristentDeviceStorageFilename
	 *            The name of the persistent storage to use (path on the file
	 *            system), can be null for in-memory storage.
	 * @param listener
	 *            The device listener to notify when devices are added,
	 *            modified, removed. This is the only listener "able" to listen
	 *            to device notifications stemming from the restoring of a
	 *            peristent storage.
	 */
	public EnJConnection(EnJLink linkLayer,
			String peristentDeviceStorageFilename, EnJDeviceListener listener)
	{
		this.initCommon(linkLayer, peristentDeviceStorageFilename, listener);
	}

	private void initCommon(EnJLink linkLayer,
			String peristentDeviceStorageFilename, EnJDeviceListener listener)
	{
		// initialize the logger
		this.logger = Logger.getLogger(EnJConnection.class.getName());

		// initialize the set of device listeners
		this.deviceListeners = new HashSet<>();
		
		//initialize the set of teach-in listeners
		this.teachInListeners = new HashSet<>();

		// initialize the update delivery executor
		this.deviceUpdateDeliveryExecutor = Executors.newCachedThreadPool();

		// initialize the teachIn flag at false
		this.teachIn = false;
		this.deviceToTeachIn = null;

		// set the smart teach-in at false by default
		this.smartTeachIn = false;

		// initialize the teach in timer
		this.teachInTimer = new Timer();

		// store a reference to the link layer
		this.linkLayer = linkLayer;

		// store a reference to the EEPRegistry, this call also triggers dynamic
		// discovery of supported EEPs
		this.registry = EEPRegistry.getInstance();

		// initialize the persistent device store and sets autosave at on
		// TODO: check how to pass the filename here...
		this.knownDevices = new EnJPersistentDeviceSet(
				peristentDeviceStorageFilename, true);

		// notify device listeners if any
		if (listener != null)
		{
			// add the listener
			this.deviceListeners.add(listener);

			// notify the listener
			for (EnOceanDevice device : this.knownDevices.listAll())
			{
				this.notifyEnJDeviceListeners(device,
						EnJDeviceChangeType.CREATED);
			}
		}

		// add this connection layer as listener for incoming events
		this.linkLayer.addPacketListener(this);

	}

	/**
	 * Adds a device listener to the set of listeners to be notified about
	 * device events: creation, modification, deletion.
	 * 
	 * @param listener
	 *            filename * The {@link EnJDeviceListener} to notify to.
	 */
	public void addEnJDeviceListener(EnJDeviceListener listener)
	{
		// store the listener in the set of currently active listeners
		this.deviceListeners.add(listener);
	}

	/**
	 * Removes a device listener from the ste of listeners to be notified about
	 * filename * device events.
	 * 
	 * @param listener
	 *            The {@link EnJDeviceListener} to remove.
	 * @return true if removal was successful, false, otherwise.
	 */
	public boolean removeEnJDeviceListener(EnJDeviceListener listener)
	{
		return this.deviceListeners.remove(listener);
	}
	
	/**
	 * Adds a teach in listener to the set of listeners to be notified about
	 * teach-in status
	 * 
	 * @param listener
	 *            filename * The {@link EnJDeviceListener} to notify to.
	 */
	public void addEnJTeachInListener(EnJTeachInListener listener)
	{
		// store the listener in the set of currently active listeners
		this.teachInListeners.add(listener);
	}

	/**
	 * Removes a teach-in listener from the set of listeners to be notified about
	 * teach-in events.
	 * 
	 * @param listener
	 *            The {@link EnJDeviceListener} to remove.
	 * @return true if removal was successful, false, otherwise.
	 */
	public boolean removeEnJDeviceListener(EnJTeachInListener listener)
	{
		return this.teachInListeners.remove(listener);
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

	public void enableTeachIn(String hexDeviceAddress,
			String eepIdentifierAsString)
	{
		this.enableTeachIn(hexDeviceAddress, eepIdentifierAsString,
				EnJConnection.TEACH_IN_TIME);
	}

	public void enableTeachIn(String hexDeviceAddress,
			String eepIdentifierAsString, int time)
	{
		if ((hexDeviceAddress != null) && (!hexDeviceAddress.isEmpty())
				&& (eepIdentifierAsString != null)
				&& (!eepIdentifierAsString.isEmpty()))
		{
			// convert - parse strings to corresponding data

			// parse the identifier
			EEPIdentifier eepIdentifier = EEPIdentifier
					.parse(eepIdentifierAsString);

			byte address[] = EnOceanDevice.parseAddress(hexDeviceAddress);

			EnOceanDevice device = new EnOceanDevice(address, null);
			device.setEEP(this.registry.getEEP(eepIdentifier));

			// store the device to learn
			this.deviceToTeachIn = device;

			// start reset timer
			this.enableTeachIn(time);
		}
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
			this.teachInTimer
					.schedule(new CancelTeachInTask(this), teachInTime);

			// TODO place logger here
			this.logger.info("Teach-in enabled");
			
			//notify listeners
			for(EnJTeachInListener listener : this.teachInListeners)
				listener.teachInEnabled(isSmartTeachInEnabled());
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
	 * handlePacket * ignored.
	 */
	public synchronized void disableTeachIn()
	{
		// stop any pending timer
		this.teachInTimer.purge();

		// disable the teach in procedure
		this.teachIn = false;
		this.deviceToTeachIn = null;

		// TODO place logger here
		this.logger.info("Teach-in disabled");
		
		//notify listeners
		for(EnJTeachInListener listener : this.teachInListeners)
			listener.teachInDisabled();
	}

	/**
	 * Sends the given payload encapsulated into a Radio message, automatically
	 * adds sender address and status to the payload, thus completing the actual
	 * payload being sent. By default the gateway address is fixed at 0x00ffffff
	 * and the status byte at 0x00.
	 * 
	 * @param address
	 * @param payload
	 */
	public void sendRadioCommand(byte[] address, byte[] payload)
	{
		// add sender address and status
		byte actualPayload[] = new byte[payload.length + 5];

		// copy the payload
		for (int i = 0; i < payload.length; i++)
			actualPayload[i] = payload[i];

		// sender address
		// TODO: remove hardcoding if possible!!!
		actualPayload[payload.length] = (byte) 0x00;
		actualPayload[payload.length + 1] = (byte) 0xFF;
		actualPayload[payload.length + 2] = (byte) 0xFF;
		actualPayload[payload.length + 3] = (byte) 0xFF;

		// status
		actualPayload[payload.length + 4] = (byte) 0x00;

		// build the link-layer packet
		Radio enjLinkPacket = Radio.getRadio(address, actualPayload, true);

		// send the packet
		this.linkLayer.send(enjLinkPacket);
	}

	/**
	 * @return the smartTeachIn
	 */
	public boolean isSmartTeachInEnabled()
	{
		return smartTeachIn;
	}

	/**
	 * @param smartTeachIn
	 *            the smartTeachIn to set
	 */
	public void setSmartTeachIn(boolean smartTeachIn)
	{
		this.smartTeachIn = smartTeachIn;
	}

	/**
	 * 
	 * @param hexDeviceAddress
	 * @param eepIdentifier
	 */
	public void addNewDevice(String hexDeviceAddress, String eepIdentifier)
	{
		byte deviceAddress[] = EnOceanDevice.parseAddress(hexDeviceAddress);
		EEPIdentifier eepId = EEPIdentifier.parse(eepIdentifier);

		// check if the device is already known
		if ((this.knownDevices.getByLowAddress(deviceAddress) == null)
				&& (deviceAddress != null) && (eepId != null))
		{
			this.addNewDevice(deviceAddress, null, eepId);
		}
	}
	
	

	/**
	 * Returns the set of currently known devices
	 * @return the knownDevices
	 */
	public Collection<EnOceanDevice> getKnownDevices()
	{
		return knownDevices.listAll();
	}

	/**
	 * 
	 * @param address
	 * @param manufacturerId
	 * @param eepId
	 */
	private void addNewDevice(byte address[], byte manufacturerId[],
			EEPIdentifier eepId)
	{
		EnOceanDevice device = new EnOceanDevice(address, manufacturerId);
		Class<? extends EEP> deviceEEP = this.registry.getEEP(eepId);

		if (deviceEEP != null)
		{
			device.setEEP(deviceEEP);

			// notify listeners
			this.notifyEnJDeviceListeners(device, EnJDeviceChangeType.CREATED);

			// store the device
			this.knownDevices.add(device);
		}
	}

	/**
	 * adds sender address and status to the given payload, thus completing the
	 * Returns the EnOcean device having the given UID
	 * 
	 * @param deviceUID
	 * @return
	 */
	public EnOceanDevice getDevice(int deviceUID)
	{
		return this.knownDevices.getByUID(deviceUID);
	}

	@Override
	/**
	 * Handles packets received at the link layer
	 */
	public void handlePacket(ESP3Packet pkt)
	{
		// check if the packet is an asynchronous information coming from the
		// network or a response
		try
		{
			if (pkt.isRadio())
			{
				this.handleRadioPacket(new Radio(pkt));
			}
			else if (pkt.isResponse())
			{
				this.handleResponse(new Response(pkt));
			}
			else if (pkt.isEvent())
			{
				this.handleEvent(new Event(pkt));
			}
		}
		catch (Exception e)
		{
			this.logger.warning("Error while handling received packet"+ e);
		}
	}

	private void handleRadioPacket(Radio pkt)
	{
		this.logger.info("Received: "
				+ ByteUtils.toHexString(pkt.getPacketAsBytes()));
		// this.logger.info("Payload: "+ByteUtils.toHexString(pkt.getDataPayload()));
		EEP26Telegram telegram = EEP26TelegramFactory.getEEP26Telegram(pkt);

		if (telegram != null)
		{

			if ((this.teachIn)
					&& (telegram.getTelegramType() == EEP26TelegramType.UTETeachIn))
			{
				this.handleUTETeachIn((UTETeachInTelegram) telegram);
			}
			else
			{
				// get the sender id, i.e., the address of the device
				// generating
				// the
				// packet
				byte address[] = telegram.getAddress();

				// get the corresponding device...
				EnOceanDevice device = this.knownDevices
						.getByLowAddress(address);

				// check null
				if (device == null)
				{
					// the device has never been seen before,
					// therefore the device must be learned, either
					// implicitly or
					// explicitly

					// check if the packet is an RPS one
					if (RPSTelegram.isRPSPacket(pkt))
					{
						// handle RPS teach-in, can either be done
						// implicitly,
						// an
						// F60201 EEP will be used, or explicitly if teachIn
						// is
						// true
						// and the device to teach in has been completely
						// specified.
						device = this.handleRPSTeachIn(pkt);
					}
					else if (OneBSTelegram.is1BSPacket(pkt))
					{
						// handle 1BS telegrams (much similar to RPS)
						device = this.handle1BSTeachIn(pkt);
					}
					else if (FourBSTelegram.is4BSPacket(pkt))
					{
						// handle 3 variations of 4BS teach in: explicit
						// with
						// application-specified EEP, explicit with
						// device-specified
						// EEP or bi-directional.
						device = this.handle4BSTeachIn(pkt);
					}

				}
				else
				// the device is already known therefore message handling
				// can be
				// delegated
				{

					// delegate to the device
					EEP deviceEEP = device.getEEP();

					// check not null
					if (deviceEEP != null)
					{
						if (!deviceEEP.handleProfileUpdate(telegram))
						{
							// log the error
							this.logger
									.warning("Profile update for"
											+ ByteUtils.toHexString(device
													.getAddress())
											+ " was not handled successfully");
						}
					}
					else
					{
						// log the error
						this.logger
								.warning("No suitable EEP found for the given device...");
					}
				}
			}
		}

	}

	/**
	 * Handles the UTE teach-in process, it can either result in a new device
	 * being added, in a teach-in procedure disabling or it could just refuse
	 * the physical teach-in request if not supported.
	 * 
	 * @param pkt
	 *            The teach-in request packet
	 */
	private void handleUTETeachIn(UTETeachInTelegram pkt)
	{
		// the possible response to send back to the transceiver
		UTETeachInTelegram response = null;

		// check the packet type
		if (pkt.isTeachInRequest())
		{
			// check the eep
			if (this.registry.isEEPSupported(pkt.getEEP()))
			{
				// build the response packet
				response = pkt
						.buildResponse(UTETeachInTelegram.BIDIRECTIONAL_TEACH_IN_SUCCESSFUL);

				// build the device
				this.addNewDevice(pkt.getAddress(), pkt.getManId(),
						pkt.getEEP());
			}
			else
				// build the response packet
				response = pkt
						.buildResponse(UTETeachInTelegram.BIDIRECTIONAL_TEACH_IN_REFUSED);
		}
		else if (pkt.isTeachInDeletionRequest())
		{
			// stop the learning process
			this.disableTeachIn();

			// check if response is required
			// build the response packet
			response = pkt
					.buildResponse(UTETeachInTelegram.BIDIRECTIONAL_TEACH_IN_DELETION_ACCEPTED);
		}
		else if (pkt.isNotSpecifiedTeachIn())
		{
			// currently not supported
			response = pkt
					.buildResponse(UTETeachInTelegram.BIDIRECTIONAL_TEACH_IN_REFUSED);
		}

		if ((pkt.isResponseRequired()) && (response != null)
				&& (response.isResponse()))
		{
			// send the packet back to the transceiver, with high
			// priority as a maximum 500ms latency is allowed.
			this.linkLayer.send(response.getRawPacket(), true);
		}
	}

	private EnOceanDevice handleRPSTeachIn(ESP3Packet pkt)
	{
		// parse the packet
		RPSTelegram rpsTelegram = new RPSTelegram(pkt);

		// initially null
		EnOceanDevice device = null;

		// actually everything shall be ignored unless teach-in is
		// enabled
		if (this.teachIn)
		{

			// the only teach-in procedure supported by the EEP2.6 specification
			// is direct and esplicit tech-in, mening that the device to
			// teach-in and the corresponding profile are known in advance.
			// Since this assumption may sometimes be a little bit limiting, a
			// mechanism for enabling implicit teach-in is also provided. This
			// latter option, however always matches 2 rocker switches as there
			// is no way to distinguish between the two EEPs by just looking at
			// the packet content.
			if (this.deviceToTeachIn != null)
			{
				this.explicitTeachIn(rpsTelegram);
			}
			else if (this.smartTeachIn)
			{
				// build a new RPS device
				this.addNewDevice(rpsTelegram.getAddress(), null,
						new EEPIdentifier(F602.rorg, F602.func, F60201.type));
			}
		}

		return device;

	}

	private EnOceanDevice handle1BSTeachIn(ESP3Packet pkt)
	{
		// parse the packet
		OneBSTelegram oneBSTelegram = new OneBSTelegram(pkt);

		// initially null
		EnOceanDevice device = null;

		// actually everything shall be ignored unless teach-in is
		// enabled
		if (this.teachIn)
		{

			// the only teach-in procedure supported by the EEP2.6 specification
			// is direct and esplicit tech-in, mening that the device to
			// teach-in and the corresponding profile are known in advance.
			// Since this assumption may sometimes be a little bit limiting, a
			// mechanism for enabling implicit teach-in is also provided. This
			// latter option, matches the only device defined by the
			// specification i.e., the D5-00-01 Contact Switch
			if (this.deviceToTeachIn != null)
			{
				this.explicitTeachIn(oneBSTelegram);
			}
			else if (this.smartTeachIn)
			{

				// build a new RPS device,
				this.addNewDevice(oneBSTelegram.getAddress(), null,
						new EEPIdentifier(D500.rorg, D500.func, D50001.type));
			}
		}

		return device;

	}

	private EnOceanDevice handle4BSTeachIn(ESP3Packet pkt)
	{
		// parse the packet
		FourBSTelegram bs4Telegram = new FourBSTelegram(pkt);

		// prepare the device to return
		EnOceanDevice device = null;

		// actually everything shall be ignored unless teach-in is
		// enabled
		if (this.teachIn)
		{
			// check if the received packet is teach in
			if (FourBSTeachInTelegram.isTeachIn(bs4Telegram))
			{
				// wrap the telegram
				FourBSTeachInTelegram bs4TeachInTelegram = new FourBSTeachInTelegram(
						bs4Telegram);

				// --------- Teach-in variation 2 ------
				if (bs4TeachInTelegram.isWithEEP())
				{
					// build a new 4BS device,
					this.addNewDevice(bs4TeachInTelegram.getAddress(),
							bs4TeachInTelegram.getManId(), new EEPIdentifier(
									bs4TeachInTelegram.getRorg(),
									bs4TeachInTelegram.getEEPFunc(),
									bs4TeachInTelegram.getEEPType()));
				}
				else if (this.deviceToTeachIn != null)
				{
					device = this.explicitTeachIn(bs4TeachInTelegram);
				}
				else
				{
					// log not supported
					this.logger
							.warning("Neither implicit or explicit learn succeeded; bi-directional teach-in currently not supported for 4BS telegrams.");

					// log the address
					String msg = "";
					for (int i = 0; i < 4; i++)
						msg = msg
								+ String.format("%02x",
										bs4TeachInTelegram.getAddress()[i]);
					this.logger.info("Device address:" + msg);
				}
			}
		}

		return device;
	}

	private void notifyEnJDeviceListeners(EnOceanDevice device,
			EnJDeviceChangeType changeType)
	{
		// use asynchronous delivery here, to avoid blocking / delaying the
		// messaging procedure
		this.deviceUpdateDeliveryExecutor
				.execute(new EnJDeviceChangeDeliveryTask(device, changeType,
						this.deviceListeners));
	}

	private void handleResponse(Response pkt)
	{
		// debug
		this.logger.info("Received response: " + pkt.toString());

	}

	private void handleEvent(Event pkt)
	{
		// debug
		this.logger.info("Received event: " + pkt.toString());
	}

	private EnOceanDevice explicitTeachIn(EEP26Telegram telegram)
	{
		// device initially null
		EnOceanDevice device = null;

		// Debug can be commented out in production

		// dump the address of the device to be taught in
		String msg = "toTeachIn: ";
		for (int i = 0; i < 4; i++)
			msg = msg
					+ String.format("%02x",
							this.deviceToTeachIn.getAddress()[i]);
		this.logger.info(msg);

		// dump the address of the device sending the data message
		msg = "received: ";
		for (int i = 0; i < 4; i++)
			msg = msg + String.format("%02x", telegram.getAddress()[i]);

		// write the debug information in the log
		this.logger.fine(msg);

		// check if the address of the device and the address
		// of the telegram match
		if (Arrays.equals(this.deviceToTeachIn.getAddress(),
				telegram.getAddress()))
		{

			// use the device to teach in

			// notify listeners
			this.notifyEnJDeviceListeners(this.deviceToTeachIn,
					EnJDeviceChangeType.CREATED);

			// store the device
			this.knownDevices.add(this.deviceToTeachIn);

			// set the device learnt
			device = this.deviceToTeachIn;

			// reset the device to teach in
			this.deviceToTeachIn = null;
		}

		return device;
	}
}