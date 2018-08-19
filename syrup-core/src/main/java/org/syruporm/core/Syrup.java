package org.syruporm.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Persists an object using Jackson and Properties
 * 
 * <p>
 * Marshals an object to JSON using Jackson, digests the annotations to act
 * accordingly in the implementation of the AnnotationHandlers, then stores it
 * in a persistent (or not) medium according to the implementation of the
 * PersistenceHandler
 * </p>
 * 
 * <p>
 * Retrieval is the exact reversal of storage
 * </p>
 * 
 * @since 0.1.0
 */
public class Syrup {

	private Class<? extends Object> myClass;
	private PersistenceHandler persistenceHandler;
	private AnnotationDigester annotationDigester;
	private ObjectMapper objectMapper = new ObjectMapper();
	private Properties properties = new Properties();

	/**
	 * Finds the specific object by the provided id, relative to the Properties
	 * id
	 * 
	 * @param id
	 *            Specific Properties id of the object wished to be retrieved
	 * @return The requested object, or null if not found
	 * @since 0.1.0
	 */
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

	/**
	 * Finds the specific object by the name of a field within the class, found
	 * by the provided comparator
	 * 
	 * @param fieldName
	 *            The name of the field in the class to be searched
	 * @param comparator
	 *            The object of which to compare the value of the specified
	 *            field
	 * @return The requested object, or null if not found
	 * @since 0.1.0
	 */
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

	/**
	 * Modifies the object according to the AnnotationHandlers, then persists
	 * the object according to the PersistenceHandler
	 * 
	 * <p>
	 * Only saves new objects
	 * </p>
	 * 
	 * @param object
	 *            The object to be modified and persisted
	 * @return True or false relative to the success of the operation
	 * @see AnnotationHandler
	 * @see PersistenceHandler
	 * @since 0.1.0
	 */
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

	/**
	 * Modifies the object according to the AnnotationHandlers, then persists
	 * the object according to the PersistenceHandler
	 * 
	 * <p>
	 * Updates existing objects only
	 * </p>
	 * 
	 * @param object
	 *            The object to be modified and persisted
	 * @return True or false relative to the success of the operation
	 * @see AnnotationHandler
	 * @see PersistenceHandler
	 * @since 0.1.0
	 */
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

	/**
	 * Modifies the object according to the AnnotationHandlers, then deletes the
	 * persisted object according to the PersistenceHandler
	 * 
	 * @param object
	 *            The object to be modified and deleted
	 * @return True or false relative to the success of the operation
	 * @see AnnotationHandler
	 * @see PersistenceHandler
	 * @since 0.2.0
	 */
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

	/**
	 * Destroys the persistence medium relative to Syrup class as dictated by
	 * the PersistenceHandler
	 * 
	 * @return True or false relative to the success of the operation
	 * @see PersistenceHandler
	 * @since 0.1.0
	 */
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
	 * Provides an instance of Syrup to handle the provided class to persist in
	 * a manner dictated by the PersistenceHandler
	 * 
	 * @param myClass
	 *            The class to be persisted
	 * @param persistenceHandler
	 *            PersistenceHandler to dictate method of persistence
	 * @throws Exception
	 * @see PersistenceHandler
	 * @since 0.1.0
	 */
	protected Syrup(Class<? extends Object> myClass, PersistenceHandler persistenceHandler) throws Exception {
		this.myClass = myClass;
		this.persistenceHandler = persistenceHandler;

		loadProperties();
		annotationDigester = new AnnotationDigester(myClass);
	}
}
