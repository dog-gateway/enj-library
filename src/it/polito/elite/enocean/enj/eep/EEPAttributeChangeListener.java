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


/**
 * Define the set of methods by which a software entity (class) can be notified
 * about the change of a "monitored" EEP26 attribute.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public interface EEPAttributeChangeListener
{
	/**
	 * Notifies a listener about the change of the given attribute, for the given channel.
	 * @param channelId The id of the channel involved by the attribute change.
	 * @param attribute The changed attribute (to ease handling).
	 */
	public void handleAttributeChange(int channelId, EEPAttribute<?> attribute);
}
