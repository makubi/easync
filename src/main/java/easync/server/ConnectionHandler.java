package easync.server;

import java.net.Socket;

import easync.network.NetworkHandler;

/**
 * This class takes care of the communication between the server and always one
 * client.
 */
public class ConnectionHandler extends Thread {

	private NetworkHandler network;

	/**
	 * Starts a network connection and the thread of this class.
	 * 
	 * @param controlSocket
	 *            - Socket of the control stream
	 * @param dataSocket
	 *            - Socket of the data stream
	 */
	public ConnectionHandler(Socket controlSocket, Socket dataSocket) {
		network = new NetworkHandler(controlSocket, dataSocket);
	}

	@Override
	public void run() {
		network.connect();
	}
}
