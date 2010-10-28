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
	
	public void writeLine(int number) {
		writeLine(""+number);
	}
	
	public void writeLine(long number) {
		writeLine(""+number);
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
