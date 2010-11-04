package easync.network;

import easync.filehandling.NetworkFile;

public interface FileTransceiverListener {
	
	/**
	 * Starts the receiving of a file on the data stream.
	 * 
	 * @param filepath - Filepath (folder/filename)
	 * @param bufferSize - Size of the buffer
	 * @param chunks - Parts of data
	 */
	public void receivingFile(String filepath, int bufferSize, int chunks, int leftoverBytes);
	
	/**
	 * Transmits a file over the network.
	 * @param networkFile - NetworkFile to be sent
	 */
	public void transmitFile(NetworkFile networkFile);
}
