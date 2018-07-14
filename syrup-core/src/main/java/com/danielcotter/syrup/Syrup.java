package com.danielcotter.syrup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Syrup {

	private Class<? extends Object> myClass;
	private ObjectMapper objectMapper = new ObjectMapper();
	private Properties properties = new Properties();
	private String filepath;

	public Object getById(String id) {
		Object myReturn = null;

		try {
			loadProperties();
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
			loadProperties();
			Set<Entry<Object, Object>> entries = properties.entrySet();

			for (Entry<Object, Object> entry : entries) {
				Object retrievedObject = getById(entry.getKey().toString());

				Field thisField = myClass.getDeclaredField(fieldName);
				thisField.setAccessible(true);

				if (thisField.get(retrievedObject).equals(comparator))
					return retrievedObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return myReturn;
	}

	public Boolean save(Object object) {
		try {
			loadProperties();
			String propertiesId = new Integer(properties.keySet().size()).toString();
			Field idField = findAnnotation(javax.persistence.Id.class);

			if (idField == null)
				throw new Exception("Class missing @Id");

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
			loadProperties();
			Field idField = findAnnotation(javax.persistence.Id.class);

			if (idField == null)
				throw new Exception("Class missing @Id");

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

		for (Field thisField : myClass.getDeclaredFields())
			if (thisField.isAnnotationPresent(needle)) {
				myField = thisField;
				break;
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

		properties.store(outputStream, "syrup");
		outputStream.close();
	}

	/**
	 * @param myClass
	 * @throws Exception
	 */
	public Syrup(Class<? extends Object> myClass) throws Exception {
		this.myClass = myClass;
		filepath = System.getProperty("user.home") + File.separator + "syrup" + File.separator + myClass.getName();

		File directories = new File(System.getProperty("user.home") + File.separator + "syrup" + File.separator);
		File file = new File(filepath);

		if (!directories.exists() && !directories.mkdirs())
			throw new Exception("Unable to create directory");

		if (!file.exists() && !file.createNewFile())
			throw new Exception("Unable to create file");
	}
}
