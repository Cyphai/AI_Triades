package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import models.Brick;
import tools.ConfigCreator;
import translation.Messages;
import dataPack.Activite;
import dataPack.DataPack;

public class OptionNewFrameDatapackCreator extends JFrame {

	private static final long serialVersionUID = -3406404251988354757L;

	protected JRadioButton datapack;
	protected JRadioButton brick;
	protected JRadioButton newFile;
	protected JRadioButton openFile;
	protected JRadioButton lastFile;
	protected JTextArea description;
	protected JTextArea descriptionBis;
	private boolean isStepTwo;
	private final JPanel panelMain;
	private final JPanel panelInit;
	private JList pathsList;

	public OptionNewFrameDatapackCreator(MainFrame mf, DataPack dtp) {
		isStepTwo = false;
		setAlwaysOnTop(true);
		setTitle(Messages.getString("OptionNewFrameDatapackCreator.0")); // titre //$NON-NLS-1$
		Image icone = Toolkit.getDefaultToolkit().getImage(
		"Icones/16x16/triades.png"); //$NON-NLS-1$
		setIconImage(icone);
		setSize(400, 400); // taille
		setLocationRelativeTo(mf); // fenêtre centrée sur la main frame
		setResizable(true); // non redimensionnable
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // la croix quitte
		// l'application

		panelMain = this.myPanel(mf, dtp);
		panelInit = this.getInitialStep(mf);
		getContentPane().add(this.myPanel(mf, dtp));

	}

