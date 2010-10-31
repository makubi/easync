package easync.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import easync.network.NetworkHelper;

/**
 * Stellt Konfigurationsparameter bereit, die Server sowie Client enthalten.
 *
 */
public abstract class EasyncConfig {
	protected final File configDir = new File(System.getProperty("user.home")
			+ File.separator + ".easync" + File.separator);
	
	protected final Properties properties = new Properties();
	
	protected int port = 43443;
	
	/**
	 * Fuegt ein Key/Value-Paar zu der Properties-Map hinzu und speichert dieses in der Properties-Datei.
	 * 
	 * @param property - Key der Property
	 * @param value - Wert der Property
	 */
	protected void putProperty(String property, String value) {
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(getConfigFile());
			properties.put(property, value);
			properties.store(outStream, "Automatically created config.");
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
	 * Liefert den verwendeten Port.
	 * 
	 * @return Verwendeter Port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Setzt den zu verwenden Port und schreibt ihn in die Properties-Datei.
	 * Der Port muss zwischen 1 und 65534 liegen. Ist diese nicht gegeben, wird eine IllegalArgumentException geworfen.
	 * Fuer einen Port < 1024 werden Root-Rechte benoetigt.
	 * 
	 * @param port - Port, der gesetzt werden soll
	 * @throws IllegalArgumentException
	 */
	public void setPort(int port) {
		if (port > 0 && port < 65535) {
			this.port = port;
			putProperty("port", "" + port);
		} else {
			throw new IllegalArgumentException("Port out of range");
		}
	}
	
	/**
	 * Liefert die Konfigurationsdatei.
	 * 
	 * @return Verwendete Konfigurationsdatei
	 */
	protected abstract File getConfigFile();
}
