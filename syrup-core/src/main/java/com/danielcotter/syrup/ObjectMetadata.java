/**
 * 
 */
package com.danielcotter.syrup;

import java.lang.reflect.Field;
import java.util.Properties;

public class ObjectMetadata {

	private Object object;
	private Properties properties;
	private Field field;
	private String id;

	public ObjectMetadata() {

	}

	/**
	 * @param object
	 * @param properties
	 * @param field
	 * @param id
	 */
	public ObjectMetadata(Object object, Properties properties, Field field, String id) {
		this.object = object;
		this.properties = properties;
		this.field = field;
		this.id = id;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ObjectMetadata))
			return false;
		ObjectMetadata other = (ObjectMetadata) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ObjectMetadata [object=" + object + ", properties=" + properties + ", field=" + field + ", id=" + id
				+ "]";
	}
}