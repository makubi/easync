package easync.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import easync.config.EasyncServerConfig;
import easync.socketestablishment.ConnectionEstablishedListener;
import easync.socketestablishment.SocketConnector;


/**
 * Main class of the server.
 * It takes care of the connection to new clients.
 */
public class EasyncServer implements ConnectionEstablishedListener {
	
	private final static Logger LOGGER = Logger.getLogger(EasyncServer.class);
	
	private int port;
	private String workDir;
	
	private ArrayList<ConnectionHandler> connectedSockets = new ArrayList<ConnectionHandler>();;
	private HashMap<Integer,SocketCheckThread> socketCache = new HashMap<Integer, SocketCheckThread>();
		
	public EasyncServer() {
		EasyncServerConfig config = new EasyncServerConfig();
		port = config.getPort();
		workDir = config.getWorkDir();
		
		handleConnections();
	}

	/**
	 * Takes care of new connections and creates a ConnectionHandler for every new connected client.
	 * @see easync.server.ConnectionHandler
	 */
	private void handleConnections() {
		try {
			int i = 0;
			ServerSocket serverSocket = new ServerSocket(port);
			Socket socket;
			SocketCheckThread socketCheckThread;
			LOGGER.info("Easync-Server started on port "+port+". Using work dir: ["+workDir+"]");
			while(true) {
				try {
					// Waits until a client connects.
					socket = serverSocket.accept();
					
					// Creates a new thread that waits for the second socket request.
					socketCheckThread = new SocketCheckThread(socket,i);
					socketCheckThread.setSocketCheckMap(socketCache);
					socketCheckThread.setConnectionEstablishedListener(this);
					
					// Adds that thread to the map and starts it.
					socketCache.put(i, socketCheckThread);
					socketCheckThread.start();
					
					LOGGER.debug("Socket #"+i+" requested.");
					i++;
				}
				catch (NullPointerException e) {
					LOGGER.error("A NullPointerException occured. Initializing new connection to this client aborted.",e);
				}
			}
		}
		catch (IOException e) {
			LOGGER.error("An I/O Exception occured. Server exiting.",e);
		}
	}
	
	public static void main(String[] args) {
		new EasyncServer();
	}

	@Override
	public void connectionEstablished(SocketConnector socketConnector) {
		Socket controlSocket = socketConnector.getControlSocket();
		Socket dataSocket = socketConnector.getDataSocket();
		
		// Create new ConnectionHandler with the two Sockets and start it.
		ConnectionHandler connectionHandler = new ConnectionHandler(controlSocket, dataSocket);
		connectedSockets.add(connectionHandler);
		connectionHandler.start();
		
		// Remove the SocketCheckThread from the map.
		int socketCheckNumber = ((SocketCheckThread) socketConnector).getCheckNumber();
		socketCache.remove(socketCheckNumber);
		
		LOGGER.info("Client #" + socketCheckNumber/2 + " connected at " + controlSocket.getInetAddress());
	}
}
