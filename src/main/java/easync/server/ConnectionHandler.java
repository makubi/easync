package easync.server;

import java.net.Socket;

import easync.NetworkAccess;
import easync.NetworkHandler;

/**
 * Diese Klasse kuemmert sich um die Kommunkation zwischen Server und jeweils eines Clients.
 *
 */
public class ConnectionHandler implements Runnable {

	private NetworkAccess network;

	/**
	 * Startet einen Netzwerk-Thread und den Thread dieses Objekts.
	 * 
	 * @param controlSocket - Socket des Control-Streams
	 * @param dataSocket - Socket des Data-Streams
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
				network.writeLine("Server: #1");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
