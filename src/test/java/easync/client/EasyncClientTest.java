package easync.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import easync.config.EasyncClientConfig;
import easync.config.EasyncClientConfigTest;
import easync.config.EasyncServerConfig;
import easync.server.EasyncServer;
@Ignore
public class EasyncClientTest {
	
	private final static Logger LOGGER = Logger.getLogger(EasyncClientTest.class);
		
	private final static String host = "localhost";
	private final static int port = 43443;
	private final static File clientSyncDir = new File("/tmp/easync/client");
	private final static File serverWorkDir = new File("/tmp/easync/server");
	private static File tempFile;
	
	@SuppressWarnings("unused")
	private static EasyncServer server;
	private static EasyncClient client;
	private static EasyncServerConfig serverConf;
	private static EasyncClientConfig clientConf;
	
	@BeforeClass
	public static void setUpClass() {
		initProperties();
		initNetwork();
		
		if(!clientSyncDir.exists()) {
			clientSyncDir.mkdirs();
		}
		if(!serverWorkDir.exists()) {
			serverWorkDir.mkdirs();
		}
		
		try {
			tempFile = File.createTempFile("easync_tmp", null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void testSendFile() {
		client.transmitFile(tempFile.getAbsolutePath());
		Assert.assertTrue(new File(clientSyncDir+File.separator+tempFile.getName()).exists());
	}

	private static void initProperties() {
		serverConf = mock(EasyncServerConfig.class);
		clientConf = mock(EasyncClientConfig.class);
		
		when(serverConf.getPort()).thenReturn(port);
		when(serverConf.getWorkDir()).thenReturn(serverWorkDir.getAbsolutePath());
		when(clientConf.getHost()).thenReturn(host);
		when(clientConf.getPort()).thenReturn(port);
		when(clientConf.getSyncFolder()).thenReturn(clientSyncDir.getAbsolutePath());
	}

	private static void initNetwork() {
		server = new EasyncServer();
		client = new EasyncClient();
	}
}
