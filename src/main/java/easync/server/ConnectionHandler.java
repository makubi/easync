package easync.server;

import java.net.Socket;

import easync.NetworkAccess;
import easync.NetworkHandler;

public class ConnectionHandler implements Runnable {

	private NetworkAccess network;

	public ConnectionHandler(Socket controlSocket, Socket dataSocket) {
		network = new NetworkHandler(controlSocket, dataSocket);
		Thread connectionHandler = new Thread(this);
		connectionHandler.start();
	}

	@Override
	public void run() {
			try {
				network.connect();
				network.writeLine("Server: #1");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
