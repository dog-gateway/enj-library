package it.polito.elite.enocean.enj.eep;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

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


		Set<Class<? extends EEP>> allEEPs = this.getStaticEEPs();
		
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
	
	/**
	 * This should not be used, unless in OSGi where reflections does not work.
	 * TODO: either apply the reflections fix at http://w3facility.org/question/reflections-library-not-working-when-used-in-an-eclipse-plug-in/ or
	 * define a better way for registering eeps
	 * @return
	 */
	private Set<Class<? extends EEP>> getStaticEEPs()
	{
		HashSet<Class<? extends EEP>> eeps = new HashSet<Class<? extends EEP>>();
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50201.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50202.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50203.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50204.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50205.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50206.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50207.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50208.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50209.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A5020A.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A5020B.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50210.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50211.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50212.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50213.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50214.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50215.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50216.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50217.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50218.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50219.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A5021A.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A5021B.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50220.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A502.A50230.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A504.A50401.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.A5.A507.A50701.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.D2.D201.D20108.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.D2.D201.D20109.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.D2.D201.D2010A.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.D5.D500.D50001.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.F6.F602.F60201.class);
		eeps.add(it.polito.elite.enocean.enj.eep.eep26.F6.F602.F60202.class);
		
		return eeps;
	}
}
