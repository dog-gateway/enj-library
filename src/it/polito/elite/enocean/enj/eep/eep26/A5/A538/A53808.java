package it.polito.elite.enocean.enj.eep.eep26.A5.A538;

import it.polito.elite.enocean.enj.communication.EnJConnection;
import it.polito.elite.enocean.enj.eep.EEPIdentifier;
import it.polito.elite.enocean.enj.eep.eep26.attributes.EEP26DimLevel;


/**
 * A class representing the A5-38-08 EnOcean Equipment Profile.
 * (Gateway control)
 *
 * @author <a href="mailto:kickass_kemmler@gmx.de">Jan Kemmler</a>
 *
 */
public class A53808 extends A538 {

    // the type definition
    public static final byte type = (byte) 0x08;

    // the used channel
    public static int CHANNEL = 0;

    // the byte identifier for all output channels
    public static byte ALL_OUTPUT_CHANNEL = 0x1e;

    /**
     *
     */
    public A53808() {
        super();

        this.addChannelAttribute(A53808.CHANNEL, new EEP26DimLevel());
    }

    public void dimming(EnJConnection connection, byte[] deviceAddress, int dimValue, int dimTime, boolean isTeachIn,
            boolean storeValue, boolean switchingCommand) {
        // check limits
        if (dimValue < 0) {
            dimValue = 0;
        } else if (dimValue > 100) {
            dimValue = 100;
        }

        if (dimTime < 0) {
            dimTime = 0;
        } else if (dimTime > 255) {
            dimTime = 255;
        }

        byte teachInByte = (byte) ((isTeachIn) ? 0 : 1);

        byte storeValueByte = (byte) ((storeValue) ? 1 : 0);

        byte switchingCommandByte = (byte) ((switchingCommand) ? 1 : 0);

        // prepare the data payload to host "desired" values
        byte dataByte[] = new byte[5];

        // add the packet rorg
        dataByte[0] = A538.rorg.getRorgValue();

        // CMD code (0x01), the first 4 bits are not used
        dataByte[1] = (byte) 0x02;

        // Dim value
        dataByte[2] = (byte) dimValue;

        // Dim time
        dataByte[3] = (byte) dimTime;

        // teachin boolean: bit 3, dimming range: bit 2, store value boolean: bit 1, switching command boolean: bit 0
        dataByte[4] = (byte) ((teachInByte << 3) + (0 << 2) + (storeValueByte << 1) + switchingCommandByte);

        // send the payload for connection-layer encapsulation
        connection.sendRadioCommand(deviceAddress, dataByte);

    }

    public void switching(EnJConnection connection, byte[] deviceAddress, boolean switchOn, boolean isTeachIn) {

        byte teachInByte = (byte) ((isTeachIn) ? 0 : 1);

        byte switchingCommandByte = (byte) ((switchOn) ? 1 : 0);

        // prepare the data payload to host "desired" values
        byte dataByte[] = new byte[4];

        // add the packet rorg
        dataByte[0] = A538.rorg.getRorgValue();

        // CMD code (0x01), the first 4 bits are not used
        dataByte[1] = (byte) 0x01;

        // Time
        dataByte[2] = (byte) 0x00;

        // Dim value
        dataByte[3] = (byte) ((teachInByte << 3) + switchingCommandByte);

        // send the payload for connection-layer encapsulation
        connection.sendRadioCommand(deviceAddress, dataByte);

    }

    /*
     * (non-Javadoc)
     *
     * @see it.polito.elite.enocean.enj.eep.EEP#getEEPIdentifier()
     */
    @Override
    public EEPIdentifier getEEPIdentifier() {
        // return the EEPIdentifier for this profile
        return new EEPIdentifier(A538.rorg, A538.func, A53808.type);

    }
}
