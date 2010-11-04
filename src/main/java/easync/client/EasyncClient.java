package easync.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import easync.network.NetworkHandler;

/**
 * Main class of the client.
 *
 */
public class EasyncClient {

	private final Logger logger = Logger.getLogger(EasyncClient.class);
	
	private NetworkHandler network;
	
	public EasyncClient() {
		network = new NetworkHandler();
		network.connect();
		
		writeLine("Client says: Hello! :-)");
		
		File tmpFile = new File("/bin/bash");
		try {
			tmpFile = File.createTempFile("easync_tmp", null);
		} catch (IOException e) {
		}
		transmitFile(tmpFile.getAbsolutePath());
		transmitFile("test_file");
		transmitFile("test_file2");
	}
	
	public void writeLine(String line) {
		network.writeLine(line);
	}
	
	public void transmitFile(String filepath) {
		try {
			network.transmitFile(filepath);
		} catch (FileNotFoundException e) {
			logger.error("The file " + filepath + " was not found. File transmission aborted.", e);
		}
	}
	
	public static void main(String[] args) {
		new EasyncClient();
	}

}
