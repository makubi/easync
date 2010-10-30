package easync.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EasyncClientConfigTest {

	private EasyncClientConfig properties;

	private int port;

	@Before
	public void setUp() {
		properties = new EasyncClientConfig();
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
