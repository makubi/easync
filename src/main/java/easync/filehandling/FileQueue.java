package easync.filehandling;

import java.io.FileNotFoundException;

/**
 * 
 * Interface zu einer Queue, die sich um File-Handlinung und Netzwerk kuemmert.
 */
public interface FileQueue {
	
	/**
	 * Adds a new NetworkFile to the queue. 
	 * @param file - NetworkFile that represents the file that should be sent
	 * @throws FileNotFoundException This Exception is thrown, if the path of the file can't be found
	 */
	public void addFileToQueue(NetworkFile file) throws FileNotFoundException;
	
	
	/**
	 * Returns the next NetworkFile and removes it from the queue.
	 * @return The next NetworkFile
	 */
	public NetworkFile getNextNetworkFile();

}
