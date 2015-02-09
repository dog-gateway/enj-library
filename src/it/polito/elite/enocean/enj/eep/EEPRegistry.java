package it.polito.elite.enocean.enj.eep;

import java.util.Hashtable;

public class EEPRegistry
{
	private static EEPRegistry theInstance;

	// the set of supported profiles
	Hashtable<EEPIdentifier, Class<? extends EEP>> supportedProfiles;

	// singleton pattern
	private EEPRegistry()
	{
		// build the profiles hashtable
		this.supportedProfiles = new Hashtable<>();
	}

	public static EEPRegistry getInstance()
	{
		if (EEPRegistry.theInstance == null)
			EEPRegistry.theInstance = new EEPRegistry();

		return EEPRegistry.theInstance;
	}

	// static method for checking if the given profile is supported by the
	// current EnJ api.
	public boolean isEEPSupported(EEPIdentifier eep)
	{
		return this.supportedProfiles.containsKey(eep);
	}
	
	public void addProfile(EEPIdentifier profileId, Class<? extends EEP> profile)
	{
		if((profileId!=null)&&(profile!=null))
			this.supportedProfiles.put(profileId, profile);
	}

	/**
	 * Returns an EEP class given the corresponding EEP identifier
	 * @param eepId The EEP identifier.
	 * @return The EEP class.
	 */
	public Class<? extends EEP> getEEP(EEPIdentifier eepId)
	{
		return this.supportedProfiles.get(eepId);
	}
}
