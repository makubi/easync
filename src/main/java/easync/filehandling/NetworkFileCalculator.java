package easync.filehandling;

import java.io.File;


/**
 * This class is used to calculate the needed parameters for a NetworkFile.
 * At the moment this parameters are the number of parts in which it should be transferred, the path of the file (which should be known before), the size of the buffer (standard: 512) and the number of bytes that did not fit to the buffer.
 * 
 */
public class NetworkFileCalculator {
		
	/**
	 * Calculates the needed parameters for the given NetworkFile and returns it with calculated values.
	 * @param networkFile - The NetworkFile, values should be calculated from
	 * @return The calculated NetworkFile
	 */
	public NetworkFile calculateNetworkFileParams(NetworkFile networkFile) {
		File file = networkFile.getFile();
		
		// Gets the size of the buffer.
		int bufferSize = networkFile.getBufferSize();
		
		// Calculates the number of chunks.
		long chunks = file.length() / bufferSize;
		
		// Calculates the number of leftover bytes.
		long LeftoverBytes = file.length() % bufferSize;
		
		// Sets the number of chunks.
		networkFile.setChunks(chunks);
		networkFile.setLeftoverBytes(LeftoverBytes);
		
		return networkFile;
	}

}
