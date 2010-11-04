package easync.server;

import java.net.Socket;

import easync.network.NetworkHandler;

/**
 * This class takes care of the communication between the server and always one client.
 */
public class ConnectionHandler implements Runnable {

	private NetworkHandler network;

	/**
	 * Starts a network connection and the thread of this class.
	 * 
	 * @param controlSocket - Socket of the control stream
	 * @param dataSocket - Socket of the data stream
	 */
	public ConnectionHandler(Socket controlSocket, Socket dataSocket) {
		network = new NetworkHandler(controlSocket, dataSocket);
		Thread connectionHandler = new Thread(this);
		connectionHandler.start();
	}

	@Override
	public void run() {
			try {
				network.connect();
				network.writeLine("Client-Handler on server-side says: Hello! :-)");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
