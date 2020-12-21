package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import models.Brick;
import models.BrickView;
import tools.ConfigCreator;
import tools.ConfigTriades;
import translation.Messages;
import Mailing.MailAddRelationForm;
import Mailing.MailForm;
import Mailing.MailView;
import client.ActorSheetAccess;
import client.ActorsSheetNavigationView;
import client.export.ExportModel;
import client.export.ExportPopUp;
import client.export.ExportView;
import dataPack.AutoSaveCreator;
import dataPack.DataPack;
import dataPack.JTreeActors;
import dataPack.SavableObject;
import dataPack.TreeListener;
import encrypting.Encrypting;
import encrypting.KeyChooserView;

public abstract class MainFrame extends JFrame {

	private static final long serialVersionUID = -1230397799638872517L;

	protected Controller controller;
	protected JTabbedPane tabbedPane;
	protected JPanel pArborescence;
	protected JPanel pSchema;
	protected JMenuBar menuBar;
	public DataPack datapack;
	protected JTreeActors mainJta;
	//public PopUpHelpView popUpHelp;
	public AutoSaveCreator autoSaveCreator;

	protected JMenu mFenetre;
	protected JMenu mFichier;
	protected JMenu mEdition;
	protected JMenu mAide;
	protected JMenu mLangue;
	protected JMenu mOutils;

	public MainFrame() {
		this(true);
	}

	public MainFrame(boolean init) {
		super();

		if(init) {
			createAndShowGUI();
		}
	}

