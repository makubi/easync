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
		network.writeLine("Client: #1");
		network.sendFile("/tmp/a");
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}

}
