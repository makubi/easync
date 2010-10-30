package easync.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import easync.EasyncConfig;
import easync.network.NetworkHelper;

/**
 * Stellt Konfigurationsparameter des Clients zur Verfuegung.
 *
 */
public class EasyncServerConfig extends EasyncConfig {

	private final File configFile = new File(configDir.getAbsolutePath()
			+ File.separator + "server.conf");
	
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
	
	@Override
	protected File getConfigFile() {
		return configFile;
	}

}
