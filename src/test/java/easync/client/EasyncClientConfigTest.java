package easync.client;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyncClientConfigTest {

	private EasyncClientConfig properties;

	private static String host;
	private static int port;

	@BeforeClass
	public static void setUpClass() {
		EasyncClientConfig properties = new EasyncClientConfig();
		host = properties.getHost();
		port = properties.getPort();
	}
	
	@Before
	public void setUp() {
		properties = new EasyncClientConfig();
	}
	
	@AfterClass
	public static void tearDownClass() {
		EasyncClientConfig properties = new EasyncClientConfig();
		properties.setHost(host);
		properties.setPort(port);
	}

	@Test
	public void testPutValidPort() {
		int value = 50000;
		properties.setPort(value);
		assertEquals(value, properties.getPort());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutInvalidPort() {
		int value = -1;
		properties.setPort(value);
		assertEquals(port, properties.getPort());
	}

	@Test
	public void testPutHost() {
		String value = "my.host";
		properties.setHost(value);
		assertEquals(value, properties.getHost());
	}

}
