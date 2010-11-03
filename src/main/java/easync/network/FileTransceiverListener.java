package easync.network;

import easync.filehandling.NetworkFile;

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
	 * @deprecated Use {@link FileTransceiverListener}{@link #transmitFile(NetworkFile)} instead.
	 */
	@Deprecated
	public void sendFile(String filepath);
	
	/**
	 * Transmits a file over the network.
	 * @param networkFile - NetworkFile to be sent
	 */
	public void transmitFile(NetworkFile networkFile);
}
