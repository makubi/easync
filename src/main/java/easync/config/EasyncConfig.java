package easync.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import easync.network.NetworkHelper;

/**
 * Provides configuration parameters that is used by both, the client and the server.
 * This is done by using an Properties-File.
 *
 */
public abstract class EasyncConfig {
	
	private final Logger logger = Logger.getLogger(EasyncConfig.class);
	
	protected final File configDir = new File(System.getProperty("user.home")
			+ File.separator + ".easync" + File.separator);
	
	protected final Properties properties = new Properties();
	
	protected int port = 43443;
	
	/**
	 * Puts a pair of key/value to the properties-map and saves it to the properties file.
	 * 
	 * @param property - Key of the property
	 * @param value - Value of the property
	 */
	protected void putProperty(String property, String value) {
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(getConfigFile());
			properties.put(property, value);
			properties.store(outStream, "Automatically created config.");
		} catch (FileNotFoundException e) {
			logger.error("The file " + getConfigFile().getAbsolutePath() + " was not found. Saving new property aborted.",e);
		} catch (IOException e) {
			logger.error("An I/O Exception occured. Saving properties aborted.",e);
		} finally {
			NetworkHelper.closeStream(outStream);
		}
	}
	
	/**
	 * Liefert den verwendeten Port.
	 * Returns the used port.
	 * 
	 * @return Used port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port that should be used an saves it to the properties file.
	 * The port has to be between 1 and 65534. If that isn't the case, an IllegalArgumentException is thrown.
	 * To use a port < 1024, root privileges are needed.
	 * 
	 * @param port - Port that should be set
	 * @throws IllegalArgumentException
	 */
	public void setPort(int port) throws IllegalArgumentException {
		if (port > 0 && port < 65535) {
			this.port = port;
			putProperty("port", "" + port);
		} else {
			logger.info("Tried to set invalid port ["+port+"].");
			throw new IllegalArgumentException("Port out of range");
		}
	}
	
	/**
	 * Returns the file to be used as properties file.
	 * 
	 * @return Used configuration file
	 */
	protected abstract File getConfigFile();
}
