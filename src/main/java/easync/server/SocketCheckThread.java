package easync.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import org.apache.log4j.Logger;

import easync.socketestablishment.ConnectionEstablishedListener;
import easync.socketestablishment.SocketConnector;

/**
 * This class manages the establishment of a connection between the server and the client.
 * It takes care of the association between two sockets.
 * If two sockets are bound to the same client, the server is called to continue the connection process.
 *
 * @see ConnectionEstablishedListener#connectionEstablished(SocketCheckThread)
 */
public class SocketCheckThread extends Thread implements SocketConnector{

	private final static Logger LOGGER = Logger.getLogger(SocketCheckThread.class);
	
	private Socket socket;
	private Socket foreignSocket;
	
	private ConnectionEstablishedListener connectionEstablishedListener;
	
	private HashMap<Integer,SocketCheckThread> socketCheckMap;
	private int checkNumber;
	
	public SocketCheckThread(Socket socket, int checkNumber) {
		this.checkNumber = checkNumber;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// Writing the number I got from the server.
			socket.getOutputStream().write(checkNumber);
			socket.getOutputStream().flush();
			
			// Reading the number that was returned by the client.
			int readNum = socket.getInputStream().read();
			LOGGER.debug("Reading number: "+readNum);
			
			// On the first connection, the local and the currently read number are equal.
			// I would get my own reference here, so only get it, if the numbers differ.
			if(readNum != checkNumber) {
				
				// Sets the Socket of first Socket.
				foreignSocket = socketCheckMap.get(readNum).getSocket();
				
				// Removes the SocketCheckThread of the first Socket from the map.
				socketCheckMap.remove(readNum);
				LOGGER.debug("My checkNumber: "+checkNumber);
				LOGGER.debug("Read checkNumber "+readNum+ " and returned associated Socket.");
				
				// Contacts the Listener, that the connection is established.
				connectionEstablishedListener.connectionEstablished(this);
			}
			else {
				LOGGER.debug("Got "+readNum+" but it is the first connection.");
			}
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured while exchanging check numbers.",e);
		}
	}

	/**
	 * Returns the Socket related to this SocketCheckThread.
	 * @return - The current socket
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Returns the socket of the control stream.
	 * This method should only be called, after the second socket is set.
	 * @return Control socket
	 */
	@Override
	public Socket getControlSocket() {
		return foreignSocket;
	}

	/**
	 * Returns the socket of the data stream.
	 * @return Data socket.
	 */
	@Override
	public Socket getDataSocket() {
		return socket;
	}

	/**
	 * Returns the assigned check number.
	 * @return Check number of this SocketCheckThread
	 */
	public int getCheckNumber() {
		return checkNumber;
	}
	
	public void setSocketCheckMap(HashMap<Integer, SocketCheckThread> socketCheckMap) {
		this.socketCheckMap = socketCheckMap;
	}
	
	public void setConnectionEstablishedListener(ConnectionEstablishedListener connectionEstablishedListener) {
		this.connectionEstablishedListener = connectionEstablishedListener;
	}
}
