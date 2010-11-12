package easync.client;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * This class syncs the given folder.
 * It waits the given timeout between every sync event.
 * The file list is always read before syncing.
 * If autosync is set to false, this thread will stop, but has to be started again, if needed.
 */
public class FileSyncThread extends Thread {

	private final static Logger LOGGER = Logger.getLogger(FileSyncThread.class);
	
	private NetworkAccessProvider networkAccess;
	private String syncFolder;
	private int timeout;
	
	private boolean autosync = true;

	public FileSyncThread(NetworkAccessProvider networkAccess,
			String syncFolder, int timeout) {
		this.networkAccess = networkAccess;
		this.syncFolder = syncFolder;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		while (autosync) {
			for (File file : new File(syncFolder).listFiles()) {
				networkAccess.transmitFile(file.getAbsolutePath());
			}
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				LOGGER.error("An InterruptedException occured.",e);
			}
		}
	}
	
	public void setAutosync(boolean autosync) {
		this.autosync = autosync;
	}

	public boolean isAutosync() {
		return autosync;
	}
}
