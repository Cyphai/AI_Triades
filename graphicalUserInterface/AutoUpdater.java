package graphicalUserInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tools.Config;
import tools.GenericTools;
import translation.Messages;
import client.Session;
import dataPack.DataPack;

public class AutoUpdater {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Program.setIsTriade(true);
		
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

		
		File oldFile = new File(Config.settingsDirectory+File.separator+"datapack.dte"); //$NON-NLS-1$
		File newFile = new File(Config.settingsDirectory+File.separator+"new_datapack.dte"); //$NON-NLS-1$


		String address = null;
		if (!newFile.exists())
		{
			boolean ok = false;
			while (!ok)
			{
				address = JOptionPane.showInputDialog(Messages.getString("Version_1_0_3.AutoUpdater.2")); //$NON-NLS-1$
				if (address == null)
					return;
				ok = GenericTools.downloadFile(address, Config.settingsDirectory+File.separator+"new_datapack.dte"); //$NON-NLS-1$
			}	
			newFile = new File(Config.settingsDirectory+File.separator+"new_datapack.dte"); //$NON-NLS-1$

		}

		if (!oldFile.exists())
		{
			newFile.renameTo(oldFile);
			return;
		}

		DataPack oldDatapack = (DataPack)Program.loadSavableObject(oldFile.getAbsolutePath(), true);
		DataPack newDatapack = (DataPack)Program.loadSavableObject(newFile.getAbsolutePath(), true);
		if (oldDatapack == null)
		{
			if (DialogHandlerFrame.showYesNoCancelDialog(Messages.getString("Version_1_0_3.AutoUpdater.5")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
			{
				backupAndRename(oldFile);
				newFile.renameTo(new File(Config.settingsDirectory+File.separator+"datapack.dte")); //$NON-NLS-1$
			}
			return;
		}
		if (newDatapack == null)
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.AutoUpdater.7")); //$NON-NLS-1$
			return;
		}

		if (oldDatapack.getStringTranslator() != null && !oldDatapack.getStringTranslator().isEmpty())
		{	
			if (newDatapack.getStringTranslator() != null && !newDatapack.getStringTranslator().isEmpty())
			{
				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_1_0_3.AutoUpdater.8")) == JOptionPane.NO_OPTION) //$NON-NLS-1$
				{
					newDatapack.setStringTranslator(oldDatapack.getStringTranslator());
				}
			}
			else
			{
				newDatapack.setStringTranslator(oldDatapack.getStringTranslator());
			}
		}

		if (oldDatapack.getExportModule().getSessionList().size() > 0)
		{
			for (Session s : oldDatapack.getExportModule().getSessionList())
			{
				if (newDatapack.getExportModule().getSessionList().contains(s))
				{
					if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_1_0_3.AutoUpdater.9")+s.getName()+Messages.getString("Version_1_0_3.AutoUpdater.10")+s.getName()+Messages.getString("Version_1_0_3.AutoUpdater.0")) == JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					{
						s.setName(s.getName()+Messages.getString("Version_1_0_3.AutoUpdater.0")); //$NON-NLS-1$
					}
					else
					{
						continue;
					}
				}
				newDatapack.getExportModule().getSessionList().add(s);
			}
		}

		newDatapack.copyDemoData(oldDatapack, address);

		backupAndRename(oldFile);
		newDatapack.setFilePath(Config.settingsDirectory+File.separator+"datapack.dte"); //$NON-NLS-1$
		Program.save(newDatapack, true);
		newFile.delete();
		DialogHandlerFrame.showInformationDialog(Messages.getString("Version_1_0_3.AutoUpdater.14")); //$NON-NLS-1$
	}

	private static void backupAndRename(File oldFile)
	{
		String oldPath = oldFile.getAbsolutePath();
		File backupFile = new File(Config.settingsDirectory+File.separator+"datapack_backup"+File.separator+"datapack_"+new SimpleDateFormat("dd_MM_yy-HH_mm").format(new Date())+".dte"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		backupFile.getParentFile().mkdirs();
		if (backupFile.exists())
		{
			if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_1_0_3.AutoUpdater.15")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
			{
				if (backupFile.delete())
				{
					oldFile.renameTo(backupFile);
				}
				else
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.AutoUpdater.16"));//$NON-NLS-1$
					oldFile.delete();
				}
			}
		}
		else
		{
			oldFile.renameTo(backupFile);
		}
		
		File verifFile = new File(oldPath);
		if (verifFile.exists())
		{
			verifFile.delete();
			
		}
	}

}
