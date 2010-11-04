package easync.network;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Reads the input stream of the control socket and e.g. starts the method of the receiving of a file.
 */
public class NetworkInputHandler extends Thread {
	
	private BufferedReader input;
	private FileTransceiverListener networkFileTransceiver;
	
	@Override
	public void run() {
		String line;
		try {
			while((line = input.readLine()) != null) {
				System.out.println("Received command: "+line);
				
				if(line.equals(NetworkCommands.CMD_SEND_FILE)) {
					String filepath = input.readLine();
					int bufferSize = Integer.parseInt(input.readLine());
					int chunks = Integer.parseInt(input.readLine());
					int leftoverBytes = Integer.parseInt(input.readLine());
					networkFileTransceiver.receivingFile(filepath, bufferSize, chunks, leftoverBytes);
				}
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
