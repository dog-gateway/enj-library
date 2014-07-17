package it.polito.elite.enocean.enj.EEP26;

import java.io.Serializable;

public class EEPIdentifier implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param rorg
	 * @param function
	 * @param type
	 */
	public EEPIdentifier(Rorg rorg, byte function, byte type)
	{
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
	public Rorg getRorg()
	{
		return rorg;
	}

	/**
	 * @param rorg
	 *            the rorg to set
	 */
	public void setRorg(Rorg rorg)
	{
		this.rorg = rorg;
	}

	/**
	 * @return the function
	 */
	public byte getFunction()
	{
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(byte function)
	{
		this.function = function;
	}

	/**
	 * @return the type
	 */
	public byte getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(byte type)
	{
		this.type = type;
	}

	public static EEPIdentifier parse(String eepIdentifierAsString)
	{
		EEPIdentifier identifier = null;
		// check the right lenght
		if ((eepIdentifierAsString.length() == 6)
				|| (eepIdentifierAsString.length() == 4))
		{
			// parses the eep identifier expressed according to the EEP
			// specification, i.e., rrfftt
			byte rorg = Byte.parseByte("0x"
					+ eepIdentifierAsString.substring(0, 2));
			byte func = Byte.parseByte("0x"
					+ eepIdentifierAsString.substring(2, 4));
			
			// TODO handle higher EEP e.g. D201
			byte type = (byte)0xff;
			if (eepIdentifierAsString.length() == 6)
			{
				type = Byte.parseByte("0x"
						+ eepIdentifierAsString.substring(4, 6));
			}
			
			identifier  = new EEPIdentifier(new Rorg(rorg), func, type);
		}
		
		return identifier;
	}
}
