package easync.server;

import java.net.Socket;

import easync.network.NetworkHandler;

/**
 * Diese Klasse kuemmert sich um die Kommunkation zwischen Server und jeweils eines Clients.
 *
 */
public class ConnectionHandler implements Runnable {

	private NetworkHandler network;

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
				network.writeLine("Client-Handler on server-side says: Hello! :-)");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
