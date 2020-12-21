package graphicalUserInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import tools.Config;

public class Logger {
	final private PrintStream err = System.err;
	final private PrintStream out = System.out;

	final private String fileLogName = Config.settingsDirectory + "log"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	final private String fileLogTriadeName = "triadeLog"; //$NON-NLS-1$
	final private String fileLogCreatorName = "creatorLog"; //$NON-NLS-1$

	private String fileLogePath;
	
	static private Logger singleton = null;
	
	public void setLoggerTriade(Boolean value) {
		if (value) {
			setLogger(fileLogName + File.separatorChar + fileLogTriadeName); //$NON-NLS-1$
		} else {
			setStandardLogger();
		}
	}

	public void setLoggerCreator(Boolean value) {
		if (value) {
			setLogger(fileLogName + File.separatorChar + fileLogCreatorName); //$NON-NLS-1$
		} else {
			setStandardLogger();
		}
	}

	private void setLogger(String directory) {
		File logPath = new File(directory);
		if ((logPath.exists() && logPath.isDirectory()) == false) {
			logPath.mkdirs();
		}

		String date = new Date().toString();
		date = date.replace(':', '-');

		FileOutputStream fos = null;

		try {
			fileLogePath = directory + File.separatorChar + "log_erreur-" + date; //$NON-NLS-1$
			fos = new FileOutputStream(fileLogePath, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		PrintStream pS = new PrintStream(fos, true);

		System.setErr(pS);
		System.setOut(pS);
	}

	public void setStandardLogger() {
		System.setErr(err);
		System.setOut(out);
	}
	
	public String getFileLogPath() {
		return fileLogePath;
	}
	
	static public Logger getInstance() {
		if(singleton == null) {
			singleton = new Logger();
		}
		
		return singleton;
	}
}
