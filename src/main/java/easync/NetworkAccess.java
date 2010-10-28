package easync;

import java.io.BufferedReader;


public interface NetworkAccess {

	/**
	 * Schreibe Zeile auf dem control-Stream.
	 * @param line - Zeile, die geschrieben werden soll
	 */
	public void writeLine(String line);
	
	/**
	 * Verbindung herstellen.
	 */
	public void connect();
	
	public void receivingFile(String filename, int bufferSize, int chunks);
	
	public void sendFile(String filename);

	public BufferedReader getInput();
}
