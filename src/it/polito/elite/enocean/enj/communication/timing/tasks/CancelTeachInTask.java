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
package it.polito.elite.enocean.enj.communication.timing.tasks;

import it.polito.elite.enocean.enj.communication.EnJConnection;

import java.util.TimerTask;

/**
 * A TimerTask subclass which disables the teach in procedure on the given
 * EnJConnection instance.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 * 
 */
public class CancelTeachInTask extends TimerTask
{

	// the EnJConnection layer reference
	private EnJConnection theConnection;

	/**
	 * Builds a task which disables the teach in procedure on the given
	 * EnJConnection instance.
	 * 
	 * @param theConnection
	 */
	public CancelTeachInTask(EnJConnection theConnection)
	{
		// store a reference to the connection layer for which teach in must be
		// disabled
		this.theConnection = theConnection;
	}

	@Override
	public void run()
	{
		this.theConnection.disableTeachIn();
	}

}
