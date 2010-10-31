package easync.config;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import easync.config.EasyncClientConfig;



public class EasyncConfigTest {
	
	private EasyncClientConfig properties;
	
	private static int port;

	@BeforeClass
	public static void setUpClass() {
		EasyncClientConfig properties = new EasyncClientConfig();
		port = properties.getPort();
	}
	
	@Before
	public void setUp() {
		properties = new EasyncClientConfig();
		properties.setPort(port);
	}
	
	@AfterClass
	public static void tearDownClass() {
		EasyncClientConfig properties = new EasyncClientConfig();
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
	public void testGetPort() {
		assertEquals(port, properties.getPort());
	}
}
