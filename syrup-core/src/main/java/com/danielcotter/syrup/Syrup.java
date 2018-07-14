package com.danielcotter.syrup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Syrup {

	private Class<? extends Object> myClass;
	private ObjectMapper objectMapper = new ObjectMapper();
	private Properties properties = new Properties();
	private String filepath;
	private Field idField;

	public Object getById(String id) {
		Object myReturn = null;

		try {
			String value = (String) properties.getProperty(id);

			if (value != null && !value.isEmpty())
				myReturn = objectMapper.readValue(value, myClass);
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
			String propertiesId = new Integer(properties.keySet().size()).toString();

			Field thisIdField = myClass.getDeclaredField(idField.getName());
			thisIdField.setAccessible(true);

			Object value = thisIdField.get(object);

			if (value == null) {
				switch (thisIdField.getType().getSimpleName()) {
				case "String":
					thisIdField.set(object, propertiesId);
					break;

				case "Integer":
					thisIdField.set(object, new Integer(propertiesId));
					break;

				case "Long":
					thisIdField.set(object, new Long(propertiesId));
					break;

				case "Double":
					thisIdField.set(object, new Double(propertiesId));
					break;

				case "Float":
					thisIdField.set(object, new Float(propertiesId));
					break;
				}
			} else if (properties.get(value.toString()) != null) {
				throw new Exception("Duplicate id");
			} else {
				propertiesId = value.toString();
			}

			properties.put(propertiesId, objectMapper.writeValueAsString(object));

			saveProperties();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Boolean update(Object object) {
		try {
			Field thisIdField = object.getClass().getDeclaredField(idField.getName());
			thisIdField.setAccessible(true);

			Object value = thisIdField.get(object);

			if (properties.get(value.toString()) == null)
				throw new Exception("Id does not exist");

			properties.put(value.toString(), objectMapper.writeValueAsString(object));

			saveProperties();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	protected Boolean deleteFile() {
		try {
			File file = new File(filepath);

			if (file.exists())
				return file.delete();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private Field findAnnotation(Class<? extends Annotation> needle) {
		Field myField = null;

		try {
			for (Field thisField : myClass.getDeclaredFields())
				if (thisField.isAnnotationPresent(needle)) {
					myField = thisField;
					break;
				}

			if (myField == null)
				throw new Exception("@Id field is missing");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return myField;
	}

	private void loadProperties() throws Exception {
		InputStream inputStream = new FileInputStream(filepath);

		properties.load(inputStream);
		inputStream.close();
	}

	private void saveProperties() throws Exception {
		OutputStream outputStream = new FileOutputStream(filepath);

		properties.store(outputStream, "syrup-orm");
		outputStream.close();
	}

	/**
	 * @param myClass
	 * @param directory
	 * @throws Exception
	 */
	public Syrup(Class<? extends Object> myClass, String directory) throws Exception {
		this.myClass = myClass;
		filepath = directory + myClass.getName();

		File directories = new File(directory);
		File file = new File(filepath);

		if (!directories.exists() && !directories.mkdirs())
			throw new Exception("Unable to create directory");

		if (!file.exists() && !file.createNewFile())
			throw new Exception("Unable to create file");

		loadProperties();
		idField = findAnnotation(javax.persistence.Id.class);
	}
}
