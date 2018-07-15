package org.syruporm.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SyrupFactory {

	private Map<Class<? extends Object>, Syrup> pantry = new HashMap<>();
	private String directory = null;

	public Syrup getSyrup(Class<? extends Object> thisClass) {
		try {
			if (!pantry.containsKey(thisClass))
				pantry.put(thisClass, new Syrup(thisClass, directory));
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
			pantry.put(thisClass, new Syrup(thisClass, directory));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public SyrupFactory() {
		String tomcat = System.getProperty("catalina.base");
		String jetty = System.getProperty("jetty.base");

		if (tomcat != null)
			directory = tomcat + File.separator + ".syrup" + File.separator;

		if (jetty != null)
			directory = tomcat + File.separator + ".syrup" + File.separator;

		if (directory == null)
			directory = System.getProperty("user.home") + File.separator + ".syrup" + File.separator;
	}

	/**
	 * @param directory
	 */
	public SyrupFactory(String directory) {
		this.directory = (directory.substring(directory.length() - 1, directory.length()).equals(File.separator))
				? directory : directory + File.separator;
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
}