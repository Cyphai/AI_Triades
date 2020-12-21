package graphicalUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.EventHandler;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import models.BrickView;
import tools.ConfigCreator;
import tools.ConfigTriades;
import translation.Messages;
import client.export.JpegGenerator;
import dataPack.DataPack;
import dataPack.SavableObject;

public final class Controller {

	private final MainFrame mf;

	Action actionNew = getAction("onButtonNew", "New", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionOpen = getAction("onButtonOpen", "Open", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionSave = getAction("onButtonSave", "Save", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionSaveAs = getAction("onButtonSaveAs", "SaveAs", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionExoprterDataPack = getAction("onButtonExporterDataPack", "ExporterDataPack", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionCloseAll = getAction("onButtonCloseAll", "CloseAll", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionExit = getAction("onButtonExit", "Exit", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionMinimize = getAction("onButtonMinimize", "Minimize", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionClose = getAction("onButtonClose", "Close", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionTreeVisibility = getAction("onCheckButtonTree", "Tree", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionSchemaVisibility = getAction("onCheckButtonSchema", "Schema", //$NON-NLS-1$ //$NON-NLS-2$
			null);
	Action actionMaximize = getAction("onButtonMaximize", "Maximize", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionDelete = getAction("onButtonDelete", "Delete", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionRename = getAction("onButtonRename", "Rename", null); //$NON-NLS-1$ //$NON-NLS-2$
	Action actionRemoveTab = getAction("onButtonRemoveTab", "RemoveTab", null); //$NON-NLS-1$ //$NON-NLS-2$

	ItemListener itemListener = getItemListener("onChange"); //$NON-NLS-1$

	public Controller(MainFrame _mf) {
		mf = _mf;
	}

	private Action getAction(final String command, final String name, Icon icon) {
		return new TrampolineAction(command, name, icon, this);
	}

	private ItemListener getItemListener(String method) {
		return EventHandler.create(ItemListener.class, this,
				method, ""); //$NON-NLS-1$
	}

	public void onButtonNew(ActionEvent e) {
		if (!Program.isTriades()) {
			((MainFrameDatapackCreator) mf).myOptionNewFrame
			.setTitle(Messages.getString("Controller.34")); //$NON-NLS-1$
			((MainFrameDatapackCreator) mf).myOptionNewFrame
			.setLocationRelativeTo(mf);
			((MainFrameDatapackCreator) mf).myOptionNewFrame.setVisible(true);
		} else
			if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Controller.0")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
				((MainFrameTriades)mf).showAssistant(); //$NON-NLS-1$
	}

	public void onButtonOpen(ActionEvent e) {
		SavableObject obj = mf.datapack;
		String str = Messages.getString("Controller.36"); //$NON-NLS-1$
		if (!ExecutionMode.isIntern())
		{
			if (mf.datapack == null)
			{
				DialogHandlerFrame.showErrorDialog(Messages.getString("Controller.1")); //$NON-NLS-1$
				System.exit(-1);
			}
			if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Controller.2")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
				((MainFrameTriades)mf).showAssistant();
			return;
		}
		else
		{
			if (obj != null) {
				int choice = DialogHandlerFrame
						.showYesNoCancelDialog(Messages.getString("Controller.37") + str //$NON-NLS-1$
								+ Messages.getString("Controller.38")); //$NON-NLS-1$
				switch (choice) {
				case JOptionPane.YES_OPTION:
					if(!Program.isTriades()) {
						if (!Program.save(obj, true))
							return;
					} else {
						if (ExecutionMode.isIntern())
						{
							if (!Program.save(obj, true))
								return;
						}
						else
						{

						}
					}
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
				default:
					break;
				}

				mf.autoSaveCreator.setPaused(true);
				mf.autoSaveCreator.removeAutoSavedFile();
				mf.setDataPack(null);
				mf.tabbedPane.removeAll();
			}

			DataPack dtp = (DataPack) Program.loadSavableObject(null, true);
			if (dtp != null) {
				dtp.init();
				mf.setDataPack(dtp);
			}
		}
	}

	public void onButtonSave(ActionEvent e) {
		SavableObject obj = mf.datapack;


		Program.save(obj, true);

	}

	public void onButtonSaveAs(ActionEvent e) {
		SavableObject obj = mf.datapack;

		if (obj != null) {
			String oldFilePath = obj.getFilePath();
			boolean saved;

			obj.setFilePath(null);
			if(!Program.isTriades()) {
				saved = Program.save(obj, true);
				if(saved) {
					ConfigCreator.getInstance().getLastDatapack().addLastObject(obj.getFilePath());
					ConfigCreator.getInstance().save();
				} 
			} else {
				saved = Program.save(obj, true);
			}

			if(!saved) {
				obj.setFilePath(oldFilePath);
			} else {
				String newFilePath = obj.getFilePath();
				obj.setFilePath(oldFilePath);
				mf.autoSaveCreator.removeAutoSavedFile();
				obj.setFilePath(newFilePath);
			}
		}
	}

	public void onButtonExporterDataPack(ActionEvent e) {
		if (Program.isTriades()) {
			JpegGenerator.getSingleton().exportSession(
					mf.getDataPack().getCurrentSession());
		} else {
			mf.datapack.exportDataPack();
		}
	}

	public void onButtonCloseAll(ActionEvent e) {
		SavableObject obj = mf.datapack;

		if (Program.isTriades() && Program.myMainFrame.datapack.getCurrentSession() != null)
		{
			if (!Program.myMainFrame.datapack.getCurrentSession().isValid())
			{
				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Controller.3")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
					return;

			}
			else
			{
				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Controller.4")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
					return;
			}
		}
		else if (!Program.isTriades())
		{
			if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Controller.5")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
				return;
		}

		if(!Program.isTriades()) {
			ConfigCreator.getInstance().save();
		} else {
			ConfigTriades.getInstance().save();
		}

		if (!Program.isTriades())
		{
			int choice = DialogHandlerFrame
					.showYesNoCancelDialog(Messages.getString("Controller.39")); //$NON-NLS-1$
			switch (choice) {
			case JOptionPane.YES_OPTION:
				if (!Program.save(obj, true)) {
					System.out.println("Impossible de sauvegarder le datapack"); //$NON-NLS-1$
					return;
				}
				break;
			case JOptionPane.NO_OPTION:
				break;
			default:
				return;

			}
		}
		else
		{
			Program.save(obj,true);
		}
		if (!Program.isTriades())
			mf.setDataPack(null);
		mf.tabbedPane.removeAll();

		if(Program.isTriades()) {
			((MainFrameTriades)mf).showAssistant();
		}
	}

	public void onButtonExit(ActionEvent e) {
		mf.wouldYouLikeToSaveAndExit();
	}

	public void onButtonMinimize(ActionEvent e) {
		AbstractButton aBut = (AbstractButton) e.getSource();
		aBut.getParent().getParent().getParent().setVisible(false);
		aBut.getParent().getParent().getParent().getParent().getComponent(0)
		.setVisible(true);
	}

	public void onButtonMaximize(ActionEvent e) {
		AbstractButton aBut = (AbstractButton) e.getSource();
		aBut.getParent().setVisible(false);
		aBut.getParent().getParent().getComponent(1).setVisible(true);
	}

	public void onButtonClose(ActionEvent e) {
		mf.pSchema.setVisible(false);
	}

	public void onCheckButtonTree(ActionEvent e) {
		JCheckBoxMenuItem checkBox;
		if (e.getSource().getClass() == JButton.class) {
			checkBox = (JCheckBoxMenuItem) ((JMenu) mf.menuBar.getComponent(2))
					.getMenuComponent(0);
			checkBox.setSelected(false);
			mf.pArborescence.setVisible(false);
		} else {
			checkBox = (JCheckBoxMenuItem) e.getSource();
			mf.pArborescence.setVisible(checkBox.isSelected());
		}
	}

	public void onCheckButtonSchema(ActionEvent e) {
		JCheckBoxMenuItem checkBox;
		if (mf.pSchema != null)
		{
			if (e.getSource().getClass() == JButton.class) {

				checkBox = (JCheckBoxMenuItem) ((JMenu) mf.menuBar.getComponent(2))
						.getMenuComponent(1);
				checkBox.setSelected(false);
				mf.pSchema.setVisible(false);

			} else {
				checkBox = (JCheckBoxMenuItem) e.getSource();
				mf.pSchema.setVisible(checkBox.isSelected());
			}
		}
	}

	public void onButtonRemoveTab(ActionEvent e) {
		int index = mf.tabbedPane.getSelectedIndex();
		if (Program.isTriades() && (index == 0 || index == 1))
			onButtonCloseAll(e);
		else if (index == 0) {
			onButtonCloseAll(e);
		} else if (index > 0)
		{
			if (mf.getClass().equals(MainFrameDatapackCreator.class)) {
				((MainFrameDatapackCreator) mf).removeTab();
			}
			mf.tabbedPane.remove(index);

		}
	}

	public void onButtonDelete(ActionEvent e) {
		int index = mf.tabbedPane.getSelectedIndex();
		if (index > 0) {
			BrickView<?,?> mV = mf.getActiveModelView();
			if (mV != null) {
				mV.getMousePlugin().removeSelectedVertex();
				mV.repaint();
			}
		} else if (index == 0) {
			mf.datapack.removeActor();
		}
	}

	public void onButtonRename(ActionEvent e) {
		int index = mf.tabbedPane.getSelectedIndex();
		if (index == 0) {
			mf.getMainJTree().startEditing();
		}
	}

	public void onChange(ItemEvent event) {
		JRadioButton button = (JRadioButton) event.getItemSelectable();
		OptionNewFrameDatapackCreator onf = ((MainFrameDatapackCreator) mf).myOptionNewFrame;

		if (button == onf.datapack)
			onf.description
			.setText(Messages.getString("Controller.40")); //$NON-NLS-1$
		else if (button == onf.brick)
			onf.description
			.setText(Messages.getString("Controller.41")); //$NON-NLS-1$
		else if (button == onf.newFile)
			onf.descriptionBis.setText(Messages.getString("Controller.42")); //$NON-NLS-1$
		else if (button == onf.openFile)
			onf.descriptionBis.setText(Messages.getString("Controller.43")); //$NON-NLS-1$
	}
}