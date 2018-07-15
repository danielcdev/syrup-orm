package com.danielcotter.syrup;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerFactory {

	private static final String packageName = "com.danielcotter.syrup.annotationhandler.";
	private static final String classSuffix = "AnnotationHandler";
	private static AnnotationHandlerFactory instance;
	private Map<Annotation, AnnotationHandler> annotationHandlers = new HashMap<>();

	protected AnnotationHandler getAnnotationHandler(Annotation thisAnnotation) {
		try {
			if (!annotationHandlers.containsKey(thisAnnotation)) {
				AnnotationHandler thisHandler = (AnnotationHandler) Class
						.forName(packageName + thisAnnotation.annotationType().getSimpleName() + classSuffix)
						.newInstance();

				annotationHandlers.put(thisAnnotation, thisHandler);
			}
		} catch (Exception e) {
			e.printStackTrace();
			annotationHandlers.put(thisAnnotation, null);
			return null;
		}

		return annotationHandlers.get(thisAnnotation);
	}

	protected static synchronized AnnotationHandlerFactory getInstance() {
		if (instance == null || !(instance instanceof AnnotationHandlerFactory))
			instance = new AnnotationHandlerFactory();

		return instance;
	}

	private AnnotationHandlerFactory() {

	}
}
