package easync;

import java.io.BufferedReader;


/**
 * Interface des Netzwerkzugriffs.
 *
 */
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
	
	/**
	 * Wird diese Methode aufgerufen, wird mit dem Empfang einer Datei begonnen.
	 * Verwendet zum Empfang der Daten der Datei das Data-Socket.
	 * 
	 * @param filename - Dateiname, der zu uebertragenden Datei
	 * @param bufferSize - Puffer-Groesse
	 * @param chunks - Anzahl der Datenstuecke
	 */
	public void receivingFile(String filename, int bufferSize, int chunks);
	
	/**
	 * Sendet eine Datei.
	 * Verwendet zum Uebertragen der Daten der Datei das Data-Socket.
	 * 
	 * @param filename - Name der Datei, die uebertragen werden soll
	 */
	public void sendFile(String filename);

	/**
	 * Liefert den Input-Stream des Control-Sockets.
	 * 
	 * @return Input-Stream des Control-Sockets
	 */
	public BufferedReader getInput();
}
