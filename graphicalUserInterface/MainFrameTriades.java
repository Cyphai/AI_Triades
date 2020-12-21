package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import main.ActionTime;
import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import models.BrickView;
import translation.Messages;
import client.ActorsSheetNavigationView;
import client.NavigationTree;
import client.SchemaTree;
import client.Session;
import client.export.ExportInformationPanel;
import client.export.ExportPopUp;
import client.export.ExportTree;
import client.export.ExportView;
import client.export.ExportingMousePlugin;
import dataPack.Acteur;
import dataPack.AutoSaveCreator;
import dataPack.Content;
import dataPack.DataPack;
import dataPack.JTreeActors;

public class MainFrameTriades extends MainFrame {

	private static final long serialVersionUID = -1230397799638872518L;

	protected ActorListView actorListView;

	protected AssistantFrameTriades myAssistantFrameTriades;
	protected Vector<BrickView<?,?>> schemaViewList;

	public MainFrameTriades() {
		setTitle(Messages.getString("MainFrameTriades.0")); //$NON-NLS-1$
		schemaViewList = new Vector<BrickView<?,?>>();
	}

	@Override
	protected JPanel myPanel(Object object, JPanel mainPanel) {
		JTree jta = null;
		PopUpView popUp = null;
		PopUpHelpView popUpHelp = null;
		pSchema = new JPanel(new BorderLayout());

		if (object.getClass() == ActorListView.class) {
			jta = new JTreeActors(datapack);
			mainJta = (JTreeActors)jta;
			actorListView = (ActorListView) object;
			pSchema.add(new TitleBar(IconDatabase.iconSchema,
					Messages.getString("MainFrameTriades.1")), BorderLayout.NORTH); //$NON-NLS-1$
			pSchema.add(actorListView, BorderLayout.CENTER);
			popUpHelp = new PopUpHelpView();
			popUpHelp.setActorListHelp();

			// } else if (object.getClass() == SchemaSelectionView.class) {
			// SchemaSelectionView view = (SchemaSelectionView) object;
			// if (view != null) {
			// jta = new JTreeActors(view.getSchema());
			//
			// pSchema.add(new TitleBar(IconDatabase.iconSchema,
			// "Schémas générés"), BorderLayout.NORTH);
			// pSchema.add(view, BorderLayout.CENTER);
			// }
		} else if (object.getClass() == Brick.class) {
			Brick brick= (Brick) object;

			if (brick != null) {
				BrickView<BrickVertex, BrickEdge> view = new BrickView<BrickVertex, BrickEdge>(brick, new RelationChooserPopUp(), datapack, null);
				jta = new SchemaTree(brick, view.getMousePlugin());
				view.setTreeListener(((JTreeActors)jta).getListener());
				// jta = new JTreeActors(schema);
				popUpHelp = new PopUpHelpView();

				popUpHelp.setNoteMode(datapack.getCurrentSession().getNoteForBrick(brick));
				popUpHelp.setVisible(true);

				popUp = view.getPopUp();
				pSchema.add(new TitleBar(IconDatabase.iconSchema, Messages.getString("MainFrameTriades.2")), //$NON-NLS-1$
						BorderLayout.NORTH);
				pSchema.add(view, BorderLayout.CENTER);
			}

		} else if (object.getClass() == ExportView.class) {
			ExportView view = (ExportView) object;
			if (view != null) {
				jta = new ExportTree(view.getExportModel(), (ExportPopUp)view.getPopUp(), view.getMousePlugin());

				JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				splitPane.setLeftComponent(new JScrollPane(jta));
				splitPane.setRightComponent(new ExportInformationPanel(view.getExportModel().getExportInformations(), view));
			//	splitPane.setDividerLocation(500);
				splitPane.setDividerSize(10);
				view.setTreeListener(((JTreeActors)jta).getListener());

				popUp = view.getPopUp();
				((ExportingMousePlugin)view.getMousePlugin()).setExportView(view);
				pSchema.add(new TitleBar(IconDatabase.iconSchema, Messages.getString("MainFrameTriades.3")), //$NON-NLS-1$
						BorderLayout.NORTH);
				pSchema.add(view, BorderLayout.CENTER);		
				return drawPanel(splitPane, popUp, pSchema, mainPanel);
			}
		} else if(object.getClass() == ActorsSheetNavigationView.class) {
			ActorsSheetNavigationView navigationView = (ActorsSheetNavigationView)object;
			jta = navigationView.getActorSheetTree();
			pSchema.add(navigationView.getActorSheetView());
		}
		if (popUpHelp == null)
			return drawPanel(jta, popUp, pSchema, mainPanel);
		else

			return drawPanel(jta, popUp, pSchema, mainPanel, popUpHelp);
	}

