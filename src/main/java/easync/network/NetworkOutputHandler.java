package easync.network;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Takes care of the transmission over the control stream.
 *
 */
public class NetworkOutputHandler extends Thread {

	private final static Logger LOGGER = Logger.getLogger(NetworkOutputHandler.class);
	
	private BufferedWriter output;

	// TODO: Implementierung ueber Kommando-Objekte und Queues, um
	// Race-Conditions (und synchronized-Bloecke) zu vermeiden.

	@Override
	public void run() {

	}
	
	/**
	 * Writes a line to the control stream.
	 * 
	 * @param line - Text that should be sent
	 * 
	 */
	public synchronized void writeLine(String line) {
		try {
			output.write(line + '\n');
			output.flush();
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured while writing an line to the output stream.",e);
		}
	}

	/**
	 * Writes an int number to the control stream
	 * 
	 * @param number
	 *            - Number that should be sent
	 */
	public void writeLine(int number) {
		writeLine("" + number);
	}

	/**
	 * Writes a long number to the control stream
	 * 
	 * @param number
	 *            - Number that should be sent
	 */
	public void writeLine(long number) {
		writeLine("" + number);
	}
	
	public void setOutputStream(BufferedWriter output) {
		this.output = output;
	}
}
