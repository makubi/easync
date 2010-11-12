package easync.client;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import easync.config.EasyncClientConfig;
import easync.server.EasyncServer;

@Ignore
public class EasyncClientTest {

	private final static Logger LOGGER = Logger.getLogger(EasyncClientTest.class);
	
	private static EasyncClient client;
	
	@SuppressWarnings("unused")
	private static EasyncServer server;
	
	private static EasyncClientConfig config = Mockito.mock(EasyncClientConfig.class);
	
	@BeforeClass
	public static void setUpClass() {
		Thread serverThread = new Thread() {
			@Override
			public void run() {
				server = new EasyncServer();
			}
		};
		serverThread.start();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client = new EasyncClient();
		EasyncClientConfig conf = new EasyncClientConfig();
		Mockito.when(config.getSyncFolder()).thenReturn(conf.getSyncFolder());
	}
	
	@Test
	public void testWriteLine() {
		client.writeLine("Client says: Hello! :-)");
	}

	@Test
	public void testTransmitFile() {
		try {
			File file = File.createTempFile("easync_test", null);
			client.transmitFile(file.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured. Could not create tempfile",e);
		}

	}
	
	@Test
	public void fileSyncTest() {
		for (String file : new File(config.getSyncFolder()).list()) {
			client.transmitFile(file);
		}
	}

}
