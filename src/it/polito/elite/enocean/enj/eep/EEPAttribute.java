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

import java.util.HashSet;
import java.util.Set;

/**
 * The superclass for all attributes supported by EEPs. Allows keeping track of
 * the current value associated to the attribute, i.e., of the current state of
 * a device, with reference to the specific EEP.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 */
public abstract class EEPAttribute<T>
{
	/**
	 * Default channel to which the attribute is associated if no channel id is
	 * specified. Used to handle default attribute change dispatching.
	 */
	public static final int DEFAULT_CHANNEL = 1;

	// the attribute name
	protected String name;

	// the attribute value
	// e.g., power measurement.
	protected T value;

	// The unit of measure for the value, if any.
	protected String unit;

	// The set of attribute listeners to notify
	protected Set<EEPAttributeChangeListener> registeredListeners;

	/**
	 * The class constructor, initializes the data structures shared between all
	 * EEPFunctions, i.e., the name and the set of supported EEPs (storing their
	 * identifiers).
	 */
	public EEPAttribute(String name)
	{
		// store the name
		this.name = name;

		// null unit
		this.unit = null;

		// initialize the set of listeners
		this.registeredListeners = new HashSet<EEPAttributeChangeListener>();
	}

	/**
	 * Get the attribute name, should be one, unique and matching the EEP
	 * specification
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set the attribute name, should be one, unique and matching the EEP
	 * specification
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Get the current value of this EEP attribute
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public T getValue()
	{
		return this.value;
	}

	/**
	 * Set the current value of this EEP attribute
	 * 
	 * @param <T>
	 * 
	 * @param value
	 * @return
	 */
	public boolean setValue(T value)
	{
		this.value = value;
		return true;
	}

	/**
	 * Get the unit of measure associated to the value of this attribute, if
	 * available or null otherwise.
	 * 
	 * @return
	 */
	public String getUnit()
	{
		return this.unit;
	}

	/**
	 * Set the unit of measure associated to the attribute value.
	 */
	public boolean setUnit(String unit)
	{
		this.unit = unit;
		return true;
	}

	/**
	 * Adds an attribute change listener to the set of listeners registered to
	 * this attribute
	 * 
	 * @param listener
	 *            The {@link EEPAttributeChangeListener} instance to add.
	 * @return true if the listener is successfully added, false otherwise.
	 */
	public synchronized boolean addAttributeChangeListener(
			EEPAttributeChangeListener listener)
	{
		return this.registeredListeners.add(listener);
	}

	/**
	 * Removes an attribute change listener from the set of listeners registered
	 * to this attribute.
	 * 
	 * @param listener
	 *            The {@link EEPAttributeChangeListener} instance to remove.
	 * @return true if the listener is successfully removed, false otherwise.
	 */
	public synchronized boolean removeAttributeChangeListener(
			EEPAttributeChangeListener listener)
	{
		return this.registeredListeners.remove(listener);
	}

	/**
	 * Notifies all registered listeners for a change of the represented
	 * attribute
	 * 
	 * @param channelId
	 *            The channel for which the attribute changed (1 by default)
	 */
	public void notifyAttributeListeners(int channelId)
	{
		synchronized (this.registeredListeners)
		{
			for (EEPAttributeChangeListener listener : this.registeredListeners)
			{
				listener.handleAttributeChange(channelId, this);
			}
		}

	}

	/**
	 * Notifies all registered listeners for a change of the represented
	 * attribute
	 */
	public void notifyAttributeListeners()
	{
		this.notifyAttributeListeners(EEPAttribute.DEFAULT_CHANNEL);
	}

	/**
	 * Returns the byte representation (respecting the EEP2.6 standard) of this
	 * attribute value
	 * 
	 * @return
	 */
	public abstract byte[] byteValue();
}
