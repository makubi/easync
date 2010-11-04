package easync.filehandling;

import java.io.FileNotFoundException;

/**
 * Interface to a queue that provides functionality to handle files that should e.g. be sent over the network.
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
