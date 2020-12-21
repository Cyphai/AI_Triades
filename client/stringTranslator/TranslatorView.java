package client.stringTranslator;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.Program;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.DefaultMutableTreeNode;

import main.ActionTime;
import models.Brick;
import relations.Goal;
import relations.Mean;
import relations.RelationDescription;
import tools.Config;
import tools.ConfigCreator;
import tools.ConfigTriades;
import tools.UniqueInstance;
import translation.Messages;
import dataPack.Activite;
import dataPack.DataPack;
import dataPack.Groupe;
import dataPack.JeuActeur;

public class TranslatorView extends JPanel  {
	private static final long serialVersionUID = -1617618019555022483L;

	private static Dimension windowsSize = new Dimension(1024, 768);

	private final Icon hidenIcon;
	private final Icon displayIcon;
	private final Icon bulletIcon;

	static private int VerticalStructSize = 15;

	private final DataPack dataPack;
	private final JFrame mainFrame;

	private final Hashtable<String, Vector<JPanel>> titles;
	protected final JPanel access;
	protected JPanel allViews;
	protected final JScrollPane scrollPane;
	private final Vector<JPanel> allPanels;
	private final Vector<JTextField> allTextFields;
	private final Vector<String> allLabels;
	private final Vector<Object> allObjects;
	private final Vector<Hashtable<Object, String>> allTables;

	public TranslatorView(DataPack dataPack, JFrame mainFrame) {
		super(new BorderLayout());

		Program.setIsTriade(true);
		setTranslucentBackGroundColor(this);
		allPanels = new Vector<JPanel>();
		allTextFields = new Vector<JTextField>();
		allLabels = new Vector<String>();
		allObjects = new Vector<Object>();
		allTables = new Vector<Hashtable<Object,String>>();
		scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);

