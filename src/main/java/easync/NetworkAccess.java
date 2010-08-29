package easync;


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
}
