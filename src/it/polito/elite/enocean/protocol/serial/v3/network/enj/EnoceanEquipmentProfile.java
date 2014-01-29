package it.polito.elite.enocean.protocol.serial.v3.network.enj;

public class EnoceanEquipmentProfile {
	/**
	 * Constructor
	 * 
	 * @param rorg
	 * @param function
	 * @param type
	 */
	public EnoceanEquipmentProfile(Rorg rorg, byte function, byte type) {
		super();
		this.rorg = rorg;
		this.function = function;
		this.type = type;
	}

	// Identify the Radio-Telegram organization
	Rorg rorg;
	
	// Funcion of the device
	byte function;
	
	// Type of device
	byte type;
	
	/**
	 * @return the rorg
	 */
	public Rorg getRorg() {
		return rorg;
	}
	/**
	 * @param rorg the rorg to set
	 */
	public void setRorg(Rorg rorg) {
		this.rorg = rorg;
	}
	/**
	 * @return the function
	 */
	public byte getFunction() {
		return function;
	}
	/**
	 * @param function the function to set
	 */
	public void setFunction(byte function) {
		this.function = function;
	}
	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}
}
