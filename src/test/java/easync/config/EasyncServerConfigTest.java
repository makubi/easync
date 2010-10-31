package easync.config;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.security.auth.DestroyFailedException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import easync.config.EasyncServerConfig;

public class EasyncServerConfigTest {

	private EasyncServerConfig properties;

	private static String workDir;

	@BeforeClass
	public static void setUpClass() {
		EasyncServerConfig properties = new EasyncServerConfig();
		workDir = properties.getWorkDir();
	}
	
	@Before
	public void setUp() {
		properties = new EasyncServerConfig();
		workDir = properties.getWorkDir();
	}
	
	@AfterClass
	public static void tearDownClass() {
		EasyncServerConfig properties = new EasyncServerConfig();
		properties.setWorkDir(workDir);
	}
	
	@After
	public void tearDown() {
		File tmpWorkDir = new File(properties.getWorkDir());
		if(tmpWorkDir.exists()) {
			if(!tmpWorkDir.delete()) {
				try {
					throw new DestroyFailedException("Could not delete folder "+tmpWorkDir.getAbsolutePath());
				} catch (DestroyFailedException e) {}
			}
		}
	}
	
	@Test
	public void testPutworkDir() {
		String value = "/tmp/workDir";
		properties.setWorkDir(value);
		assertEquals(value, properties.getWorkDir());
	}
	
}
