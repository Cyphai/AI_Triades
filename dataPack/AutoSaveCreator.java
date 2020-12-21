package dataPack;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.Program;

import java.io.File;

import javax.swing.JOptionPane;

import translation.Messages;

public class AutoSaveCreator implements Runnable {
	public static String extentionAutoSave = "~"; //$NON-NLS-1$
	private final static String defaultAutoSamveName = "." + File.separatorChar + "defaultAutoSave.dtp"; //$NON-NLS-1$ //$NON-NLS-2$
	private final static int saveTimer = ExecutionMode.isIntern() ? 300 : 20; // seconde

	private DataPack dataPack;
	private final Thread thread;
	private boolean enable;
	private boolean paused;

	public AutoSaveCreator(DataPack dataPack) {
		this.dataPack = dataPack;

		enable = true;
		thread = new Thread(this);
		thread.start();
	}

	public AutoSaveCreator() {
		this(null);
	}

	@Override
	public void run() {
		boolean crypted = true;

		while (enable) {
			try {
				Thread.currentThread();
				Thread.sleep(saveTimer * 1000);
				
				if (getDataPack() == null || (enable == false) || paused) {
					continue;
				}

				String filePath = getDataPack().getFilePath();

				if (filePath != null) {
					if(filePath.endsWith(extentionAutoSave)) {
						filePath = getPathWithoutAutoSaveExtention(filePath);
					}

					Program.saveObject(getDataPack(), filePath + extentionAutoSave, crypted);
				} else {
					Program.saveObject(getDataPack(), defaultAutoSamveName
							+ extentionAutoSave, crypted);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static public String getPathWithoutAutoSaveExtention(String filePath) {
		if(filePath.endsWith(extentionAutoSave)) {
		return filePath.substring(0, filePath.length()
				- extentionAutoSave.length());
		} else {
			return filePath;
		}
	}

	static public String getAutosaveFilePath(String filePath) {
		if(filePath.endsWith(extentionAutoSave)) {
			return filePath;
		} else {
			return filePath + extentionAutoSave;
		}
	}

	static public void dataPackSaved(SavableObject dataPack) {
		File autoSaveFilePath = new File(getAutosaveFilePath(dataPack.getFilePath()));

		if (autoSaveFilePath.exists() && autoSaveFilePath.isFile()) {
			autoSaveFilePath.delete();
		}
	}

	public static boolean isAutoSaveFile(String filePath) {
		return filePath.endsWith(extentionAutoSave);
	}

	public static boolean hasAutoSaveFile(String filePath) {
		File autoSaveFile = new File(getAutosaveFilePath(filePath));
		return autoSaveFile.exists() && autoSaveFile.isFile();
	}

	public static String askLoadAutoSaveFile(String filePath) {
		if(Program.isTriades()) {
			return filePath;
		}
		
		if (hasAutoSaveFile(filePath)) {
			File autoSaveFile = new File(getAutosaveFilePath(filePath));
			File file = new File(filePath);

			if(autoSaveFile.lastModified() < file.lastModified()) {
				return filePath;
			}
			
			int loadAutosave;
			
			if(ExecutionMode.isIntern() == false) {
				loadAutosave = JOptionPane.YES_OPTION;
			} else {
				loadAutosave = DialogHandlerFrame.showYesNoDialog(Messages.getString("AutoSaveCreator.2")); //$NON-NLS-1$
			}

			if (loadAutosave == JOptionPane.YES_OPTION) {
				if(file.exists()) {
					file.delete();
				}
				autoSaveFile.renameTo(file);
			} else {
				System.out.println("Remove autosave : " + autoSaveFile.getPath()); //$NON-NLS-1$
				autoSaveFile.delete();
			}
		}

		return filePath;
	}

	public void removeAutoSavedFile() {
		if(Program.isTriades()) {
			return;
		}
		
		if(getDataPack() == null || getDataPack().getFilePath() == null) {
			return;
		}
		
		File autoSaveFile = new File(getAutosaveFilePath(getDataPack().getFilePath()));
		
		if(autoSaveFile.exists()) {
			System.out.println("Remove autosave : " + autoSaveFile.getPath()); //$NON-NLS-1$
			autoSaveFile.delete();
		}
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	synchronized public void setEnable(boolean value) {
		enable = value;
	}
	
	public synchronized DataPack getDataPack() {
		return dataPack;
	}

	public synchronized void setDataPack(DataPack dataPack) {
		removeAutoSavedFile();
		
		paused = false;
		this.dataPack = dataPack;
	}
}
