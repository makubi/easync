package easync.socketestablishment;

import easync.server.SocketCheckThread;

/**
 * Every class implementing this interface can be informed, if the connection (two sockets) to the client is established.
 *
 */
public interface ConnectionEstablishedListener {
	
	/**
	 * Contacts the Listener that the connection to the client is connected.
	 * That means, that two sockets are bound to it.
	 * The Listener should then remove the {@link SocketCheckThread} from the local socket-cache.
	 * @param socketCheckThread - SocketCheckThread to be closed
	 */
	public void connectionEstablished(SocketConnector socketConnector);
}
