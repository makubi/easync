package easync.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import easync.config.EasyncClientConfig;
import easync.socketestablishment.ConnectionEstablishedListener;
import easync.socketestablishment.SocketConnector;

public class ClientSocketCreator extends Thread implements SocketConnector{

	private final static Logger LOGGER = Logger.getLogger(ClientSocketCreator.class);
	
	private Socket controlSocket;
	private Socket dataSocket;
	
	private EasyncClientConfig config;
	
	private ConnectionEstablishedListener listener;
	
	public ClientSocketCreator(EasyncClientConfig config) {
		this.config = config;
	}
	
	public void setListener(ConnectionEstablishedListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void run() {
		try {
			// Retrieves the first socket.
			controlSocket = new Socket();
			controlSocket.connect(new InetSocketAddress(config.getHost(), config.getPort()), 5000);
			int num = controlSocket.getInputStream().read();
			LOGGER.debug("Client got num: "+num);
			controlSocket.getOutputStream().write(num);
			controlSocket.getOutputStream().flush();
			
			// Retrieves the second socket.
			dataSocket = new Socket();
			dataSocket.connect(new InetSocketAddress(config.getHost(), config.getPort()), 5000);
			// Just ignore the second number you get.
			dataSocket.getInputStream().read();
			dataSocket.getOutputStream().write(num);
			dataSocket.getOutputStream().flush();
			
			listener.connectionEstablished(this);
			
		} catch (UnknownHostException e) {
			LOGGER.error("Unknown host "+config.getHost(), e);
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured.", e);
		}
	}
	
	@Override
	public Socket getControlSocket() {
		return controlSocket;
	}

	@Override
	public Socket getDataSocket() {
		return dataSocket;
	}

}
