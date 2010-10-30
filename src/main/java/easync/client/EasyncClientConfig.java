package easync.client;

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
public class EasyncClientConfig extends EasyncConfig {

	private final File configFile = new File(configDir.getAbsolutePath()
			+ File.separator + "client.conf");

	private String host = "localhost";

	/**
	 * Laedt die Konfiguration und erstellt vorher eine Neue, falls nicht vorhanden.
	 */
	public EasyncClientConfig() {
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

			if (properties.containsKey("host")) {
				host = properties.getProperty("host");
			}

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
			properties.put("host", host);
			properties.put("port", "" + port);
			properties.store(outStream, "Automatically created  config.");
			outStream.close();
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
	 * Liefert den verwendeten Hostnamen oder IP-Adresse.
	 * @return Verwendeter Host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Setzt den zu verwendeten Hostname oder die zu verwendente IP-Adresse und speicherte diesen Wert in die Properties-Datei.
	 * @param host - Host, der verwendet werden soll
	 */
	public void setHost(String host) {
		this.host = host;
		putProperty("host", host);
	}

	@Override
	protected File getConfigFile() {
		return configFile;
	}
}
