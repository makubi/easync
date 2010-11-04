package easync.filehandling;

/**
 * Represents a file that is to be sent over the network.
 * First, you need to set the path and then you have to added that object to a queue.
 * The queue should take care of calculating the missing parameters, number of chunks and optionally buffer-size. 
 */
public class NetworkFile {
	
	private String path;
	private int bufferSize = 512;
	private long chunks;
	
	/**
	 * Returns the path of the file that should be transmitted.
	 * @return Path of the file that should be transmitted
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Sets the path of the file that should be transmitted. After that, normally the NetworkFile should be add to the related queue.
	 * @param path - Path of the file that should be transmitted
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Returns the buffer-size that was calculated. It is connected to the number of chunks, so recalculate them, if you change it.
	 * @return The buffer-size to be used or null if not set yet.
	 */
	public int getBufferSize() {
		return bufferSize;
	}
	
	/**
	 * You can override the buffer-size. The standard is 512.
	 * If you do so, do not forget to change the number of chunks.
	 * @param bufferSize - buffer-size to be used
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	/**
	 * Returns the number of chunks the file should be sent in. This number is connected to the buffer-size, so watch out if you change it.
	 * @return The number of chunks or null if not set yet.
	 */
	public long getChunks() {
		return chunks;
	}
	
	/**
	 * Sets the number of chunks the file should be transmitted in.
	 * If this parameter is not null, you should watch out and also re-set the buffer-size.
	 * @param chunks
	 */
	public void setChunks(long chunks) {
		this.chunks = chunks;
	}
	
}
