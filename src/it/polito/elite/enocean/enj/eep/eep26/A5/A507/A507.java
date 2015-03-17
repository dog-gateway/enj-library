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
package it.polito.elite.enocean.enj.eep.eep26.A5.A507;

import it.polito.elite.enocean.enj.eep.EEP;
import it.polito.elite.enocean.enj.eep.Rorg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

;

/**
 * @author bonino
 *
 */
public abstract class A507 extends EEP
{
	// the EEP26 definition, according to the EEP26 specification
	public static final Rorg rorg = new Rorg((byte) 0xa5);
	public static final byte func = (byte) 0x07;

	// func must be defined by extending classes

	// Executor Thread Pool for handling attribute updates
	protected volatile ExecutorService attributeNotificationWorker;

	// -------------------------------------------------
	// Parameters defined by this EEP, which
	// might change depending on the network
	// activity.
	// --------------------------------------------------

	// --------------------------------------------------

	/**
	 * The class constructor
	 */
	public A507()
	{
		// call the superclass constructor
		super("2.6");

		// build the attribute dispatching worker
		this.attributeNotificationWorker = Executors.newFixedThreadPool(1);
	}
}
