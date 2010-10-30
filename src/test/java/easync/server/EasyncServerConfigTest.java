package easync.server;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyncServerConfigTest {

	private EasyncServerConfig properties;

	private static int port;

	@BeforeClass
	public static void setUpClass() {
		EasyncServerConfig properties = new EasyncServerConfig();
		port = properties.getPort();
	}
	
	@Before
	public void setUp() {
		properties = new EasyncServerConfig();
	}
	
	@AfterClass
	public static void tearDownClass() {
		EasyncServerConfig properties = new EasyncServerConfig();
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
	
}
