package easync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkHandler implements NetworkAccess, Runnable {

	private String server = "localhost";
	private int port = 43443;

	private Socket controlSocket;
	private Socket dataSocket;

	private BufferedReader input;
	private BufferedWriter output;
	
	private Thread networkThread;
	private Thread networkInputThread;

	public NetworkHandler() {
		networkThread = new Thread(this);
		
		networkThread.start();
	}

	public NetworkHandler(Socket controlSocket, Socket dataSocket) {
		this.controlSocket = controlSocket;
		this.dataSocket = dataSocket;

		networkThread = new Thread(this);
		
		
		networkThread.start();
		
	}

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
			networkInputThread = new NetworkInputHandler(input);
			networkInputThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeLine(String line) {
		try {
			output.write(line + '\n');
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

	@Override
	public void run() {		
		
	}
}
