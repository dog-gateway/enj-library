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
package it.polito.elite.enocean.enj.EEP26.D2.D201;

import javax.jws.Oneway;

import it.polito.elite.enocean.enj.EEP26.attributes.EEPPowerFailure;

/**
 * @author bonino
 *
 */
public class D201ActuatorStatusResponse
{
	private boolean powerFailureEnabled;
	private boolean powerFailureDetected;
	private int commandId;
	private boolean overCurrentSwitchOffExecuted;
	private D201ErrorLevel errorLevel;
	
	/**
	 * 
	 */
	public D201ActuatorStatusResponse()
	{
		// TODO Auto-generated constructor stub
	}

	public D201ActuatorStatusResponse(byte powerFailure,
			byte powerFailureDetection, byte commandId,
			byte overCurrentSwitchOff, byte errorLevel, byte channelId,
			byte localControl, byte outputValue)
	{
		// store the power failure attribute
		this.powerFailureEnabled = (powerFailure == 0) ? false : true;

		// store the power failure detection state
		this.powerFailureDetected = (powerFailureDetection == 0) ? false : true;

		// store the command id
		this.commandId = (int)this.commandId;

		// store the overcurrent switch-off flag
		this.overCurrentSwitchOffExecuted = (overCurrentSwitchOff == 0) ? false
				: true;
		
		// store the error level
		this.errorLevel = D201ErrorLevel.valueOf(errorLevel);
	}

}