		hidenIcon = IconDatabase.iconMinimize;
		displayIcon = IconDatabase.iconMaximize;
		bulletIcon = IconDatabase.iconClose;
		access = this;
		this.dataPack = dataPack;
		this.mainFrame = mainFrame;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titles = new Hashtable<String, Vector<JPanel>>();

		Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,newForwardKeys);

		forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,newForwardKeys);

		forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,newForwardKeys);

		initTitles();
		initView();
		repaint();
		validate();
		resizeView();
	}

	private void initTitles() {
		// //actors

		Vector<JPanel> panels = new Vector<JPanel>();

		// activies
		panels = new Vector<JPanel>();
		for (Activite activity : dataPack.getActivities().getActivities()) {

			panels.add(createStringView(activity, dataPack
					.getStringTranslator().activities, createBulletPanel(createTranslucentLabel(
							StringTranslator.getNotTranslatedString(activity))), StringTranslator.getNotTranslatedString(activity)));
		}
		titles.put(Messages.getString("TranslatorView.0"), panels); //$NON-NLS-1$

		// Steps
		panels = new Vector<JPanel>();
		for (String step : dataPack.getSteps()) {
			panels.add(createStringView(step,
					dataPack.getStringTranslator().steps, createBulletPanel(createTranslucentLabel(
							StringTranslator.getNotTranslatedString(step)))
							, StringTranslator.getNotTranslatedString(step)));
		}
		titles.put(Messages.getString("TranslatorView.1"), panels); //$NON-NLS-1$

		// Action Times
		panels = new Vector<JPanel>();
		for (ActionTime actionTime : dataPack.getActionTimeList()) {
			panels.add(createStringView(actionTime, dataPack
					.getStringTranslator().actionTimes, createBulletPanel(createTranslucentLabel(
							StringTranslator.getNotTranslatedString(actionTime))), StringTranslator.getNotTranslatedString(actionTime)));
		}
		titles.put(Messages.getString("TranslatorView.2"), panels); //$NON-NLS-1$

		//Bricks
		panels = new Vector<JPanel>();
		for (Brick brick : dataPack.getBrickList()) {
			panels.add(createStringView(brick, dataPack
					.getStringTranslator().bricks, createBulletPanel(createTranslucentLabel(
							StringTranslator.getNotTranslatedString(brick))), StringTranslator.getNotTranslatedString(brick)));
		}
		titles.put(Messages.getString("TranslatorView.6"), panels); //$NON-NLS-1$



		//Relations
		panels = new Vector<JPanel>();
		for (RelationDescription relationDescription : dataPack.getRelations().getRelationDescriptions()) {
			JPanel goalsPanel = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);
			for(Goal goal : relationDescription.getPossibility().getMap().keySet()) {
				JPanel meansPanel = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);
				for(Mean mean : relationDescription.getPossibility().getMap().get(goal)) {
					JPanel meanTitleView = createDefautlTranslucentJPanel(BoxLayout.X_AXIS);
					meanTitleView.add(Box.createHorizontalStrut(VerticalStructSize*2));
					meanTitleView.add(setTranslucentBackGroundColor(createBulletPanel(createTranslucentLabel(StringTranslator.getNotTranslatedString(mean)))));
					meansPanel.add(createStringView(mean, dataPack.getStringTranslator().moyensRelation, meanTitleView, StringTranslator.getNotTranslatedString(mean)));
				}

				JPanel goalTitleView = createDefautlTranslucentJPanel(BoxLayout.X_AXIS);
				goalTitleView.add(Box.createHorizontalStrut(VerticalStructSize));
				goalTitleView.add(createExpendedPanel(createTranslucentLabel(StringTranslator.getNotTranslatedString(goal)), meansPanel));

				goalsPanel.add(createStringView(goal, dataPack.getStringTranslator().objectifs, goalTitleView, StringTranslator.getNotTranslatedString(goal)));
				goalsPanel.add(meansPanel);
			}

			//			JPanel relationTitleView = createDefautlTranslucentJPanel(BoxLayout.X_AXIS);
			//			relationTitleView.add(Box.createHorizontalStrut(VerticalStructSize));
			//			relationTitleView.add(createExpendedPanel(createTranslucentLabel(StringTranslator.getNotTranslatedString(relationDescription)), goalsPanel));
			//			
			//			JPanel relationView = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);
			//			relationView.add(createStringView(relationDescription, dataPack.getStringTranslator().groups, relationTitleView, StringTranslator.getNotTranslatedString(relationDescription)));
			//			relationView.add(goalsPanel);

			panels.add(goalsPanel);			
		}
		titles.put(Messages.getString("TranslatorView.3"), panels); //$NON-NLS-1$
	}

	private JPanel createDefautlTranslucentJPanel(int axe) {
		JPanel panel = new JPanel();
		setTranslucentBackGroundColor(panel);
		panel.setLayout(new BoxLayout(panel, axe));
		return panel;
	}

	private void initView() {
		allViews = new JPanel();
		allViews.setLayout(new BoxLayout(allViews, BoxLayout.Y_AXIS));
		setTranslucentBackGroundColor(allViews);

		// Actors
		JPanel actorsPanel = new JPanel();
		JPanel verticalStruct = new JPanel();
		verticalStruct.add(Box.createHorizontalStrut(1));
		verticalStruct.setOpaque(false);
		//		actorsPanel.add(verticalStruct);
		actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.Y_AXIS));
		actorsPanel.add(createGroupPanel(dataPack.getActorsModel().getGroupeActeurs(), dataPack.getStringTranslator().actors));
		actorsPanel.setBackground(Color.lightGray);
		setDefaultTitleBorder(actorsPanel, Messages.getString("TranslatorView.4")); //$NON-NLS-1$

		// jeux acteurs
		//		JPanel jaView = new JPanel();
		//		jaView.setBackground(Color.blue);
		//		jaView.setLayout(new BoxLayout(jaView, BoxLayout.Y_AXIS));
		//		setDefaultTitleBorder(jaView, "Jeux d'acteurs");

		for (JeuActeur jeuActeur : dataPack.getJeuxActeur()) {
			actorsPanel.add(createJeuxActeurPanel(jeuActeur));
		}

		allViews.add(actorsPanel);
		//		allViews.add(jaView);

		// Moyens
		JComponent moyensPanel = setDefaultTitleBorder(createGroupPanel(dataPack.getActorsModel().getGroupeMoyens(), dataPack.getStringTranslator().moyensObjet), Messages.getString("TranslatorView.5")); //$NON-NLS-1$
		moyensPanel.setBackground(Color.pink);
		moyensPanel.setOpaque(true);
		allViews.add(moyensPanel);

		// Others
		allViews.setLayout(new BoxLayout(allViews, BoxLayout.Y_AXIS));
		Color[] colors = new Color[]{new Color(220, 220, 220), new Color(210, 90, 90), new Color(90, 210, 90), new Color(90, 90, 210)};
		int colorIndex = 0;
		for (String title : titles.keySet()) {
			Vector<JPanel> stringViews = titles.get(title);
			JComponent otherPanel = setDefaultTitleBorder(createTitleView(title, stringViews), title);
			otherPanel.setOpaque(true);
			otherPanel.setBackground(colors[(colorIndex++)%colors.length]);
			allViews.add(otherPanel);
		}

		//allViews.add(Box.createGlue());
		scrollPane.setViewportView(allViews);

		//scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel globalView = new JPanel(new BorderLayout());
		globalView.add(createSearchView(), BorderLayout.NORTH);
		globalView.add(scrollPane, BorderLayout.CENTER);
		globalView.add(createButtonPanel(), BorderLayout.SOUTH);

		add(globalView);

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {	}

			@Override
			public void componentResized(ComponentEvent e) {
				resizeView();
			}

			@Override
			public void componentMoved(ComponentEvent e) {	}

			@Override
			public void componentHidden(ComponentEvent e) { }
		});

	}

	private JComponent setTranslucentBackGroundColor(JComponent panel) {
		panel.setOpaque(false);
		panel.setBackground(Color.yellow);
		return panel;
	}

	private JComponent setDefaultTitleBorder(JComponent view, String text) {
		view.setBorder(BorderFactory.createBevelBorder(1));
		return view;
	}

	private JComponent createBulletPanel(JComponent view) {
		JPanel titlePanel = new JPanel();
		setTranslucentBackGroundColor(titlePanel);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		titlePanel.add(setTranslucentBackGroundColor(new JLabel(bulletIcon)));
		titlePanel.add(view);

		return titlePanel;
	}

	private JPanel createSearchView() {
		JPanel panel = new JPanel(new GridLayout());

		JTextField searchText = new JTextField();
		searchText.addFocusListener(getFocusListener());
		searchText.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(e.getSource() instanceof JTextField) {
					String searchString = ((JTextField)e.getSource()).getText();
					for(int i = 0 ; i < allPanels.size() ; i++){
						boolean display = false;
						if(searchString.compareTo("") == 0) { //$NON-NLS-1$
							display = true;
						}else if(allLabels.elementAt(i).toLowerCase().contains(searchString.toLowerCase()) || allTextFields.elementAt(i).getText().toLowerCase().contains(searchString.toLowerCase())) {
							display = true;
						}

						allPanels.elementAt(i).setVisible(display);
					}
				}
			}
		});

		panel.add(new JLabel(Messages.getString("TranslatorView.7"))); //$NON-NLS-1$
		panel.add(searchText);
		panel.setBorder(BorderFactory.createBevelBorder(5));

		JPanel result = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);

		result.add(Box.createVerticalStrut(10));
		result.add(panel);
		result.add(Box.createVerticalStrut(10));

		return result;
	}

	private FocusListener getFocusListener() {
		return new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}

			@Override
			public void focusGained(FocusEvent e) {
				Component source = (Component)e.getSource();
				scrollPane.getViewport().scrollRectToVisible(SwingUtilities.convertRectangle(source, source.getBounds(), scrollPane.getViewport()));
			}
		};
	}

	private JPanel createStringView(final Object object, final Hashtable<Object, String> translation, JComponent leftView, String label) {
		JPanel panel = new JPanel(new GridLayout());
		setTranslucentBackGroundColor(panel);

		final JTextField textField = new JTextField();
		Dimension textFielDimension = new Dimension(35, 30);
		textField.setMaximumSize(textFielDimension);
		textField.setPreferredSize(textFielDimension);
		textField.setMinimumSize(textFielDimension);

		if (translation.get(object) != null) {
			textField.setText(translation.get(object));
		}

		textField.addCaretListener(createTextFieldListener(object, translation));

		textField.addFocusListener(getFocusListener());

		final JPanel labelPanel = new JPanel();
		setTranslucentBackGroundColor(labelPanel);
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));

		if (leftView != null) {
			labelPanel.add(leftView);
		}

		// labelPanel.add(new
		// JLabel(StringTranslator.getNotTranslatedString(object)));
		labelPanel.add(Box.createGlue());

		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				labelPanel.setOpaque(false);
				scrollPane.repaint();
				updateAllTextField();
			}

			@Override
			public void focusGained(FocusEvent e) {
				labelPanel.setOpaque(true);
				scrollPane.repaint();
			}
		});

		panel.add(labelPanel);
		panel.add(textField);
		textField.validate();
		textField.repaint();

		allLabels.add(label);
		allTextFields.add(textField);
		allPanels.add(panel);
		allObjects.add(object);
		allTables.add(translation);

		return panel;
	}

	private void updateAllTextField() {
		for(int i = 0 ; i < allTextFields.size() ; i++) {
			String string = allTables.elementAt(i).get(allObjects.elementAt(i));
			if(string != null); {
				allTextFields.elementAt(i).setText(string);
			}
		}
	}

	private JPanel createJeuxActeurPanel(JeuActeur jeuActeur) {
		JPanel panel = new JPanel();
		setTranslucentBackGroundColor(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel subView = new JPanel();
		setTranslucentBackGroundColor(subView);
		subView.setLayout(new BoxLayout(subView, BoxLayout.Y_AXIS));
		for (String corp : jeuActeur.getListeCorps()) {
			subView.add(createGroupPanel(jeuActeur.getGroupeCorps(corp),
					dataPack.getStringTranslator().actors, 1));
		}

		JPanel titlePanel = new JPanel();
		setTranslucentBackGroundColor(titlePanel);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		JLabel expendeButton = createExpendedImage(subView);
		setTranslucentBackGroundColor(expendeButton);

		titlePanel.add(expendeButton);
		titlePanel.add(createTranslucentLabel(StringTranslator
				.getNotTranslatedString(jeuActeur.getGroupeJeuActeur())));
		titlePanel.add(Box.createGlue());

		// JPanel expendedButton =
		// createExpendedPanel(jeuActeur.getGroupeJeuActeur(),
		// dataPack.getStringTranslator().groups, subView);
		// createExpendedPanel(createStringView(jeuActeur.getGroupeJeuActeur(),
		// dataPack.getStringTranslator().groups), subView)
		panel.add(createStringView(jeuActeur.getGroupeJeuActeur(), dataPack.getStringTranslator().groups, titlePanel, StringTranslator.getNotTranslatedString(jeuActeur.getGroupeJeuActeur())));
		panel.add(subView);

		//		panel.setBorder(BorderFactory.createLineBorder(Color.blue, 1));

		return panel;
	}

	private JLabel createTranslucentLabel(String text) {
		String labelText;
		//		int maxSize = 80;
		JLabel label = (JLabel)setTranslucentBackGroundColor(new JLabel());
		//		if(text.length() > maxSize) {
		//			labelText = text.substring(0, maxSize-4) + "..."; //$NON-NLS-1$
		//		} else {
		labelText = text;
		//		}

		label.setText(labelText);
		label.setToolTipText(text);

		label.setPreferredSize(new Dimension(425, 25));
		label.setMaximumSize(new Dimension(425, 25));
		label.setMinimumSize(new Dimension(425, 25));

		return label;
	}

	private JLabel createExpendedImage(final JPanel subView) {
		final JLabel label = new JLabel(hidenIcon);
		setTranslucentBackGroundColor(label);
		label.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				subView.setVisible(!subView.isVisible());
				label.setIcon(subView.isVisible() ? hidenIcon : displayIcon);
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}
		});

		return label;
	}

	private CaretListener createTextFieldListener(final Object object,
			final Hashtable<Object, String> translation) {
		return new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (e.getSource() instanceof JTextField) {
					String text = ((JTextField) e.getSource()).getText();
					if (text.compareTo("") != 0) { //$NON-NLS-1$
						translation.put(object, text);
					} else {
						translation.remove(object);
					}
				}
			}
		};
	}

	private JPanel createTitleView(String title, Vector<JPanel> stringViews) {
		JPanel globalPanel = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);
		JLabel titleView = createTranslucentLabel(title);

		JPanel stringView = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);
		for (JPanel jPanel : stringViews) {
			setTranslucentBackGroundColor(jPanel);
			stringView.add(jPanel);
		}

		globalPanel.add(createExpendedPanel(titleView, stringView));
		globalPanel.add(stringView);
		globalPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		return globalPanel;
	}

	private JPanel createExpendedPanel(JComponent view, final JComponent subView) {
		JPanel panel = new JPanel(new BorderLayout());
		setTranslucentBackGroundColor(panel);

		final JLabel button = new JLabel(hidenIcon);
		setTranslucentBackGroundColor(button);
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				subView.setVisible(!subView.isVisible());
				button.setIcon(subView.isVisible() ? hidenIcon : displayIcon);
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}
		});

		panel.add(button, BorderLayout.WEST);
		panel.add(view, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createGroupPanel(DefaultMutableTreeNode group,
			Hashtable<Object, String> table) {
		return createGroupPanel(group, table, 0);
	}

	private JPanel createGroupPanel(DefaultMutableTreeNode group,
			Hashtable<Object, String> table, int depth) {
		JPanel panel = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);
		JPanel titlePanel = createDefautlTranslucentJPanel(BoxLayout.X_AXIS);

		if (group instanceof Groupe) {
			// create children panel
			titlePanel.add(Box.createHorizontalStrut(VerticalStructSize*depth));
			final JPanel childrenPanels = createDefautlTranslucentJPanel(BoxLayout.Y_AXIS);

			for (Enumeration<DefaultMutableTreeNode> nodes = group.children(); nodes
					.hasMoreElements();) {
				DefaultMutableTreeNode node = nodes.nextElement();
				childrenPanels.add(createGroupPanel(node, table, depth+1));
			}

			JLabel expendeButton = createExpendedImage(childrenPanels);

			titlePanel.add(expendeButton);
			titlePanel.add(setTranslucentBackGroundColor(createTranslucentLabel(StringTranslator
					.getNotTranslatedString(group))));

			panel.add(createStringView(group,
					dataPack.getStringTranslator().groups, titlePanel, StringTranslator.getNotTranslatedString(group)), StringTranslator.getNotTranslatedString(group));
			panel.add(childrenPanels);
		} else {
			// create string panel

			titlePanel.add(Box.createHorizontalStrut(VerticalStructSize*(depth-1) + VerticalStructSize/2));
			titlePanel.add(createBulletPanel(createTranslucentLabel(StringTranslator.getNotTranslatedString(group))));

			panel.add(createStringView(group, table, titlePanel, StringTranslator.getNotTranslatedString(group)));
		}

		//		panel.setBorder(BorderFactory.createLineBorder(Color.red, 1));

		return panel;
	}


	private JPanel createButtonPanel()
	{
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));
		JPanel buttonPanel = new JPanel(new GridLayout(1,3,5,5));

		JButton stat = new JButton(Messages.getString("Version_1_0_2.TranslatorView.8")); //$NON-NLS-1$
		stat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				displayStats();
			}
		});

		buttonPanel.add(stat);
		JButton importButton = new JButton(Messages.getString("Version_1_0_1.TranslatorView.8")); //$NON-NLS-1$
		importButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_1_0_1.TranslatorView.9")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
				{
					loadTranslationAndRebuildView();
				}
			}
		});
		buttonPanel.add(importButton);

		JButton exportButton = new JButton(Messages.getString("Version_1_0_1.TranslatorView.10")); //$NON-NLS-1$
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dataPack.getStringTranslator().setFilePath(null);
				Program.save(dataPack.getStringTranslator(), "dtt", Messages.getString("Version_1_0_1.TranslatorView.12"), true); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});

		buttonPanel.add(exportButton);

		JButton exitButton = new JButton(Messages.getString("Version_1_0_1.TranslatorView.13")); //$NON-NLS-1$
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Program.save(dataPack, true);

				if(Program.isTriades()) {
					ConfigTriades.getInstance().save();
				} else {
					ConfigCreator.getInstance().save();
				}
				System.exit(0);
			}
		});
		buttonPanel.add(exitButton);

		result.add(Box.createGlue());
		result.add(buttonPanel);
		result.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		return result;

	}

	protected void loadTranslationAndRebuildView()
	{
		mainFrame.remove(this);
		mainFrame.repaint();
		dataPack.loadNewTranslation();
		TranslatorView tV = new TranslatorView(dataPack, mainFrame); 
		mainFrame.add(tV);

		mainFrame.validate();
		mainFrame.repaint();
	}

	public void resizeView()
	{
		Dimension d = allViews.getSize();
		d.width = mainFrame.getWidth() - 50;
		allViews.setMaximumSize(d);
		revalidate();
	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Program.setIsTriade(true);
		final JFrame frame = new JFrame();

	
		frame.setIconImage(IconDatabase.vectorIconMoyens.elementAt(3).getImage());

		if(ConfigTriades.getInstance().getUseLocalhost()) {
			UniqueInstance uniqueInstance = new UniqueInstance(ConfigTriades.getInstance().getPort(), UniqueInstance.defaultMessage, new Runnable() {

				@Override
				public void run() {
					if(!frame.isVisible())
						frame.setVisible(true);
					// On demande à la mettre au premier plan.
					frame.toFront();
					frame.validate();
					frame.repaint();
				}
			});

			if(uniqueInstance.launch() == false) {
				DialogHandlerFrame.showErrorDialog(Messages.getString("Program.6")); //$NON-NLS-1$
				System.exit(0);
			}
		} else {
			if(DialogHandlerFrame.showYesNoDialog(Messages.getString("Program.9")) != JOptionPane.YES_OPTION) { //$NON-NLS-1$
				System.exit(0);
			}
		}

		new IconDatabase();
		String path = null;

		if(ExecutionMode.isIntern() == false) {
			path = Config.settingsDirectory + "datapack.dte";  //$NON-NLS-1$
		}

		final DataPack dataPack = (DataPack) Program.loadSavableObject(path, true);

		if(dataPack == null) {
			return;
		}
		//		 dataPack.setStringTranslator(null);

		if(dataPack.getStringTranslator() == null) {
			System.out.println(Messages.getString("Version_1_0_2.TranslatorView.9")); //$NON-NLS-1$
			dataPack.setStringTranslator(new StringTranslator());
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) {
				Program.save(dataPack, true);
				if(Program.isTriades()) {
					ConfigTriades.getInstance().save();
				} else {
					ConfigCreator.getInstance().save();
				}
			}
		});
		frame.setSize(windowsSize);
		TranslatorView tV = new TranslatorView(dataPack, frame); 
		frame.add(tV);
		frame.validate();
		frame.repaint();
		frame.setVisible(true);
	}

	private int wordCount(String s)
	{
		StringTokenizer tokenizer = new StringTokenizer(s, " "); //$NON-NLS-1$
		int result = 0;
		while (tokenizer.hasMoreTokens())
		{
			String t = tokenizer.nextToken();
			if (t.matches(".*\\w.*")) //$NON-NLS-1$
				result ++;
		}
		return result;
	}

	private static class TranslatorStat{
		protected int itemTranslated = 0, wordTranslated = 0;

	};

	private void statForTable(Map<Object, String> table, TranslatorStat stats)
	{


		for (Map.Entry<Object, String> entry : table.entrySet())
		{

			if (entry.getValue() != null)
			{
				int count = wordCount(entry.getValue());
				if (count > 0)
					stats.itemTranslated ++;
				stats.wordTranslated += count;
			}

		}
	}

	private void displayStats()
	{
		StringTranslator translator = dataPack.getStringTranslator();
		TranslatorStat stats = new TranslatorStat();
		statForTable(translator.actors, stats);
		statForTable(translator.groups, stats);
		statForTable(translator.actionTimes, stats);
		statForTable(translator.activities, stats);
		statForTable(translator.objectifs, stats);
		statForTable(translator.moyensRelation, stats);
		statForTable(translator.moyensObjet, stats);
		statForTable(translator.bricks, stats);
		statForTable(translator.steps, stats);

		String message = Messages.getString("Version_1_0_2.TranslatorView.12")+stats.itemTranslated+Messages.getString("Version_1_0_2.TranslatorView.13")+stats.wordTranslated+Messages.getString("Version_1_0_2.TranslatorView.14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$


		DialogHandlerFrame.showInformationDialog(message);


	}
}
