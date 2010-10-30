package easync.network;

public interface FileTransceiverListener {

	public void receivingFile(String filepath, int bufferSize, int chunks);
	public void sendFile(String filepath);
}
