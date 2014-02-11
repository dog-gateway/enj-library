/**
 * 
 */
package it.polito.elite.enocean.enj.EEP2_5.primitives;

import java.io.Serializable;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Device implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	/**
	 * @param eep
	 * @param address
	 * @param manufacturerId
	 */

	public Device(){
	}

	public Device(EnoceanEquipmentProfile eep, byte[] address,
			byte[] manufacturerId) {
		super();
		this.eep = eep;
		this.address = address;
		this.manufacturerId = manufacturerId;
	}

	EnoceanEquipmentProfile eep;
	byte[] address = new byte[4];
	byte[] manufacturerId = new byte[3];

	/**
	 * @return the deviceRorg
	 */
	public EnoceanEquipmentProfile getEEP() {
		return eep;
	}
	/**
	 * @param deviceRorg the deviceRorg to set
	 */
	public void setEEP(EnoceanEquipmentProfile eep) {
		this.eep = eep;
	}
	/**
	 * @return the address
	 */
	public byte[] getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(byte[] address) {
		this.address = address;
	}
	/**
	 * @return the manufacturerId
	 */
	public byte[] getManufacturerId() {
		return manufacturerId;
	}
	/**
	 * @param manufacturerId the manufacturerId to set
	 */
	public void setManufacturerId(byte[] manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
}