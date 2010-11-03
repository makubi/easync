package easync.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import easync.filehandling.NetworkFile;

/**
 * Kuemmert sich um den Dateiempfang und -versand.
 *
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
	 * Liefert die Position, bei der Datei- und Ordnername getrennt werden.
	 * Position des ersten '/' oder '\' der von hinten im Dateipfad gefunden wird.
	 * 
	 * @param filepath - Dateipfad, der ueberprueft werden soll
	 * @return Position des hintersten Trennzeichens 
	 */
	private int getFileSeparationPos(String filepath) {
		int pos = filepath.lastIndexOf('/');
		if(pos == -1)
			pos = filepath.lastIndexOf('\\');
		return pos;
	}
	
	/**
	 * Ueberprueft, ob der Dateipfad relativ zum Sync-Folder angegeben wurde und ergaenzt ihn, falls dies zutrifft.
	 * 
	 * @param filepath - Dateipfad, der ueberprueft werden soll
	 * @return Vollen Dateipfad
	 */
	private String getFullFilepath(String filepath) {
		return !filepath.startsWith(File.separator) ? filepath = syncFolder + File.separator + filepath : filepath;
	}
	
	/**
	 * Ermittelt den Dateipfad, der uebertragen werden soll.
	 * Wird der Pfad zur Datei im Sync-Folder gefunden, wird dieser Pfad abgeschnitten, ansonsten bleibt er gleich.
	 * 
	 * @param filepath - Dateipfad, der ueberprueft werden soll
	 * @return Dateipfad, der uebertragen werden soll
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
