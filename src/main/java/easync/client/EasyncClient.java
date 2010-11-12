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
public class EasyncClient implements ConnectionEstablishedListener,
		NetworkAccessProvider {

	private final static Logger LOGGER = Logger.getLogger(EasyncClient.class);

	private NetworkHandler network;
	private EasyncClientConfig config;

	private ClientSocketCreator socketConnector;

	private FileSyncThread syncThread;
	
	public EasyncClient() {
		config = new EasyncClientConfig();

		establishConnection();
	}

	public EasyncClient(String[] args) {
		this();
		checkArgs(args);
	}

	/**
	 * Starts the establishment of the network connection and the requests two
	 * sockets from the server.
	 */
	private void establishConnection() {
		LOGGER.info("Connecting to " + config.getHost() + ":"
				+ config.getPort() + ". Using sync folder: ["
				+ config.getSyncFolder() + "]");

		socketConnector = new ClientSocketCreator(config);
		socketConnector.setListener(this);
		socketConnector.start();
	}

	@Override
	public void writeLine(String line) {
		if (network != null) {
			network.writeLine(line);
		} else {
			LOGGER.info("Network not connected yet.");
		}
	}

	@Override
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
		if (args == null) {
			new EasyncClient();
		} else {
			new EasyncClient(args);
		}
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

	private void checkArgs(String[] args) {
		args = new String[] { "--autosync" };
		for (String arg : args) {
			if (arg.equals("--autosync")) {
				syncThread = new FileSyncThread(this, config.getSyncFolder(), 10000);
				syncThread.start();
			}
		}
	}
}
