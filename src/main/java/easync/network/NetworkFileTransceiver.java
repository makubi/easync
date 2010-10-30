package easync.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Kuemmert sich um den Dateiempfang und -versand.
 *
 */
public class NetworkFileTransceiver implements FileTransceiverListener {
	
	private Socket dataSocket;
	private NetworkOutputHandler networkOutputHandler;
	
	@Override
	public void receivingFile(String filepath, int bufferSize, int chunks) {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		try {
			int dirFileSeparationPos = getFileSeparationPos(filepath);
			
			// TODO: Achtung auf / und \
			String filename = filepath.substring(dirFileSeparationPos + 1);
			String filedir = filepath.substring(0, dirFileSeparationPos);

			File dir = new File(filedir);
			dir.mkdirs();

			File file = new File(filedir + File.separator + filename);
			file.createNewFile();
			fos = new FileOutputStream(file);

			byte[] buffer = new byte[bufferSize];
			bis = new BufferedInputStream(dataSocket.getInputStream());

			for (int i = 0; i < chunks; i++) {
				int len = bis.read(buffer);
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			NetworkHelper.closeStream(fos);
			NetworkHelper.closeStream(bis);
		}
	}
	
	@Override
	public void sendFile(String filepath) {
		FileInputStream fis = null;
		BufferedOutputStream bos = null;
		try {
			int bufferSize = 512;
			
			File file = new File(filepath);
			fis = new FileInputStream(file);
			
			byte[] buffer = new byte[bufferSize];
			bos = new BufferedOutputStream(
					dataSocket.getOutputStream());
			
			long chunks = file.length() / buffer.length;
			if (file.length() % buffer.length != 0)
				chunks++;

			networkOutputHandler.writeLine(NetworkCommands.CMD_SEND_FILE);
			
			networkOutputHandler.writeLine(filepath);
			networkOutputHandler.writeLine(bufferSize);
			networkOutputHandler.writeLine(chunks);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				bos.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			NetworkHelper.closeStream(fis);
			NetworkHelper.closeStream(bos);
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

	public void setDataSocket(Socket dataSocket) {
		this.dataSocket = dataSocket;
	}

	public void setNetworkOutputHandler(NetworkOutputHandler networkOutputHandler) {
		this.networkOutputHandler = networkOutputHandler;
	}
	
	
	
}
