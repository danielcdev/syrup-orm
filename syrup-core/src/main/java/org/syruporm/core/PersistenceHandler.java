package org.syruporm.core;

import java.io.InputStream;
import java.io.OutputStream;

public interface PersistenceHandler {

	public OutputStream save(Class<? extends Object> myClass);

	public InputStream load(Class<? extends Object> myClass);

	public Boolean delete(Class<? extends Object> myClass);
}
