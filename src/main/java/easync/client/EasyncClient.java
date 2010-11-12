package easync.client;

import java.io.FileNotFoundException;
import java.net.Socket;

import org.apache.log4j.Logger;

import easync.config.EasyncClientConfig;
import easync.network.NetworkHandler;
import easync.socketestablishment.ConnectionEstablishedListener;
import easync.socketestablishment.SocketConnector;

/**
 * Main class of the client.
 * 
 */
public class EasyncClient implements ConnectionEstablishedListener {

	private final static Logger LOGGER = Logger.getLogger(EasyncClient.class);

	private NetworkHandler network;
	private EasyncClientConfig config;

	private ClientSocketCreator socketConnector;

	public EasyncClient() {
		config = new EasyncClientConfig();

		establishConnection();
	}

	/**
	 * Starts the establishment of the network connection and the requests two sockets from the server.
	 */
	private void establishConnection() {
		socketConnector = new ClientSocketCreator(config);
		socketConnector.setListener(this);
		socketConnector.start();
	}

	/**
	 * Writes a line to the control stream, if the network is connected, else does nothing.
	 * @param line Line to be written
	 */
	public void writeLine(String line) {
		if (network != null) {
			network.writeLine(line);
		}
		else {
			LOGGER.info("Network not connected yet.");
		}
	}

	/**
	 * Transmits a file to the server, if the network is connected, else does nothing.
	 * @param filepath Path to the file to be sent
	 */
	public void transmitFile(String filepath) {
		if (network != null) {
			try {
				network.transmitFile(filepath);
			} catch (FileNotFoundException e) {
				LOGGER.error("The file " + filepath
						+ " was not found. File transmission aborted.", e);
			}
		} else {
			LOGGER.info("Network not connected yet.");
		}
	}

	public static void main(String[] args) {
		new EasyncClient();
	}

	@Override
	public void connectionEstablished(SocketConnector socketConnector) {
		Socket controlSocket = socketConnector.getControlSocket();
		Socket dataSocket = socketConnector.getDataSocket();

		this.socketConnector = null;
		
		// Initializes the network.
		network = new NetworkHandler(controlSocket, dataSocket, config);
		network.connect();
		
	}
}
