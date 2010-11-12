package easync.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import easync.config.EasyncClientConfig;
import easync.network.NetworkHandler;

/**
 * Main class of the client.
 *
 */
public class EasyncClient {

	private final static Logger LOGGER = Logger.getLogger(EasyncClient.class);
	
	private NetworkHandler network;
	private EasyncClientConfig config;
	
	public EasyncClient() {
		config = new EasyncClientConfig();
		
		establishConnection();
		
		// Testing connection.
		writeLine("Client says: Hello! :-)");
		
		// Testing file transmission (sync).
		for(String file : new File(config.getSyncFolder()).list()) {
			transmitFile(file);
		}
	}
	
	private void establishConnection() {
		Socket controlSocket;
		Socket dataSocket;
		try {
			// TODO: Funktionalitaet in Netzwerkklasse (Client) auslagern.
			// Retrieves the first socket.
			controlSocket = new Socket();
			controlSocket.connect(new InetSocketAddress(config.getHost(), config.getPort()), 5000);
			int num = controlSocket.getInputStream().read();
			LOGGER.debug("Client got num: "+num);
			controlSocket.getOutputStream().write(num);
			controlSocket.getOutputStream().flush();
			
			// Retrieves the second socket.
			dataSocket = new Socket();
			dataSocket.connect(new InetSocketAddress(config.getHost(), config.getPort()), 5000);
			// Just ignore the second number you get.
			dataSocket.getInputStream().read();
			dataSocket.getOutputStream().write(num);
			dataSocket.getOutputStream().flush();
			
			// Initializes the network.
			network = new NetworkHandler(controlSocket, dataSocket, config);
			network.connect();
		} catch (UnknownHostException e) {
			LOGGER.error("Unknown host "+config.getHost(), e);
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured.", e);
		}
	}
	
	public void writeLine(String line) {
		network.writeLine(line);
	}
	
	public void transmitFile(String filepath) {
		try {
			network.transmitFile(filepath);
		} catch (FileNotFoundException e) {
			LOGGER.error("The file " + filepath + " was not found. File transmission aborted.", e);
		}
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}
}
