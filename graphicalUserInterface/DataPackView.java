package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import main.ActionTime;
import main.Statut;
import main.StatutObjet;
import models.Brick;
import relations.EnsembleRelation;
import relations.RelationBrowser;
import translation.Messages;
import dataPack.Activite;
import dataPack.CompanyInfoView;
import dataPack.DataPack;
import dataPack.JeuActeur;
import dataPack.Moyen;

public class DataPackView extends JPanel {

	private static final long serialVersionUID = -4909765521512012778L;

	protected DataPack dtp;
	JLabel nameLabel;

	public DataPackView(DataPack _dtp, MainFrame mf) {
		dtp = _dtp;
		build(mf);
	}

	private void build(final MainFrame mf) {
		final DataPackView access = this;

		this.setLayout(new BorderLayout());
		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel(new BorderLayout());
		JPanel south = new JPanel(new BorderLayout());
		JPanel pTitle = new JPanel();
		JPanel pButtons = new JPanel();
		JPanel pInfo = new JPanel();
		JPanel pAddActorSetView = new JPanel();
		JPanel pAddActor = new JPanel();
		JPanel pAddGroup = new JPanel();
		JPanel pAddActivity = new JPanel();
		JPanel pAddMoyen = new JPanel();
		JPanel pAddActivityMoyen = new JPanel();
		JPanel pEditRelation = new JPanel();
		JPanel pAddBrick = new JPanel();
		JPanel pAddActionTime = new JPanel();
		JPanel pAddStep = new JPanel();
		JPanel megaAdd = new JPanel();
		JPanel superAdd = new JPanel();
		JPanel pLists = new JPanel(new BorderLayout());
		JPanel pInformation = new JPanel(new GridLayout(0, 2));
		final JList listBricks = new JList(dtp.getBrickList());
		JScrollPane jspBricks = new JScrollPane(listBricks);
		JScrollPane jspCenter = new JScrollPane(center);
		final JComboBox genericMoyens = new JComboBox(main.StatutObjet.values());
		final JComboBox genericActors = new JComboBox(main.Statut.values());
		final JComboBox activities = new JComboBox(dtp.getActivities()
				.getActivities());
		final JComboBox moyens = new JComboBox(dtp.getMoyenListe());
		final JComboBox iconActivities = new JComboBox();
		final JComboBox actionTimes = new JComboBox(dtp.getActionTimeList());
		final JComboBox steps = new JComboBox(dtp.getSteps());
		final JComboBox actorSets = new JComboBox(dtp
				.getJeuxActeur());
		final JTextField newActorName = new JTextField(
				Messages.getString("DataPackView.0")); //$NON-NLS-1$
		final JTextField newGroupName = new JTextField(
				Messages.getString("DataPackView.1")); //$NON-NLS-1$
		final JTextField newActivityName = new JTextField(
				Messages.getString("DataPackView.2")); //$NON-NLS-1$
		final JTextField newMoyenName = new JTextField(
				Messages.getString("DataPackView.3")); //$NON-NLS-1$
		final JTextField newActionTimeName = new JTextField(
				Messages.getString("DataPackView.4")); //$NON-NLS-1$
		final JTextField newStepName = new JTextField(
				Messages.getString("DataPackView.5")); //$NON-NLS-1$
		JButton infoButton = new JButton(Messages.getString("DataPackView.64")); //$NON-NLS-1$
		JButton save = new JButton(Messages.getString("DataPackView.6")); //$NON-NLS-1$
		JButton addActorSetView = new JButton(Messages.getString("DataPackView.7")); //$NON-NLS-1$
		JButton addActor = new JButton(Messages.getString("DataPackView.8")); //$NON-NLS-1$
		JButton addGroup = new JButton(Messages.getString("DataPackView.9")); //$NON-NLS-1$
		JButton addActivity = new JButton(Messages.getString("DataPackView.10")); //$NON-NLS-1$
		JButton addMoyen = new JButton(Messages.getString("DataPackView.11")); //$NON-NLS-1$
		JButton addActionTime = new JButton(Messages.getString("DataPackView.12")); //$NON-NLS-1$
		JButton addStep = new JButton(Messages.getString("DataPackView.13")); //$NON-NLS-1$

		// Barre de titre
		pTitle.setBackground(Color.WHITE);
		pTitle.setLayout(new BoxLayout(pTitle, BoxLayout.X_AXIS));
		pTitle.add(Box.createHorizontalStrut(10));
		pTitle.add(new JLabel(Messages.getString("DataPackView.14") + dtp.toString())); //$NON-NLS-1$
		pTitle.add(Box.createGlue());
		pTitle.add(new JLabel(IconDatabase.iconTitleDataPack));
		north.add(new JSeparator(), BorderLayout.NORTH);
		north.add(pTitle, BorderLayout.CENTER);
		north.add(new JSeparator(), BorderLayout.SOUTH);

		//Informations sur l'entreprise
		pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.X_AXIS));
		pInfo.setBorder(BorderFactory.createTitledBorder(Messages.getString("DataPackView.70"))); //$NON-NLS-1$
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CompanyInfoView(mf.datapack.getCompanyInfo(), access);
			}
		});

		nameLabel = new JLabel(getCompanyNameLabel());
		pInfo.add(nameLabel);
		pInfo.add(Box.createVerticalGlue());
		pInfo.add(Box.createHorizontalGlue());
		pInfo.add(infoButton);

		// Ajouter acteur
		pAddActor.setLayout(new BoxLayout(pAddActor, BoxLayout.X_AXIS));
		pAddActor.add(new JLabel(IconDatabase.iconAddActor));
		pAddActor.add(Box.createHorizontalStrut(10));
		newActorName.setPreferredSize(new Dimension(200, 25));
		newActorName.setMaximumSize(new Dimension(200, 25));
		newActorName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.15"))); //$NON-NLS-1$
		pAddActor.add(newActorName);
		pAddActor.add(Box.createHorizontalStrut(10));
		pAddActor.add(new JLabel(Messages.getString("DataPackView.16"))); //$NON-NLS-1$
		pAddActor.add(Box.createHorizontalStrut(10));
		genericActors.setPreferredSize(new Dimension(200, 25));
		genericActors.setMaximumSize(new Dimension(200, 25));
		pAddActor.add(genericActors);
		pAddActor.add(Box.createHorizontalGlue());
		addActor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((newActorName.getText().length() > 0)
						&& (!newActorName.getText().equals(
								Messages.getString("DataPackView.17")))) { //$NON-NLS-1$
					if (dtp.addActor(
							((Statut) genericActors.getSelectedItem()).id,
							newActorName.getText()))
					{
						newActorName.setText(Messages.getString("DataPackView.18")); //$NON-NLS-1$
						newActorName.requestFocus();
					}
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.19")); //$NON-NLS-1$
			}

		});
		pAddActor.add(addActor);

		// Ajouter groupe
		pAddGroup.setLayout(new BoxLayout(pAddGroup, BoxLayout.X_AXIS));
		pAddGroup.add(new JLabel(IconDatabase.iconAddGroup));
		pAddGroup.add(Box.createHorizontalStrut(10));
		newGroupName.setPreferredSize(new Dimension(200, 25));
		newGroupName.setMaximumSize(new Dimension(200, 25));
		newGroupName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.20"))); //$NON-NLS-1$
		pAddGroup.add(newGroupName);
		pAddGroup.add(Box.createHorizontalGlue());
		addGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((newGroupName.getText().length() > 0)
						&& (!newGroupName.getText().equals(
								Messages.getString("DataPackView.21")))) { //$NON-NLS-1$
					if (dtp.addGroup(newGroupName.getText())) {
						newGroupName.setText(Messages.getString("DataPackView.22")); //$NON-NLS-1$
						newGroupName.requestFocus();
					}
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.23")); //$NON-NLS-1$
			}

		});
		pAddGroup.add(addGroup);
		// Ajout du tout au panel supérieur
		superAdd.setLayout(new BoxLayout(superAdd, BoxLayout.Y_AXIS));
		superAdd.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.24"))); //$NON-NLS-1$
		superAdd.add(pAddGroup);
		superAdd.add(Box.createVerticalStrut(10));
		superAdd.add(pAddActor);

		// Ajouter activité ou moyen
		pAddActivity.setLayout(new BoxLayout(pAddActivity, BoxLayout.X_AXIS));
		pAddActivity.add(new JLabel(IconDatabase.iconAddActivity));
		pAddActivity.add(Box.createHorizontalStrut(10));
		newActivityName.setPreferredSize(new Dimension(200, 25));
		newActivityName.setMaximumSize(new Dimension(200, 25));
		newActivityName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.25"))); //$NON-NLS-1$
		pAddActivity.add(newActivityName);
		pAddActivity.add(Box.createHorizontalStrut(10));
		pAddActivity.add(new JLabel(Messages.getString("DataPackView.26"))); //$NON-NLS-1$
		pAddActivity.add(Box.createHorizontalStrut(12));
		iconActivities.setPreferredSize(new Dimension(70, 25));
		iconActivities.setMaximumSize(new Dimension(70, 25));
		for (int i = 0; i < IconDatabase.vectorIconActivities.size(); i += 2)
			iconActivities.addItem(IconDatabase.vectorIconActivities
					.elementAt(i));
		pAddActivity.add(iconActivities);
		pAddActivity.add(Box.createHorizontalGlue());
		addActivity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((newActivityName.getText().length() > 0)
						&& (!newActivityName.getText().equals(
								Messages.getString("DataPackView.27")))) { //$NON-NLS-1$
					Activite newActivity = new Activite(newActivityName
							.getText(), iconActivities.getSelectedIndex() * 2);
					activities.addItem(newActivity);
					activities.setSelectedItem(newActivity);
					activities.validate();
					newActivityName.setText(Messages.getString("DataPackView.28")); //$NON-NLS-1$
					newActivityName.requestFocus();
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.29")); //$NON-NLS-1$
			}
		});
		pAddActivity.add(addActivity);
		//
		pAddMoyen.setLayout(new BoxLayout(pAddMoyen, BoxLayout.X_AXIS));
		pAddMoyen.add(new JLabel(IconDatabase.iconAddMoyen));
		pAddMoyen.add(Box.createHorizontalStrut(10));
		newMoyenName.setPreferredSize(new Dimension(200, 25));
		newMoyenName.setMaximumSize(new Dimension(200, 25));
		newMoyenName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.30"))); //$NON-NLS-1$
		pAddMoyen.add(newMoyenName);
		pAddMoyen.add(Box.createHorizontalStrut(10));
		pAddMoyen.add(new JLabel(Messages.getString("DataPackView.31"))); //$NON-NLS-1$
		pAddMoyen.add(Box.createHorizontalStrut(15));
		genericMoyens.setPreferredSize(new Dimension(200, 25));
		genericMoyens.setMaximumSize(new Dimension(200, 25));
		pAddMoyen.add(genericMoyens);
		pAddMoyen.add(Box.createHorizontalGlue());
		addMoyen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((newMoyenName.getText().length() > 0)
						&& (!newMoyenName.getText().equals(
								Messages.getString("DataPackView.32")))) { //$NON-NLS-1$
					Moyen newMoyen = dtp.addMoyen(newMoyenName.getText(),
							((StatutObjet) genericMoyens.getSelectedItem()).id);

					if (newMoyen != null) {
						moyens.addItem(newMoyen);
						moyens.validate();
					} else {
						JOptionPane.showMessageDialog(null,
								Messages.getString("DataPackView.33"), Messages.getString("DataPackView.34"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.WARNING_MESSAGE);
					}
					newMoyenName.setText(Messages.getString("DataPackView.35")); //$NON-NLS-1$
					newMoyenName.requestFocus();
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.36")); //$NON-NLS-1$
			}
		});
		pAddMoyen.add(addMoyen);
		//
		pAddActivityMoyen.setLayout(new BoxLayout(pAddActivityMoyen,
				BoxLayout.Y_AXIS));
		pAddActivityMoyen.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.37"))); //$NON-NLS-1$
		pAddActivityMoyen.add(pAddActivity);
		// pAddActivityMoyen.add(Box.createVerticalStrut(10));
		pAddActivityMoyen.add(pAddMoyen);

		// Editer le jeu de relation
		pEditRelation.setLayout(new BoxLayout(pEditRelation, BoxLayout.X_AXIS));
		pEditRelation.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.38"))); //$NON-NLS-1$
		JButton editRelation = new JButton(Messages.getString("DataPackView.39")); //$NON-NLS-1$
		editRelation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < mf.tabbedPane.getTabCount(); i++)
				{
					if (mf.tabbedPane.getTitleAt(i).equals(Messages.getString("DataPackView.40"))) //$NON-NLS-1$
					{
						mf.tabbedPane.setSelectedIndex(i);
						return;
					}

				}
				if (mf.datapack.getRelations() == null)
				{
					mf.datapack.setRelations(new EnsembleRelation());
					System.out.println(Messages.getString("DataPackView.41")); //$NON-NLS-1$
				}
				mf.tabbedPane.addTab(Messages.getString("DataPackView.42"), new RelationBrowser( //$NON-NLS-1$
						mf.datapack.getRelations()));
				mf.tabbedPane.setSelectedIndex(mf.tabbedPane.getTabCount() - 1);
			}

		});
		
		JButton cleanRelation = new JButton(Messages.getString("DataPackView.95")); //$NON-NLS-1$
		cleanRelation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dtp.cleanRelation();
			}
		});
		
		JButton renameAllRelation = new JButton(Messages.getString("DataPackView.100")); //$NON-NLS-1$
		renameAllRelation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtp.renameAllRelations();
			}
		});
		
		pEditRelation.add(Box.createGlue());
		
		if(ExecutionMode.isDebug()) {
			pEditRelation.add(renameAllRelation);
		}
		
		pEditRelation.add(cleanRelation);
		pEditRelation.add(editRelation);

		// Ajouter un temps d'action
		pAddActionTime
		.setLayout(new BoxLayout(pAddActionTime, BoxLayout.X_AXIS));
		pAddActionTime.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.43"))); //$NON-NLS-1$
		pAddActionTime.add(new JLabel(IconDatabase.iconAddActionTime));
		pAddActionTime.add(Box.createHorizontalStrut(10));
		newActionTimeName.setPreferredSize(new Dimension(200, 25));
		newActionTimeName.setMaximumSize(new Dimension(200, 25));
		newActionTimeName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.44"))); //$NON-NLS-1$
		pAddActionTime.add(newActionTimeName);
		pAddActionTime.add(Box.createHorizontalGlue());
		addActionTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((newActionTimeName.getText().length() > 0)
						&& (!newActionTimeName.getText().equals(
								Messages.getString("DataPackView.45")))) { //$NON-NLS-1$
					dtp.addActionTime(newActionTimeName.getText());
					actionTimes.validate();
					actionTimes
					.setSelectedIndex(actionTimes.getItemCount() - 1);
					newActionTimeName
					.setText(Messages.getString("DataPackView.46")); //$NON-NLS-1$
					newActionTimeName.requestFocus();
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.47")); //$NON-NLS-1$
			}
		});
		pAddActionTime.add(addActionTime);

		// Ajouter une étape
		pAddStep.setLayout(new BoxLayout(pAddStep, BoxLayout.X_AXIS));
		pAddStep.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.48"))); //$NON-NLS-1$
		pAddStep.add(new JLabel(IconDatabase.iconAddStep));
		pAddStep.add(Box.createHorizontalStrut(10));
		newStepName.setPreferredSize(new Dimension(200, 25));
		newStepName.setMaximumSize(new Dimension(200, 25));
		newStepName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.49"))); //$NON-NLS-1$
		pAddStep.add(newStepName);
		pAddStep.add(Box.createHorizontalGlue());
		addStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((newStepName.getText().length() > 0)
						&& (!newStepName.getText().equals(
								Messages.getString("DataPackView.50")))) { //$NON-NLS-1$
					dtp.addStep(newStepName.getText());
					steps.validate();
					steps.setSelectedIndex(steps.getItemCount() - 1);
					newStepName.setText(Messages.getString("DataPackView.51")); //$NON-NLS-1$
					newStepName.requestFocus();
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.52")); //$NON-NLS-1$
			}
		});
		pAddStep.add(addStep);

		// Ajouter jeu d'acteurs
		pAddActorSetView.setLayout(new BoxLayout(pAddActorSetView,
				BoxLayout.Y_AXIS));
		pAddActorSetView.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.53"))); //$NON-NLS-1$
		//
		JPanel panelUp = new JPanel();
		panelUp.setLayout(new BoxLayout(panelUp, BoxLayout.X_AXIS));
		final JTextField newActorSetName = new JTextField(
				Messages.getString("DataPackView.54")); //$NON-NLS-1$
		newActorSetName.setPreferredSize(new Dimension(200, 25));
		newActorSetName.setMaximumSize(new Dimension(200, 25));
		newActorSetName
		.addFocusListener(generateMouseListener(Messages.getString("DataPackView.55"))); //$NON-NLS-1$
		addActorSetView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = newActorSetName.getText();
				if ((name.length() > 0)
						&& (!name.equals(Messages.getString("DataPackView.56")))) { //$NON-NLS-1$
					if (dtp.getJeuActeurByName(name) == null) {
						JeuActeur ja = new JeuActeur(name);
						dtp.addJeuActeur(ja);
						actorSets.validate();
						actorSets
						.setSelectedIndex(actorSets.getItemCount() - 1);
						int index = mf.tabbedPane.getTabCount();
						mf.tabbedPane.add(ja.toString(), new ActorSetView(ja));
						mf.tabbedPane.setSelectedIndex(index);
						newActorSetName
						.setText(Messages.getString("DataPackView.58")); //$NON-NLS-1$

					} else
						DialogHandlerFrame
						.showErrorDialog(mf,
								Messages.getString("DataPackView.59")); //$NON-NLS-1$
				} else
					DialogHandlerFrame.showErrorDialog(mf,
							Messages.getString("DataPackView.60")); //$NON-NLS-1$
			}
		});
		panelUp.add(newActorSetName);
		panelUp.add(Box.createHorizontalGlue());
		panelUp.add(addActorSetView);
		//
		JPanel panelDown = new JPanel();
		panelDown.setLayout(new BoxLayout(panelDown, BoxLayout.X_AXIS));
		actorSets.setPreferredSize(new Dimension(225, 25));
		actorSets.setMaximumSize(new Dimension(225, 25));
		JButton modify = new JButton(Messages.getString("DataPackView.61")); //$NON-NLS-1$
		modify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JeuActeur ja = dtp.getJeuActeurByName(actorSets
						.getSelectedItem().toString());
				if (actorSets.getSelectedItem() != null
						&& actorSets.getSelectedItem().toString().length() > 0
						&& ja != null) {
					int index = mf.tabbedPane.getTabCount();
					if (!mf.showTabByName(ja.toString()))
					{
						mf.tabbedPane.add(ja.toString(),
								new ActorSetView(ja));
						mf.tabbedPane.setSelectedIndex(index);
					}
				} else
					DialogHandlerFrame
					.showErrorDialog(
							mf,
							Messages.getString("DataPackView.62")); //$NON-NLS-1$
			}
		});
		panelDown.add(actorSets);
		panelDown.add(Box.createHorizontalGlue());
		panelDown.add(modify);
		//
		pAddActorSetView.add(panelUp);
		pAddActorSetView.add(panelDown);

		// Super barre : ajouter acteur/gpe + ajouter activité/moyen + ajouter
		// brique + ajouter tps action
		megaAdd.setLayout(new BoxLayout(megaAdd, BoxLayout.Y_AXIS));
		megaAdd.add(pInfo);
		megaAdd.add(superAdd);
		megaAdd.add(pEditRelation);
		megaAdd.add(pAddActivityMoyen);
		megaAdd.add(pAddBrick);
		megaAdd.add(pAddActionTime);
		megaAdd.add(pAddStep);
		megaAdd.add(pAddActorSetView);

		// JLists
		MouseListener listenerList = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					JList source = (JList) arg0.getSource();
					Object object = source.getSelectedValue();
					if (object != null) {
						mf.addTab(object);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		};
		listBricks.addMouseListener(listenerList);
		listBricks
		.setToolTipText(Messages.getString("DataPackView.63")); //$NON-NLS-1$

		FocusListener focusListenerList = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				listBricks.setListData(dtp.getBrickList());
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		};
		mf.tabbedPane.addFocusListener(focusListenerList);
		listBricks.addFocusListener(focusListenerList);

		ComponentListener compListListener = new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
				System.out.println("Appel componentShown JLIST"); //$NON-NLS-1$
				if (e.getSource() == listBricks) {
					if (listBricks.getModel().getSize() < dtp.getBrickList()
							.size()) {
						DefaultListModel listModel = (DefaultListModel) listBricks
						.getModel();
						listModel.removeAllElements();
						for (Brick brick : dtp.getBrickList()) {
							listModel.addElement(brick);
						}
					}
				}

			}
		};
		listBricks.addComponentListener(compListListener);
		jspBricks.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DataPackView.65"))); //$NON-NLS-1$
		pLists.add(jspBricks, BorderLayout.CENTER);

		JPanel sideListPanel = new JPanel(new BorderLayout());
		JPanel listButtonPanel = new JPanel();
		listButtonPanel.setLayout(new BoxLayout(listButtonPanel,BoxLayout.Y_AXIS));
		final BrickParameterPopUp brickParameterPanel = new BrickParameterPopUp((Brick)listBricks.getSelectedValue());
		sideListPanel.add(brickParameterPanel, BorderLayout.CENTER);
		JButton openBrick = new JButton(IconDatabase.iconOpen);
		openBrick.setToolTipText(Messages.getString("DataPackView.66")); //$NON-NLS-1$
		openBrick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (listBricks.getSelectedValue() != null)
					mf.addTab(listBricks.getSelectedValue());
			}
		});

		JButton editBrick = new JButton(IconDatabase.iconRename);
		editBrick.setToolTipText(Messages.getString("DataPackView.67")); //$NON-NLS-1$
		editBrick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (listBricks.getSelectedValue() != null)
					brickParameterPanel.showMeTheBrick((Brick)listBricks.getSelectedValue());
			}
		});
		
		JButton copyBrick = new JButton(IconDatabase.iconNew);
		copyBrick.setToolTipText(Messages.getString("DataPackView.96")); //$NON-NLS-1$
		copyBrick.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listBricks.getSelectedValue() != null)
				{
					Brick source = (Brick)listBricks.getSelectedValue();
					int index = listBricks.getSelectedIndex();
					Brick copy = new Brick(source);
					copy.setName(Messages.getString("DataPackView.97")+source.getName()); //$NON-NLS-1$
					brickParameterPanel.showMeTheBrick(copy);
					dtp.getBrickList().insertElementAt(copy, index+1);
					dtp.sortBrick();
				}
			}
		});

		JButton deleteBrick = new JButton(IconDatabase.iconDelete);
		deleteBrick.setToolTipText(Messages.getString("DataPackView.68")); //$NON-NLS-1$
		deleteBrick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Program.myMainFrame.closeTab(((Brick)listBricks.getSelectedValue()).toString());

				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("DataPackView.69")+ listBricks.getSelectedValue()+ " ?") == JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
				{
					dtp.removeBrick((Brick)listBricks.getSelectedValue());
				}


			}
		});

		listButtonPanel.add(Box.createGlue());
		listButtonPanel.add(openBrick);
		listButtonPanel.add(editBrick);
		listButtonPanel.add(copyBrick);
		listButtonPanel.add(deleteBrick);
		listButtonPanel.add(Box.createGlue());
		sideListPanel.add(listButtonPanel,BorderLayout.EAST);
		pLists.add(sideListPanel, BorderLayout.EAST);

		// ComboBoxes : activités, groupes et bricktypes

		JPanel activitiesCB = new JPanel();
		activitiesCB.setLayout(new BoxLayout(activitiesCB, BoxLayout.X_AXIS));
		JButton delete = new JButton(IconDatabase.iconDelete);
		JButton rename = new JButton(IconDatabase.iconRename);
		rename.setToolTipText(Messages.getString("DataPackView.71")); //$NON-NLS-1$
		delete.setToolTipText(Messages.getString("DataPackView.72")); //$NON-NLS-1$

		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (activities.getSelectedItem() == null)
					return;

				String newName = JOptionPane.showInputDialog(Messages.getString("DataPackView.73") + activities.getSelectedItem() + Messages.getString("DataPackView.74"), activities.getSelectedItem()); //$NON-NLS-1$ //$NON-NLS-2$
				newName.trim();
				if (newName.length()>0 && !newName.equals(activities.getSelectedItem().toString()))
				{
					if (dtp.renameActivity(activities.getSelectedItem().toString(), newName))
						activities.repaint();
					else
						DialogHandlerFrame.showErrorDialog(Messages.getString("DataPackView.75")); //$NON-NLS-1$
				}


			}
		});


		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Activite deletedActivity = (Activite) activities
				.getSelectedItem();
				if (deletedActivity != null)
				{
					if (dtp.removeActivity(deletedActivity)) {
						if(activities.getItemCount() > 0) {
							activities.setSelectedIndex(0);
						} else {
							activities.setSelectedItem(null);
						}
						activities.validate();
					}
				}
			}
		});
		activitiesCB.add(activities);
		activitiesCB.add(rename);
		activitiesCB.add(delete);

		JPanel stepsCB = new JPanel();
		stepsCB.setLayout(new BoxLayout(stepsCB,BoxLayout.X_AXIS));

		rename = new JButton(IconDatabase.iconRename);

		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (steps.getSelectedItem() == null)
					return;

				String newName = JOptionPane.showInputDialog(Messages.getString("DataPackView.76")+ steps.getSelectedItem() +Messages.getString("DataPackView.77"), steps.getSelectedItem()); //$NON-NLS-1$ //$NON-NLS-2$
				newName.trim();
				if (newName.length()>0 && !newName.equals(steps.getSelectedItem().toString()))
				{
					if (dtp.renameStep(steps.getSelectedItem().toString(), newName))
						steps.repaint();
					else
						DialogHandlerFrame.showErrorDialog(Messages.getString("DataPackView.78")); //$NON-NLS-1$
				}
			}
		});

		delete = new JButton(IconDatabase.iconDelete);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String deletedStep = (String) steps
				.getSelectedItem();
				if (deletedStep != null)
				{
					if (dtp.removeStep(deletedStep)) {
						if(steps.getItemCount() > 0) {
							steps.setSelectedIndex(0);
						} else {
							steps.setSelectedItem(null);
						}
						steps.validate();
					}
				}

			}
		});

		rename.setToolTipText(Messages.getString("DataPackView.79")); //$NON-NLS-1$
		delete.setToolTipText(Messages.getString("DataPackView.80")); //$NON-NLS-1$
		stepsCB.add(steps);
		stepsCB.add(rename);
		stepsCB.add(delete);
		if (ExecutionMode.isDebug())
		{
			JButton moveUp = new JButton(IconDatabase.iconArrowUpLeft);
			moveUp.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String movedStep = (String)steps.getSelectedItem();
					if (movedStep != null)
					{
						dtp.getSteps().remove(movedStep);
						dtp.getSteps().insertElementAt(movedStep, 0);
					}
				}
			});
			stepsCB.add(moveUp);
		}

		JPanel actionTimesCB = new JPanel();
		actionTimesCB.setLayout(new BoxLayout(actionTimesCB, BoxLayout.X_AXIS));

		rename = new JButton(IconDatabase.iconRename);

		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (actionTimes.getSelectedItem() == null)
					return;

				String newName = JOptionPane.showInputDialog(Messages.getString("DataPackView.81") + actionTimes.getSelectedItem() + Messages.getString("DataPackView.82"), actionTimes.getSelectedItem()); //$NON-NLS-1$ //$NON-NLS-2$
				newName.trim();
				if (newName.length()>0 && !newName.equals(actionTimes.getSelectedItem().toString()))
				{
					if (dtp.renameActionTime(actionTimes.getSelectedItem().toString(), newName))
						actionTimes.repaint();
					else
						DialogHandlerFrame.showErrorDialog(Messages.getString("DataPackView.83")); //$NON-NLS-1$
				}
			}
		});

		delete = new JButton(IconDatabase.iconDelete);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				ActionTime deletedActionTime = (ActionTime) actionTimes
				.getSelectedItem();
				if (deletedActionTime != null) {
					if (dtp.removeActionTime(deletedActionTime)) {
						{
							if(actionTimes.getItemCount() > 0) {
								actionTimes.setSelectedIndex(0);
							} else {
								actionTimes.setSelectedItem(null);
							}
							actionTimes.validate();
						}
					}
				}

			}
		});

		rename.setToolTipText(Messages.getString("DataPackView.84")); //$NON-NLS-1$
		delete.setToolTipText(Messages.getString("DataPackView.85")); //$NON-NLS-1$
		actionTimesCB.add(actionTimes);
		actionTimesCB.add(rename);
		actionTimesCB.add(delete);

		JPanel moyensCB = new JPanel();
		moyensCB.setLayout(new BoxLayout(moyensCB, BoxLayout.X_AXIS));

		rename = new JButton(IconDatabase.iconRename);

		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DialogHandlerFrame.showInformationDialog(mf, Messages.getString("DataPackView.86"), Messages.getString("DataPackView.87"), null); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});

		delete = new JButton(IconDatabase.iconDelete);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Moyen deletedMoyen = (Moyen) moyens.getSelectedItem();
				if (deletedMoyen != null) {
					if (dtp.removeMoyen(deletedMoyen)) {
						moyens.removeItem(deletedMoyen);
						if(moyens.getItemCount() > 0) {
							moyens.setSelectedIndex(0);
						} else {
							moyens.setSelectedItem(null);
						}
						moyens.validate();
					}
				}

			}
		});

		rename.setToolTipText(Messages.getString("DataPackView.88")); //$NON-NLS-1$
		delete.setToolTipText(Messages.getString("DataPackView.89")); //$NON-NLS-1$
		moyensCB.add(moyens);
		moyensCB.add(rename);
		moyensCB.add(delete);

		pInformation
		.setBorder(BorderFactory.createTitledBorder(Messages.getString("DataPackView.90"))); //$NON-NLS-1$
		pInformation.add(new JLabel(Messages.getString("DataPackView.91"))); //$NON-NLS-1$
		pInformation.add(new JLabel(Messages.getString("DataPackView.92"))); //$NON-NLS-1$
		pInformation.add(activitiesCB);
		pInformation.add(stepsCB);
		pInformation.add(new JLabel(Messages.getString("DataPackView.93"))); //$NON-NLS-1$
		pInformation.add(new JLabel(Messages.getString("DataPackView.94"))); //$NON-NLS-1$
		pInformation.add(actionTimesCB);
		pInformation.add(moyensCB);

		// Espace central
		center.add(megaAdd, BorderLayout.NORTH);
		center.add(pInformation, BorderLayout.CENTER);
		center.add(pLists, BorderLayout.SOUTH);
		jspCenter.setBorder(null);

		// Barre de boutons
		pButtons.setLayout(new BoxLayout(pButtons, BoxLayout.X_AXIS));
		pButtons.add(Box.createGlue());
		save.addActionListener(Program.myMainFrame.controller.actionSave);
		pButtons.add(save);
		pButtons.add(Box.createGlue());
		south.add(new JSeparator(), BorderLayout.NORTH);
		south.add(pButtons, BorderLayout.CENTER);

		// Panel total
		add(north, BorderLayout.NORTH);
		jspCenter.getVerticalScrollBar().setUnitIncrement(50);
		add(jspCenter, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}

	private String getCompanyNameLabel() {
		String companyName = dtp.getCompanyInfo().getName();
		if(companyName == null) {
			companyName = Messages.getString("DataPackView.98"); //$NON-NLS-1$
		} else {
			companyName = Messages.getString("DataPackView.99") + companyName; //$NON-NLS-1$
		}

		return companyName;
	}

	public static FocusListener generateMouseListener(
			final String initialString) {
		return new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				JTextField txtField = (JTextField) e.getSource();
				if (txtField.getText().equals(initialString)) {
					txtField.setText(""); //$NON-NLS-1$
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txtField = (JTextField) e.getSource();
				if (txtField.getText().equals("")) { //$NON-NLS-1$
					txtField.setText(initialString);
				}
			}
		};
	}

	public void updateView() {
		nameLabel.setText(getCompanyNameLabel());
	}
}
