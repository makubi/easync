package easync.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

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
	
	@Override
	public synchronized void receivingFile(String filepath, int bufferSize, int chunks, int leftoverBytes) {
		FileOutputStream fos = null;
		try {
			int dirFileSeparationPos = getFileSeparationPos(filepath);
						
			// The filename starts at the next position after the separation.
			String filename = filepath.substring(dirFileSeparationPos + 1);
			String filedir = filepath.substring(0, dirFileSeparationPos);

			// Create the sub-directory in the sync folder.
			File dir = new File(syncFolder + File.separator + filedir);
			dir.mkdirs();

			// Create the file that should be written to.
			File file = new File(dir.getAbsolutePath() + File.separator + filename);
			file.createNewFile();
			
			fos = new FileOutputStream(file);
			
			byte[] buffer = new byte[bufferSize];
			
			LOGGER.info("Receiving file "+file.getAbsolutePath());
			
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
			// Retrieves the infos from the file.
			int bufferSize = networkFile.getBufferSize();
			long chunks = networkFile.getChunks();
			long leftOverBytes = networkFile.getLeftoverBytes();
			
			// Opens the file for reading.
			File file = networkFile.getFile();
			fis = new FileInputStream(file);

			byte[] buffer = new byte[bufferSize];
			
			// Sets the filepath to be transmitted.
			String transmittedFilepath = getTransmittedFilepath(file.getAbsolutePath());

			LOGGER.info("Sending file "+file.getAbsolutePath());
			
			// Transmits the infos.
			networkOutputHandler.writeLine(NetworkCommands.CMD_SEND_FILE);
			networkOutputHandler.writeLine(transmittedFilepath);
			networkOutputHandler.writeLine(bufferSize);
			networkOutputHandler.writeLine(chunks);
			networkOutputHandler.writeLine(leftOverBytes);

			// Writes the file data to the data stream.
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
