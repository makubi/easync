package easync.network;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Kuemmert sich um das Senden ueber den Control-Stream.
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
	 * Schreibt eine Zeile auf den control-stream.
	 * 
	 * @param line - Text, der geschrieben werden soll
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
	 * Schreibt eine int-Zahl auf den control-stream.
	 * 
	 * @param number
	 *            - Zahl, die uebertragen werden soll
	 */
	public void writeLine(int number) {
		writeLine("" + number);
	}

	/**
	 * Schreibt eine long-Zahl auf den control-stream.
	 * 
	 * @param number
	 *            - Zahl, die uebertragen werden soll
	 */
	public void writeLine(long number) {
		writeLine("" + number);
	}
	
	public void setOutputStream(BufferedWriter output) {
		this.output = output;
	}
}
