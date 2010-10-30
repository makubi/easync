package easync.network;

import java.io.Closeable;
import java.io.IOException;

/**
 * Stellt Hilfsmethoden fuer Netzwerkklassen zur Verfuegung.
 * 
 */
public class NetworkHelper {

	/**
	 * Schliesst ein Objekt, dass das Interface Closeable implementiert. Dies kann beispielsweise ein Stream sein.
	 * @param closeable - Objekt, das geschlossen werden soll
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
