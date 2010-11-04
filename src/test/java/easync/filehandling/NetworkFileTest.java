package easync.filehandling;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class NetworkFileTest {

	private final static Logger LOGGER = Logger.getLogger(NetworkFileTest.class);
	
	private final NetworkFile networkFile = new NetworkFile();
	
	private final File file = new File("/tmp/test.tmp");
	private final int bufferSize = 99;
	private final int chunks = 1337;
	private final int leftoverBytes = 123;
	
	@Before
	public void setUp() {
		networkFile.setFile(file);
		networkFile.setBufferSize(bufferSize);
		networkFile.setChunks(chunks);
		networkFile.setLeftoverBytes(leftoverBytes);
	}
	
	@Test
	public void testGetFile() {
		Assert.assertEquals(file, networkFile.getFile());
	}

	@Test
	public void testSetFile() {
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile("easync_test", null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		networkFile.setFile(tmpFile);
		Assert.assertEquals(tmpFile, networkFile.getFile());
	}

	@Test
	public void testGetBufferSize() {
		Assert.assertEquals(bufferSize, networkFile.getBufferSize());
	}

	@Test
	public void testSetBufferSize() {
		int tmpBufferSize = 999;
		networkFile.setBufferSize(tmpBufferSize);
		Assert.assertEquals(tmpBufferSize, networkFile.getBufferSize());
	}

	@Test
	public void testGetChunks() {
		Assert.assertEquals(chunks, networkFile.getChunks());
	}

	@Test
	public void testSetChunks() {
		int tmpChunks = 444;
		networkFile.setChunks(tmpChunks);
		Assert.assertEquals(tmpChunks, networkFile.getChunks());
	}

	@Test
	public void testSetLeftoverBytes() {
		int tmpLeftoverBytes = 1;
		networkFile.setLeftoverBytes(tmpLeftoverBytes);
		Assert.assertEquals(tmpLeftoverBytes, networkFile.getLeftoverBytes());
	}

	@Test
	public void testGetLeftoverBytes() {
		Assert.assertEquals(leftoverBytes, networkFile.getLeftoverBytes());
	}

}
