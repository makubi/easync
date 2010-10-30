package easync.client;

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
		network.sendFile("/tmp/a");
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}

}
