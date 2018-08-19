package org.syruporm.core;

import java.util.HashMap;
import java.util.Map;

import org.syruporm.persistencehandler.LocalFileHandler;

/**
 * Instantiates and provides the correct instance of Syrup for a given
 * persistent class
 * 
 * @since 0.1.0
 */
public class SyrupFactory {

	private Map<Class<? extends Object>, Syrup> pantry = new HashMap<>();
	private PersistenceHandler persistenceHandler = new LocalFileHandler();

	/**
	 * Checks to see if an instance of Syrup exists for the specified class, if
	 * not it creates one.
	 * 
	 * @param thisClass
	 *            The class to be persisted
	 * @return Instance of Syrup that will handle the specified class
	 * @see Syrup
	 * @since 0.1.0
	 */
	public Syrup getSyrup(Class<? extends Object> thisClass) {
		try {
			if (!pantry.containsKey(thisClass))
				pantry.put(thisClass, new Syrup(thisClass, persistenceHandler));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return pantry.get(thisClass);
	}

	/**
	 * Resets the actual file and/or method of persistence, then provides a new
	 * instance of Syrup for the specified class
	 * 
	 * @param thisClass
	 *            The associated class to be reset
	 * @return True or false in relation to the success of the operation
	 * @since 0.1.0
	 */
	public Boolean resetSyrup(Class<? extends Object> thisClass) {
		try {
			Syrup thisSyrup = getSyrup(thisClass);

			thisSyrup.deleteFile();
			pantry.put(thisClass, new Syrup(thisClass, persistenceHandler));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Provides a factory with the default PersistenceHandler
	 * 
	 * @see PersistenceHandler
	 * @see LocalFileHandler
	 * @since 0.1.0
	 */
	public SyrupFactory() {

	}

	/**
	 * Instantiates a SyrupFactory with the specified PersistanceHandler
	 * 
	 * @param persistenceHandler
	 *            The implementation of PersistenceHandler for the factory to
	 *            leverage
	 * @see PersistenceHandler
	 * @since 1.0.0
	 */
	public SyrupFactory(PersistenceHandler persistenceHandler) {
		this.persistenceHandler = persistenceHandler;
	}

	/**
	 * Specify an implementation of a PersistenceHandler for the factory to
	 * leverage
	 * 
	 * @param persistenceHandler
	 *            Implementation of PersistenceHandler
	 * @return Instance of SyrupFactory with specified options
	 * @see PersistenceHandler
	 * @since 1.0.0
	 */
	public SyrupFactory withPersistenceHandler(PersistenceHandler persistenceHandler) {
		this.persistenceHandler = persistenceHandler;

		return this;
	}

	/**
	 * @return the persistenceHandler
	 */
	public PersistenceHandler getPersistenceHandler() {
		return persistenceHandler;
	}

	/**
	 * @param persistenceHandler
	 *            the persistenceHandler to set
	 */
	public void setPersistenceHandler(PersistenceHandler persistenceHandler) {
		this.persistenceHandler = persistenceHandler;
	}
}