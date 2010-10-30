package easync.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Hauptklasse des Servers.
 * Dieser kuemmert sich um den Verbindungsaufbau zwischen Server und Clients.
 *
 */
public class EasyncServer {
	private int port = 43443;
	private ArrayList<ConnectionHandler> connectedSockets;
	
	public EasyncServer() {
		connectedSockets = new ArrayList<ConnectionHandler>();
		handleConnections();
	}

	/**
	 * Kuemmert sich um die Verbindung mit neuen Clients und startet fuer jeden neu Verbundenen einen ConnectionHandler-Thread.
	 * @see easync.server.ConnectionHandler
	 */
	private void handleConnections() {
		try {
			int i = 0;
			ServerSocket serverSocket = new ServerSocket(port);
			Socket controlSocket;
			Socket dataSocket;
			ConnectionHandler connectionHandler = null;
			System.out.println("Easync-Server started on port "+port);
			while(true) {
				try {
					controlSocket = serverSocket.accept();
					dataSocket = serverSocket.accept();
					connectionHandler = new ConnectionHandler(controlSocket, dataSocket);
					connectedSockets.add(connectionHandler);
					System.out.println("Client #" + i++ + " connected at " + controlSocket.getInetAddress());
				}
				catch (NullPointerException e) {}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new EasyncServer();
	}
}
