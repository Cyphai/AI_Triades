package graphicalUserInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.channels.FileLock;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileNameExtensionFilter;

import tools.Config;
import tools.ConfigCreator;
import tools.ConfigTriades;
import tools.GenericTools;
import tools.StringCreator;
import tools.UniqueInstance;
import translation.Messages;
import Mailing.MailErrorForm;
import Mailing.MailView;
import client.export.ExportImagesView;
import dataPack.AutoSaveCreator;
import dataPack.DataPack;
import dataPack.Moyen;
import dataPack.SavableObject;
import encrypting.Encrypting;

public class Program {
	static public final short majorVersion = 1;
	static public final short mediumVersion = 0;
	static public final short minorVersion = 1;

	public static MainFrame myMainFrame;
	private static boolean isTriades;
	private static boolean isTriadesLoading;
	public static FileLock fileLock;

	private static String currentUsedFilePath = null;

	//Actions à effectuer lorsqu'une autre instance essaye de démarrer.
	static final Runnable runOnReceive = new Runnable() {
		public void run() {
			// On exécute ce runnable dans l'EDT
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if(Program.myMainFrame != null) {
						// Si la fenêtre n'est pas visible (uniquement dans le systray par exemple), on la rend visible.
						if(!Program.myMainFrame.isVisible())
							Program.myMainFrame.setVisible(true);
						// On demande à la mettre au premier plan.
						Program.myMainFrame.toFront();
						Program.myMainFrame.validate();
						Program.myMainFrame.repaint();
					}
				}
			});
		}                   
	};

	public static void main(String[] args) {
		isTriades = !(args != null && args.length > 0);
		isTriadesLoading = false;

		if(isTriades == false && ExecutionMode.isIntern() == false) {
			DialogHandlerFrame.showErrorDialog("Datapack Creator en mode extern !"); //$NON-NLS-1$
			System.exit(0);
		}


		ToolTipManager.sharedInstance().setDismissDelay(60000);
		ToolTipManager.sharedInstance().setReshowDelay(50);

		final UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				if (myMainFrame != null && myMainFrame.autoSaveCreator != null) {
					myMainFrame.autoSaveCreator.setPaused(true);

					if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Program.0")+e.getMessage()+Messages.getString("Program.2"))==JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
					{
						new MailView(Messages.getString("Program.4"), new MailErrorForm(e)); //$NON-NLS-1$
					} else {
						askContinueOnErreur();
					}
				}

				e.printStackTrace();


				//oldHandler.uncaughtException(t, e);
			}
		});

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		boolean useLocalhost = isTriades ? ConfigTriades.getInstance().getUseLocalhost() : ConfigCreator.getInstance().getUseLocalhost();

		if(useLocalhost) {
			int port = isTriades ? ConfigTriades.getInstance().getPort() : ConfigCreator.getInstance().getPort();
			UniqueInstance uniqueInstance = new UniqueInstance(port, UniqueInstance.defaultMessage, runOnReceive);
			if(uniqueInstance.launch() == false) {
				DialogHandlerFrame.showErrorDialog(Messages.getString("Program.6")); //$NON-NLS-1$
				System.exit(0);
			}
		} else {
			if(DialogHandlerFrame.showYesNoDialog(Messages.getString("Program.9")) != JOptionPane.YES_OPTION) { //$NON-NLS-1$
				System.exit(0);
			}
		}


		if (!isTriades) {
			if (args[0] != null && args[0].equals("creator")) { //$NON-NLS-1$
				myMainFrame = new MainFrameDatapackCreator();
				myMainFrame.initialState(myMainFrame);
			}
		} else {
			isTriades = false;
			isTriadesLoading = true;

			ExportImagesView.createFiles();

			myMainFrame = new MainFrameTriades();

			try{
				if (ExecutionMode.isIntern())			
					myMainFrame.setDataPack((DataPack) loadSavableObject(null, true));
				else
				{

					String datapackPath = Config.settingsDirectory + "datapack.dte"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					File dtpFile = new File(datapackPath);
					if (!dtpFile.exists())
					{
						if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_1_0_3.Program.18")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
						{
							String address = null;
							do {
								address = JOptionPane.showInputDialog(Messages.getString("Version_1_0_3.Program.19"));//$NON-NLS-1$
								if (address != null)
								{

									if (GenericTools.downloadFile(address, datapackPath))
										address = null;
								}
							} while (address != null);
						}

					}


					DataPack dtp = (DataPack) loadSavableObject(datapackPath, true);
					if (dtp != null)
						myMainFrame.setDataPack(dtp);
					else
						System.exit(-1);
				}

				myMainFrame.initialState(myMainFrame);
				isTriades = true;

			}
			catch (Exception e)
			{
				myMainFrame.initialState(myMainFrame);
				isTriades = true;
			}
		}
	}

	public static SavableObject loadSavableObject(String filepath, boolean crypted) {
		String extension = null;
		String description = null;
		if (!Program.isTriades() && !Program.isTriadesLoading()) {
			description = Messages.getString("Program.1"); //$NON-NLS-1$
			extension = "dtp"; //$NON-NLS-1$
		} else {
			description = Messages.getString("Program.3"); //$NON-NLS-1$
			extension = "dte"; //$NON-NLS-1$
		}
		return (loadSavableObject(filepath, extension, description, crypted));


	}

	public static SavableObject loadSavableObject(String filepath, String extension, String description, boolean crypted)
	{
		return loadSavableObject(filepath, extension, description, crypted, true);
	}

	protected static SavableObject loadSavableObject(String _filePath,
			String extension, String description, boolean crypted, boolean firstTry) {
		String filePath = _filePath;

		SavableObject result = null;

		if (filePath == null) {
			JFileChooser chooser = new JFileChooser(currentUsedFilePath);
			FileNameExtensionFilter filter;
			filter = new FileNameExtensionFilter(description, extension);
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(myMainFrame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filePath = chooser.getSelectedFile().getPath();
				currentUsedFilePath = filePath;
			} else
				return null;
		}

		filePath = AutoSaveCreator.askLoadAutoSaveFile(filePath);

		if(crypted) {
			SavableObject object = (SavableObject)Encrypting.getInstance().loadEncryptedObject(filePath);
			if(object != null) {
				result = object;
			} else {
				System.out.println("Fichier charge null : " + filePath);
			}
		} 

		if (!crypted || result == null)
		{
			FileInputStream file;
			try {
				file = new FileInputStream(filePath);

				ObjectInputStream ois = new ObjectInputStream(file);
				SavableObject temp = (SavableObject) ois.readObject();

				if (filePath.endsWith(AutoSaveCreator.extentionAutoSave)) {
					filePath = AutoSaveCreator
							.getPathWithoutAutoSaveExtention(filePath);
				}

				ConfigCreator.getInstance().getLastDatapack().addLastObject(filePath);
				ConfigCreator.getInstance().save();
				result = temp;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		if(result == null) {
			if (firstTry)
			{
				Moyen.setOldHashCodeMode(true);
				loadSavableObject(filePath, extension, description, crypted, false);
			}
			else
				return null;
		}

		result.setFilePath(filePath);
		if (result instanceof DataPack)
		{
			((DataPack)result).checkDatapackValidity();
		}

		return result;
	}

	public static boolean save(SavableObject object, boolean crypted) {
		boolean result = false;
		if (object instanceof DataPack) {
			DataPack datapack = (DataPack) object;
			EventListenerList listenerList = datapack.getActorsModel().popListenerList();
			result = saveAux(object, crypted);
			datapack.getActorsModel().setListenerList(listenerList);
		} else {
			result = saveAux(object, crypted);
		}
		return result;
	}


	public static boolean saveAux(SavableObject object, boolean crypted) {
		if (!Program.isTriades() && !Program.isTriadesLoading()) {
			return save(object, "dtp", Messages.getString("Program.8"), crypted); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return save(object, "dte", Messages.getString("Program.10"), crypted); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static boolean save(SavableObject object, String extension, String description, boolean crypted) {


		if (object == null) {
			System.err.println("Erreur: object == null, rien à sauvegarder"); //$NON-NLS-1$
			return false;
		}

		String savePath = null;
		File selectedFile = null;
		boolean isAllowedToSave = false;

		if (object.getFilePath() == null) {
			JFileChooser chooser = new JFileChooser(currentUsedFilePath);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					description, extension);
			chooser.setFileFilter(filter);
			while (!isAllowedToSave) {
				int returnVal = chooser.showSaveDialog(myMainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFile = chooser.getSelectedFile();
					savePath = selectedFile.getPath();
					currentUsedFilePath = savePath;
					if (selectedFile.exists()) {
						int returnV = DialogHandlerFrame
								.showYesNoDialog(
										chooser,
										Messages.getString("Program.12") //$NON-NLS-1$
										+ savePath
										+ Messages.getString("Program.13")); //$NON-NLS-1$
						if (returnV == JOptionPane.YES_OPTION) {
							isAllowedToSave = true;
						}
					} else {
						isAllowedToSave = true;
					}
				} else if (returnVal == JFileChooser.CANCEL_OPTION)
					return false;
			}
		} else
			savePath = object.getFilePath();
		if (!savePath.endsWith("." + extension) //$NON-NLS-1$
				&& object.getClass() != ConfigUser.class) {
			savePath += ("." + extension); //$NON-NLS-1$
		}

		boolean result = saveObject(object, savePath, crypted);

		if(result && object.getFilePath() == null) {
			object.setFilePath(savePath);
		}

		return result;
	}

	static public boolean saveObject(SavableObject object, String filePath, boolean crypted) {
		boolean result;

		if(crypted) {
			result = Encrypting.getInstance().saveEncryptedObject(object, filePath);
			if(!result) {
				System.out.println(Messages.getString
						("Program.5") + object + Messages.getString("Program.7") + filePath); //$NON-NLS-1$ //$NON-NLS-2$
			}

			return result;
		}

		try {
			File file = new File(filePath);
			File tmpFile;
			boolean exist = file.exists();

			if (exist) {
				tmpFile = File.createTempFile("datapack", "tmp"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				tmpFile = new File(filePath);
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile));
			oos.writeObject(object);
			oos.close();
			if (exist)
			{
				if(Encrypting.renameFile(tmpFile, file)) {
					System.out.println(Messages.getString("Program.14") + object + Messages.getString("Program.15") + filePath); //$NON-NLS-1$ //$NON-NLS-2$
					return false;
				}
			}
			System.out.println("Enregistrer: success! : " + filePath); //$NON-NLS-1$
			if (filePath != null && AutoSaveCreator.isAutoSaveFile(filePath) == false) {
				AutoSaveCreator.dataPackSaved(object);
			}

			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		System.out.println(Messages.getString("Program.16") + object + Messages.getString("Program.17") + filePath); //$NON-NLS-1$ //$NON-NLS-2$
		return false;
	}

	public static boolean isTriades() {
		return isTriades;
	}

	static public void setIsTriade(boolean value) {
		isTriades = value;
	}

	public static boolean isTriadesLoading() {
		return isTriadesLoading;
	}

	public static void askContinueOnErreur() {
		if(DialogHandlerFrame.showYesNoDialog(Messages.getString("Program.11")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
			Program.myMainFrame.autoSaveCreator.setPaused(false);
		} else {
			System.exit(1);
		}			
	}

	public static String getSoftwareString() {
		return StringCreator.createVersionString(majorVersion, mediumVersion, minorVersion);
	}
}
