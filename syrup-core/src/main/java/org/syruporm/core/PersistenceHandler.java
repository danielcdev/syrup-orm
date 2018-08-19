package org.syruporm.core;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides an extensible way to persist data in any manner desired
 * 
 * @since 1.0.0
 */
public interface PersistenceHandler {

	/**
	 * Method to be called when the data is to be persisted
	 * 
	 * @param myClass Class to be persisted
	 * @since 1.0.0
	 */
	public OutputStream save(Class<? extends Object> myClass);

	/**
	 * Method to be called when data is to be loaded from a persistence contrivance
	 * 
	 * @param myClass Class to be persisted
	 * @since 1.0.0
	 */
	public InputStream load(Class<? extends Object> myClass);

	/**
	 * Method to be called when the data in it's entirety should be removed from its
	 * persistent state
	 * 
	 * @param myClass Class to be persisted
	 * @since 1.0.0
	 */
	public Boolean delete(Class<? extends Object> myClass);
}
