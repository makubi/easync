package easync.network;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Takes care of the transmission over the control stream.
 *
 */
public class NetworkOutputHandler extends Thread {

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
			e.printStackTrace();
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
