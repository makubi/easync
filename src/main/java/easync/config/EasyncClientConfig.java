package easync.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import easync.network.NetworkHelper;

/**
 * Provides configuration parameters to the client.
 * This is done by using an Properties-File.
 * 
 */
public class EasyncClientConfig extends EasyncConfig {

	private final static Logger LOGGER = Logger.getLogger(EasyncClientConfig.class);
	
	private final File configFile = new File(configDir.getAbsolutePath()
			+ File.separator + "client.conf");

	private String host = "localhost";
	private File syncFolder = new File(System.getProperty("user.home")+File.separator+"EaSync");

	/**
	 * Loads the configuration and creats a new one, if non exists.
	 */
	public EasyncClientConfig() {
		if (!configFile.exists()) {
			initNewConfig();
		}
		loadProperties();
	}

	/**
	 * Loads the properties that were found.
	 */
	private void loadProperties() {
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(configFile);

			properties.load(inStream);

			if (properties.containsKey("host")) {
				host = properties.getProperty("host");
			}

			if (properties.containsKey("port")) {
				port = Integer.parseInt(properties.getProperty("port"));
			}
			
			if(properties.containsKey("syncFolder")) {
				syncFolder = new File(properties.getProperty("syncFolder"));
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("The file " + configFile.getAbsolutePath() + " was not found. Loading properties aborted.",e);
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured. Loading properties aborted.",e);
		} finally {
			NetworkHelper.closeStream(inStream);
		}
	}

	/**
	 * Creates a new configuration and fills it with the default values. 
	 */
	private void initNewConfig() {
		FileOutputStream outStream = null;
		try {
			if(!configDir.exists()) {
				configDir.mkdirs();
			}
			configFile.createNewFile();
			outStream = new FileOutputStream(configFile);
			properties.put("host", host);
			properties.put("port", "" + port);
			properties.put("syncFolder", syncFolder.getAbsolutePath());
			properties.store(outStream, "Automatically created  config.");
		} catch (FileNotFoundException e) {
			LOGGER.error("The file " + configFile.getAbsolutePath() + " was not found. Initialization of new config aborted.",e);
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured. Initialization of new config aborted.",e);
		} finally {
			NetworkHelper.closeStream(outStream);
		}
	}

	/**
	 * Returns the used hostname or ip address.
	 * @return The used host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the hostname or ip address that should be used and saves it to the properties file.
	 * @param host - Host that should be used
	 */
	public void setHost(String host) {
		this.host = host;
		putProperty("host", host);
	}

	@Override
	protected File getConfigFile() {
		return configFile;
	}

	/**
	 * Sets the folder, files should be synchronized to and saves that path to the properties file.
	 * @param syncFolder - Folder, files should be synchronized to.
	 */
	public void setSyncFolder(String syncFolder) {
		this.syncFolder = new File(syncFolder);
		putProperty("syncFolder", syncFolder);
	}

	/**
	 * Returns the path to the current synchronization folder and creates the folders to that path if it does not exist yet.
	 * @return The path to the current synchronization folder
	 */
	public String getSyncFolder() {
		if(!syncFolder.exists()) {
			syncFolder.mkdirs();
		}
		return syncFolder.getAbsolutePath();
	}
}
