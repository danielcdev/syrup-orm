package org.syruporm.core.test.model;

import javax.persistence.Id;

public class DoubleModel {

	@Id
	private Double id;

	public DoubleModel() {

	}

	/**
	 * @param id
	 */
	public DoubleModel(Double id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Double getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Double id) {
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
		if (!(obj instanceof DoubleModel))
			return false;
		DoubleModel other = (DoubleModel) obj;
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
		return "DoubleModel [id=" + id + "]";
	}
}
