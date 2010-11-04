package easync.filehandling;

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of a FileQueue.
 * This class provides functionality to add elements to or retrieve some from the used queue.
 * If a {@link NetworkFile} is added, the needed parameters for it are calculated. 
 */
public class FileSendingQueue implements FileQueue {

	private BlockingQueue<NetworkFile> queue;
	private NetworkFileCalculator calculator;
	
	/**
	 * Initializes a {@link LinkedBlockingQueue} and a {@link NetworkFileCalculator}.
	 */
	public FileSendingQueue() {
		queue = new LinkedBlockingQueue<NetworkFile>();
		calculator = new NetworkFileCalculator();
	}
	
	@Override
	public void addFileToQueue(NetworkFile networkFile) throws FileNotFoundException {
		NetworkFile newNetworkFile = calculator.calculateNetworkFileParams(networkFile);
		
		if(networkFile.equals(newNetworkFile)) {
			queue.add(newNetworkFile);
		}
		else {
			throw new FileNotFoundException("The file "+networkFile.getPath()+" was not found.");
		}
	}

	@Override
	public NetworkFile getNextNetworkFile() {
		return queue.poll();
	}

}
