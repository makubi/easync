package easync.filehandling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class NetworkFileCalculatorTest {

	private final static Logger LOGGER = Logger.getLogger(NetworkFileCalculatorTest.class);
	
	private NetworkFileCalculator calculator = new NetworkFileCalculator();
		
	private static NetworkFile networkFile;
	
	private static File file;
	private static int bufferSize;
	private static long chunks;
	private static long leftOverBytes;
	
	@BeforeClass
	public static void setUpClass() {
		networkFile = new NetworkFile();
		file = null;
		bufferSize = 10;
		
		try {
			file = File.createTempFile("easync_test", null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		
		FileOutputStream fos = null;
		byte[] randomData = new byte[512];
		try {
			fos = new FileOutputStream(file);
			
			Random random = new Random();
			random.nextBytes(randomData);
			
			fos.write(randomData);
			fos.flush();
		} catch (IOException e) {
			LOGGER.error(e);
		}
		finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
		networkFile.setFile(file);
		networkFile.setBufferSize(bufferSize);
		chunks = file.length() / bufferSize;
		leftOverBytes = file.length() % bufferSize;
	}
	
	@Test
	public void testCalculateNetworkFileParams() {
		calculator.calculateNetworkFileParams(networkFile);
		
		Assert.assertEquals(file, networkFile.getFile());
		Assert.assertEquals(bufferSize, networkFile.getBufferSize());
		Assert.assertEquals(chunks, networkFile.getChunks());
		Assert.assertEquals(leftOverBytes, networkFile.getLeftoverBytes());
	}

}
