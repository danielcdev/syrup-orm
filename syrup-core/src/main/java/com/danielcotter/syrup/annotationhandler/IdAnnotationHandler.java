package com.danielcotter.syrup.annotationhandler;

import com.danielcotter.syrup.AnnotationHandler;
import com.danielcotter.syrup.ObjectMetadata;

public class IdAnnotationHandler implements AnnotationHandler {

	@Override
	public void get(ObjectMetadata objectMetadata) throws Exception {

	}

	@Override
	public void save(ObjectMetadata objectMetadata) throws Exception {
		Object idFieldValue = objectMetadata.getField().get(objectMetadata.getObject());
		String propertiesId = new Integer(objectMetadata.getProperties().keySet().size()).toString();

		if (idFieldValue == null) {
			switch (objectMetadata.getField().getType().getSimpleName()) {
			case "String":
				objectMetadata.getField().set(objectMetadata.getObject(), propertiesId);
				break;

			case "Integer":
				objectMetadata.getField().set(objectMetadata.getObject(), new Integer(propertiesId));
				break;

			case "Long":
				objectMetadata.getField().set(objectMetadata.getObject(), new Long(propertiesId));
				break;

			case "Double":
				objectMetadata.getField().set(objectMetadata.getObject(), new Double(propertiesId));
				break;

			case "Float":
				objectMetadata.getField().set(objectMetadata.getObject(), new Float(propertiesId));
				break;
			}
		} else if (objectMetadata.getProperties().get(idFieldValue.toString()) != null) {
			throw new Exception("Duplicate id");
		} else {
			propertiesId = idFieldValue.toString();
		}

		objectMetadata.setId(propertiesId);
	}

	@Override
	public void update(ObjectMetadata objectMetadata) throws Exception {
		Object idFieldValue = objectMetadata.getField().get(objectMetadata.getObject());

		if (objectMetadata.getProperties().get(idFieldValue.toString()) == null)
			throw new Exception("Id does not exist");

		objectMetadata.setId(idFieldValue.toString());
	}
}