	@Override
	public void setDataPack(DataPack dtp) {
		datapack = dtp;

		if(autoSaveCreator == null) {
			autoSaveCreator = new AutoSaveCreator(datapack);
			AutoSaveCreator.extentionAutoSave = ""; //$NON-NLS-1$
		} else {
			autoSaveCreator.setDataPack(datapack);			
		}

		if (dtp != null && dtp.getCompanyInfo().getName() != null) {
			String name = dtp.getCompanyInfo().getName().trim();
			setTitle(Messages.getString("MainFrameTriades.0") + (name.compareTo("") == 0 ? "" : (" : " + dtp.getCompanyInfo().getName()))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else
			setTitle(Messages.getString("MainFrameTriades.0")); //$NON-NLS-1$

		tabbedPane.removeAll();

		if (datapack == null) {
			return;
		}

		String edgeLabelString = Messages.getString("Version_1_0_1.MainFrameTriades.6"); //$NON-NLS-1$
		String noteEditing = Messages.getString("Version_1_0_1.MainFrameTriades.7"); //$NON-NLS-1$

		for (int i = 0; i < mEdition.getMenuComponentCount(); i++)
		{
			Component c = mEdition.getMenuComponent(i);
			if (c instanceof JMenu && ((JMenu)c).getText().equals(edgeLabelString))
			{
				mEdition.remove(c);
				i--;
			}
			else if (c instanceof JCheckBoxMenuItem && ((JCheckBoxMenuItem)c).getText().equals(noteEditing))
			{
				mEdition.remove(c);
				i--;
			}
		}

		//Ajout de l'option de mofication des labels dans le menu edition
		JMenu edgeLabelDisplayMenu = new JMenu(edgeLabelString);

		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem structRel = new JRadioButtonMenuItem(Messages.getString("Version_1_0_1.MainFrameTriades.8")); //$NON-NLS-1$
		structRel.addActionListener(createSwitchEdgeLabelModeAction(0));
		group.add(structRel);

		JRadioButtonMenuItem realRel = new JRadioButtonMenuItem(Messages.getString("Version_1_0_1.MainFrameTriades.9")); //$NON-NLS-1$
		realRel.addActionListener(createSwitchEdgeLabelModeAction(1));
		group.add(realRel);

		edgeLabelDisplayMenu.add(structRel);
		edgeLabelDisplayMenu.add(realRel);

		if (datapack.getProgramSettings().getDefaultEdgeLabel() == 0)
			structRel.setSelected(true);
		else if (datapack.getProgramSettings().getDefaultEdgeLabel() == 1)
			realRel.setSelected(true);
		int i = 2;
		edgeLabelDisplayMenu.addSeparator();
		for (ActionTime aT : datapack.getActionTimeList())
		{
			JRadioButtonMenuItem rB = new JRadioButtonMenuItem(Messages.getString("Version_1_0_1.MainFrameTriades.10")+aT.toString()); //$NON-NLS-1$
			rB.addActionListener(createSwitchEdgeLabelModeAction(i));
			group.add(rB);
			edgeLabelDisplayMenu.add(rB);
			if (datapack.getProgramSettings().getDefaultEdgeLabel() == i)
				rB.setSelected(true);

			i++;
		}

		mEdition.add(edgeLabelDisplayMenu);

		final JCheckBoxMenuItem allowPopUpEditing = new JCheckBoxMenuItem(noteEditing);
		allowPopUpEditing.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				datapack.getProgramSettings().setHelpEditing(allowPopUpEditing.isSelected());
			}
		});
		mEdition.add(allowPopUpEditing);
		allowPopUpEditing.setSelected(datapack.getProgramSettings().isHelpEditing());

		
		dtp.init();

		//	popUpHelp = new PopUpHelpView();
		//	popUpHelp.setView(PopUpHelpView.showSelectionActorsHelp());
		showAssistant();

	}

	protected ActionListener createSwitchEdgeLabelModeAction(final int value)
	{
		final MainFrameTriades mf = this;

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DataPack dtp = Program.myMainFrame.getDataPack();
				if (dtp != null)
				{
					dtp.getProgramSettings().setDefaultEdgeLabel(value);
					mf.repaint();
				}
			}
		};
	}

	@Override
	public void initialState(MainFrame mf) {

	}


	@Override
	public BrickView<?,?> getActiveModelView() {
		// TODO Compléter cette fonction inconnue... :D bonne chance
		return null;
	}

	public void displayObject(Session session) {
		JPanel drawingPanel = new JPanel(new GridLayout());

		PopUpHelpView popUpHelp = new PopUpHelpView();
		popUpHelp.setNavigationHelp();
		JPanel panel = drawPanel(new NavigationTree(session, drawingPanel),
				null, drawingPanel, new JPanel(), popUpHelp);

		clearTabsAndShow(Messages.getString("MainFrameTriades.4"), panel); //$NON-NLS-1$

		Acteur actor = null;
		for(Content content : session.getActorsList().keySet()) {
			if(content instanceof Acteur) {
				actor = (Acteur)content;
				break;
			}
		}

		Program.myMainFrame.datapack.getCurrentSession().showActorSheet(actor);
		showMainTab();
	}

	public void displayObject(ActorListView actorListView) {
		clearTabsAndShow(Messages.getString("MainFrameTriades.5"), myPanel(actorListView)); //$NON-NLS-1$
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
		System.out.println("MainFrameTriades.enclosing_method()");
	}

	public void showAssistant()
	{
		if (tabbedPane != null)
			tabbedPane.removeAll();
		if (myAssistantFrameTriades == null)
			myAssistantFrameTriades = new AssistantFrameTriades(this);
		myAssistantFrameTriades.refreshAndShow();
	}
}
