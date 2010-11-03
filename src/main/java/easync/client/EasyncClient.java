package easync.client;

import java.io.File;
import java.io.IOException;

import easync.network.NetworkHandler;

/**
 * Hauptklasse des Clients.
 *
 */
public class EasyncClient {

	private NetworkHandler network;
	
	public EasyncClient() {
		network = new NetworkHandler();
		network.connect();
		
		writeLine("Client says: Hello! :-)");
		
		File tmpFile = new File("/bin/bash");
		try {
			tmpFile = File.createTempFile("easync_tmp", null);
		} catch (IOException e) {
		}
		transmitFile(tmpFile.getAbsolutePath());
	}
	
	public void writeLine(String line) {
		network.writeLine(line);
	}
	
	public void transmitFile(String filepath) {
		network.transmitFile(filepath);
	}
	
	@Deprecated
	public void sendFile(String filename) {
		network.sendFile(filename);
	}
	
	@Deprecated
	public void sendFile(File file) {
		sendFile(file.getAbsolutePath());
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}

}
