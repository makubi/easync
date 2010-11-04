package easync.filehandling;

import java.util.concurrent.BlockingQueue;

import easync.network.NetworkFileTransceiver;

/**
 * This class cares about sending a file over the network, if a
 * {@link NetworkFile} is found in the queue. It uses a thread that is paused,
 * after an element is retrieved from the queue. If an element is added to the
 * queue, this thread has to be unpaused. *
 */
public class NetworkFileQueueHandler extends Thread {

	private BlockingQueue<NetworkFile> queue;
	private NetworkFileTransceiver networkFileTransceiver;

	public void setFileQueue(BlockingQueue<NetworkFile> queue) {
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
				networkFileTransceiver.transmitFile(queue.take());
				System.out.println("Queue sending file! :-)");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
