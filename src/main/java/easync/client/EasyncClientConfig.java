package easync.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import easync.EasyncConfig;

public class EasyncClientConfig extends EasyncConfig {

	private final File configFile = new File(configDir.getAbsolutePath()
			+ File.separator + "client.conf");
	private final Properties properties = new Properties();

	private String host = "localhost";
	private int port = 43443;

	public EasyncClientConfig() {
		if (!configFile.exists()) {
			initNewConfig();
		}
		loadProperties();
	}

	private void loadProperties() {
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(configFile);

			properties.load(inStream);

			if (properties.containsKey("host")) {
				host = properties.getProperty("host");
			}

			if (properties.containsKey("port")) {
				port = Integer.parseInt(properties.getProperty("port"));
			}

			inStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// TODO: close streams
		}
	}

	private void initNewConfig() {
		FileOutputStream outStream;
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
			// TODO: close streams
		}
	}

	private void putProperty(String property, String value) {
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(configFile);
			properties.put(property, value);
			properties.store(outStream, "Automatically created config.");
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// TODO: close streams
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		putProperty("host", host);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if (port > 0 && port < 65535) {
			this.port = port;
			putProperty("port", "" + port);
		} else {
			throw new IllegalArgumentException("Port out of range");
		}
	}
}
