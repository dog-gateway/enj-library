package it.polito.elite.enocean.enj.eep;

import java.util.Hashtable;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

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

		// load all the EEP class specifications
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(
						ClasspathHelper
								.forPackage("it.polito.elite.enocean.enj.eep"))
				.setScanners(new SubTypesScanner()));

		Set<Class<? extends EEP>> allEEPs =reflections.getSubTypesOf(it.polito.elite.enocean.enj.eep.EEP.class);
		
		for(Class<? extends EEP> eep: allEEPs)
			try
			{
				this.supportedProfiles.put(eep.newInstance().getEEPIdentifier(),eep);
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	}

	public synchronized static EEPRegistry getInstance()
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
		if ((profileId != null) && (profile != null))
			this.supportedProfiles.put(profileId, profile);
	}

	/**
	 * Returns an EEP class given the corresponding EEP identifier
	 * 
	 * @param eepId
	 *            The EEP identifier.
	 * @return The EEP class.
	 */
	public Class<? extends EEP> getEEP(EEPIdentifier eepId)
	{
		return this.supportedProfiles.get(eepId);
	}
}
