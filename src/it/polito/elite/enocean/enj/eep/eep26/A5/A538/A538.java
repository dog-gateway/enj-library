package it.polito.elite.enocean.enj.eep.eep26.A5.A538;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polito.elite.enocean.enj.eep.EEP;
import it.polito.elite.enocean.enj.eep.Rorg;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26Telegram;
import it.polito.elite.enocean.enj.eep.eep26.telegram.EEP26TelegramType;
import it.polito.elite.enocean.enj.eep.eep26.telegram.FourBSTelegram;

/**
 * A class representing the A5-38 family of EnOcean Equipment Profiles
 * (Central Command).
 *
 * @author <a href="mailto:kickass_kemmler@gmx.de">Jan Kemmler</a>
 *
 */
public abstract class A538 extends EEP {
    // the EEP26 definition, according to the EEP26 specification
    public static final Rorg rorg = new Rorg((byte) 0xa5);
    public static final byte func = (byte) 0x38;

    // func must be defined by extending classes

    // Executor Thread Pool for handling attribute updates
    protected volatile ExecutorService attributeNotificationWorker;


    /**
     * The class constructor
     */
    public A538() {
        // call the superclass constructor
        super("2.6");

        // build the attribute dispatching worker
        this.attributeNotificationWorker = Executors.newFixedThreadPool(1);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * it.polito.elite.enocean.enj.eep.EEP#handleProfileUpdate(it.polito.elite
     * .enocean.enj.eep.eep26.telegram.EEP26Telegram)
     */
    @Override
    public boolean handleProfileUpdate(EEP26Telegram telegram) {
        boolean success = true;
        return success;
    }

}
