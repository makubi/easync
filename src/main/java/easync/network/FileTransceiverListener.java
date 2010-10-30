package easync.network;

public interface FileTransceiverListener {
	
	/**
	 * Startet den Empfang einer Datei am Data-Stream.
	 * 
	 * @param filepath - Dateipfad (Ordner/Dateiname)
	 * @param bufferSize - Groesse das Puffers
	 * @param chunks - Datenteile
	 */
	public void receivingFile(String filepath, int bufferSize, int chunks);
	
	/**
	 * Sendet eine Datei ueber das Netzwerk.
	 * 
	 * @param filepath - Pfad zur Datei, die gesendet werden soll
	 */
	public void sendFile(String filepath);
}
