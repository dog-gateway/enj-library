# EnJ - EnOcean Java Library #
EnJ is a java library designed to be fully compliant with the EnOcean specifications. It currently supports the [EnOcean Serial Protocol](http://www.enocean.com/esp) version 3.0 and the [EnOcean Equipment Profile version 2.6](http://www.enocean-alliance.org/eep/).
The library is designed to work with any EnOcean gateway device which complies with the ESP3.0 specifications. It has been tested on the [USB-300](https://www.enocean.com/en/enocean_modules/usb-300-oem/) EnOcean module and on devices such as the [EnOceanPi](http://www.element14.com/community/docs/DOC-55169/l/enocean-pi-transforms-raspberry-pi-into-a-wireless-gateway?CMP=PRR-GLO-13-0042), based on the [TCM310](https://www.enocean.com/en/enocean_modules/tcm-310-data-sheet.pdf) and the [TCM300](https://www.enocean.com/en/enocean_modules/tcm-300-data-sheet.pdf)/[320](https://www.enocean.com/en/enocean_modules/tcm-320-data-sheet.pdf) gateway chips.

The library currently supports a subset of profiles defined in the EEP2.6 specifications but a progressive increase of supported profiles is envisioned. Supported profiles include:
* all A5-02-XX profiles (Temperature Sensors)
* A5-07-01 (Occupancy Sensor)
* all D5-00-XX profiles (Single Contact Switch)
* D2-01-08 (Metering Plug)
* F6-02-01 and F6-02-02 (Rocker Switches)

Profiles are included in the library after a test process involving real devices. For example, we exploited temperature sensors by [NodOn](http://www.nodon.fr/en/#Produits), who kindly donated a full set of EnOcean devices, or Occupancy sensors by [PEHA](http://www.peha.de/cms/front_content.php?client=1&lang=1&idcatart=1234&Page=3&ProductsPage=12&keyword=&catID=44&prodID=26284).

EnJ aims at providing easy access to EnOcean networks from Java, with a clear and syntethic approach. Protocol peculiarities are hidden in the library classes and the process of connecting to and controlling EnOcean devices is kept as simple as possible. The typical code needed to instantiate the library and connect to a given gateway is as follows:

```
// The EnJ link layer, uses the identifier of the serial port on which the gateway is connected
EnJLink linkLayer = new EnJLink("/dev/ttyUSB0");

// build the connection layer, which abstracts network peculiarities and
// provides an event-based access to connected devices
EnJConnection connection = new EnJConnection(linkLayer, null); //null persistent storage

// build a simple device listener to "listen" to device notifications
SimpleDeviceListener listener = new SimpleDeviceListener();

// add the listener to the connection layer
connection.addEnJDeviceListener(listener);

// connect the link to the physical network
linkLayer.connect();
```

The library is organized as follows:
* packages (and classes) stemming from the root ```it.polito.elite.enocean.enj.eep``` define and include all the knowledge needed to handle the EEP specification and in particular the EEP2.6 version;
* packages (and classes) stemming from ```it.polito.elite.enocean.enj.communication``` deal with high-level, event-based representation of EnOcean networks;
* packages (and classes) stemming from ```it.polito.elite.enocean.enj.link``` address the issues related to the ESP3 serial protocol specification;
* packages (and classes) stemming from ```it.polito.elite.enocean.protocol.serial.v3``` provide data models for the different low-level messages exchanged over the ESP3 communication with the gateway module;
* packages (and classes) inheriting from ```it.polito.elite.enocean.test``` define unit tests and sample applications for testing the library functions.

Dependencies of the library are managed through Maven, the corresponding project POM file is included in the repository. To compile the library just type:

```mvn clean package```

To use the library as an eclipse plugin project after the above command, run
```mvn eclipse:eclipse -Declipse.pde```

#### Extending supported profiles ####
Supported profiles can be easily extended by developing the proper classes in the ```it.polito.elite.enocean.enj.eep.eep26``` tree. An abstract skeleton is already provided for EEPs and for attributes (values) defined in each EEP. Each specific profile should inherit from a family abstract class, e.g., A507 for devices belonging to the A5-07-XX EEPs. As an example the A507 superclass is defined as follows:

```
/*
* EnJ - EnOcean Java API
*
* Copyright 2014 Dario Bonino
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
  protected ExecutorService attributeNotificationWorker;

  // -------------------------------------------------
  // Parameters defined by this EEP, which
  // might change depending on the network
  // activity.
  // --------------------------------------------------

  // --------------------------------------------------

  /**
  * The class constructor
  */
  public A507(String version)
  {
    // call the superclass constructor
    super(version);

    // build the attribute dispatching worker
    this.attributeNotificationWorker = Executors.newFixedThreadPool(1);
  }
}
```
The A5-07-01 implementation class, conversely, is reported below and follows as simple schema with two required methods to be implemented: a ```getEEPIdentifier``` method for getting the exact identifier of the specific EEP profile implemented by the class and a ```handleProfileUpdate``` for updateing the last snapshot of EEP attribute values as reported from the low level layers connected to the EnOcean network.

```
/*
* EnJ - EnOcean Java API
*
* Copyright 2014  Dario Bonino
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

import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.EEPAttributeChangeDispatcher;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26PIRStatus;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26SupplyVoltage;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26SupplyVoltageAvailability;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

import java.io.Serializable;
import java.util.ArrayList;

/**
* @author bonino
*
*/
public class A50701 extends A507 implements Serializable
{
  /**
  * class version number for serialization / de-serialization
  */
  private static final long serialVersionUID = 1L;

  // the type definition
  public static final byte type = (byte) 0x01;

  /**
  * @param version
  */
  public A50701()
  {

    super("2.6");

    // add attributes,
    this.addChannelAttribute(1, new EEP26SupplyVoltage(0.0,5.0));
    this.addChannelAttribute(1, new EEP26SupplyVoltageAvailability());
    this.addChannelAttribute(1, new EEP26PIRStatus());
  }

  /*
  * (non-Javadoc)
  *
  * @see it.polito.elite.enocean.enj.eep.EEP#getEEPIdentifier()
  */
  @Override
  public EEPIdentifier getEEPIdentifier()
  {
    return new EEPIdentifier(A507.rorg, A507.func, A50701.type);
  }

  /*
  * (non-Javadoc)
  *
  * @see
  * it.polito.elite.enocean.enj.eep.EEP#handleProfileUpdate(it.polito.elite
    * .enocean.enj.eep.eep26.telegram.EEP26Telegram)
    */
    @Override
    public boolean handleProfileUpdate(EEP26Telegram telegram)
    {
      boolean success = false;
      // handle the telegram, as first cast it at the right type (or fail)
      if (telegram.getTelegramType() == EEP26TelegramType.FourBS)
      {
        // cast the telegram to handle to its real type
        FourBSTelegram profileUpdate = (FourBSTelegram) telegram;

        // get the packet payload
        byte[] payload = profileUpdate.getPayload();

        //parse the telegram as an A50701 message
        A50701OccupancySensingMessage message = new A50701OccupancySensingMessage(payload);

        //check if its valid
        if(message.isValid())
        {
          // prepare the list of changed attributes (only one)
          ArrayList<EEPAttribute<?>> changedAttributes = new ArrayList<EEPAttribute<?>>();

          //------- get the attributes

          // supply voltage
          EEP26SupplyVoltage supplyVoltage = (EEP26SupplyVoltage)this.getChannelAttribute(1, EEP26SupplyVoltage.NAME);

          // supply voltage availability
          EEP26SupplyVoltageAvailability supplyVoltageAvailability = (EEP26SupplyVoltageAvailability)this.getChannelAttribute(1, EEP26SupplyVoltageAvailability.NAME);

          // occupancy status
          EEP26PIRStatus pirStatus = (EEP26PIRStatus)this.getChannelAttribute(1, EEP26PIRStatus.NAME);

          //set the attribute values
          if(supplyVoltageAvailability!=null)
          {
            //set the availability value
            supplyVoltageAvailability.setValue(message.isSupplyVoltageAvailable());

            //update the list of changed attributes
            changedAttributes.add(supplyVoltageAvailability);

            // if the supply voltage attribute exists and a valid value had been specified in the message
            if((message.isSupplyVoltageAvailable())&&(supplyVoltage!=null))
            {
              //store the voltage value
              supplyVoltage.setRawValue(message.getSupplyVoltage());

              //update the list of changed attributes
              changedAttributes.add(supplyVoltage);
            }
          }

          // set the pir status if the corresponding attribute is available
          if(pirStatus!=null)
          {
            //set the pir status value
            pirStatus.setValue(message.isMotionDetected());

            //update the list of changed attributes
            changedAttributes.add(pirStatus);
          }


          //if some attribute changed, notify it to listeners
          if(!changedAttributes.isEmpty())
          {
            // build the dispatching task
            EEPAttributeChangeDispatcher dispatcherTask = new EEPAttributeChangeDispatcher(
              changedAttributes, 1);

              // submit the task for execution
              this.attributeNotificationWorker.submit(dispatcherTask);

              // set success at true
              // TODO check what to do if nothing changes, i.e., with success
              // equal to false.
              success = true;
            }
          }
        }

        return success;
      }

    }
  ```
It esy to notice that "values" corresponding to the current device conditions (states) are managed through so-called attributes (stemming from the abstract class ```EEPAttribute<T>```). Generics are widely used in the library to enable customization of abstract datatypes.

Referring to the above code sample, the attribute class representing the current supply voltage for an occupancy sensor with the A5-07-01 profile is the following.

```
/*
* EnJ - EnOcean Java API
*
* Copyright 2014 Dario Bonino
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
package it.polito.elite.enocean.enj.eep.eep26.attributes;

import java.nio.ByteBuffer;

import it.polito.elite.enocean.enj.eep.EEPAttribute;

/**
* @author bonino
*
*/
public class EEP26SupplyVoltage extends EEPAttribute<Double>
{
  // the EEPFunction name
  public static final String NAME = "SupplyVoltage";

  // the allowed range
  private double minV;
  private double maxV;

  /**
  * @param name
  */
  public EEP26SupplyVoltage()
  {
    super(EEP26SupplyVoltage.NAME);

    // set the default value
    this.value = 0.0;
    this.unit = "V";
    this.minV = 0;
    this.maxV = 5;
  }

  public EEP26SupplyVoltage(Double value, String unit)
  {
    super(EEP26SupplyVoltage.NAME);

    if ((unit != null)

    && (!unit.isEmpty())
    && ((unit.equalsIgnoreCase("Volt") || unit
    .equalsIgnoreCase("V"))))
    {
      // store the value
      this.value = value;

      // store the unit
      this.unit = unit;

      // set the maximum range
      this.minV = 0.0;
      this.maxV = 5.0;
    }

    else
    {
      throw new NumberFormatException(
        "Wrong unit or null value for supply voltage in Volt (V)");
      }

    }

    public EEP26SupplyVoltage(Double minV, Double maxV)
    {
      super(EEP26SupplyVoltage.NAME);

      // default value 0V
      this.value = 0.0;
      this.unit = "V";
      this.minV = minV;
      this.maxV = maxV;
    }


    /**
    * @return the minV
    */
    public double getMinV()
    {
      return minV;
    }

    /**
    * @param minV the minV to set
    */
    public void setMinV(double minV)
    {
      this.minV = minV;
    }

    /**
    * @return the maxV
    */
    public double getMaxV()
    {
      return maxV;
    }

    /**
    * @param maxV the maxV to set
    */
    public void setMaxV(double maxV)
    {
      this.maxV = maxV;
    }

    /*
    * @see it.polito.elite.enocean.enj.eep.EEPAttribute#setValue
    */
    @Override
    public boolean setValue(Double value)
    {
      boolean stored = false;

      if (value instanceof Number)
      {
        // store the current value
        this.value = value;

        // updated the operation result
        stored = true;
      }

      return stored;
    }

    public void setRawValue(int value)
    {
      // perform scaling (value should be between 0 and 250 included
        if ((value >= 0) && (value <= 250))
        this.value = ((this.maxV - this.minV) * ((double) value) / 250.0)
        + this.minV;
      }

      /*
      * @see it.polito.elite.enocean.enj.eep.EEPAttribute#setUnit
      */
      @Override
      public boolean setUnit(String unit)
      {
        boolean stored = false;

        if ((unit != null)

        && (!unit.isEmpty())
        && ((unit.equalsIgnoreCase("Volt") || unit
        .equalsIgnoreCase("V"))))
        {

          // store the unit
          this.unit = unit;

          // set the stored flag at true
          stored = true;
        }

        return stored;
      }

      /*
      * (non-Javadoc)
      *
      * @see it.polito.elite.enocean.enj.eep.EEPAttribute#byteValue()
      */
      @Override
      public byte[] byteValue()
      {
        // it is likely to never be used...

        // use byte buffers to ease double encoding / decoding

        // a byte buffer wrapping an array of 4 bytes
        ByteBuffer valueAsBytes = ByteBuffer.wrap(new byte[4]);

        // store the current value
        valueAsBytes.putDouble(this.value);

        // return the value as byte array
        return valueAsBytes.array();
      }

      /**
      * Checks if the current attribute represents a value in the declared valid
      * range or not.
      *
      * @return
      */
      public boolean isValid()
      {
        return ((this.value >= this.minV) && (this.value <= this.maxV));
      }

    }
  ```
