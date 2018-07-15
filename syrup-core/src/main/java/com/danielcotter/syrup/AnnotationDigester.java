package com.danielcotter.syrup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationDigester implements AnnotationHandler {

	private Class<? extends Object> myClass;
	private AnnotationHandlerFactory annotationHandlerFactory = AnnotationHandlerFactory.getInstance();
	private Map<Annotation, List<Field>> fieldsAndAnnotations = new HashMap<>();

	@Override
	public void get(ObjectMetadata objectMetadata) throws Exception {
		for (Map.Entry<Annotation, List<Field>> entry : fieldsAndAnnotations.entrySet())
			for (Field thisField : entry.getValue()) {
				objectMetadata.setField(thisField);

				annotationHandlerFactory.getAnnotationHandler(entry.getKey()).get(objectMetadata);
			}
	}

	@Override
	public void save(ObjectMetadata objectMetadata) throws Exception {
		for (Map.Entry<Annotation, List<Field>> entry : fieldsAndAnnotations.entrySet())
			for (Field thisField : entry.getValue()) {
				objectMetadata.setField(thisField);

				annotationHandlerFactory.getAnnotationHandler(entry.getKey()).save(objectMetadata);
			}
	}

	@Override
	public void update(ObjectMetadata objectMetadata) throws Exception {
		for (Map.Entry<Annotation, List<Field>> entry : fieldsAndAnnotations.entrySet())
			for (Field thisField : entry.getValue()) {
				objectMetadata.setField(thisField);

				annotationHandlerFactory.getAnnotationHandler(entry.getKey()).update(objectMetadata);
			}
	}
	
	@Override
	public void delete(ObjectMetadata objectMetadata) throws Exception {
		for (Map.Entry<Annotation, List<Field>> entry : fieldsAndAnnotations.entrySet())
			for (Field thisField : entry.getValue()) {
				objectMetadata.setField(thisField);

				annotationHandlerFactory.getAnnotationHandler(entry.getKey()).delete(objectMetadata);
			}
	}

	private void addToMap(Field thisField, Annotation thisAnnotation) {
		if (!fieldsAndAnnotations.containsKey(thisAnnotation))
			fieldsAndAnnotations.put(thisAnnotation, new ArrayList<Field>(Arrays.asList(thisField)));
		else
			fieldsAndAnnotations.get(thisAnnotation).add(thisField);
	}

	private void findAnnotations(Field thisField) {
		for (Annotation thisAnnotation : thisField.getDeclaredAnnotations()) {
			AnnotationHandler thisHandler = annotationHandlerFactory.getAnnotationHandler(thisAnnotation);

			if (thisHandler != null) {
				addToMap(thisField, thisAnnotation);
				thisField.setAccessible(true);
			}
		}
	}

	private void populateFieldsAndAnnotations() {
		for (Field thisField : myClass.getDeclaredFields())
			findAnnotations(thisField);
	}

	/**
	 * @param myClass
	 */
	protected AnnotationDigester(Class<? extends Object> myClass) {
		this.myClass = myClass;

		populateFieldsAndAnnotations();
	}
}