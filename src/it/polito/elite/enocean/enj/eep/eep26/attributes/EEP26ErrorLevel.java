package it.polito.elite.enocean.enj.eep.eep26.attributes;

import it.polito.elite.enocean.enj.eep.EEPAttribute;
import it.polito.elite.enocean.enj.eep.eep26.D2.D201.D201ErrorLevel;

public class EEP26ErrorLevel extends EEPAttribute<D201ErrorLevel>
{
	// the EEPFunction name
	public static final String NAME = "ErrorLevel";

	public EEP26ErrorLevel()
	{
		super(EEP26ErrorLevel.NAME);

		// default no error
		this.value = D201ErrorLevel.HARDWARE_OK;
	}

	public EEP26ErrorLevel(D201ErrorLevel errorLevel)
	{
		// call the super class method
		super(EEP26ErrorLevel.NAME);

		// store the given error level
		this.value = errorLevel;
	}

	@Override
	public byte[] byteValue()
	{
		// return the byte representation of the current error level
		return new byte[] { ((D201ErrorLevel) this.value).getCode() };
	}

}
