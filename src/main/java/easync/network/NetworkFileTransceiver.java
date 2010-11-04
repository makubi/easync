package easync.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import easync.filehandling.NetworkFile;

/**
 * Implements functionality to receive and send files.
 */
public class NetworkFileTransceiver implements FileTransceiverListener {
	
	private NetworkOutputHandler networkOutputHandler;
	private BufferedInputStream dataInput;
	private BufferedOutputStream dataOutput;
	
	private String syncFolder;
	
	// TODO: Implementierung ueber Kommando-Objekte und Queues, um
	// Race-Conditions (und synchronized-Bloecke) zu vermeiden.
	
	@Override
	public synchronized void receivingFile(String filepath, int bufferSize, int chunks) {
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

			for (int i = 0; i < chunks; i++) {
				int len = dataInput.read(buffer);
				fos.write(buffer, 0, len);
				fos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			NetworkHelper.closeStream(fos);
		}
	}
	
	@Override
	@Deprecated
	public synchronized void sendFile(String filepath) {
		FileInputStream fis = null;
		try {
			int bufferSize = 512;
			
			filepath = getFullFilepath(filepath);
						
			File file = new File(filepath);
			fis = new FileInputStream(file);
			
			byte[] buffer = new byte[bufferSize];
			
			long chunks = file.length() / buffer.length;
			if (file.length() % buffer.length != 0)
				chunks++;

			networkOutputHandler.writeLine(NetworkCommands.CMD_SEND_FILE);
			
			String transmittedFilepath = getTransmittedFilepath(filepath);
			
			networkOutputHandler.writeLine(transmittedFilepath);
			networkOutputHandler.writeLine(bufferSize);
			networkOutputHandler.writeLine(chunks);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				dataOutput.write(buffer, 0, len);
				dataOutput.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			NetworkHelper.closeStream(fis);
		}
	}
	
	@Override
	public void transmitFile(NetworkFile networkFile) {
		FileInputStream fis = null;
		try {
			String filepath = networkFile.getPath();
			int bufferSize = networkFile.getBufferSize();
			long chunks = networkFile.getChunks();
			
			File file = new File(filepath);
			fis = new FileInputStream(file);
			
			networkOutputHandler.writeLine(NetworkCommands.CMD_SEND_FILE);

			byte[] buffer = new byte[bufferSize];
			String transmittedFilepath = getTransmittedFilepath(filepath);
			
			networkOutputHandler.writeLine(transmittedFilepath);
			networkOutputHandler.writeLine(bufferSize);
			networkOutputHandler.writeLine(chunks);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				dataOutput.write(buffer, 0, len);
				dataOutput.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
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
	 * Checks, if the file path was given relatively to the sync folder and adds it, if this applies.
	 * 
	 * @param filepath - Path to the file that should be checked
	 * @return Full file path
	 */
	private String getFullFilepath(String filepath) {
		return !filepath.startsWith(File.separator) ? filepath = syncFolder + File.separator + filepath : filepath;
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
