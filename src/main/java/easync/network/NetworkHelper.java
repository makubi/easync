package easync.network;

import java.io.Closeable;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Provides help functionality for the network.
 */
public class NetworkHelper {

	private final static Logger LOGGER = Logger.getLogger(NetworkHelper.class);

	/**
	 * Closes an object that implements the {@link Closeable} interface. E.g. this can be a stream.
	 * @param closeable - Object to be closed
	 * @see java.io.Closeable
	 */
	public static void closeStream(Closeable closeable) {
		if(closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				LOGGER.error("An I/O Error occured at closing an stream.", e);
			}
		}
	}
}
