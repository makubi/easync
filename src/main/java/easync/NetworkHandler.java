package easync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Konkrete Implementierung der Netzwerkfaehigkeit.
 * Diese Klasse kuemmert sich um die Kommunikation zwischen Client und Server.
 * Sie ist auch (noch) zustaendig fuer den Dateiempfang und -versand.
 * Das Lesen des Input-Streams passiert in einer eigenen Klasse.
 * 
 * @see easync.NetworkInputHandler
 *
 */
public class NetworkHandler implements NetworkAccess, Runnable {

	private String server = "localhost";
	private int port = 43443;

	private Socket controlSocket;
	private Socket dataSocket;

	private BufferedReader input;
	private BufferedWriter output;

	private Thread networkThread;
	private Thread networkInputThread;

	
	/**
	 * Beim Aufruf wird der Netzwerk-Thread, der zum Lesen des Input-Streams zustaendig ist, gestartet.
	 * Wenn die connect-Methode aufgerufen wird, werden zwei Sockets (Control, Data) initialisert und eine Verbindung zum Server hergestellt.
	 * @see easync.NetworkHandler#connect()
	 */
	public NetworkHandler() {
		networkThread = new Thread(this);
		networkThread.start();
	}
	
	/**
	 * Beim Aufruf wird der Netzwerk-Thread, der zum Lesen des Input-Streams zustaendig ist, gestartet.
	 * Wird die connect-Methode aufgerufen, werden die uebergebenen Sockets benutzt.
	 * 
	 * @param controlSocket
	 * @param dataSocket
	 */
	public NetworkHandler(Socket controlSocket, Socket dataSocket) {
		this.controlSocket = controlSocket;
		this.dataSocket = dataSocket;

		networkThread = new Thread(this);
		networkThread.start();
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
			networkInputThread = new NetworkInputHandler(this);
			networkInputThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
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
	
	
	/**
	 * Schreibt eine int-Zahl auf den control-stream.
	 * 
	 * @param number - Zahl, die uebertragen werden soll
	 */
	public void writeLine(int number) {
		writeLine(""+number);
	}
	
	/**
	 * Schreibt eine long-Zahl auf den control-stream.
	 * 
	 * @param number - Zahl, die uebertragen werden soll
	 */
	public void writeLine(long number) {
		writeLine(""+number);
	}

	@Override
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
	public void receivingFile(String filepath, int bufferSize, int chunks) {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		try {
			int dirFileSeparationPos = getFileSeparationPos(filepath);
			
			// TODO: Achtung auf / und \
			String filename = filepath.substring(dirFileSeparationPos + 1);
			String filedir = filepath.substring(0, dirFileSeparationPos);

			File dir = new File(filedir);
			dir.mkdirs();

			File file = new File(filedir + File.separator + filename);
			file.createNewFile();
			fos = new FileOutputStream(file);

			byte[] buffer = new byte[bufferSize];
			bis = new BufferedInputStream(dataSocket.getInputStream());

			for (int i = 0; i < chunks; i++) {
				int len = bis.read(buffer);
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				bis.close();
				// TODO: Close streams
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Liefert die Position, bei der Datei- und Ordnername getrennt werden.
	 * Position des ersten '/' oder '\' der von hinten im Dateipfad gefunden wird.
	 * 
	 * @param filepath - Dateipfad, der ueberprueft werden soll
	 * @return Position des hintersten Trennzeichens 
	 */
	private int getFileSeparationPos(String filepath) {
		int pos = filepath.lastIndexOf('/');
		if(pos == -1)
			pos = filepath.lastIndexOf('\\');
		return pos;
	}

	@Override
	public void sendFile(String filepath) {
		FileInputStream fis = null;
		BufferedOutputStream bos = null;
		try {
			int bufferSize = 512;
			
			File file = new File(filepath);
			fis = new FileInputStream(file);
			
			byte[] buffer = new byte[bufferSize];
			bos = new BufferedOutputStream(
					dataSocket.getOutputStream());
			
			long chunks = file.length() / buffer.length;
			if (file.length() % buffer.length != 0)
				chunks++;

			writeLine(NetworkCommands.CMD_SEND_FILE);
			
			writeLine(filepath);
			writeLine(bufferSize);
			writeLine(chunks);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				bos.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bos.close();
				// TODO: Close streams
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public BufferedReader getInput() {
		return input;
	}

}
