package easync;

import java.io.File;

public abstract class EasyncConfig {
	protected final File configDir = new File(System.getProperty("user.home")
			+ File.separator + ".easync" + File.separator);
}
