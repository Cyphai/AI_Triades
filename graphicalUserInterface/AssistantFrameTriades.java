package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import translation.Messages;
import client.Session;
import client.SessionManagementView;

public class AssistantFrameTriades extends JFrame {

	private static final long serialVersionUID = 5303866988917452819L;

	protected JList listOpen;
	protected MainFrame mf;
	protected JRadioButton jrbOpen;
	protected JRadioButton jrbAll;
	protected JRadioButton jrbNew;
	public AssistantFrameTriades(MainFrame mf) {
		this.mf = mf;
		setAlwaysOnTop(true);
		setTitle(Messages.getString("AssistantFrameTriades.0")); // titre //$NON-NLS-1$
		Image icone = Toolkit.getDefaultToolkit().getImage(
				"Icones" + File.separatorChar + "16x16" + File.separatorChar + "triades.png"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setIconImage(icone);
		setSize(450, 600); // taille
		setLocationRelativeTo(mf); // fenêtre centrée sur la main frame
		setResizable(false); // non redimensionnable
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		getContentPane().add(this.myPanel(mf));
	}

	private JPanel myPanel(final MainFrame mf) {
		// Déclarations + initialisations
		final JPanel panel = new JPanel(new BorderLayout());
		JPanel north = new JPanel(new BorderLayout());
		JPanel south = new JPanel(new BorderLayout());
		JPanel center = new JPanel();
		JPanel pTitle = new JPanel();
		JPanel pChoices = new JPanel(new GridLayout(3, 1));
		JPanel p2Buttons = new JPanel();
		p2Buttons.setLayout(new GridLayout(1,2));
		JPanel pButtons = new JPanel();
		listOpen = new JList(mf.getDataPack().getExportModule()
				.getSessionList().toArray());
		ButtonGroup buttonGroup = new ButtonGroup();
		jrbAll = new JRadioButton(
				Messages.getString("AssistantFrameTriades.2"), true); //$NON-NLS-1$
		jrbNew = new JRadioButton(
				Messages.getString("AssistantFrameTriades.3"), true); //$NON-NLS-1$
		jrbOpen = new JRadioButton(
				Messages.getString("AssistantFrameTriades.4"), true); //$NON-NLS-1$
		final JButton jbOk = new JButton(Messages.getString("AssistantFrameTriades.5")); //$NON-NLS-1$
		JButton sessionManagement = new JButton(Messages.getString("Version_1_0_1.AssistantFrameTriades.1")); //$NON-NLS-1$
		JButton jbCancel = new JButton(Messages.getString("AssistantFrameTriades.6")); //$NON-NLS-1$

		if (listOpen.getModel().getSize() == 0) {
			listOpen.setEnabled(false);
			jrbOpen.setEnabled(false);
		}


		// Barre de titre
		pTitle.setBackground(Color.WHITE);
		pTitle.setLayout(new BoxLayout(pTitle, BoxLayout.X_AXIS));
		pTitle.add(Box.createHorizontalStrut(10));
		pTitle.add(new JLabel(Messages.getString("AssistantFrameTriades.7"))); //$NON-NLS-1$
		pTitle.add(Box.createGlue());
		pTitle.add(new JLabel(IconDatabase.iconAssistant));
		north.add(pTitle, BorderLayout.CENTER);
		north.add(new JSeparator(), BorderLayout.SOUTH);

		// Zone à choix
		buttonGroup.add(jrbNew);
		buttonGroup.add(jrbOpen);
		buttonGroup.add(jrbAll);
		jrbAll.setSelected(true);

		JPanel p1 = new JPanel(new FlowLayout());
		p1.add(new JLabel(IconDatabase.iconModel));
		p1.add(jrbAll);
		pChoices.add(p1);

		p1 = new JPanel(new FlowLayout());
		p1.add(new JLabel(IconDatabase.iconNewFile));
		p1.add(jrbNew);
		pChoices.add(p1);

		p1 = new JPanel(new FlowLayout());
		p1.add(new JLabel(IconDatabase.iconOpenFile));
		p1.add(jrbOpen);
		pChoices.add(p1);

		listOpen.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (listOpen.isEnabled())
				{
					jrbOpen.setSelected(true);
					if (arg0.getClickCount() == 2) {
						onButtonOk();
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
		});
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.add(pChoices);
		JScrollPane scrollPane = new JScrollPane(listOpen);//, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
		scrollPane.setPreferredSize(new Dimension(350, 115));
		scrollPane.setBorder(BorderFactory.createTitledBorder(Messages.getString("AssistantFrameTriades.8"))); //$NON-NLS-1$
		center.add(scrollPane);

		// Zone boutons
		jbOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onButtonOk();
			}
		});
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		sessionManagement.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((MainFrameTriades)mf).addTab(new SessionManagementView(), Messages.getString("Version_1_0_1.AssistantFrameTriades.9")); //$NON-NLS-1$
				setVisible(false);
			}
		});
		JPanel allDownButtonPanel = new JPanel(new BorderLayout());
		allDownButtonPanel.add(sessionManagement, BorderLayout.WEST);
		
		allDownButtonPanel.add(Box.createHorizontalStrut(50), BorderLayout.CENTER);
		
		p2Buttons.add(jbOk);
		p2Buttons.add(jbCancel);
		allDownButtonPanel.add(p2Buttons, BorderLayout.EAST);
		
		pButtons.add(Box.createHorizontalStrut(100));
		pButtons.add(allDownButtonPanel);
		pButtons.add(Box.createHorizontalStrut(100));
		south.add(new JSeparator(), BorderLayout.NORTH);
		south.add(pButtons, BorderLayout.CENTER);

		// Panel global
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);
		return panel;
	}

	protected void onButtonOk() {
		if (jrbAll.isSelected()) {
			setVisible(false);
			mf.getDataPack().setCurrentSession(mf.getDataPack().getExportModule().getFullSession());
			((MainFrameTriades) mf).displayObject(mf.getDataPack().getExportModule().getFullSession());
		} else if (jrbNew.isSelected()) {
			setVisible(false);
			((MainFrameTriades) mf)
			.displayObject(new ActorListView(mf));
		} else if (jrbOpen.isSelected()) {
			setVisible(false);
			Session selectedSession = (Session)listOpen.getSelectedValue();
			//			if (selectedSession == null)
			//				return;
			mf.getDataPack().getExportModule().upSession(
					selectedSession);
			mf.getDataPack().setCurrentSession(selectedSession);
			((MainFrameTriades) mf).displayObject(selectedSession);
		}		
	}

	public void refreshAndShow() {
		
		
		DefaultComboBoxModel listModel = new DefaultComboBoxModel(mf.getDataPack().getExportModule().getSessionList().toArray());
		mf.getDataPack().setCurrentSession(null);
		listOpen.setModel(listModel);
		if (listModel.getSize() == 0) {
			listOpen.setEnabled(false);
			jrbOpen.setEnabled(false);
		} else {
			listOpen.setEnabled(true);
			jrbOpen.setEnabled(true);
		}
		jrbAll.setSelected(true);

		validate();
		setVisible(true);
	}
}
