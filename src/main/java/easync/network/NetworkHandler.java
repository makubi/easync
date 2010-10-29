package easync.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Implementierung der Netzwerkfaehigkeit.
 * Diese Klasse kuemmert sich um die Kommunikation zwischen Client und Server.
 * Das Lesen des Input-Streams und das Senden ueber den Output-Stream passieren in eigenen Klassen.
 * 
 * @see easync.network.NetworkInputHandler
 * @see easync.network.NetworkOutputHandler
 *
 */
public class NetworkHandler implements Runnable {

	private String server = "localhost";
	private int port = 43443;

	private Socket controlSocket;
	private Socket dataSocket;

	private BufferedReader input;
	private BufferedWriter output;

	private NetworkFileTransceiver networkFileTransceiver;
	
	private NetworkOutputHandler networkOutputThread;
	private NetworkInputHandler networkInputThread;

	
	/**
	 * Wenn die connect-Methode aufgerufen wird, werden zwei Sockets (Control, Data) initialisert und eine Verbindung zum Server hergestellt.
	 * @see easync.network.NetworkHandler#connect()
	 */
	public NetworkHandler() {}
	
	/**
	 * Wird die connect-Methode aufgerufen, werden die uebergebenen Sockets benutzt.
	 * 
	 * @param controlSocket
	 * @param dataSocket
	 */
	public NetworkHandler(Socket controlSocket, Socket dataSocket) {
		this.controlSocket = controlSocket;
		this.dataSocket = dataSocket;
	}

	/**
	 * Baut die Verbindung zum Gegenueber auf und startet das Initialisieren der Streams.
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
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialisiert den Input- und Output-Stream des Control-Sockets.
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
			
			initNetworkHandler();
			
			wireNetworkFileTransceiver();
			wireInputHandler();
			wireOutputHandler();
			
			startNetworkThreads();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Baut den NetworkFileTransceiver zusammen.
	 */
	private void wireNetworkFileTransceiver() {
		networkFileTransceiver.setNetworkOutputHandler(networkOutputThread);
		networkFileTransceiver.setDataSocket(dataSocket);
	}
	
	/**
	 * Baut den NetworkInputHandler zusammen.
	 */
	private void wireInputHandler() {
		networkInputThread.setInputStream(input);
		networkInputThread.setNetworkFileTransceiver(networkFileTransceiver);
	}
	
	/**
	 * Baut den NetworkOutputHandler zusammen.
	 */
	private void wireOutputHandler() {
		networkOutputThread.setOutputStream(output);
	}
	
	/**
	 * Startet den Netzwerk-Input- und Output-Thread.
	 */
	private void startNetworkThreads() {
		networkInputThread.start();
		networkOutputThread.start();
	}
	
	/**
	 * Initialisiert den NetworkTransceiver, NetworkInputHandler und NetworkOutputHandler.
	 */
	private void initNetworkHandler() {
		networkFileTransceiver = new NetworkFileTransceiver();
		networkInputThread = new NetworkInputHandler();
		networkOutputThread = new NetworkOutputHandler();
	}

	@Override
	public void run() {
	}

	/**
	 * Schreibt eine Zeile auf den control-stream.
	 * 
	 * @param line - Text, der geschrieben werden soll
	 * 
	 * @deprecated Dient nur noch als Verbindungsstueck, NetworkOutputHandler sollte direkt angesprochen werden.
	 * @see easync.network.NetworkOutputHandler
	 */
	@Deprecated
	public void writeLine(String line) {
		networkOutputThread.writeLine(line);
	}
	
	/**
	 * Schreibt eine int-Zahl auf den control-stream.
	 * 
	 * @param number - Zahl, die uebertragen werden soll
	 * 
	 * @deprecated Dient nur noch als Verbindungsstueck, NetworkOutputHandler sollte direkt angesprochen werden.
	 * @see easync.network.NetworkOutputHandler
	 */
	@Deprecated
	public void writeLine(int number) {
		writeLine(""+number);
	}
	
	/**
	 * Schreibt eine long-Zahl auf den control-stream.
	 * 
	 * @param number - Zahl, die uebertragen werden soll
	 * 
	 * @deprecated Dient nur noch als Verbindungsstueck, NetworkOutputHandler sollte direkt angesprochen werden.
	 * @see easync.network.NetworkOutputHandler
	 */
	@Deprecated
	public void writeLine(long number) {
		writeLine(""+number);
	}
	
	/**
	 * Sendet eine Datei an das Gegenueber.
	 * 
	 * @param file - Datei, die uebertragen werden soll
	 * 
	 * @deprecated Dient nur noch als Verbindungsstueck, NetworkOutputHandler sollte direkt angesprochen werden.
	 * @see easync.network.NetworkOutputHandler
	 */
	public void sendFile(String file) {
		networkFileTransceiver.sendFile(file);
	}

}
