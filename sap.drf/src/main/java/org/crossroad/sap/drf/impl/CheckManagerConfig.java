package org.crossroad.sap.drf.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.crossroad.sap.drf.exception.PopertiesLoaderException;

public class CheckManagerConfig extends AbstractLogger {
	public static final String SOURCE = "source";
	public static final String DESTINATION = "destination";
	private final static CheckManagerConfig instance = new CheckManagerConfig();

	private Properties properties = new Properties();
	private String homeDirectory = null;

	private CheckManagerConfig() {
		// TODO Auto-generated constructor stub
	}

	public static CheckManagerConfig getInstance() {
		return instance;
	}

	public void load(String path) throws PopertiesLoaderException {
		this.homeDirectory = path;
		try {
			log.info("Loading configuration....");
			if (!properties.isEmpty()) {
				properties.clear();
			}

			/*
			 * 
			 */
			importIntoProperties(getConfigurationDir() + File.separator + "drfcheck.properties");

		} catch (Exception e) {
			log.error(e);
			throw new PopertiesLoaderException(e);
		}
	}

	private void importIntoProperties(String file) throws PopertiesLoaderException {
		FileInputStream stream = null;
		try {
			log.info("Configuration located in " + file);
			stream = new FileInputStream(file);
			properties.load(stream);
		} catch (Exception e) {
			log.error(e);
			throw new PopertiesLoaderException(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {

				}
				stream = null;
			}
		}
	}
	
	public String getHomeDirectory()
	{
		return this.homeDirectory;
	}
	
	public String getLogDirectory()
	{
		return getHomeDirectory() + File.separator + "log";
	}
	
	public String getOutDirectory()
	{
		return getHomeDirectory() + File.separator + "out";
	}
	

	public String getConfigurationDir() {
		return this.homeDirectory + File.separator + "conf";
	}
	

	public String getUrl(String id, String type) {
		return getValue(id + "." + type +".url");
	}

	public String getUser(String id, String type) {
		return getValue(id + "." + type +".username");
	}
	

	public String getPassword(String id, String type) {
		return getValue(id + "." + type +".password");
	}
	
	public String getSchema(String id, String type) {
		return getValue(id + "." + type +".schema");
	}
	
	public String getValue(String id,String type, String value)
	{
		return getValue(id + "." + type + value);
	}
	
	public String getSQL(String id, String type, String query)
	{
		return getValue(id + "." + type + ".sql."+ query);
	}
	
	
	private String getValue(String key) {
		return (this.properties.containsKey(key)) ? this.properties.getProperty(key).trim() : null;
	}
}
