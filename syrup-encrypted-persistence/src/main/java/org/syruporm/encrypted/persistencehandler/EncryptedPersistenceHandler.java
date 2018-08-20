package org.syruporm.encrypted.persistencehandler;

import java.io.InputStream;
import java.io.OutputStream;

import org.syruporm.core.PersistenceHandler;

public class EncryptedPersistenceHandler implements PersistenceHandler {

	private PersistenceHandler actualPersistenceHandler;

	@Override
	public OutputStream save(Class<? extends Object> myClass) {
		EncryptedOutputStreamInterceptor outputStreamInterceptor = new EncryptedOutputStreamInterceptor(
				actualPersistenceHandler, myClass);

		return outputStreamInterceptor;
	}

	@Override
	public InputStream load(Class<? extends Object> myClass) {
		DecryptedInputStreamInterceptor inputStreamInterceptor = new DecryptedInputStreamInterceptor(
				actualPersistenceHandler, myClass);

		return inputStreamInterceptor;
	}

	@Override
	public Boolean delete(Class<? extends Object> myClass) {
		return actualPersistenceHandler.delete(myClass);
	}

	/**
	 * @param actualPersistenceHandler
	 */
	public EncryptedPersistenceHandler(PersistenceHandler actualPersistenceHandler) {
		this.actualPersistenceHandler = actualPersistenceHandler;
	}
}