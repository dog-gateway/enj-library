/**
 * 
 */
package it.polito.elite.enocean.enj.EEP2_5.receiveEvent;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public interface ReceivedDataEventListener {
	public void handleReceiveData(ReceiveDataEvent event);
}
