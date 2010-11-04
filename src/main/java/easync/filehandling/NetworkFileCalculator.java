package easync.filehandling;

import java.io.File;

/**
 * This class is used to calculate the needed parameters for a NetworkFile.
 * At the moment this parameters are the number of parts in which it should be transferred, the path of the file (which should be known before) and the size of the buffer (standard: 512).
 * If the file at the previous set path can't be found, the method, that calculates the remaining parameters, returns null.  
 * 
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
