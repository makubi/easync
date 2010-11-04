package easync.network;

import java.io.Closeable;
import java.io.IOException;

/**
 * Provides help functionality for the network.
 */
public class NetworkHelper {

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
				e.printStackTrace();
			}
		}
	}
}
