package org.syruporm.core;

import java.util.HashMap;
import java.util.Map;

import org.syruporm.persistencehandler.LocalFileHandler;

public class SyrupFactory {

	private Map<Class<? extends Object>, Syrup> pantry = new HashMap<>();
	private PersistenceHandler persistenceHandler = new LocalFileHandler();

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

	public SyrupFactory() {

	}

	/**
	 * @param persistenceHandler
	 */
	public SyrupFactory(PersistenceHandler persistenceHandler) {
		this.persistenceHandler = persistenceHandler;
	}

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