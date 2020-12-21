package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;

import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import models.BrickView;
import relations.EnsembleRelation;
import relations.RelationBrowser;
import translation.Messages;
import dataPack.AutoSaveCreator;
import dataPack.DataPack;
import dataPack.JTreeActors;

public class MainFrameDatapackCreator extends MainFrame {

	private static final long serialVersionUID = -1230397799638872519L;

	protected DataPackView dataPackView;
	protected Vector<BrickView<BrickVertex,BrickEdge>> modelViewList;
	public OptionNewFrameDatapackCreator myOptionNewFrame;

	public MainFrameDatapackCreator() {
		modelViewList = new Vector<BrickView<BrickVertex,BrickEdge>>();
		setTitle(Messages.getString("MainFrameDatapackCreator.0")); //$NON-NLS-1$
	}

	@Override
	protected JPanel myPanel(Object object, JPanel mainPanel) {
		JTree jta = null;
		PopUpView popUp = new RelationChooserPopUp();
		JPanel pSchema = new JPanel(new BorderLayout());

		if (object == null) {
			if (datapack != null) {
				jta = new JTreeActors(datapack);
				mainJta = (JTreeActors)jta;
				dataPackView = new DataPackView(datapack, this);
				pSchema.add(new TitleBar(IconDatabase.iconSchema,
				Messages.getString("MainFrameDatapackCreator.1")), BorderLayout.NORTH); //$NON-NLS-1$
				pSchema.add(dataPackView, BorderLayout.CENTER);
			}
		} else if (object.getClass() == Brick.class) {
			Brick brick = (Brick) object;
			if (datapack != null) {
				jta = new JTreeActors(datapack, brick);

				BrickView<BrickVertex, BrickEdge> modelView = new BrickView<BrickVertex,BrickEdge>(
						brick, popUp, datapack, ((JTreeActors)jta).getListener());
				pSchema.add(new TitleBar(IconDatabase.iconSchema,
				Messages.getString("MainFrameDatapackCreator.2")), BorderLayout.NORTH); //$NON-NLS-1$
				modelViewList.add(modelView);
				pSchema.add(modelView, BorderLayout.CENTER);
			}
		}

		return drawPanel(jta, popUp, pSchema, mainPanel);
	}