	private void createAndShowGUI() {
		JPanel unPanelAnodin = new JPanel(new BorderLayout());
		controller = new Controller(this);
		tabbedPane = new JTabbedPane();
		mainJta = null;
		menuBar = myMenuBar();

		//		Image icone = Toolkit.getDefaultToolkit().getImage(
		//				"Icones/16x16/triades.png"); //$NON-NLS-1$

		// index 4 pour le jaune (fr) et index 2 pour le rouge (en)
		setIconImage(IconDatabase.vectorIconActorsBig.elementAt(2).getImage());
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				wouldYouLikeToSaveAndExit();
			}
		});

		setJMenuBar(menuBar);
		getContentPane().setLayout(new BorderLayout());
		unPanelAnodin.add(myToolBar(), BorderLayout.CENTER);
		unPanelAnodin.add(new JSeparator(), BorderLayout.SOUTH);
		getContentPane().add(unPanelAnodin, BorderLayout.NORTH);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		setVisible(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);

	}

	protected JMenuBar myMenuBar() {
		// Déclaration + Initialisations
		JMenuBar menuBar = new JMenuBar();
		mFichier = new JMenu(Messages.getString("MainFrame.1")); //$NON-NLS-1$
		mEdition = new JMenu(Messages.getString("MainFrame.2")); //$NON-NLS-1$
		mFenetre = new JMenu(Messages.getString("MainFrame.3")); //$NON-NLS-1$
		mAide = new JMenu(Messages.getString("MainFrame.4")); //$NON-NLS-1$
		JMenuItem nouveau = new JMenuItem(Messages.getString("MainFrame.5"), IconDatabase.iconNew); //$NON-NLS-1$
		JMenuItem ouvrir = new JMenuItem(Messages.getString("MainFrame.6"), IconDatabase.iconOpen); //$NON-NLS-1$
		JMenuItem enregistrer = new JMenuItem(Messages.getString("MainFrame.7"), //$NON-NLS-1$
				IconDatabase.iconSave);
		JMenuItem enregistrerSous = new JMenuItem(Messages.getString("MainFrame.8"), //$NON-NLS-1$
				IconDatabase.iconSaveAs);
		JMenuItem exporterDatapack = new JMenuItem(Messages.getString("MainFrame.9"), //$NON-NLS-1$
				IconDatabase.iconSaveAs);
		JMenuItem fermer = new JMenuItem(Messages.getString("MainFrame.10"), IconDatabase.iconClose); //$NON-NLS-1$
		JMenuItem quitter = new JMenuItem(Messages.getString("MainFrame.11"), IconDatabase.iconExit); //$NON-NLS-1$
		JMenuItem supprimer = new JMenuItem(Messages.getString("MainFrame.12"), //$NON-NLS-1$
				IconDatabase.iconDelete);
		JMenuItem renommer = new JMenuItem(Messages.getString("MainFrame.13"), IconDatabase.iconRename); //$NON-NLS-1$
		JMenuItem fermerOnglet = new JMenuItem(Messages.getString("MainFrame.14"), //$NON-NLS-1$
				IconDatabase.iconRemoveTab);
		//	JMenuItem aide = new JMenuItem(Messages.getString("MainFrame.15"), IconDatabase.iconHelp); //$NON-NLS-1$
		JMenuItem maj = new JMenuItem(Messages.getString("MainFrame.16"), IconDatabase.iconUpdate); //$NON-NLS-1$
		JMenuItem aPropos = new JMenuItem(Messages.getString("MainFrame.17"), //$NON-NLS-1$
				IconDatabase.iconAbout);
		JMenuItem sendMail = new JMenuItem(Messages.getString("MainFrame.18"), IconDatabase.iconRename); //$NON-NLS-1$
		JMenuItem showKeyGen = new JMenuItem(Messages.getString("Version_1_0_3.MainFrame.15"), IconDatabase.iconMoyen);
		JMenuItem exportLimitedDatapack = new JMenuItem(Messages.getString("Version_1_0_3.MainFrame.19"), IconDatabase.iconSaveAs);
		JMenuItem unlockDatapack = new JMenuItem(Messages.getString("Version_1_0_3.MainFrame.unlockDatapackButton"), IconDatabase.iconMoyen);
		//

		// Menu "Fichier"
		nouveau.addActionListener(controller.actionNew);
		nouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_DOWN_MASK));
		mFichier.add(nouveau);
		ouvrir.addActionListener(controller.actionOpen);
		ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_DOWN_MASK));
		mFichier.add(ouvrir);
		//
		mFichier.addSeparator();
		//
		enregistrer.addActionListener(controller.actionSave);
		enregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		mFichier.add(enregistrer);

		if(Program.isTriadesLoading() == false) {
			enregistrerSous.addActionListener(controller.actionSaveAs);
			enregistrerSous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
			mFichier.add(enregistrerSous);
		}

		if(Program.isTriadesLoading() == false) {
			exporterDatapack.addActionListener(controller.actionExoprterDataPack);
			mFichier.add(exporterDatapack);
		}

		//
		mFichier.addSeparator();
		//		
		fermer.addActionListener(controller.actionCloseAll);
		fermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.CTRL_DOWN_MASK));
		mFichier.add(fermer);
		//
		mFichier.addSeparator();
		//
		quitter.addActionListener(controller.actionExit);
		quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.CTRL_DOWN_MASK));
		mFichier.add(quitter);

		// Menu "Edition"
		supprimer.addActionListener(controller.actionDelete);
		supprimer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		renommer.addActionListener(controller.actionRename);
		// supprimer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		if(Program.isTriadesLoading() == false) {
			mEdition.add(renommer);
			mEdition.add(supprimer);

			mEdition.addSeparator();
		}
		JMenuItem changeTrad = new JMenuItem(Messages.getString("Version_1_0_1.MainFrame.37")); //$NON-NLS-1$
		changeTrad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (datapack != null)
					datapack.loadNewTranslation();
			}
		});

		mEdition.add(changeTrad);

		if (ExecutionMode.isDebug())
		{
			JMenuItem changeKey = new JMenuItem("Changer la clé");
			changeKey.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SecretKeySpec newKey = KeyChooserView.createView();
					if (newKey != null)
						Encrypting.getInstance().setKey(newKey);
	//				DialogHandlerFrame.showErrorDialog("Code commenté pour ne pas fournir KeyChooserView");
				}
			});
			mEdition.add(changeKey);
		}

		//
		//
		fermerOnglet.addActionListener(controller.actionRemoveTab);
		fermerOnglet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));

		// Menu "Fenetre"
		mFenetre.add(fermerOnglet);

		// Menu "Aide"
		//TODO Aide à remettre une fois l'aide mis à jour
		//		aide.addActionListener(controller.actionHelp);
		//		aide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		//		mAide.add(aide);

		final MainFrame frame = this;

		maj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogHandlerFrame
				.showInformationDialog(frame, Messages.getString("MainFrame.21"), //$NON-NLS-1$
						Messages.getString("MainFrame.22"), //$NON-NLS-1$
						IconDatabase.iconUpgrade);
			}

		});

		//Menus langue et outils
		if (Program.isTriades() || Program.isTriadesLoading())
		{
			mLangue = new JMenu(Messages.getString("Version_1_0_3.MainFrame.24"));

			final Locale usedLocale;
			usedLocale = ConfigTriades.getInstance().getUsedLocale();
			ButtonGroup languageGroup = new ButtonGroup();
			for(Locale l : Messages.getAvailableLocales())
			{
				JRadioButtonMenuItem itemLangue = new JRadioButtonMenuItem(l.getDisplayName());
				itemLangue.setSelected(l.equals(usedLocale));
				languageGroup.add(itemLangue);
				final Locale fLocale = l;
				itemLangue.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (!fLocale.equals(usedLocale))
						{

							if (Program.isTriades() || Program.isTriadesLoading())
							{
								ConfigTriades.getInstance().setUsedLocale(fLocale);
							}
							DialogHandlerFrame.showInformationDialog(Messages.getString("Version_1_0_3.MainFrame.27"));
						}
					}
				});
				mLangue.add(itemLangue);
			}
			mOutils = new JMenu(Messages.getString("Version_1_0_3.MainFrame.34"));

			showKeyGen.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					KeyGenerator.showKeyGenerator();
				}
			});
			mOutils.add(showKeyGen);

			exportLimitedDatapack.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					datapack.exportLimitedDatapack();
				}
			});
			mOutils.add(exportLimitedDatapack);

		}



		mAide.add(maj);
		aPropos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String message = Messages.getString("MainFrame.24"); //$NON-NLS-1$
				if ((Program.isTriades() || Program.isTriadesLoading()) && datapack != null && datapack.getEndDemoCalendar() != null)
				{
					Calendar c = datapack.getEndDemoCalendar();
					Locale l = ConfigTriades.getInstance().getUsedLocale();
					message += Messages.getString("Version_1_0_3.MainFrame.38")+c.get(Calendar.DAY_OF_MONTH)+" "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, l)+" "+c.get(Calendar.YEAR); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
				}
				DialogHandlerFrame
				.showInformationDialog(
						frame,
						Messages.getString("MainFrame.23"), //$NON-NLS-1$
						message, //$NON-NLS-1$
						IconDatabase.iconTriad);
			}

		});
		mAide.add(aPropos);

		final MailForm mailForm = new MailAddRelationForm();
		sendMail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MailView(Messages.getString("MainFrame.25"), mailForm); //$NON-NLS-1$
			}
		});
		mAide.add(sendMail);

		unlockDatapack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!datapack.isLocked())
				{
					DialogHandlerFrame.showInformationDialog(Messages.getString("Version_1_0_3.MainFrame.alreadyUnlocked"));
					return;
				}
				UnlockingPopup.showPopup(false);
			}
		});
		mAide.add(unlockDatapack);

		// Barre de menu
		menuBar.add(mFichier);

		menuBar.add(mEdition);

		menuBar.add(mFenetre);
		if(mLangue != null)
			menuBar.add(mLangue);
		if (mOutils != null)
			menuBar.add(mOutils);
		menuBar.add(mAide);



		return menuBar;
	}

	protected JPanel myPanel(Object object) {
		return myPanel(object, new JPanel());
	}
	protected abstract JPanel myPanel(Object object, JPanel mainPanel);

	protected JPanel drawPanel(JComponent jta, PopUpView popUp, JPanel pSchema, JPanel panel)
	{
		return drawPanel(jta,popUp, pSchema, panel, null);
	}

	protected JPanel drawPanel(JComponent jta, PopUpView popUp, JPanel pSchema, JPanel panel, PopUpHelpView popUpHelp) {
		// Déclarations + initialisations
		JSplitPane splitPane = new JSplitPane();

		panel.setLayout(new BorderLayout());
		JPanel superArbo = new JPanel(new BorderLayout());
		JPanel superSchema = new JPanel(new BorderLayout());
		pArborescence = new JPanel(new BorderLayout());
		JLayeredPane layeredSchema = new JLayeredPane();
		JToolBar pArborescenceMini = new JToolBar(JToolBar.VERTICAL);
		JToolBar pSchemaMini = new JToolBar(JToolBar.VERTICAL);
		JButton agrandirArbo = new JButton(IconDatabase.iconMaximize);
		JButton agrandirSche = new JButton(IconDatabase.iconMaximize);
		JComponent jsp;

		// Arborescence réduite
		agrandirArbo.setFocusable(false);
		agrandirArbo.setToolTipText(Messages.getString("MainFrame.26")); //$NON-NLS-1$
		agrandirArbo.addActionListener(controller.actionMaximize);
		pArborescenceMini.setFloatable(false);
		pArborescenceMini.addSeparator();
		pArborescenceMini.add(new JLabel(IconDatabase.iconTree));
		pArborescenceMini.add(agrandirArbo);
		pArborescenceMini.setVisible(false);

		// Arborescence agrandie
		pArborescence.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory
				.createLineBorder(Color.GRAY)));
		if (jta == null)
			jsp = new JScrollPane();
		else if (jta instanceof JTree){
			jsp = new JScrollPane(jta,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		else
		{
			jsp = jta;
		}
		jsp.setBorder(BorderFactory.createEmptyBorder());
		jsp.setMaximumSize(new Dimension(100,2000));
		//	pArborescence.add(new TitleBar(IconDatabase.iconTree, Messages.getString("MainFrame.27")), //$NON-NLS-1$
		//			BorderLayout.NORTH);
		pArborescence.add(jsp, BorderLayout.CENTER);
		pArborescence.setMaximumSize(new Dimension(100,2000));

		// Super arborescence
		//superArbo.add(pArborescenceMini, BorderLayout.WEST);
		superArbo.add(pArborescence, BorderLayout.CENTER);

		// Schéma réduit
		agrandirSche.setFocusable(false);
		agrandirSche.setToolTipText(Messages.getString("MainFrame.28")); //$NON-NLS-1$
		agrandirSche.addActionListener(controller.actionMaximize);
		pSchemaMini.setFloatable(false);
		pSchemaMini.addSeparator();
		pSchemaMini.add(new JLabel(IconDatabase.iconSchema));
		pSchemaMini.add(agrandirSche);
		pSchemaMini.setVisible(false);

		pSchema.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(2, 2, 2, 2), BorderFactory
				.createLineBorder(Color.GRAY)));

		// Super schéma
		superSchema.add(pSchemaMini, BorderLayout.WEST);
		superSchema.add(pSchema, BorderLayout.CENTER);

		// Layered schema
		GridBagLayout gridBag = new GridBagLayout();
		layeredSchema.setLayout(gridBag);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		layeredSchema.add(superSchema, c);
		layeredSchema.setLayer(superSchema, 1);
		if (popUp != null) {
			c.anchor = GridBagConstraints.LAST_LINE_START;
			c.fill = GridBagConstraints.NONE;
			layeredSchema.add(popUp, c);
			layeredSchema.setLayer(popUp, 2);
		}
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		//TODO Aide Mettre à jour la popup help
		if (popUpHelp != null)
		{

			layeredSchema.setLayer(popUpHelp, 3);
			layeredSchema.add(popUpHelp, c);
		}
		//popUpHelp.setVisible(false);
		// Panel total
		//		panel.add(superArbo, BorderLayout.WEST);
		//		panel.add(layeredSchema, BorderLayout.CENTER);
		splitPane.setLeftComponent(superArbo);
		splitPane.setRightComponent(layeredSchema);

		panel.add(splitPane, BorderLayout.CENTER);
		//		splitPane.setDividerLocation(0.3);
		splitPane.setDividerLocation(330);
		return panel;
	}

	private JToolBar myToolBar() {
		// Déclarations + initialisations
		//
		JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
		JButton nouveau = new JButton(IconDatabase.iconNew);
		JButton ouvrir = new JButton(IconDatabase.iconOpen);
		JButton enregistrer = new JButton(IconDatabase.iconSave);
		JButton enregistrerSous = new JButton(IconDatabase.iconSaveAs);
		JButton delete = new JButton(IconDatabase.iconDelete);
		//	JButton aide = new JButton(IconDatabase.iconHelp);
		JButton deleteTab = new JButton(IconDatabase.iconRemoveTab);

		// Construction de la tool bar
		//
		nouveau.setFocusable(false);
		nouveau.setToolTipText(Messages.getString("MainFrame.29")); //$NON-NLS-1$
		nouveau.addActionListener(controller.actionNew);
		toolBar.add(nouveau);
		ouvrir.setFocusable(false);
		ouvrir.setToolTipText(Messages.getString("MainFrame.30")); //$NON-NLS-1$
		ouvrir.addActionListener(controller.actionOpen);
		toolBar.add(ouvrir);
		//
		toolBar.addSeparator();
		//
		enregistrer.setFocusable(false);
		enregistrer.setToolTipText(Messages.getString("MainFrame.31")); //$NON-NLS-1$
		enregistrer.addActionListener(controller.actionSave);
		toolBar.add(enregistrer);

		if(Program.isTriadesLoading() == false) {
			enregistrerSous.setFocusable(false);
			enregistrerSous.setToolTipText(Messages.getString("MainFrame.32")); //$NON-NLS-1$
			enregistrerSous.addActionListener(controller.actionSaveAs);
			toolBar.add(enregistrerSous);
		}
		//
		toolBar.addSeparator();
		//
		if(Program.isTriadesLoading() == false) {
			delete.setFocusable(false);
			delete.setToolTipText(Messages.getString("MainFrame.33")); //$NON-NLS-1$
			delete.addActionListener(controller.actionDelete);
			toolBar.add(delete);

			//
			toolBar.addSeparator();
		}

		//TODO Aide a remettre quand on aura une aide à jour
		//		aide.setFocusable(false);
		//		aide.setToolTipText(Messages.getString("MainFrame.34")); //$NON-NLS-1$
		//		aide.addActionListener(controller.actionHelp);
		//		toolBar.add(aide);
		//
		toolBar.add(Box.createGlue());
		//
		deleteTab.setFocusable(false);
		deleteTab.setToolTipText(Messages.getString("MainFrame.35")); //$NON-NLS-1$
		deleteTab.addActionListener(controller.actionRemoveTab);
		toolBar.add(deleteTab);

		return toolBar;
	}

	public Object addTab(Object object) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).equals(object.toString())) {
				tabbedPane.setSelectedIndex(i);
				return object;
			}
		}

		if (object.getClass() == Brick.class) {
			//popUpHelp = new PopUpHelpView();
			//popUpHelp.setView(PopUpHelpView.showBrickHelp());
			tabbedPane.addTab(object.toString(), myPanel(object));
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		}  else if (object.getClass() == ExportModel.class) {
			ExportModel exportModel = (ExportModel)object;

			JPanel mainPanel = new JPanel();
			ExportView eVTemp = new ExportView(exportModel, new TreeListener(),
					new ExportPopUp(exportModel, mainPanel));

			tabbedPane.add(exportModel.getName(), myPanel(eVTemp, mainPanel));
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);			
		} else if(object.getClass() == ActorsSheetNavigationView.class) {
			if(datapack != null) {
				tabbedPane.addTab(ActorSheetAccess.ActorsSheetTabName, myPanel(object));
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			}
		} else {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("MainFrame.36")); //$NON-NLS-1$
		}
		return object;
	}

	public JComponent addTab(JComponent view, String title) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTitleAt(i).equals(title)) {
				tabbedPane.setSelectedIndex(i);
				return view;
			}
		}

		tabbedPane.add(title, view);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		return view;
	}

	public Component getTabByName(String name)
	{
		for (int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			if (tabbedPane.getTitleAt(i).equals(name))
			{
				return tabbedPane.getComponentAt(i);
			}
		}
		return null;
	}

	public boolean renameTab(String oldName, String newName) {
		if(oldName.trim().equals(newName.trim()) ||  newName.trim().equals("")) { //$NON-NLS-1$
			return true;
		}

		for(int i = 0 ; i < tabbedPane.getTabCount() ; i++) {
			if(tabbedPane.getTitleAt(i).equals(oldName)) {
				Component tab = getTabByName(tabbedPane.getTitleAt(i));
				tabbedPane.removeTabAt(i);
				tabbedPane.insertTab(newName, null, tab, null, i);
				tabbedPane.setSelectedIndex(i);
				tabbedPane.validate();
				return true;
			}
		}

		return false;
	}

	public boolean closeTab(String name)
	{

		for (int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			if (tabbedPane.getTitleAt(i).equals(name))
			{
				tabbedPane.removeTabAt(i);
				System.out.println("Fermeture d'un onglet"); //$NON-NLS-1$
				return true;
			}
			else
			{
				System.out.println("Titre de l'onglet : "+tabbedPane.getTitleAt(i)+" et titre recherché : "+name); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return false;

	}

	public void clearTabsAndShow(String title, JPanel panel)
	{
		tabbedPane.removeAll();
		tabbedPane.addTab(title, panel);
	}

	protected void wouldYouLikeToSaveAndExit() {
		SavableObject obj = datapack;

		if(datapack == null) {
			System.exit(1);
		}

		if(Program.isTriades()) {
			ConfigTriades.getInstance().save();
		} else {
			ConfigCreator.getInstance().getLastDatapack().addLastObject(obj.getFilePath());
			ConfigCreator.getInstance().save();
		}

		Program.myMainFrame.autoSaveCreator.setEnable(false);
		Program.myMainFrame.autoSaveCreator.removeAutoSavedFile();

		//TODO voir pour ajouter un LockFile ou pas pour lancer qu'une instance. 
		//		try {
		//			Program.fileLock.release();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}

		boolean exit = false;

		if (obj != null) 
		{
			if (!ExecutionMode.isIntern())
			{
				if (DialogHandlerFrame.showYesNoDialog(Messages.getString("MainFrame.0")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
				{
					Program.save(obj, true);
					exit = true;
				}
				else
				{
					return;
				}
			}
			else
			{
				setAlwaysOnTop(true);
				int choice = DialogHandlerFrame.showYesNoCancelDialog(this,
						Messages.getString("MainFrame.40")); //$NON-NLS-1$

				if (choice == JOptionPane.YES_OPTION) {
					if(!Program.isTriades()) {
						Program.save(obj, true);
					} else {
						Program.save(obj, true);
					}

					exit = true;
				} else if (choice == JOptionPane.NO_OPTION) {
					exit = true;
				}
			}
		} else
			exit = true;

		if(exit) {
			if(Program.fileLock != null) {
				try {
					Program.fileLock.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.exit(0);
		}

		setAlwaysOnTop(false);
	}

	public JTreeActors getMainJTree() {
		return mainJta;
	}

	public Integer getTabLength() {
		return new Integer(tabbedPane.getComponentCount());
	}

	public DataPack getDataPack() {
		return datapack;
	}

	public abstract void initialState(MainFrame mf);

	public abstract void setDataPack(DataPack dtp);

	public abstract BrickView<?, ?> getActiveModelView();

	public void showMainTab() {
		tabbedPane.setSelectedIndex(0);
	}

	public boolean showTabByName(String name) {
		for (int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			if (tabbedPane.getTitleAt(i).equals(name))
			{
				tabbedPane.setSelectedIndex(i);
				return true;
			}
		}
		return false;
	}
}
