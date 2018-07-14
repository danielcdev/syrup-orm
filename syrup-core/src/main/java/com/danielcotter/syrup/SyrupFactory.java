package com.danielcotter.syrup;

import java.util.HashMap;
import java.util.Map;

public class SyrupFactory {

	private Map<Class<? extends Object>, Syrup> pantry = new HashMap<>();

	public Syrup getSyrup(Class<? extends Object> thisClass) {
		try {
			if (!pantry.containsKey(thisClass))
				pantry.put(thisClass, new Syrup(thisClass));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return pantry.get(thisClass);
	}

	public Boolean resetSyrup(Class<? extends Object> thisClass) {
		try {
			Syrup thisSyrup = getSyrup(thisClass);

			thisSyrup.deleteFile();
			pantry.put(thisClass, new Syrup(thisClass));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}