package easync.filehandling;

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * Implementierung einer FileQueue.
 * Diese Klasse ist zustaendig fuer das Hinzufuegen neuer Elemente zur Queue und fuer das Herausnehmen aus dieser.
 *  Wird ein NetworkFile {@link NetworkFile} hinzugefuegt, werden fuer dieses die benoetigten Parameter berechnet.
 */
public class FileSendingQueue implements FileQueue {

	private BlockingQueue<NetworkFile> queue;
	private NetworkFileCalculator calculator;
	
	/**
	 * Erzeugt eine LinkedBlockingQueue und den NetworkFileCalculator.
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
