package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import models.BrickVertex.VerticeRank;
import translation.Messages;
import client.ActorSheet;
import client.ActorSheetAccess;
import client.Session;
import dataPack.Acteur;
import dataPack.ActeurSelectionne;
import dataPack.Content;
import dataPack.ListeActeurSelectionne;

public class ActorListView extends JPanel {

	private static final long serialVersionUID = -8042407423948049392L;

	private JPanel panelCenter;
	
	private final ListeActeurSelectionne list;
	private JPanel mainList;
	private JPanel secondaryList;
	private final Hashtable<Acteur, ActorSheet> actorSheets;

	public ActorListView(MainFrame mf) {
		
		list = new ListeActeurSelectionne();
		actorSheets = new Hashtable<Acteur, ActorSheet>();
		build(mf);
	}

	private void build(final MainFrame mf) {
		// Declarations + initialisations
		JPanel panelNorth = new JPanel(new BorderLayout());
		JPanel panelSouth = new JPanel(new BorderLayout());
		JPanel panelTitle = new JPanel();
		JPanel panelButtons = new JPanel();
		JPanel panelUnusedSpace = new JPanel(new BorderLayout());
		panelCenter = new JPanel();
		mainList = new JPanel();
		mainList.setLayout(new BoxLayout(mainList, BoxLayout.Y_AXIS));
		secondaryList = new JPanel();
		secondaryList.setLayout(new BoxLayout(secondaryList, BoxLayout.Y_AXIS));
		mainList.setBorder(BorderFactory.createTitledBorder(Messages.getString("ActorListView.1"))); //$NON-NLS-1$
		secondaryList.setBorder(BorderFactory.createTitledBorder(Messages.getString("ActorListView.9"))); //$NON-NLS-1$
		JPanel mainGluePanel = new JPanel();
		mainGluePanel.add(Box.createVerticalStrut(5));
		mainList.add(mainGluePanel);
		JPanel secondaryGluePanel = new JPanel();
		secondaryGluePanel.add(Box.createVerticalStrut(5));
		secondaryList.add(secondaryGluePanel);

		final JTextField sessionName = new JTextField(Messages.getString("ActorListView.0") //$NON-NLS-1$
				+ (new SimpleDateFormat("dd MMM yyyy kk'h'mm")) //$NON-NLS-1$
				.format(new Date()));
		sessionName.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("ActorListView.2"))); //$NON-NLS-1$
		JScrollPane scrollPane = new JScrollPane(panelUnusedSpace,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JToolBar toolBarAddActor = new JToolBar();
		JButton buttonAddMainActor = new JButton(
				Messages.getString("ActorListView.3"), //$NON-NLS-1$
				IconDatabase.iconAddActor);
		JButton buttonAddSecondaryActor = new JButton(Messages.getString("ActorListView.10"), IconDatabase.iconAddActor); //$NON-NLS-1$
		JButton buttonGenerate = new JButton(Messages.getString("ActorListView.4")); //$NON-NLS-1$


		// Barre de titre
		panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.X_AXIS));
		panelTitle.setBackground(Color.WHITE);
		panelTitle.add(Box.createHorizontalStrut(10));
		panelTitle.add(sessionName);
		panelTitle.add(Box.createGlue());
		panelTitle.add(new JLabel(IconDatabase.iconActorSet));
		panelNorth.add(new JSeparator(), BorderLayout.NORTH);
		panelNorth.add(panelTitle, BorderLayout.CENTER);
		panelNorth.add(new JSeparator(), BorderLayout.SOUTH);

		// Panel central
		buttonAddMainActor.setFocusable(false);
		buttonAddSecondaryActor.setFocusable(false);
		buttonAddMainActor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mf.mainJta.getListener().getSelectedActors() == null) {
					DialogHandlerFrame.showInformationDialog(
							Program.myMainFrame, Messages.getString("ActorListView.5"), //$NON-NLS-1$
							Messages.getString("ActorListView.6"), null); //$NON-NLS-1$
					return;
				}
				for (Acteur actor : mf.mainJta.getListener()
						.getSelectedActors()) {
					addActorToView(new ActeurSelectionne(actor,VerticeRank.primary),
							mf);
				}
				panelCenter.getParent().validate();
			}
		});
		buttonAddSecondaryActor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mf.mainJta.getListener().getSelectedActors() == null) {
					DialogHandlerFrame.showInformationDialog(
							Program.myMainFrame, Messages.getString("ActorListView.5"), //$NON-NLS-1$
							Messages.getString("ActorListView.6"), null); //$NON-NLS-1$
					return;
				}
				for (Acteur actor : mf.mainJta.getListener()
						.getSelectedActors()) {
					addActorToView(new ActeurSelectionne(actor,VerticeRank.secondary),
							mf);
				}
				panelCenter.getParent().validate();
			}
		});
		toolBarAddActor.setFloatable(false);
		toolBarAddActor.add(buttonAddMainActor);
		toolBarAddActor.add(buttonAddSecondaryActor);
		toolBarAddActor.add(new JLabel());
		toolBarAddActor.add(Box.createHorizontalGlue());
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
		panelCenter.add(toolBarAddActor);
		panelCenter.add(new JSeparator());
		panelCenter.add(mainList);
		panelCenter.add(secondaryList);

		// Barre de boutons
		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
		panelButtons.add(Box.createGlue());
		buttonGenerate.setFocusable(false);
		buttonGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				TreeMap<Content, VerticeRank> selectedActors = getSelectedActors();
				boolean ok = selectedActors != null;
					
					
				
				if (!ok)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("ActorListView.11")); //$NON-NLS-1$
					return;
				}
				
				
				Session newSession = new Session(selectedActors, sessionName.getText(), null);

				mf.getDataPack().getExportModule().addNewSession(newSession);
				newSession.setActorSheets(actorSheets);
				newSession.initSession();
				
				mf.getDataPack().setCurrentSession(newSession);
				((MainFrameTriades) mf).displayObject(newSession);
			}
		});
		panelButtons.add(buttonGenerate);

		panelButtons.add(Box.createGlue());
		panelSouth.add(new JSeparator(), BorderLayout.NORTH);
		panelSouth.add(panelButtons, BorderLayout.CENTER);

		// Panel intermédiaire permettant à panelCenter de ne pas prendre tout
		// l'espace
		panelUnusedSpace.add(panelCenter, BorderLayout.NORTH);

		// Panel total
		this.setLayout(new BorderLayout());
		this.add(panelNorth, BorderLayout.NORTH);
		scrollPane.getVerticalScrollBar().setUnitIncrement(50);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(panelSouth, BorderLayout.SOUTH);
	}

	private TreeMap<Content, VerticeRank> getSelectedActors() {
		TreeMap<Content, VerticeRank> selectedActors = new TreeMap<Content, VerticeRank>();
		boolean ok = false;
		for (ActeurSelectionne aS : list.getActorsSelection())
		{
			selectedActors.put(aS.getActeur(), aS.getRank());
			if (aS.getRank().getValue() == VerticeRank.primary.getValue())
				ok = true;
		}				
		
		return ok ? selectedActors : null;
	}
	
	public void addActorToView(final ActeurSelectionne actor, final MainFrame mf) {
		list.ajouterActeurSelectionne(actor);

		// Déclarations et initialisations
		final JPanel panelActor = new JPanel();
		panelActor.setOpaque(false);
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(panelActor, BorderLayout.CENTER);
		final JLabel buttonDeleteActor = new JLabel(
				IconDatabase.iconRemoveActor0);

		// Construction des éléments du panel
		buttonDeleteActor.setToolTipText(Messages.getString("ActorListView.8")); //$NON-NLS-1$
		buttonDeleteActor.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				buttonDeleteActor.setIcon(IconDatabase.iconRemoveActor2);
				if (actor.getRank() == VerticeRank.primary)
				{
					removeMainActor(panel);
				}
				else if (actor.getRank() == VerticeRank.secondary)
					removeSecondaryActor(panel);
				panelCenter.getParent().validate();
			
				list.supprimerActeurSelectionne(actor);
				mf.mainJta.getListener().showActor(actor.getActeur());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				buttonDeleteActor.setIcon(IconDatabase.iconRemoveActor1);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				buttonDeleteActor.setIcon(IconDatabase.iconRemoveActor0);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				buttonDeleteActor.setIcon(IconDatabase.iconRemoveActor2);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		final Acteur accessActor = actor.getActeur();
		JLabel newNameLabel = new JLabel(Messages.getString("ActorListView.12")); //$NON-NLS-1$
		final JTextField newNameTextField = new JTextField();
		newNameTextField.setPreferredSize(new Dimension(120, 25));
		newNameTextField.setMaximumSize(new Dimension(120, 25));
		newNameTextField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(actorSheets.get(accessActor) == null) {
					actorSheets.put(accessActor, new ActorSheet(accessActor));
				}
				
				if(newNameTextField.getText().trim().equals("")) { //$NON-NLS-1$
					actorSheets.get(accessActor).setActorName(null);
				} else {
					actorSheets.get(accessActor).setActorName(newNameTextField.getText());
				}
			}
		});
		
		newNameTextField.addFocusListener(new FocusListener() {
			@Override public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(actorSheets.get(accessActor) != null) {
					newNameTextField.setText(actorSheets.get(accessActor).getActorName());
				}
			}
		});
		
		JButton actorSheetButton = new JButton(Messages.getString("ActorListView.14")); //$NON-NLS-1$
		actorSheetButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(actorSheets.get(accessActor) == null) {
					actorSheets.put(accessActor, new ActorSheet(accessActor));
				}
				
				Session sessionTemp = new Session(getSelectedActors(), "", null); //$NON-NLS-1$
				sessionTemp.setActorSheets(actorSheets);
				mf.datapack.setCurrentSession(sessionTemp);
				Program.myMainFrame.closeTab(ActorSheetAccess.ActorsSheetTabName);
				Program.myMainFrame.datapack.getCurrentSession().showActorSheet(accessActor);
			}
		});
		
		// Construction du panel
		panelActor.setLayout(new BoxLayout(panelActor, BoxLayout.X_AXIS));
		buttonDeleteActor.setPreferredSize(new Dimension(25, 25));
		buttonDeleteActor
		.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		panelActor.add(buttonDeleteActor);
		panelActor.add(Box.createHorizontalStrut(10));
		JLabel label = new JLabel(actor.getActeur().toString());
		label.setPreferredSize(new Dimension(200, 25));
		panelActor.add(label);
		
		panelActor.add(Box.createHorizontalGlue());
		panelActor.add(new JSeparator(JSeparator.VERTICAL));
		panelActor.add(Box.createHorizontalGlue());

		panelActor.add(newNameLabel);
		panelActor.add(Box.createHorizontalStrut(10));
		panelActor.add(newNameTextField);
		panelActor.add(Box.createHorizontalStrut(10));
		panelActor.add(actorSheetButton);

		panelActor.add(Box.createHorizontalGlue());
		panelActor.add(new JSeparator(JSeparator.VERTICAL));
		panelActor.add(Box.createHorizontalGlue());

		ButtonGroup group = new ButtonGroup();
		final JRadioButton mainActor = new JRadioButton(Messages.getString("ActorListView.15")); //$NON-NLS-1$
		JRadioButton secondaryActor = new JRadioButton(Messages.getString("ActorListView.16")); //$NON-NLS-1$
		mainActor.setOpaque(false);
		secondaryActor.setOpaque(false);
		group.add(mainActor);
		group.add(secondaryActor);
		mainActor.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange()==ItemEvent.SELECTED && actor.getRank() != VerticeRank.primary)
				{
					removeSecondaryActor(panel);
					addMainActor(panel);
					actor.setRank(VerticeRank.primary);
				}

			}
		});
		secondaryActor.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange()==ItemEvent.SELECTED && actor.getRank() != VerticeRank.secondary)
				{
					removeMainActor(panel);
					addSecondaryActor(panel);
					actor.setRank(VerticeRank.secondary);
				}

			}
		});
		panelActor.add(mainActor);
		panelActor.add(secondaryActor);

		if (actor.getRank() == VerticeRank.primary)
		{
			mainActor.setSelected(true);
			addMainActor(panel);
		}
		else if (actor.getRank() == VerticeRank.secondary)
		{

			secondaryActor.setSelected(true);
			addSecondaryActor(panel);
		}
		mf.mainJta.getListener().hideActor(actor.getActeur());
	}

	protected void removeSecondaryActor(JPanel actor)
	{
		secondaryList.remove(actor);
		updateActorColor();
		closeActorSheetView();
		validate();
	}

	protected void removeMainActor(JPanel actor)
	{
		mainList.remove(actor);
		updateActorColor();
		closeActorSheetView();
		validate();
	}

	protected void addSecondaryActor(JPanel actor)
	{
		secondaryList.remove(actor);
		secondaryList.add(actor);
		updateActorColor();		
		closeActorSheetView();
		validate();
	}

	protected void addMainActor(JPanel actor)
	{
		mainList.remove(actor);
		mainList.add(actor);
		updateActorColor();
		closeActorSheetView();
		validate();
	}
	
	protected void closeActorSheetView() {
		Program.myMainFrame.closeTab(ActorSheetAccess.ActorsSheetTabName);
	}
	
	private void updateActorColor() {
		for(int i = 1 ; i < secondaryList.getComponentCount() ; i++) {
			secondaryList.getComponents()[i].setBackground(i % 2 == 1 ? new Color(249, 249, 249) : new Color(229, 229, 229));
		}
		
		for(int i = 1 ; i < mainList.getComponentCount() ; i++) {
			mainList.getComponents()[i].setBackground(i % 2 == 1 ? new Color(249, 249, 249) : new Color(229, 229, 229));
		}
	}

}
