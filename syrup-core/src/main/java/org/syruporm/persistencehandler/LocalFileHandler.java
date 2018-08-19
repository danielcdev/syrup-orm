package org.syruporm.persistencehandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.syruporm.core.PersistenceHandler;

public class LocalFileHandler implements PersistenceHandler {

	private String directory;

	@Override
	public OutputStream save(Class<? extends Object> myClass) {
		String filePath = directory + myClass.getName();
		OutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return outputStream;
	}

	@Override
	public InputStream load(Class<? extends Object> myClass) {
		InputStream inputStream = null;
		String filePath = directory + myClass.getName();

		try {
			File directories = new File(directory);
			File file = new File(filePath);

			if (!directories.exists() && !directories.mkdirs())
				throw new Exception("Unable to create directory");

			if (!file.exists() && !file.createNewFile())
				throw new Exception("Unable to create file");

			inputStream = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	@Override
	public Boolean delete(Class<? extends Object> myClass) {
		String filePath = directory + myClass.getName();
		File file = new File(filePath);

		try {
			if (file.exists())
				return file.delete();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private String fixDirectory(String directory) {
		return (directory.substring(directory.length() - 1, directory.length()).equals(File.separator)) ? directory
				: directory + File.separator;
	}

	public LocalFileHandler() {
		String tomcat = System.getProperty("catalina.base");
		String jetty = System.getProperty("jetty.base");

		if (tomcat != null)
			directory = tomcat + File.separator + ".syrup" + File.separator;

		if (jetty != null)
			directory = tomcat + File.separator + ".syrup" + File.separator;

		if (directory == null)
			directory = System.getProperty("user.home") + File.separator + ".syrup" + File.separator;
	}

	public LocalFileHandler withBaseDirectory(String directory) {
		this.directory = fixDirectory(directory);

		return this;
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
		this.directory = fixDirectory(directory);
	}
}
