package com.danielcotter.syrup;

public interface AnnotationHandler {

	public void get(ObjectMetadata objectMetadata) throws Exception;

	public void save(ObjectMetadata objectMetadata) throws Exception;

	public void update(ObjectMetadata objectMetadata) throws Exception;
}