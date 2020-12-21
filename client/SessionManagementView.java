package client;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.MainFrame;
import graphicalUserInterface.MainFrameTriades;
import graphicalUserInterface.Program;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import translation.Messages;
import dataPack.SavableObject;

public class SessionManagementView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1510850269528011512L;

	protected JList sessionList;
	protected DefaultListModel listModel;

	public SessionManagementView()
	{
		super();
		buildPanel();
	}

	protected void buildPanel()
	{
		setLayout(new BorderLayout());

		listModel = new DefaultListModel();
		sessionList = new JList(listModel);
		sessionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		refreshListModel();

		sessionList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50), BorderFactory.createTitledBorder(Messages.getString("Version_1_0_1.SessionManagementView.0")))); //$NON-NLS-1$
		add(new JScrollPane(sessionList), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(11,1));




		JButton open = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.1")); //$NON-NLS-1$
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sessionList.getSelectedValues().length > 1)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.SessionManagementView.2")); //$NON-NLS-1$
				}
				else
				{
					Session selectedSession = (Session)sessionList.getSelectedValue();
					if (selectedSession != null)
					{
						MainFrame mf = Program.myMainFrame;
						mf.getDataPack().getExportModule().upSession(
								selectedSession);
						mf.getDataPack().setCurrentSession(selectedSession);
						((MainFrameTriades) mf).displayObject(selectedSession);
					}
					else 
					{
						DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.SessionManagementView.3")); //$NON-NLS-1$
					}
				}
			}
		});
		buttonPanel.add(open);

		JButton selectAll = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.4")); //$NON-NLS-1$
		selectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sessionList.getSelectionModel().setSelectionInterval(0, sessionList.getModel().getSize()-1);	
				sessionList.repaint();
			}
		});
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(selectAll);

		JButton importButton = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.5")); //$NON-NLS-1$
		importButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object loadedObject = Program.loadSavableObject(null,"dts",Messages.getString("Version_1_0_1.SessionManagementView.7"), true); //$NON-NLS-1$ //$NON-NLS-2$
				try
				{
					ExportedSessions importedSession = (ExportedSessions)loadedObject;
					if (importedSession != null)
					{
						addSessionToList(importedSession.getSessions());	

						sessionList.getSelectionModel().clearSelection();
						refreshListModel();
					}
				}
				catch (Exception ex)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.SessionManagementView.8")+ex.getMessage()); //$NON-NLS-1$
				}
			}
		});
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(importButton);

		JButton export = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.9")); //$NON-NLS-1$
		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveSelectedSessions();
				ArrayList<Session> currentSessions = Program.myMainFrame.getDataPack().getExportModule().getSessionList();
				for (Object o : sessionList.getSelectedValues())
					currentSessions.remove(o);	
				sessionList.getSelectionModel().clearSelection();
				refreshListModel();

			}
		});
		buttonPanel.add(export);

		JButton saveAs = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.10")); //$NON-NLS-1$
		saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveSelectedSessions();
			}
		});
		buttonPanel.add(saveAs);

		JButton delete = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.11")); //$NON-NLS-1$
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sessionList.getSelectedValues().length == 0)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.SessionManagementView.12")); //$NON-NLS-1$
				}
				else
				{
					boolean multi = sessionList.getSelectedValues().length > 1;
					if (DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_1_0_1.SessionManagementView.13")+(multi?Messages.getString("Version_1_0_1.SessionManagementView.14"):Messages.getString("Version_1_0_1.SessionManagementView.15"))+Messages.getString("Version_1_0_1.SessionManagementView.16")) == JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					{
						ArrayList<Session> currentSessions = Program.myMainFrame.getDataPack().getExportModule().getSessionList();
						for (Object o : sessionList.getSelectedValues())
							currentSessions.remove(o);
						sessionList.getSelectionModel().clearSelection();
						refreshListModel();
					}
				}

			}
		});
		buttonPanel.add(delete);

		JButton rename = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.17")); //$NON-NLS-1$
		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Object o : sessionList.getSelectedValues())
				{
					Session s = (Session)o;
					String newName = JOptionPane.showInputDialog(Messages.getString("Version_1_0_1.SessionManagementView.18")+s.getName(), s.getName()); //$NON-NLS-1$
					if (newName == null)
						break;
					s.setName(newName);
					sessionList.getSelectionModel().clearSelection();
					refreshListModel();
				}
			}
		});
		buttonPanel.add(rename);
		
		if (ExecutionMode.isDebug())
		{
			JButton merge = new JButton("Fusionner");
			merge.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (sessionList.getSelectedValues().length < 2)
					{
						DialogHandlerFrame.showErrorDialog("Veuillez sélectionner au moins 2 sessions pour pouvoir les fusionner");
					}
					String sessionName = (String) JOptionPane.showInputDialog(Program.myMainFrame, "Veuillez saisir le nom de la nouvelle session", "Fusion de session", JOptionPane.INFORMATION_MESSAGE, null, null, "Nom de la nouvelle session");
					if (sessionName != null)
					{
						List<Session> sessionsToMerge = new ArrayList<Session>();
						for (Object o : sessionList.getSelectedValues())
						{
							if (o instanceof Session)
								sessionsToMerge.add((Session) o);
							else 
								throw new IllegalStateException("A selected object of sessionList is not a session : "+o);
						}
						
						Session newSession = new Session(sessionName, sessionsToMerge);
						Program.myMainFrame.getDataPack().getExportModule().addNewSession(newSession);
						listModel.addElement(newSession);
						SessionManagementView.this.repaint();
					}
				}
			});
			buttonPanel.add(merge);
		}
		
		buttonPanel.add(Box.createGlue());

		JButton close = new JButton(Messages.getString("Version_1_0_1.SessionManagementView.19")); //$NON-NLS-1$
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((MainFrameTriades)Program.myMainFrame).showAssistant();
			}
		});
		buttonPanel.add(close);
		JPanel interPanel = new JPanel(new BorderLayout());
		buttonPanel.setPreferredSize(new Dimension(200,300));
		interPanel.add(buttonPanel, BorderLayout.CENTER);
		interPanel.add(Box.createGlue(), BorderLayout.NORTH);


		add(interPanel, BorderLayout.EAST);

		JLabel label = new JLabel(Messages.getString("Version_1_0_1.SessionManagementView.20")); //$NON-NLS-1$
		label.setAlignmentX((float) 0.5);
		add(label, BorderLayout.SOUTH);
		//validate();
		interPanel.add(Box.createVerticalStrut(Program.myMainFrame.getHeight() - 575), BorderLayout.NORTH);


	}

	protected class ExportedSessions implements SavableObject{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1234119027552750734L;
		protected String filepath;
		protected Vector<Session> sessions;

		public Vector<Session> getSessions() {
			return sessions;
		}

		public void setSessions(Vector<Session> sessions) {
			this.sessions = sessions;
		}

		public ExportedSessions(Vector<Session> sessions)
		{
			this.sessions = sessions;
			filepath = null;
		}

		@Override
		public String getFilePath() {
			return filepath;
		}

		@Override
		public void setFilePath(String _filePath) {
			filepath = _filePath;			
		}
	}

	protected void saveSelectedSessions()
	{
		Vector<Session> savedSessions = new Vector<Session>();
		for (Object o :sessionList.getSelectedValues())
		{
			savedSessions.add((Session)o);
		}


		if (savedSessions.size() == 0)
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.SessionManagementView.21")); //$NON-NLS-1$
		}
		else
		{
			Program.save(new ExportedSessions(savedSessions), Messages.getString("Version_1_0_1.SessionManagementView.22"), Messages.getString("Version_1_0_1.SessionManagementView.23"), true); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	protected void addSessionToList(Vector<Session> newSessions)
	{
		for (Session s : newSessions)
		{
			Session toReplace = null;
			ArrayList<Session> currentSessions = Program.myMainFrame.getDataPack().getExportModule().getSessionList();
			for (Session s2 : currentSessions)
			{
				if (s.getName().equals(s2.getName()))
				{
					toReplace = s2;
					break;
				}
			}
			if (toReplace == null)
			{
				currentSessions.add(s);
				Program.myMainFrame.getDataPack().getExportModule().upSession(s);
			}
			else
			{
				int r = DialogHandlerFrame.showYesNoCancelDialog(Messages.getString("Version_1_0_1.SessionManagementView.24")+s.getName()+Messages.getString("Version_1_0_1.SessionManagementView.25")); //$NON-NLS-1$ //$NON-NLS-2$
				if (r == JOptionPane.YES_OPTION)
				{
					currentSessions.remove(toReplace);
					currentSessions.add(s);
					Program.myMainFrame.getDataPack().getExportModule().upSession(s);
				}
				else if (r == JOptionPane.CANCEL_OPTION)
				{

					break;
				}
			}
		}
	}

	
	
	protected void refreshListModel()
	{
		listModel.removeAllElements();
		for (Session s : Program.myMainFrame.getDataPack().getExportModule().getSessionList())
			listModel.addElement(s);
		sessionList.repaint();
	}

}
