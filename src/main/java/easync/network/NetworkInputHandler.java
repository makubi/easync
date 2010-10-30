package easync.network;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Liest den Input-Stream des Control-Sockets und startet beispielsweise eine Methode zum Empfang einer Datei.
 *
 */
public class NetworkInputHandler extends Thread {
	
	private BufferedReader input;
	private FileTransceiverListener networkFileTransceiver;
	
	@Override
	public void run() {
		String line;
		try {
			while((line = input.readLine()) != null) {
				System.out.println(line);
				
				if(line.equals(NetworkCommands.CMD_SEND_FILE)) {
					String filepath = input.readLine();
					int bufferSize = Integer.parseInt(input.readLine());
					int chunks = Integer.parseInt(input.readLine());
					networkFileTransceiver.receivingFile(filepath, bufferSize, chunks);
				}
				/*
				String[] data = line.split(":");
				String command = data[0];
				String status = null;
				String message = null;
				
				if(data.length > 1)
					status = data[1];
				
				if(data.length > 2)
					message = data[2];
				*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setInputStream(BufferedReader input) {
		this.input = input;
	}

	public void setNetworkFileTransceiver(
			FileTransceiverListener networkFileTransceiver) {
		this.networkFileTransceiver = networkFileTransceiver;
	}

	
	
}
