package easync.config;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.security.auth.DestroyFailedException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyncClientConfigTest {

	private EasyncClientConfig properties;

	private static String host;
	private static String syncFolder;

	@BeforeClass
	public static void setUpClass() {
		EasyncClientConfig properties = new EasyncClientConfig();
		host = properties.getHost();
		syncFolder = properties.getSyncFolder();
	}
	
	@Before
	public void setUp() {
		properties = new EasyncClientConfig();
		properties.setHost(host);
		properties.setSyncFolder(syncFolder);
	}
	
	@AfterClass
	public static void tearDownClass() {
		EasyncClientConfig properties = new EasyncClientConfig();
		properties.setHost(host);
		properties.setSyncFolder(syncFolder);
	}
	
	@After
	public void tearDown() {
		File tmpSyncDir = new File(properties.getSyncFolder());
		if(tmpSyncDir.exists()) {
			if(!tmpSyncDir.delete()) {
				try {
					throw new DestroyFailedException("Could not delete folder "+tmpSyncDir.getAbsolutePath());
				} catch (DestroyFailedException e) {}
			}
		}
	}

	@Test
	public void testPutHost() {
		String value = "my.host";
		properties.setHost(value);
		assertEquals(value, properties.getHost());
	}
	
	@Test
	public void testGetHost() {
		assertEquals(host, properties.getHost());
	}
	
	@Test
	public void testPutSyncFolder() {
		String value = "/tmp/sync";
		properties.setSyncFolder(value);
		assertEquals(value, properties.getSyncFolder());
	}

}
