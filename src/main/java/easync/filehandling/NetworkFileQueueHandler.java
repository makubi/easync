package easync.filehandling;

import easync.network.NetworkFileTransceiver;

/**
 * Diese Klasse kuemmert sich um das Senden einer Datei, wenn ein NetworkFile in der Queue gefunden wird bzw. wenn dieser Thread durch das Hinzufuegen wieder gestartet wird.
 *
 */
public class NetworkFileQueueHandler extends Thread {

	private FileQueue queue;
	private NetworkFileTransceiver networkFileTransceiver;

	public void setFileQueue(FileQueue queue) {
		this.queue = queue;
	}

	public void setNetworkFileTransceiver(
			NetworkFileTransceiver networkFileTransceiver) {
		this.networkFileTransceiver = networkFileTransceiver;
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					wait();
					System.out.println("Queue sending file! :-)");
					networkFileTransceiver.transmitFile(queue.getNextNetworkFile());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
