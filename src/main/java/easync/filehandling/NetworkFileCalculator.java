package easync.filehandling;

import java.io.File;

/**
 * 
 * Diese Klasse kuemmert sich um das Berechnen der Parameter fuer ein NetworkFile.
 * Zur ist das nur die Anzahl der Teilstuecke, in der diese Datei gesendet werden soll, da der Pfad schon vorher bekannt sein sollte und die Groesse des Puffers standardgemaess auf 512 gesetzt ist.
 * Wird die Datei zu dem gesetzten Pfad nicht gefunden, wird null von der Methode, die zur Berechnung der Parameter zustaendig ist, zurueckgegeben.
 */
public class NetworkFileCalculator {
	
	/**
	 * Calculates the needed parameters for the given NetworkFile and returns it with calculated values.
	 * This method returns null, if the file related to the path can't be found.
	 * @param networkFile - The NetworkFile, values should be calculated from
	 * @return The calculated NetworkFile or null, if the path to the File can't be found
	 */
	public NetworkFile calculateNetworkFileParams(NetworkFile networkFile) {
		File file = new File(networkFile.getPath());
		
		// Tests, if the file related to the path does exist.
		if(!file.exists()) {
			return null;
		}
		
		// Gets the size of the buffer.
		int bufferSize = networkFile.getBufferSize();
		
		// Calculates the number of chunks.
		long chunks = file.length() / bufferSize;
		if (file.length() % bufferSize != 0)
			chunks++;
		
		// Sets the number of chunks.
		networkFile.setChunks(chunks);
		
		return networkFile;
	}
	
}
