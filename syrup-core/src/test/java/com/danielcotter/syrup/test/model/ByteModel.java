package com.danielcotter.syrup.test.model;

import javax.persistence.Id;

public class ByteModel {

	@Id
	Byte id;

	public ByteModel() {

	}

	/**
	 * @param id
	 */
	public ByteModel(Byte id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Byte getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Byte id) {
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof ByteModel))
			return false;
		ByteModel other = (ByteModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return "ByteModel [id=" + id + "]";
	}
}
