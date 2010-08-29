package easync.server;

import java.net.Socket;

import easync.NetworkAccess;
import easync.NetworkHandler;

public class ConnectionHandler implements Runnable {

	private Socket controlSocket;
	private Socket dataSocket;

	private NetworkAccess network;

	public ConnectionHandler(Socket controlSocket, Socket dataSocket) {
		this.controlSocket = controlSocket;
		this.dataSocket = dataSocket;
		Thread connectionHandler = new Thread(this);
		connectionHandler.start();
	}

	@Override
	public void run() {
			try {
				network = new NetworkHandler(controlSocket, dataSocket);
				network.connect();
				network.writeLine("Message from Server: Test");
				network.writeLine("Message from Server: Test");
				network.writeLine("Message from Server: Test");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
