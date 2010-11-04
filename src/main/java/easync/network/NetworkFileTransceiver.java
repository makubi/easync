package easync.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import easync.config.EasyncClientConfig;
import easync.filehandling.NetworkFile;

/**
 * Implements functionality to receive and send files.
 */
public class NetworkFileTransceiver implements FileTransceiverListener {
	
	private final static Logger LOGGER = Logger.getLogger(NetworkFileTransceiver.class);
	
	private NetworkOutputHandler networkOutputHandler;
	private BufferedInputStream dataInput;
	private BufferedOutputStream dataOutput;
	
	private String syncFolder;
	
	// TODO: Implementierung ueber Kommando-Objekte und Queues, um
	// Race-Conditions (und synchronized-Bloecke) zu vermeiden.
	
	@Override
	public synchronized void receivingFile(String filepath, int bufferSize, int chunks, int leftoverBytes) {
		FileOutputStream fos = null;
		try {
			int dirFileSeparationPos = getFileSeparationPos(filepath);
						
			// TODO: Achtung auf / und \
			String filename = filepath.substring(dirFileSeparationPos + 1);
			String filedir = filepath.substring(0, dirFileSeparationPos);

			File dir = new File(syncFolder + File.separator + filedir);
			dir.mkdirs();

			File file = new File(dir.getAbsolutePath() + File.separator + filename);
			file.createNewFile();
			fos = new FileOutputStream(file);
			
			byte[] buffer = new byte[bufferSize];
			
			// Reads the chunks.
			for (int i = 0; i < chunks; i++) {
				int len = dataInput.read(buffer);
				fos.write(buffer, 0, len);
				fos.flush();
			}
			
			// Reads the leftover bytes.
			buffer = new byte[leftoverBytes];
			int len = dataInput.read(buffer);
			fos.write(buffer, 0, len);
			fos.flush();
			
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured. File receiving aborted.",e);
		} finally {
			NetworkHelper.closeStream(fos);
		}
	}
		
	@Override
	public void transmitFile(NetworkFile networkFile) {
		FileInputStream fis = null;
		try {
			int bufferSize = networkFile.getBufferSize();
			long chunks = networkFile.getChunks();
			long leftOverBytes = networkFile.getLeftoverBytes();
			
			File file = networkFile.getFile();
			fis = new FileInputStream(file);
			
			networkOutputHandler.writeLine(NetworkCommands.CMD_SEND_FILE);

			byte[] buffer = new byte[bufferSize];
			String transmittedFilepath = getTransmittedFilepath(file.getAbsolutePath());
			
			networkOutputHandler.writeLine(transmittedFilepath);
			networkOutputHandler.writeLine(bufferSize);
			networkOutputHandler.writeLine(chunks);
			networkOutputHandler.writeLine(leftOverBytes);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				dataOutput.write(buffer, 0, len);
				dataOutput.flush();
			}
			
		} catch (IOException e) {
			LOGGER.error("An I/O Exception occured. File transmission aborted.",e);
		} finally {
			NetworkHelper.closeStream(fis);
		}
	}
	
	/**
	 * Returns the position, directory- and filename are separated.
	 * Position of the last occurrence of '/' or '\' found in the filepath. 
	 * 
	 * @param filepath - Filepath that should be checked
	 * @return Position of rearmost the separator
	 */
	private int getFileSeparationPos(String filepath) {
		int pos = filepath.lastIndexOf('/');
		if(pos == -1)
			pos = filepath.lastIndexOf('\\');
		return pos;
	}
	
	/**
	 * Determines the file path that should be transmitted.
	 * If the path to the sync folder is found, it is cut off, else the path stays.
	 * 
	 * @param filepath - Path of the file that should be checked
	 * @return File path that should be transmitted
	 */
	private String getTransmittedFilepath(String filepath) {
		return filepath.indexOf(syncFolder) > -1 ? filepath.substring(syncFolder.length()) : filepath;
	}

	public void setDataInput(BufferedInputStream dataInput) {
		this.dataInput = dataInput;
	}
	
	public void setDataOutput(BufferedOutputStream dataOutput) {
		this.dataOutput = dataOutput;
	}

	public void setNetworkOutputHandler(NetworkOutputHandler networkOutputHandler) {
		this.networkOutputHandler = networkOutputHandler;
	}

	public void setSyncFolder(String syncFolder) {
		this.syncFolder = syncFolder;
	}
	
}
