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
	public void receivingFile(String filepath, int bufferSize, int chunks);
	
	/**
	 * Sends a file over the network.
	 * 
	 * @param filepath - Path to file that should be sent.
	 * @deprecated Use {@link FileTransceiverListener}{@link #transmitFile(NetworkFile)} instead.
	 */
	@Deprecated
	public void sendFile(String filepath);
	
	/**
	 * Transmits a file over the network.
	 * @param networkFile - NetworkFile to be sent
	 */
	public void transmitFile(NetworkFile networkFile);
}
