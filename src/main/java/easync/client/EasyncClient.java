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
		network.writeLine("Client says: Hello! :-)");
		try {
			network.sendFile(File.createTempFile("easync_tmp", null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}

}
