package com.danielcotter.syrup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Syrup {

	private Class<? extends Object> myClass;
	private AnnotationDigester annotationDigester;
	private ObjectMapper objectMapper = new ObjectMapper();
	private Properties properties = new Properties();
	private String filepath;

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
		try {
			File file = new File(filepath);

			if (file.exists())
				return file.delete();
		} catch (Exception e) {
			return false;
		}

		return true;
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
	protected Syrup(Class<? extends Object> myClass, String directory) throws Exception {
		this.myClass = myClass;
		filepath = directory + myClass.getName();

		File directories = new File(directory);
		File file = new File(filepath);

		if (!directories.exists() && !directories.mkdirs())
			throw new Exception("Unable to create directory");

		if (!file.exists() && !file.createNewFile())
			throw new Exception("Unable to create file");

		loadProperties();
		annotationDigester = new AnnotationDigester(myClass);
	}
}
