package org.syruporm.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Syrup {

	private Class<? extends Object> myClass;
	private PersistenceHandler persistenceHandler;
	private AnnotationDigester annotationDigester;
	private ObjectMapper objectMapper = new ObjectMapper();
	private Properties properties = new Properties();

	public Object getById(String id) {
		Object myReturn = null;

		try {
			String value = (String) properties.getProperty(id);

			if (value != null && !value.isEmpty()) {
				myReturn = objectMapper.readValue(value, myClass);

				annotationDigester.get(new ObjectMetadata(myReturn, properties, null, null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return myReturn;
	}

	public Object getByField(String fieldName, Object comparator) {
		Object myReturn = null;

		try {
			Set<Object> entries = properties.keySet();

			for (Object key : entries) {
				Object persistedObject = getById(key.toString());

				Field thisField = myClass.getDeclaredField(fieldName);
				thisField.setAccessible(true);

				if (thisField.get(persistedObject).equals(comparator)) {
					myReturn = persistedObject;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return myReturn;
	}

	public Boolean save(Object object) {
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata(object, properties, null, null);
			annotationDigester.save(objectMetadata);

			properties.put(objectMetadata.getId(), objectMapper.writeValueAsString(object));
			saveProperties();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Boolean update(Object object) {
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata(object, properties, null, null);
			annotationDigester.update(objectMetadata);

			properties.put(objectMetadata.getId(), objectMapper.writeValueAsString(object));
			saveProperties();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Boolean delete(Object object) {
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata(object, properties, null, null);
			annotationDigester.delete(objectMetadata);

			properties.remove(objectMetadata.getId());
			saveProperties();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	protected Boolean deleteFile() {
		return persistenceHandler.delete(myClass);
	}

	private void loadProperties() throws Exception {
		InputStream inputStream = persistenceHandler.load(myClass);

		if (inputStream == null)
			throw new Exception("InputStream for " + myClass.getName() + " is null");

		properties.load(inputStream);
		inputStream.close();
	}

	private void saveProperties() throws Exception {
		OutputStream outputStream = persistenceHandler.save(myClass);

		if (outputStream == null)
			throw new Exception("OutputStream for " + myClass.getName() + " is null");

		properties.store(outputStream, "syrup-orm");
		outputStream.close();
	}

	/**
	 * @param myClass
	 * @param persistenceHandler
	 * @throws Exception
	 */
	protected Syrup(Class<? extends Object> myClass, PersistenceHandler persistenceHandler) throws Exception {
		this.myClass = myClass;
		this.persistenceHandler = persistenceHandler;

		loadProperties();
		annotationDigester = new AnnotationDigester(myClass);
	}
}
