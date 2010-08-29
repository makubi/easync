package easync.client;

import easync.NetworkAccess;
import easync.NetworkHandler;

public class EasyncClient {

	private NetworkAccess network;
	
	public EasyncClient() {
		network = new NetworkHandler();
		network.connect();
		network.writeLine("Message from Client: Test");
		network.writeLine("Message from Client: Test");
		network.writeLine("Message from Client: Test");
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}

}
