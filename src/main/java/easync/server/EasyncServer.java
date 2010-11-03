package easync.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import easync.config.EasyncServerConfig;


/**
 * Hauptklasse des Servers.
 * Dieser kuemmert sich um den Verbindungsaufbau zwischen Server und Clients.
 *
 */
public class EasyncServer {
	private int port;
	private ArrayList<ConnectionHandler> connectedSockets;
	
	public EasyncServer() {
		EasyncServerConfig config = new EasyncServerConfig();
		port = config.getPort();
		
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
					// TODO: Pruefen, ob das zweite Socket auch vom selben Client, wie das Erste, akzeptiert wurde, falls die Anfrage nach dem ersten serverSocket.accept() abgebrochen wird.
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