	private JPanel myPanel(final MainFrame mf, final DataPack dtp) {
		// Déclarations + initialisations
		final JPanel panel = new JPanel(new BorderLayout());
		JPanel north = new JPanel(new BorderLayout());
		final JPanel center = new JPanel(new BorderLayout());
		JPanel south = new JPanel(new BorderLayout());
		JPanel pTitle = new JPanel();
		final JPanel pChoices = new JPanel(new GridLayout(3, 2));
		JPanel pButtons = new JPanel();
		JPanel p2Buttons = new JPanel(new GridLayout(1, 2));
		description = new JTextArea(
				Messages.getString("OptionNewFrameDatapackCreator.2"), //$NON-NLS-1$
				4, 0);
		BoxLayout layoutButtons = new BoxLayout(pButtons, BoxLayout.X_AXIS);
		BoxLayout layoutTitle = new BoxLayout(pTitle, BoxLayout.X_AXIS);
		JButton ok = new JButton(Messages.getString("OptionNewFrameDatapackCreator.3")); //$NON-NLS-1$
		JButton cancel = new JButton(Messages.getString("OptionNewFrameDatapackCreator.4")); //$NON-NLS-1$
		datapack = new JRadioButton(Messages.getString("OptionNewFrameDatapackCreator.5"), true); //$NON-NLS-1$
		brick = new JRadioButton(Messages.getString("OptionNewFrameDatapackCreator.6"), false); //$NON-NLS-1$
		final ButtonGroup group = new ButtonGroup();
		final JLabel iconTitle = new JLabel(IconDatabase.iconTitleNew);
		final JTextField newBrickName = new JTextField();
		JComboBox brickSteps = null;
		JComboBox activity = null;
		JPanel pStepTwoBrick = null;
		final JPanel pStepTwoDataPack = new JPanel();
		JPanel pDataPack = new JPanel();
		final JTextField newDataPackName = new JTextField(20);
		JLabel iconBrick = new JLabel(IconDatabase.iconBrick);

		// Suivant si on a un datapack
		if (dtp == null) {

			brick.setEnabled(false);
			iconBrick.setEnabled(false);
		} else {
			brickSteps = new JComboBox(mf.datapack.getSteps());
			activity = new JComboBox(mf.datapack.getActivities().getActivities());
			pStepTwoBrick = getStepTwoBrick(mf, brickSteps, activity,
					newBrickName);
		}

		// Etape 2 de DATAPACK
		pDataPack.add(Box.createHorizontalStrut(20));
		pDataPack.add(new JLabel(Messages.getString("OptionNewFrameDatapackCreator.7"))); //$NON-NLS-1$
		pDataPack.add(Box.createHorizontalStrut(20));
		pDataPack.add(newDataPackName);
		pDataPack.add(Box.createHorizontalStrut(20));
		pStepTwoDataPack.add(Box.createVerticalStrut(175));
		pStepTwoDataPack.add(pDataPack);
		pStepTwoDataPack.add(Box.createVerticalStrut(170));

		// Barre de titre
		pTitle.setBackground(Color.WHITE);
		pTitle.setLayout(layoutTitle);
		pTitle.add(Box.createHorizontalStrut(10));
		pTitle.add(new JLabel(Messages.getString("OptionNewFrameDatapackCreator.8"))); //$NON-NLS-1$
		pTitle.add(Box.createGlue());
		pTitle.add(iconTitle);
		north.add(pTitle, BorderLayout.CENTER);
		north.add(new JSeparator(), BorderLayout.SOUTH);

		// Zone des choix
		datapack.addItemListener(mf.controller.itemListener);
		brick.addItemListener(mf.controller.itemListener);
		group.add(datapack);
		group.add(brick);
		pChoices.add(new JLabel(IconDatabase.iconDatapack));
		pChoices.add(datapack);
		pChoices.add(iconBrick);
		pChoices.add(brick);
		description.setBackground(this.getBackground());
		description.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(0, 0, 2, 0), BorderFactory
				.createTitledBorder(Messages.getString("OptionNewFrameDatapackCreator.9")))); //$NON-NLS-1$
		description.setEditable(false);
		center.add(pChoices, BorderLayout.CENTER);
		center.add(description, BorderLayout.SOUTH);

		final OptionNewFrameDatapackCreator frame = this;
		final JComboBox brickStepsFin = brickSteps;
		final JComboBox activityFin = activity;
		final JPanel pStepTwoBrickFin = pStepTwoBrick;

		// Barre des boutons
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isStepTwo) {
					boolean isAllowedToProceed = true;
					if (brick.isSelected()) {
						if (newBrickName.getText().length() > 0
								&& brickStepsFin.getSelectedItem() != null
								&& activityFin.getSelectedItem() != null) {
							Brick b = new Brick((String) brickStepsFin
									.getSelectedItem(), newBrickName.getText(),
									mf.datapack, (Activite) activityFin
									.getSelectedItem());
							mf.datapack.getBrickList().add(b);
							mf.datapack.sortBrick();
							
							mf.addTab(b);
						} else {
							DialogHandlerFrame.showErrorDialog(frame,
									Messages.getString("OptionNewFrameDatapackCreator.10")); //$NON-NLS-1$
							isAllowedToProceed = false;
						}
					} else if (datapack.isSelected()) {
						if (newDataPackName.getText().length() > 0) {
							if (mf.datapack != null) {
								int choice = DialogHandlerFrame
								.showYesNoCancelDialog(frame,
										Messages.getString("OptionNewFrameDatapackCreator.11")); //$NON-NLS-1$
								switch (choice) {
								case JOptionPane.YES_OPTION:
									if (!Program.save(Program.myMainFrame.datapack, true)) {
										System.out
												.println("OptionNewFrameDatapackCreator.myPanel(...).new ActionListener() {...}.actionPerformed()"); //$NON-NLS-1$
										System.out.println("Impossible de sauvegarder le datapack"); //$NON-NLS-1$
										return;
									}
									break;
								case JOptionPane.CANCEL_OPTION:
									return;
								default:
									break;

								}
								Program.myMainFrame.setDataPack(null);
								Program.myMainFrame.tabbedPane.removeAll();
							}
							Program.myMainFrame.setDataPack(new DataPack(
									newDataPackName.getText()));
						} else {
							DialogHandlerFrame.showErrorDialog(frame,
									Messages.getString("OptionNewFrameDatapackCreator.12")); //$NON-NLS-1$
							isAllowedToProceed = false;
						}
					}

					if (isAllowedToProceed) {
						setVisible(false);
						isStepTwo = false;
						getStepOne(iconTitle, center, pChoices);
					}
				} else {
					center.removeAll();
					getContentPane().repaint();
					if (brick.isSelected()) {
						if (dtp.getSteps().size() > 0
								&& dtp.getActivities().getNbActivites() > 0) {
							iconTitle.setIcon(IconDatabase.iconTitleNewBrick);
							center.add(pStepTwoBrickFin, BorderLayout.CENTER);
							if (activityFin.getItemCount() < Program.myMainFrame.datapack
									.getActivities().getNbActivites()) {
								activityFin.removeAllItems();
								for (Activite act : Program.myMainFrame.datapack
										.getActivities().getActivities()) {
									activityFin.addItem(act);
								}
							}
							newBrickName.setText(null);
							brickStepsFin.setSelectedIndex(0);
							activityFin.setSelectedIndex(0);
							description
							.setText(Messages.getString("OptionNewFrameDatapackCreator.13")); //$NON-NLS-1$
						} else {
							// Il faut au moins avoir une activité et une étape
							// Ce n'est pas le cas donc on ferme l'assistant de
							// création pour laisser l'utilisateur réctifier ce
							// problème

							DialogHandlerFrame
							.showErrorDialog(Messages.getString("OptionNewFrameDatapackCreator.14")); //$NON-NLS-1$

							setVisible(false);
							isStepTwo = false;
							getStepOne(iconTitle, center, pChoices);
							return;
						}
					} else if (datapack.isSelected()) {
						iconTitle.setIcon(IconDatabase.iconTitleDataPack);
						center.add(pStepTwoDataPack, BorderLayout.CENTER);
						newDataPackName.setText(null);
						description
						.setText(Messages.getString("OptionNewFrameDatapackCreator.15")); //$NON-NLS-1$

					}
					center.add(description, BorderLayout.SOUTH);
					getContentPane().validate();
					isStepTwo = true;
				}

			}

		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isStepTwo) {
					setVisible(false);
				} else {
					getStepOne(iconTitle, center, pChoices);
				}
			}

		});
		p2Buttons.add(ok);
		p2Buttons.add(cancel);
		pButtons.setLayout(layoutButtons);
		pButtons.add(Box.createHorizontalStrut(100));
		pButtons.add(p2Buttons);
		pButtons.add(Box.createHorizontalStrut(100));
		south.add(new JSeparator(), BorderLayout.NORTH);
		south.add(pButtons, BorderLayout.CENTER);

		// Panel global
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel getStepTwoBrick(MainFrame mf, JComboBox brickSteps,
			JComboBox activity, JTextField newBrickName) {
		setSize(500,500);
		JPanel panel = new JPanel();
		JPanel pBrickType = new JPanel();
		JPanel pActivity = new JPanel();
		JPanel pName = new JPanel();

		brickSteps.validate();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		pBrickType.setLayout(new BoxLayout(pBrickType, BoxLayout.X_AXIS));
		pActivity.setLayout(new BoxLayout(pActivity, BoxLayout.X_AXIS));
		pName.setLayout(new BoxLayout(pName, BoxLayout.X_AXIS));

		pBrickType.add(Box.createHorizontalStrut(71));
		pBrickType.add(new JLabel(Messages.getString("OptionNewFrameDatapackCreator.16"))); //$NON-NLS-1$
		pBrickType.add(Box.createHorizontalStrut(50));
		pBrickType.add(brickSteps);
		pBrickType.add(Box.createHorizontalStrut(20));

		pActivity.add(Box.createHorizontalStrut(88));
		pActivity.add(new JLabel(Messages.getString("OptionNewFrameDatapackCreator.17"))); //$NON-NLS-1$
		pActivity.add(Box.createHorizontalStrut(50));
		pActivity.add(activity);
		pActivity.add(Box.createHorizontalStrut(20));

		pName.add(Box.createHorizontalStrut(20));
		pName.add(new JLabel(Messages.getString("OptionNewFrameDatapackCreator.18"))); //$NON-NLS-1$
		pName.add(Box.createHorizontalStrut(50));
		pName.add(newBrickName);
		pName.add(Box.createHorizontalStrut(20));

		panel.add(Box.createVerticalStrut(52));
		panel.add(pBrickType);
		panel.add(Box.createVerticalStrut(10));
		panel.add(pActivity);
		panel.add(Box.createVerticalStrut(10));
		panel.add(pName);
		panel.add(Box.createVerticalStrut(52));

		return panel;

	}

	private void getStepOne(JLabel iconTitle, JPanel center, JPanel pChoices) {
		setSize(400,400);
		iconTitle.setIcon(IconDatabase.iconTitleNew);
		center.removeAll();
		getContentPane().repaint();
		datapack.setSelected(true);
		center.add(pChoices, BorderLayout.CENTER);
		description
		.setText(Messages.getString("OptionNewFrameDatapackCreator.19")); //$NON-NLS-1$
		center.add(description, BorderLayout.SOUTH);
		getContentPane().validate();
		isStepTwo = false;
	}

	private String getSelectedLastPath() {
		if(pathsList.getSelectedIndex() >= 0) {
			return ConfigCreator.getInstance().getLastDatapack().getLastObjects().elementAt(pathsList.getSelectedIndex());
		} else {
			return null;
		}
	}

	private JPanel getInitialStep(MainFrame mf) {
		// Declarations
		final JPanel panel = new JPanel(new BorderLayout());
		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel(new BorderLayout());
		JPanel south = new JPanel(new BorderLayout());
		JPanel pTitle = new JPanel();
		JPanel pChoices = new JPanel(new GridLayout(0, 2));
		JPanel pButtons = new JPanel();
		JPanel p2Buttons = new JPanel(new GridLayout(1, 2));
		ButtonGroup group = new ButtonGroup();
		JButton ok = new JButton(Messages.getString("OptionNewFrameDatapackCreator.20")); //$NON-NLS-1$
		JButton cancel = new JButton(Messages.getString("OptionNewFrameDatapackCreator.21")); //$NON-NLS-1$
		JPanel descriptionBisPanel = new JPanel(new BorderLayout());
		newFile = new JRadioButton(Messages.getString("OptionNewFrameDatapackCreator.22")); //$NON-NLS-1$
		openFile = new JRadioButton(Messages.getString("OptionNewFrameDatapackCreator.23")); //$NON-NLS-1$
		lastFile = new JRadioButton(Messages.getString("OptionNewFrameDatapackCreator.28")); //$NON-NLS-1$
		descriptionBis = new JTextArea(Messages.getString("OptionNewFrameDatapackCreator.24"), 2, 0); //$NON-NLS-1$
		descriptionBis.setEditable(false);
		descriptionBisPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0, 0, 2, 0), BorderFactory
				.createTitledBorder(Messages.getString("OptionNewFrameDatapackCreator.25")))); //$NON-NLS-1$
		descriptionBisPanel.setOpaque(false);
		descriptionBisPanel.add(descriptionBis);

		// Barre de titre
		pTitle.setLayout(new BoxLayout(pTitle, BoxLayout.X_AXIS));
		pTitle.setBackground(Color.WHITE);
		pTitle.add(Box.createHorizontalStrut(10));
		pTitle.add(new JLabel(Messages.getString("OptionNewFrameDatapackCreator.26"))); //$NON-NLS-1$
		pTitle.add(Box.createGlue());
		pTitle.add(new JLabel(IconDatabase.iconTitleTriad));
		north.add(pTitle, BorderLayout.CENTER);
		north.add(new JSeparator(), BorderLayout.SOUTH);

		// Centre
		newFile.setSelected(true);
		newFile.addItemListener(mf.controller.itemListener);
		openFile.addItemListener(mf.controller.itemListener);
		lastFile.addItemListener(mf.controller.itemListener);
		group.add(newFile);
		group.add(openFile);
		group.add(lastFile);
		pChoices.add(Box.createGlue());
		pChoices.add(Box.createGlue());
		pChoices.add(new JLabel(IconDatabase.iconNewFile));
		pChoices.add(newFile);
		pChoices.add(new JLabel(IconDatabase.iconOpenFile));
		pChoices.add(openFile);
		if(ConfigCreator.getInstance().getLastDatapack().getLastObjects().size() > 0) {
			pChoices.add(new JLabel(IconDatabase.iconDatapack));
			pChoices.add(lastFile);
		}
		pChoices.add(Box.createGlue());
		pChoices.add(Box.createGlue());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(pChoices);

		Vector<String> lastPath = ConfigCreator.getInstance().getLastDatapack().getLastObjects();

		if(lastPath.size() > 0) {
			Vector<String> names = new Vector<String>();
			for(int i = 0 ; i < lastPath.size() ; i++) {
				File file = new File(lastPath.elementAt(i));
				String name = "...\\"; //$NON-NLS-1$
				if(file.getParentFile() != null) {
					name = file.getParentFile().getName() + "\\"; //$NON-NLS-1$
				}

				if(file.getName().indexOf('.') >= 0) {
					name += file.getName().substring(0, file.getName().lastIndexOf(".")); //$NON-NLS-1$
				} else {
					name += file.getName();
				}

				names.add(name);
			}

			pathsList = new JList(names);
			pathsList.setLayout(new BorderLayout());

			pathsList.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseClicked(MouseEvent e) {
					lastFile.setSelected(true);
					if(e.getClickCount() > 1) {
						setVisible(false);
						
						DataPack dtp = (DataPack) Program.loadSavableObject(getSelectedLastPath(), true);
						Program.myMainFrame.setDataPack(dtp);
					}
				}
			});

			JScrollPane pathsListPanel = new JScrollPane(pathsList);
			pathsListPanel.setOpaque(false);
			pathsListPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("OptionNewFrameDatapackCreator.30"))); //$NON-NLS-1$

			centerPanel.add(pathsListPanel);

		}

		center.add(centerPanel, BorderLayout.CENTER);
		center.add(descriptionBisPanel, BorderLayout.SOUTH);

		// Sud
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(lastFile.isSelected()) {
					String path = getSelectedLastPath();
					if(path != null) {
						setVisible(false);
						DataPack dtp = (DataPack) Program.loadSavableObject(path, true);
						Program.myMainFrame.setDataPack(dtp);
					}
				} else if (newFile.isSelected()) {
					panel.setVisible(false);
					getContentPane().add(panelMain);
					// panelMain.setVisible(true);
				} else if (openFile.isSelected()) {
					setVisible(false);
					DataPack dtp = (DataPack) Program.loadSavableObject(null, true);
					Program.myMainFrame.setDataPack(dtp);
				} 
			}

		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				getContentPane().add(panelMain);
				setVisible(false);
			}

		});
		p2Buttons.add(ok);
		p2Buttons.add(cancel);
		pButtons.add(Box.createHorizontalStrut(100));
		pButtons.add(p2Buttons);
		pButtons.add(Box.createHorizontalStrut(100));
		south.add(new JSeparator(), BorderLayout.NORTH);
		south.add(pButtons, BorderLayout.CENTER);

		// Panel total
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		return panel;
	}

	public void setInitialStep() {
		setSize(400,475);
		getContentPane().remove(0);
		getContentPane().add(panelInit);
	}

}
