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
package it.polito.elite.enocean.enj.eep;

import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;

import java.util.HashMap;
import java.util.Set;

/**
 * @author bonino
 * @param <T>
 * @param <T>
 * 
 */
public abstract class EEP implements EEPAttributeChangePublisher
{
	// the EnOcean Equipment Profile version
	// TODO: handle this as a real version
	private String version;

	// the set of attributes associated to single channels defined by this
	// profile: the key is the channel id. If the EEP has only one channel id
	// the implementing classes might encode all attributes as device-wide
	protected HashMap<Integer, HashMap<String, EEPAttribute<? extends Object>>> channelAttributes;

	// the set of EEPWide attributes
	protected HashMap<String, EEPAttribute<? extends Object>> eepAttributes;

	/**
	 * 
	 */
	public EEP(String version)
	{
		// store the version number
		this.version = new String(version);

		// initialize the channel specific attributes
		this.channelAttributes = new HashMap<Integer, HashMap<String, EEPAttribute<? extends Object>>>();

		// initializa the eep wide attributes
		this.eepAttributes = new HashMap<String, EEPAttribute<? extends Object>>();
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * Adds an EEPWide attribute to this {@link EEP} instance
	 * 
	 * @param attribute
	 *            the attribute to add
	 * @return true if successful, false otherwise.
	 */
	public boolean addEEPAttribute(EEPAttribute<? extends Object> attribute)
	{
		// the operation result, initially false
		boolean stored = false;

		// check if the insertion was successful
		if (this.eepAttributes.put(attribute.getClass().getSimpleName(),
				attribute).equals(attribute))
			stored = true;

		// return the insertion result
		return stored;
	}

	/**
	 * Returns the eep-wide attribute having the given name, or null if no
	 * attribute with the given name is available.
	 * 
	 * @param attributeName
	 *            The name of the attribute to retrieve.
	 * @return The corresponding {@link EEPAttribute} instance
	 */
	public EEPAttribute<? extends Object> getEEPAttribute(String attributeName)
	{
		return this.eepAttributes.get(attributeName);
	}

	/**
	 * Returns the names of the eep-wide attributes defined for this {@link EEP}
	 * instance.
	 * 
	 * @return The {@link Set}<{@link String}> containing all the names of the
	 *         currently available {@link EEP}s.
	 */
	public Set<String> listEEPAttributes()
	{
		return this.eepAttributes.keySet();
	}

	/**
	 * Adds an channel-specific attribute to this {@link EEP} instance
	 * 
	 * @param channelId
	 *            The id of the channel to which the attribute must be
	 *            associated.
	 * @param attribute
	 *            the attribute to add
	 * @return true if successful, false otherwise.
	 */
	public boolean addChannelAttribute(Integer channelId,
			EEPAttribute<? extends Object> attribute)
	{
		// the operation result, initially false
		boolean stored = false;

		// the channel-specific attribute map
		HashMap<String, EEPAttribute<? extends Object>> channelAttributes = this.channelAttributes
				.get(channelId);

		// check not null, if null the channel does not exist and must be
		// created
		if (channelAttributes == null)
		{
			// create the channel attributes map
			channelAttributes = new HashMap<String, EEPAttribute<? extends Object>>();

			// store the map
			this.channelAttributes.put(channelId, channelAttributes);
		}

		// check if the insertion was successful
		channelAttributes.put(attribute.getName(), attribute);
		stored = true;

		// return the insertion result
		return stored;
	}

	/**
	 * Returns the a channel specific attribute having the given name, or null
	 * if no attribute with the given name is available.
	 * 
	 * @param channelId
	 *            The channel id.
	 * @param attributeName
	 *            The name of the attribute to retrieve.
	 * @return The corresponding {@link EEPAttribute} instance
	 */
	public EEPAttribute<? extends Object> getChannelAttribute(
			Integer channelId, String attributeName)
	{
		// prepare the attribute holder
		EEPAttribute<? extends Object> attribute = null;

		// get the channel-specific attributes
		HashMap<String, EEPAttribute<? extends Object>> channelAttributes = this.channelAttributes
				.get(channelId);

		// if channel-specific attributes are available
		if (channelAttributes != null)
			// store the requested attribute, if present
			attribute = channelAttributes.get(attributeName);

		// return the extracted attribute, will be null if the attribute does
		// not exist.
		return attribute;
	}

	/**
	 * Returns the names of the channel-specific attributes defined for this
	 * {@link EEP} instance, on the given channel.
	 * 
	 * @param channelId
	 *            The id of the channel.
	 * @return The {@link Set}<{@link String}> containing all the names of the
	 *         currently available {@link EEP}s.
	 */
	public Set<String> listChannelAttributes(Integer channelId)
	{
		// get the channel specific attributes
		HashMap<String, EEPAttribute<? extends Object>> attributes = this.channelAttributes
				.get(channelId);

		// return the channel attribute names if the channel exist and there are
		// attributes associated to the channel, false otherwise.
		return (attributes != null) ? attributes.keySet() : null;
	}

	/**
	 * Returns the number of channels supported by this {@link EEP} instance.
	 * 
	 * @return The number of channels as an Integer.
	 */
	public int getNumberOfChannels()
	{
		return this.channelAttributes.size();
	}

	@Override
	/**
	 * Provides the base implementation of the {@link EEP26AttributeChangePublisher) interface
	 */
	public boolean addEEP26AttributeListener(int channelId,
			String attributeName, EEPAttributeChangeListener listener)
	{
		// the success flag, initially false
		boolean success = false;

		// the map of attributes associated to the given channel
		HashMap<String, EEPAttribute<?>> attributes = this.channelAttributes
				.get(channelId);

		// get the required attribute name
		EEPAttribute<?> attribute = attributes.get(attributeName);

		// if not null, register the listener and store the result of the
		// process
		if (attribute != null)
			success = attribute.addAttributeChangeListener(listener);

		// return the final status of the registration (either true or false)
		return success;
	}

	@Override
	/**
	 * Provides the base implementation of the {@link EEP26AttributeChangePublisher) interface
	 */
	public boolean removeEEP26AttributeListener(int channelId,
			String attributeName, EEPAttributeChangeListener listener)
	{
		// the success flag, initially false
		boolean success = false;

		// the map of attributes associated to the given channel
		HashMap<String, EEPAttribute<?>> attributes = this.channelAttributes
				.get(channelId);

		// get the required attribute name
		EEPAttribute<?> attribute = attributes.get(attributeName);

		// if not null, remove the listener and store the operation result
		if (attribute != null)
			success = attribute.removeAttributeChangeListener(listener);

		// return the final status of the registration (either true or false)
		return success;
	}

	/**
	 * Return the eep identifier associated to this EEP
	 * 
	 * @return
	 */
	public abstract EEPIdentifier getEEPIdentifier();

	/**
	 * Handles the profile data update, must be specifically implemented by each
	 * profile class
	 */
	public abstract boolean handleProfileUpdate(EEP26Telegram telegram);
}
