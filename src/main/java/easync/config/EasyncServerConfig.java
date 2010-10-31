package easync.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import easync.network.NetworkHelper;

/**
 * Stellt Konfigurationsparameter des Clients zur Verfuegung.
 *
 */
public class EasyncServerConfig extends EasyncConfig {

	private final File configFile = new File(configDir.getAbsolutePath()
			+ File.separator + "server.conf");
	
	private File workDir = new File(System.getProperty("user.home")+File.separator+"EaSync");

	/**
	 * Laedt die Konfiguration und erstellt vorher eine Neue, falls nicht vorhanden.
	 */
	public EasyncServerConfig() {
		if (!configFile.exists()) {
			initNewConfig();
		}
		loadProperties();
	}
	
	/**
	 * Laedt Properties, die gefunden werden.
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
	 * Legt eine neue Konfiguration an und schreibt die Standard-Werte hinein.
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
	
	public String getWorkDir() {
		if(!workDir.exists()) {
			workDir.mkdirs();
		}
		return workDir.getAbsolutePath();
	}

	public void setWorkDir(String workDir) {
		this.workDir = new File(workDir);
		putProperty("workDir", workDir);
	}
	
	@Override
	protected File getConfigFile() {
		return configFile;
	}

}
