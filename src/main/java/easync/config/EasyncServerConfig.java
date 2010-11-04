package easync.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import easync.network.NetworkHelper;

/**
 * Provides configuration parameters to the server.
 * This is done by using an Properties-File.
 *
 */
public class EasyncServerConfig extends EasyncConfig {

	private final File configFile = new File(configDir.getAbsolutePath()
			+ File.separator + "server.conf");
	
	private File workDir = new File(System.getProperty("user.home")+File.separator+"EaSync");

	/**
	 * Loads the configuration and creats a new one, if non exists.
	 */
	public EasyncServerConfig() {
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

			if (properties.containsKey("port")) {
				port = Integer.parseInt(properties.getProperty("port"));
			}
			
			if(properties.containsKey("workDir")) {
				workDir = new File(properties.getProperty("workDir"));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			properties.put("port", "" + port);
			properties.put("workDir", workDir.getAbsolutePath());
			properties.store(outStream, "Automatically created  config.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			NetworkHelper.closeStream(outStream);
		}
	}
	
	/**
	 * Returns the path to the current working directory and creates the folders to that path if it does not exist yet.
	 * @return The path to the current working directory
	 */
	public String getWorkDir() {
		if(!workDir.exists()) {
			workDir.mkdirs();
		}
		return workDir.getAbsolutePath();
	}

	/**
	 * Sets the folder, the server works in, e.g. synchronize data to.
	 * Then it saves the path to that folder to the properties file.
	 * @param workDir - Sets the working directory of the server
	 */
	public void setWorkDir(String workDir) {
		this.workDir = new File(workDir);
		putProperty("workDir", workDir);
	}
	
	@Override
	protected File getConfigFile() {
		return configFile;
	}

}
