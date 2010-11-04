package easync.filehandling;

import java.io.File;

/**
 * Represents a file that is to be sent over the network.
 * First, you need to set the path and then you have to added that object to a queue.
 * The queue should take care of calculating the missing parameters, number of chunks, the bytes that are leftover from file.size%buffer.size and optionally buffer-size. 
 */
public class NetworkFile {
	
	private File file;
	private int bufferSize = 512;
	private long chunks;
	private long leftoverBytes;
	
	/**
	 * Returns the path of the file that should be transmitted.
	 * @return Path of the file that should be transmitted
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Sets the path of the file that should be transmitted. After that, normally the NetworkFile should be add to the related queue.
	 * @param path - Path of the file that should be transmitted
	 */
	public void setFile(File file) {
		this.file = file;
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

	/**
	 * Sets the number of tail bytes. These are the bytes that are the leftovers from filesize%buffer size.
	 * @param leftoverBytes - Size of leftover bytes
	 */
	public void setLeftoverBytes(long leftoverBytes) {
		this.leftoverBytes = leftoverBytes;
	}

	/**
	 * Returns the number of tail bytes. These are the bytes that do not fit to filesize%buffer size.
	 * @return Number of leftover bytes
	 */
	public long getLeftoverBytes() {
		return leftoverBytes;
	}
}
