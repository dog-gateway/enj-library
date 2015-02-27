package it.polito.elite.enocean.enj.eep;

import java.io.Serializable;

public class Rorg implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param rorgValue
	 */
	public Rorg(byte rorgValue)
	{
		super();
		this.rorgValue = rorgValue;
	}

	public static byte RPS = (byte) 0xF6;
	public static byte BS1 = (byte) 0xD5;
	public static byte BS4 = (byte) 0xA5;
	public static byte VLD = (byte) 0xD2;
	public static byte MSC = (byte) 0xF1;
	public static byte ADT = (byte) 0xA6;
	public static byte SM_LRN_REQ = (byte) 0xC6;
	public static byte SM_LRN_ANS = (byte) 0xC7;
	public static byte SM_LRN_REC = (byte) 0xA7;
	public static byte SYS_EX = (byte) 0xA6;
	public static byte SEC = (byte) 0x30;
	public static byte SEC_ENCAPS = (byte) 0x31;
	public static byte UTE = (byte) 0xD4;

	byte rorgValue;

	/**
	 * @return the rorgValue
	 */
	public byte getRorgValue()
	{
		return rorgValue;
	}

	/**
	 * @param rorg
	 *            the rorgValue to set
	 */
	public void setRorgValue(byte rorg)
	{
		this.rorgValue = rorg;
	}

	public boolean isRps()
	{
		return this.rorgValue == RPS;
	}

	public boolean isBS1()
	{
		return this.rorgValue == BS1;
	}

	public boolean isBS4()
	{
		return this.rorgValue == BS4;
	}

	public boolean isVld()
	{
		return this.rorgValue == VLD;
	}

	public boolean isMsc()
	{
		return this.rorgValue == MSC;
	}

	public boolean isAdt()
	{
		return this.rorgValue == ADT;
	}

	public boolean isSmLrnReq()
	{
		return this.rorgValue == SM_LRN_REQ;
	}

	public boolean isSmLrnAns()
	{
		return this.rorgValue == SM_LRN_ANS;
	}

	public boolean isSmLrnRec()
	{
		return this.rorgValue == SM_LRN_REC;
	}

	public boolean isSysEx()
	{
		return this.rorgValue == SYS_EX;
	}

	public boolean isSec()
	{
		return this.rorgValue == SEC;
	}

	public boolean isSecEncaps()
	{
		return this.rorgValue == SEC_ENCAPS;
	}

	public boolean isUTE()
	{
		return this.rorgValue == Rorg.UTE;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + rorgValue;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rorg other = (Rorg) obj;
		if (rorgValue != other.rorgValue)
			return false;
		return true;
	}
	
	
}