	@Override
	public BrickView<BrickVertex,BrickEdge> getActiveModelView() {

		if (tabbedPane.getSelectedIndex() > 0) {
			try {
				if (tabbedPane.getSelectedComponent() == modelViewList
						.elementAt(tabbedPane.getSelectedIndex() - 1)
						.getParent())
					return modelViewList.elementAt(tabbedPane
							.getSelectedIndex() - 1);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
			for (BrickView<BrickVertex,BrickEdge> mV : modelViewList) {

				if (tabbedPane.getSelectedComponent() == mV.getParent()
						.getParent().getParent().getParent().getParent())
					return mV;
			}
		}
		return null;
	}

	@Override
	public void setDataPack(DataPack dtp) {
		datapack = dtp;

		setTitle(Messages.getString("MainFrameDatapackCreator.0") + " " + dtp); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (autoSaveCreator == null) {
			autoSaveCreator = new AutoSaveCreator();
		}
		
		autoSaveCreator.setDataPack(datapack);
		// Fonction de vidage des brique, modèle et type de brique
		// datapack.getBrickList().clear();
		// datapack.getBrickTypeList().clear();
		// datapack.getModelList().clear();

		tabbedPane.removeAll();
		myOptionNewFrame = new OptionNewFrameDatapackCreator(this, dtp);
		if (datapack == null) {
			return;
		}
		dtp.init();
		//popUpHelp = new PopUpHelpView();
		//popUpHelp.setView(PopUpHelpView.showDataPackHelp());
		tabbedPane.addTab(Messages.getString("MainFrameDatapackCreator.3"), myPanel(null)); //$NON-NLS-1$
		dataPackView = new DataPackView(datapack, this);
	}

	@Override
	public void initialState(MainFrame mf) {
		myOptionNewFrame = new OptionNewFrameDatapackCreator(mf, null);
		myOptionNewFrame.setTitle(Messages.getString("MainFrameDatapackCreator.4")); //$NON-NLS-1$
		myOptionNewFrame.setInitialStep();
		myOptionNewFrame.setVisible(true);
	}

	protected void removeTab() {

		BrickView<BrickVertex,BrickEdge> bV = getActiveModelView();
		if (bV != null)
		{
			modelViewList.remove(bV);
		}
		return;
	}
	
	public void cleanTabAndGraphs()
	{
		int initialTabCount = tabbedPane.getTabCount();
		for (int i = 1; i < initialTabCount; i++)
		{
			tabbedPane.remove(1);
		}
		modelViewList.removeAllElements();
		for (Brick b : datapack.getBrickList())
		{
			b.resetGraph();
		}
	}

	@Override
	protected JMenuBar myMenuBar() {
		JMenuBar result = super.myMenuBar();
		JMenu relation = new JMenu(Messages.getString("MainFrameDatapackCreator.5")); //$NON-NLS-1$
		JMenuItem edit = new JMenuItem(Messages.getString("MainFrameDatapackCreator.6")); //$NON-NLS-1$
		JMenuItem export = new JMenuItem(Messages.getString("MainFrameDatapackCreator.7")); //$NON-NLS-1$
		JMenuItem add = new JMenuItem(Messages.getString("MainFrameDatapackCreator.8")); //$NON-NLS-1$
		JMenuItem replace = new JMenuItem(Messages.getString("MainFrameDatapackCreator.9")); //$NON-NLS-1$
		JMenuItem defaut = new JMenuItem(Messages.getString("MainFrameDatapackCreator.10")); //$NON-NLS-1$
		relation.add(edit);
		relation.add(export);
		relation.add(add);
		relation.add(replace);
		relation.add(defaut);
		result.add(relation);


		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					if (tabbedPane.getTitleAt(i).equals(Messages.getString("MainFrameDatapackCreator.11"))) { //$NON-NLS-1$
						tabbedPane.setSelectedIndex(i);
						return;
					}

				}
				if (datapack.getRelations() == null) {
					datapack.setRelations(new EnsembleRelation());
					System.out.println(Messages.getString("MainFrameDatapackCreator.12")); //$NON-NLS-1$
				}
				tabbedPane.addTab(Messages.getString("MainFrameDatapackCreator.13"), new RelationBrowser(datapack //$NON-NLS-1$
						.getRelations()));
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			}
		});

		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (datapack == null) {
					DialogHandlerFrame
					.showErrorDialog(Messages.getString("MainFrameDatapackCreator.14")); //$NON-NLS-1$
					return;
				}
				datapack.getRelations().setFilePath(null);
				if(Program.save(datapack.getRelations(), ".dtr", //$NON-NLS-1$
				Messages.getString("MainFrameDatapackCreator.16"), true)) { //$NON-NLS-1$
					System.out.println("Impossible de sauvegarder les relations"); //$NON-NLS-1$
				}
			}
		});

		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EnsembleRelation newRelation = (EnsembleRelation) Program
				.loadSavableObject(null, "dtr", //$NON-NLS-1$
				Messages.getString("MainFrameDatapackCreator.18"), false); //$NON-NLS-1$
				if (newRelation != null) {
					if (datapack.getRelations() == null) {
						datapack.setRelations(newRelation);
					} else {
						datapack.getRelations().addEnsembleRelation(newRelation);
					}
					Component tabComponent = Program.myMainFrame.getTabByName(Messages.getString("MainFrameDatapackCreator.19")); //$NON-NLS-1$
					if (tabComponent != null & tabComponent instanceof RelationBrowser)
					{
						RelationBrowser panel = (RelationBrowser)tabComponent;
						panel.refreshList();
					}
				}

			}
		});

		replace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(Messages.getString("MainFrameDatapackCreator.20")); //$NON-NLS-1$
				if (datapack.getRelations() != null) {
					if (DialogHandlerFrame
							.showYesNoDialog(Messages.getString("MainFrameDatapackCreator.21")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
						return;

				}
				EnsembleRelation newRelation = (EnsembleRelation) Program
				.loadSavableObject(null, "dtr", //$NON-NLS-1$
				Messages.getString("MainFrameDatapackCreator.23"), true); //$NON-NLS-1$
				if (newRelation != null)
				{
					datapack.setRelations(newRelation);

					Component tabComponent = Program.myMainFrame.getTabByName(Messages.getString("MainFrameDatapackCreator.24")); //$NON-NLS-1$
					if (tabComponent != null & tabComponent instanceof RelationBrowser)
					{
						RelationBrowser panel = (RelationBrowser)tabComponent;
						panel.refreshList();
					}
				}
			}
		});

		defaut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("MainFrameDatapackCreator.25")) != JOptionPane.YES_NO_CANCEL_OPTION) { //$NON-NLS-1$
					if (datapack.getRelations() == null) {
						datapack.setRelations(EnsembleRelation.getDefaultRelations());
					} else {
						datapack.getRelations().addEnsembleRelation(EnsembleRelation.getDefaultRelations());
					}
					Component tabComponent = Program.myMainFrame.getTabByName(Messages.getString("MainFrameDatapackCreator.26")); //$NON-NLS-1$
					if (tabComponent != null & tabComponent instanceof RelationBrowser)
					{
						RelationBrowser panel = (RelationBrowser)tabComponent;
						panel.refreshList();
					}

				} 
			}
		});
		
		if (ExecutionMode.isDebug()) {
			JMenu debug = new JMenu(Messages.getString("MainFrameDatapackCreator.15")); //$NON-NLS-1$
			JMenuItem checkActors = new JMenuItem(Messages.getString("MainFrameDatapackCreator.17")); //$NON-NLS-1$
			
			checkActors.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					datapack.checkActors();
				}
			});
			
			JMenuItem searchGoalOfMean = new JMenuItem(Messages.getString("MainFrameDatapackCreator.22")); //$NON-NLS-1$
			
			searchGoalOfMean.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String meanName = JOptionPane.showInputDialog(Messages.getString("MainFrameDatapackCreator.27")); //$NON-NLS-1$
					String goals = datapack.getRelations().getAllGoalOfMean(meanName);
					DialogHandlerFrame.showErrorDialog(goals);
				}
			});
			
			JMenuItem checkTriadesStrings = new JMenuItem("Vérifier la traduction"); //$NON-NLS-1$
			
			checkTriadesStrings.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Messages.checkAllTriadesMessages();
				}
			});
			
			JMenuItem printRelations = new JMenuItem("Lister toutes les relations");
			
			printRelations.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					datapack.getRelations().printRelations();
	
				}
			});
			
			JMenuItem cleanAndSave = new JMenuItem("Sauver sans listener");
			cleanAndSave.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Program.save(datapack,true);
				}
			});
			
			
			debug.add(cleanAndSave);
			debug.add(checkActors);
			debug.add(searchGoalOfMean);
			debug.add(checkTriadesStrings);
			debug.add(printRelations);
			
			result.add(debug);
		}

		return result;
	}

}
