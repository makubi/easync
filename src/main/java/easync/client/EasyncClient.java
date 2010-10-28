package easync.client;

import easync.NetworkAccess;
import easync.NetworkHandler;

public class EasyncClient {

	private NetworkAccess network;
	
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
