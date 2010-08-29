package easync.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class EasyncServer {
	private int port = 43443;
	private ArrayList<ConnectionHandler> connectedSockets;
	
	public EasyncServer() {
		connectedSockets = new ArrayList<ConnectionHandler>();
		handleConnections();
	}

	private void handleConnections() {
		try {
			int i = 0;
			ServerSocket serverSocket = new ServerSocket(port);
			Socket controlSocket;
			Socket dataSocket;
			ConnectionHandler connectionHandler = null;
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
