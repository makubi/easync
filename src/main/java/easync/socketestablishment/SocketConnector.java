package easync.socketestablishment;
import java.net.Socket;

/**
 * 
 * A socket connector takes care of the connection of two sockets to be used by the client connection.
 * After initialization, it holds the control- and the data socket.
 *
 */
public interface SocketConnector {
	
	/**
	 * Returns the control socket. Should only be called, after the sockets are initialized.
	 * A listener can be applied that is going to be contacted when the initialization finished.
	 * @return The socket for the control stream.
	 */
	public Socket getControlSocket();
	
	/**
	 * Returns the data socket. Should only be called, after the sockets are initialized.
	 * A listener can be applied that is going to be contacted when the initialization finished.
	 * @return The socket for the data stream.
	 */
	public Socket getDataSocket();
}
