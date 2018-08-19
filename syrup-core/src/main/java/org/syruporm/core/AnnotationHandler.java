package org.syruporm.core;

/**
 * Annotation handlers are called when the specified annotation is found within
 * a class of a Syrup handled object during a CRUD operation
 * 
 * @since 0.2.0
 */
public interface AnnotationHandler {

	/**
	 * Method called when data to be parsed is retrieved
	 * 
	 * @param objectMetadata Meta data of the object
	 * @see ObjectMetadata
	 * @since 0.2.0
	 */
	public void get(ObjectMetadata objectMetadata) throws Exception;

	/**
	 * Method called when data to be parsed is persisted
	 * 
	 * @param objectMetadata Meta data of the object
	 * @see ObjectMetadata
	 * @since 0.2.0
	 */
	public void save(ObjectMetadata objectMetadata) throws Exception;

	/**
	 * Method called when data to be parsed is updated
	 * 
	 * @param objectMetadata Meta data of the object
	 * @see ObjectMetadata
	 * @since 0.2.0
	 */
	public void update(ObjectMetadata objectMetadata) throws Exception;

	/**
	 * Method called when data to be parsed is deleted
	 * 
	 * @param objectMetadata Meta data of the object
	 * @see ObjectMetadata
	 * @since 0.2.0
	 */
	public void delete(ObjectMetadata objectMetadata) throws Exception;
}