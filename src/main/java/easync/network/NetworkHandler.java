package easync.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import easync.config.EasyncClientConfig;
import easync.config.EasyncServerConfig;
import easync.filehandling.NetworkFile;
import easync.filehandling.NetworkFileCalculator;
import easync.filehandling.NetworkFileQueueHandler;

/**
 * Implementation of the network capability. This class takes care of the
 * communication between server and client. The reading and writing on the
 * streams are done in separate classes.
 * 
 * @see easync.network.NetworkInputHandler
 * @see easync.network.NetworkOutputHandler
 */
public class NetworkHandler implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(NetworkHandler.class);
	
	private String server;
	private int port;
	private String syncFolder;

	private Socket controlSocket;
	private Socket dataSocket;

	private BufferedReader input;
	private BufferedWriter output;

	private BufferedInputStream dataInput;
	private BufferedOutputStream dataOutput;

	private BlockingQueue<NetworkFile> fileQueue;
	private NetworkFileCalculator calculator;
	private NetworkFileQueueHandler networkFileQueueHandler;
	private NetworkFileTransceiver networkFileTransceiver;

	private NetworkOutputHandler networkOutputThread;
	private NetworkInputHandler networkInputThread;

	/**
	 * If the connect method is called, two sockets (control, data) are
	 * initialized and a connection to the server is established.
	 * 
	 * @see easync.network.NetworkHandler#connect()
	 */
	public NetworkHandler() {
		EasyncClientConfig config = new EasyncClientConfig();
		server = config.getHost();
		port = config.getPort();
		syncFolder = config.getSyncFolder();
	}

	/**
	 * This constructor is used by the server-side client handler, because it
	 * gets already initialized sockets (see {@link ServerSocket#accept()}. If
	 * the connect method is called, these sockets are used.
	 * 
	 * @param controlSocket
	 *            - Socket for the control stream
	 * @param dataSocket
	 *            - Socket for the data stream
	 * 
	 * @see easync.server.EasyncServer
	 * @see easync.network.NetworkHandler#connect()
	 */
	public NetworkHandler(Socket controlSocket, Socket dataSocket) {
		this.controlSocket = controlSocket;
		this.dataSocket = dataSocket;

		EasyncServerConfig config = new EasyncServerConfig();
		this.syncFolder = config.getWorkDir();
	}

	/**
	 * Connects to the vis-a-vis and starts the initialization of the stream.
	 * 
	 * @see easync.network.NetworkHandler#initStreams()
	 */
	public void connect() {
		try {
			if (controlSocket == null) {
				controlSocket = new Socket();
				controlSocket
						.connect(new InetSocketAddress(server, port), 5000);
			}
			if (dataSocket == null) {
				dataSocket = new Socket();
				dataSocket.connect(new InetSocketAddress(server, port), 5000);
			}
			initStreams();
		} catch (IOException e) {
			LOGGER.error("An I/O Error occured. Is the server "+server+" up and running?", e);
		}
	}

	/**
	 * Initializes the input and the output stream of the control socket.
	 */
	private void initStreams() {
		try {
			if (input == null) {
				input = new BufferedReader(new InputStreamReader(
						controlSocket.getInputStream()));
			}
			if (output == null) {
				output = new BufferedWriter(new OutputStreamWriter(
						controlSocket.getOutputStream()));
			}

			dataInput = new BufferedInputStream(dataSocket.getInputStream());
			dataOutput = new BufferedOutputStream(dataSocket.getOutputStream());

			initNetworkHandler();
			initQueue();

			wireNetworkFileTransceiver();
			wireInputHandler();
			wireOutputHandler();
			wireNetworkFileQueueHandler();

			startNetworkThreads();
		} catch (IOException e) {
			LOGGER.error("An I/O Error occured. Unable to initialize streams.", e);
		}
	}

	/**
	 * Wires the used NetworkFileTransceiver.
	 */
	private void wireNetworkFileTransceiver() {
		networkFileTransceiver.setNetworkOutputHandler(networkOutputThread);
		networkFileTransceiver.setDataInput(dataInput);
		networkFileTransceiver.setDataOutput(dataOutput);
		networkFileTransceiver.setSyncFolder(syncFolder);
	}

	/**
	 * Wires the used NetworkInputHandler.
	 */
	private void wireInputHandler() {
		networkInputThread.setInputStream(input);
		networkInputThread.setNetworkFileTransceiver(networkFileTransceiver);
	}

	/**
	 * Wires the used NetworkOutputHandler.
	 */
	private void wireOutputHandler() {
		networkOutputThread.setOutputStream(output);
	}

	private void wireNetworkFileQueueHandler() {
		networkFileQueueHandler
				.setNetworkFileTransceiver(networkFileTransceiver);
		networkFileQueueHandler.setFileQueue(fileQueue);
	}

	/**
	 * Starts the network input and output thread.
	 */
	private void startNetworkThreads() {
		networkInputThread.start();
		networkOutputThread.start();
		networkFileQueueHandler.start();
	}

	private void initQueue() {
		fileQueue = new LinkedBlockingQueue<NetworkFile>();
		calculator = new NetworkFileCalculator();
	}

	/**
	 * Initializes the NetworkTransceiver, NetworkInputHandler,
	 * NetworkOutputHandler, NetworkFileQueueHandler and the FileQueue.
	 */
	private void initNetworkHandler() {
		networkFileTransceiver = new NetworkFileTransceiver();
		networkInputThread = new NetworkInputHandler();
		networkOutputThread = new NetworkOutputHandler();
		networkFileQueueHandler = new NetworkFileQueueHandler();
	}

	@Override
	public void run() {
	}

	/**
	 * Writes a line to the control streams.
	 * 
	 * @param line
	 *            - Text that should be sent
	 * 
	 * @see easync.network.NetworkOutputHandler
	 */
	public void writeLine(String line) {
		networkOutputThread.writeLine(line);
	}

	/**
	 * Writes an int number to the control stream
	 * 
	 * @param number
	 *            - Number that should be sent
	 * 
	 * @see easync.network.NetworkOutputHandler
	 */
	public void writeLine(int number) {
		networkOutputThread.writeLine(number);
	}

	/**
	 * Writes a long number to the control stream
	 * 
	 * @param number
	 *            - Number that should be sent
	 * 
	 * @see easync.network.NetworkOutputHandler
	 */
	public void writeLine(long number) {
		networkOutputThread.writeLine(number);
	}

	/**
	 * Transmits a file to the vis-a-vis. In fact, it adds the File to a queue
	 * to be transmitted. Before doing that, it checks the filepath. If the file
	 * can't be found there, a FileNotFoundException is thrown.
	 * 
	 * @param filepath
	 *            - Path to the file that should be sent
	 * @throws FileNotFoundException
	 */
	public void transmitFile(String filepath) throws FileNotFoundException {
		NetworkFile networkFile = new NetworkFile();
		File file = new File(getFullFilepath(filepath));

		// Tests, if the file related to the path does exist.
		if (!file.exists()) {
			throw new FileNotFoundException();
		}

		networkFile.setFile(file);
		
		calculator.calculateNetworkFileParams(networkFile);
		fileQueue.add(networkFile);
	}

	/**
	 * Checks, if the file path was given relatively to the sync folder and adds
	 * it, if this applies.
	 * 
	 * @param filepath
	 *            - Path to the file that should be checked
	 * @return Full file path
	 */
	private String getFullFilepath(String filepath) {
		return !filepath.startsWith(File.separator) ? filepath = syncFolder
				+ File.separator + filepath : filepath;
	}

}
