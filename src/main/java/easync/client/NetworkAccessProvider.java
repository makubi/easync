package easync.client;



public interface NetworkAccessProvider {
	
	/**
	 * Writes a line to the control stream.
	 * @param line - Text to be written
	 */
	public void writeLine(String line);
	
	/**
	 * Transmits a file, uses the data stream for that.
	 * @param filepath - Path to the file to be transmitted
	 */
	public void transmitFile(String filepath);
	
}
