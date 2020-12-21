package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import tools.GenericTools;
import translation.Messages;
import Mailing.MailSender;

public class KeyGenerator extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static KeyGenerator singleton = new KeyGenerator();

	private enum LicenceType {alreadyBilled(Messages.getString("Version_1_0_3.KeyGenerator.0")),personnal(Messages.getString("Version_1_0_3.KeyGenerator.1")),corp1(Messages.getString("Version_1_0_3.KeyGenerator.2")), corp5(Messages.getString("Version_1_0_3.KeyGenerator.3")),corp25(Messages.getString("Version_1_0_3.KeyGenerator.4")), corp100(Messages.getString("Version_1_0_3.KeyGenerator.5")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	protected String name;

	LicenceType(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
	}

	private static class UserEntry{

		public UserEntry(String mail, int usedKey)
		{
			this.mail = mail.trim().toLowerCase(Locale.FRENCH);
			code = ""; //$NON-NLS-1$
		}


		public String mail;
		public String code;

		protected void computeCode(int usedKey)
		{
			long value = mail.hashCode();
			value = Math.abs(value);
			value %= 100000000;
			int baseKey = (int) (value % 10000);
			baseKey = GenericTools.modOnDigits(baseKey, 3);
			value *= 10000;
			baseKey += usedKey;
			value += baseKey;
			code = ""+value; //$NON-NLS-1$
		}

		@Override
		public String toString()
		{
			return mail+" - "+code; //$NON-NLS-1$
		}

		public String tabSeparatorString()
		{
			return mail+"\t"+code; //$NON-NLS-1$
		}
	}

	private int usedKey;

	private LicenceType licenceType;

	private JList entryList;
	private DefaultListModel entries;

	private ArrayList<JButton> copyButtons;
	private JButton addName;
	private JButton fromClipboard;
	private JButton generateLicence;
	private JButton deleteEntry;
	private JButton modifyEntry;


	private KeyGenerator()
	{
		super();
		usedKey = 0;
		licenceType = null;
		setSize(800,800);
		setTitle(Messages.getString("Version_1_0_3.KeyGenerator.10")); //$NON-NLS-1$
		buildPanel();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(false);
	}

	private void buildPanel()
	{
		copyButtons = new ArrayList<JButton>();
		entries = new DefaultListModel();
		entryList = new JList(entries);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		JLabel addFieldLabel = new JLabel(Messages.getString("Version_1_0_3.KeyGenerator.11")); //$NON-NLS-1$
		final JTextField addNameField = new JTextField(25);
		headerPanel.add(addFieldLabel);
		headerPanel.add(addNameField);
		headerPanel.add(Box.createHorizontalStrut(15));

		addName = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.12")); //$NON-NLS-1$
		addName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String newLogin = addNameField.getText().trim();
				if (newLogin.length() < 5)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.KeyGenerator.13")); //$NON-NLS-1$
					return;
				}
				else if (newLogin.contains(" ") || newLogin.contains(",") || newLogin.contains(";")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.KeyGenerator.17")); //$NON-NLS-1$
					return;
				}

				for (int i = 0; i < entries.size(); i++)

				{
					UserEntry entry = (UserEntry)entries.elementAt(i);
					if (entry.mail.equalsIgnoreCase(newLogin))
					{
						DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.KeyGenerator.18")); //$NON-NLS-1$
						return;
					}
				}

				addNewEntry(newLogin);
				addNameField.setText(""); //$NON-NLS-1$
				addNameField.requestFocusInWindow();


			}
		});

		headerPanel.add(addName);
		headerPanel.add(Box.createHorizontalStrut(25));
		fromClipboard = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.20")); //$NON-NLS-1$
		fromClipboard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object o;
				try {
					o = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					if (o instanceof String)
					{
						String list = (String)o;
						StringTokenizer tokenizer = new StringTokenizer(list," \t\n,;"); //$NON-NLS-1$
						ArrayList<String> unusedTokens = new ArrayList<String>();
						ArrayList<String> mailToAdd = new ArrayList<String>();

						while (tokenizer.hasMoreTokens())
						{
							String token = tokenizer.nextToken();
							token = token.trim(); 
							if (token.length() < 5)
							{
								if (token.length() > 1)
									unusedTokens.add(token);
							}
							else
							{

								for (int i = 0; i < entries.size(); i++)

								{
									UserEntry entry = (UserEntry)entries.elementAt(i);
									if (entry.mail.equalsIgnoreCase(token))
									{
										unusedTokens.add(token);
										continue;
									}
								}
								mailToAdd.add(token);
							}
						}
						if (mailToAdd.isEmpty())
						{
							DialogHandlerFrame.showErrorDialog(KeyGenerator.this,Messages.getString("Version_1_0_3.KeyGenerator.22")); //$NON-NLS-1$
							return;
						}
						String text = ""+mailToAdd.size()+Messages.getString("Version_1_0_3.KeyGenerator.24"); //$NON-NLS-1$ //$NON-NLS-2$
						for (int i = 0; i < mailToAdd.size() && i < 5; i++)
						{
							text+= mailToAdd.get(i)+"\n"; //$NON-NLS-1$
						}
						if (mailToAdd.size() >= 5)
						{
							text += "...\n"; //$NON-NLS-1$
						}
						if (!unusedTokens.isEmpty())
						{
							text += "\n"+unusedTokens.size()+Messages.getString("Version_1_0_3.KeyGenerator.28"); //$NON-NLS-1$ //$NON-NLS-2$
							for (int i = 0; i < unusedTokens.size() && i < 5; i++)
							{
								text+= "\n"+unusedTokens.get(i); //$NON-NLS-1$
							}
							if (unusedTokens.size() >= 5)
							{
								text += "\n..."; //$NON-NLS-1$
							}

						}
						if (DialogHandlerFrame.showYesNoCancelDialog(KeyGenerator.this,text+Messages.getString("Version_1_0_3.KeyGenerator.31")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
						{
							for (String s : mailToAdd)
							{
								addNewEntry(s);
							}
						}
					}
				} catch (Exception e1) {
					DialogHandlerFrame.showErrorDialog(KeyGenerator.this,Messages.getString("Version_1_0_3.KeyGenerator.32")); //$NON-NLS-1$
					e1.printStackTrace();
				}
			}
		});

		headerPanel.add(fromClipboard);
		headerPanel.add(Box.createGlue());

		mainPanel.add(headerPanel, BorderLayout.NORTH);

		entryList.setPreferredSize(new Dimension(300,300));
		JScrollPane jsp = new JScrollPane(entryList);
		jsp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 25), BorderFactory.createTitledBorder(Messages.getString("Version_1_0_3.KeyGenerator.33")))); //$NON-NLS-1$

		mainPanel.add(jsp , BorderLayout.CENTER);


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


		deleteEntry = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.34")); //$NON-NLS-1$
		deleteEntry.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEntry currentEntry = getSelectedEntry();
				if (currentEntry != null)
					entries.removeElement(currentEntry);
				entryList.repaint();
			}
		});
		buttonPanel.add(deleteEntry);

		modifyEntry = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.35")); //$NON-NLS-1$
		modifyEntry.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEntry currentEntry = getSelectedEntry();
				String newName = JOptionPane.showInputDialog(KeyGenerator.this, Messages.getString("Version_1_0_3.KeyGenerator.36"), currentEntry.mail); //$NON-NLS-1$
				newName = newName.trim();

				if (newName == null)
					return;

				if (newName.length() < 5)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.KeyGenerator.37")); //$NON-NLS-1$
					return;
				}

				if (newName != null)
					currentEntry.mail = newName;
				entryList.repaint();
			}
		});
		buttonPanel.add(modifyEntry);

		buttonPanel.add(Box.createGlue());



		JButton copyLogin = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.38")); //$NON-NLS-1$
		copyLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEntry currentEntry = getSelectedEntry();
				if (currentEntry != null)
					copyToClipboard(currentEntry.mail);
			}
		});
		copyLogin.setEnabled(false);
		copyButtons.add(copyLogin);
		buttonPanel.add(copyLogin);

		buttonPanel.add(Box.createVerticalStrut(20));

		JButton copyCode = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.39")); //$NON-NLS-1$
		copyCode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEntry currentEntry = getSelectedEntry();
				if (currentEntry != null)
					copyToClipboard(currentEntry.code);
			}
		});
		copyCode.setEnabled(false);
		copyButtons.add(copyCode);
		buttonPanel.add(copyCode);

		buttonPanel.add(Box.createVerticalStrut(20));

		JButton copyLine = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.40")); //$NON-NLS-1$
		copyLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserEntry currentEntry = getSelectedEntry();
				if (currentEntry != null)
					copyToClipboard(currentEntry.tabSeparatorString());
			}
		});
		copyLine.setEnabled(false);
		copyButtons.add(copyLine);
		buttonPanel.add(copyLine);

		buttonPanel.add(Box.createVerticalStrut(20));

		JButton copyEntireList = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.41")); //$NON-NLS-1$
		copyEntireList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				copyToClipboard(getEntriesString());
			}
		});
		copyEntireList.setEnabled(false);
		copyButtons.add(copyEntireList);
		buttonPanel.add(copyEntireList);


		mainPanel.add(buttonPanel, BorderLayout.EAST);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		JPanel licencePanel = new JPanel(new GridLayout(2,0, 15,25));

		for (final LicenceType l : LicenceType.values())
		{
			JRadioButton button = new JRadioButton(l.toString());
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setLicenceType(l);
				}
			});
			licencePanel.add(button);

		}
		licencePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("Version_1_0_3.KeyGenerator.42"))); //$NON-NLS-1$

		bottomPanel.add(licencePanel);
		bottomPanel.add(Box.createHorizontalStrut(25));
		generateLicence = new JButton(Messages.getString("Version_1_0_3.KeyGenerator.43")); //$NON-NLS-1$
		generateLicence.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (licenceType == null)
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.KeyGenerator.44")); //$NON-NLS-1$
					return;
				}

				if (DialogHandlerFrame.showYesNoCancelDialog(KeyGenerator.this,Messages.getString("Version_1_0_3.KeyGenerator.45")+entries.size()+Messages.getString("Version_1_0_3.KeyGenerator.46")+licenceType.toString()+Messages.getString("Version_1_0_3.KeyGenerator.47")) != JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					return;
				
				URL url;
				try {
					url = new URL("http://www.google.fr"); //$NON-NLS-1$
					HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
					urlConn.setConnectTimeout(5000);
					urlConn.connect();
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.KeyGenerator.49")); //$NON-NLS-1$
					return;
				}
		

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(Messages.getString("Version_1_0_3.KeyGenerator.50"), "licences.txt")); //$NON-NLS-1$ //$NON-NLS-2$
				fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setDialogTitle(Messages.getString("Version_1_0_3.KeyGenerator.52")); //$NON-NLS-1$
				if (fileChooser.showSaveDialog(KeyGenerator.this) != JFileChooser.APPROVE_OPTION)
					return;

				String newLine = System.getProperty("line.separator");
				String summary = Messages.getString("Version_1_0_3.KeyGenerator.53")+(new Date().toString()); //$NON-NLS-1$
				summary += newLine+entries.size()+Messages.getString("Version_1_0_3.KeyGenerator.55")+licenceType.toString()+newLine+newLine; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				for (int i = 0; i < entries.size(); i++)
				{
					if (entries.get(i) instanceof UserEntry)
					{
						UserEntry entry = (UserEntry)entries.get(i);
						entry.computeCode(usedKey);
						summary += entry.tabSeparatorString()+newLine; //$NON-NLS-1$
					}
				}


				File file = fileChooser.getSelectedFile();
				if (!file.getName().endsWith(".licences.txt"))
				{
					file = new File(file.getAbsolutePath()+".licences.txt");
				}
				FileWriter writer = null;
				try
				{
					if (file.exists())
					{
						writer = new FileWriter(file, true);
					}
					else
					{
						writer = new FileWriter(file);
					}

					writer.write(summary);
				}
				catch (IOException err)
				{
					if (DialogHandlerFrame.showYesNoCancelDialog(KeyGenerator.this,Messages.getString("Version_1_0_3.KeyGenerator.58")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
					{
						for (int i = 0; i < entries.size(); i++)
						{
							if (entries.get(i) instanceof UserEntry)
							{
								UserEntry entry = (UserEntry)entries.get(i);
								entry.code = ""; //$NON-NLS-1$
							}
						}
						return;
					}
				}
				finally
				{
					if (writer != null)
					{
						try {
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}

				MailSender.sendAutoMail("Automail", "Génération de licences", summary); //$NON-NLS-1$ //$NON-NLS-2$

				generateLicence.setEnabled(false);
				fromClipboard.setEnabled(false);
				deleteEntry.setEnabled(false);
				modifyEntry.setEnabled(false);
				addName.setEnabled(false);
				for (JButton b : copyButtons)
					b.setEnabled(true);

				KeyGenerator.this.repaint();
			}
		});
		bottomPanel.add(generateLicence);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);

	}

	private void initPanel()
	{
		generateLicence.setEnabled(true);
		fromClipboard.setEnabled(true);
		addName.setEnabled(true);
		modifyEntry.setEnabled(true);
		deleteEntry.setEnabled(true);
		for (JButton b : copyButtons)
			b.setEnabled(false);

		entries.removeAllElements();

		repaint();
	}

	private UserEntry getSelectedEntry()
	{
		if (entryList.getSelectedValue() != null && entryList.getSelectedValue() instanceof UserEntry)
			return (UserEntry)entryList.getSelectedValue();
		return null;
	}

	private String getEntriesString()
	{
		String result = ""; //$NON-NLS-1$
		for (int i = 0; i < entries.size(); i++)
		{
			UserEntry entry = (UserEntry)entries.get(i);
			result += entry.tabSeparatorString()+"\n"; //$NON-NLS-1$
		}
		return result;
	}

	private void copyToClipboard(String value)
	{
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
	}

	private void setLicenceType(LicenceType newLicenceType)
	{
		licenceType = newLicenceType;
	}

	private void addNewEntry(String newMail)
	{
		UserEntry newEntry = new UserEntry(newMail, usedKey);

		entries.addElement(newEntry);

	}

	public static void showKeyGenerator()
	{
		int value = checkKey(); 
		if (value == 0)
			return;
		singleton.usedKey = value;
		singleton.initPanel();
		singleton.setVisible(true);
		singleton.requestFocus();

	}



	public static int checkKey()
	{
		String invalidKey = Messages.getString("Version_1_0_3.KeyGenerator.64"); //$NON-NLS-1$
		int delay = 0;
		while (true)
		{
			String key = JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("Version_1_0_3.KeyGenerator.65")); //$NON-NLS-1$
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			delay += 1000;
			if (key == null)
				return 0;
			key = key.trim();
			if (key.length() != 5 || !key.endsWith("!")) //$NON-NLS-1$
			{
				DialogHandlerFrame.showErrorDialog(invalidKey);
				continue;
			}
			int value = 0;
			try{
				value = Integer.parseInt(key.substring(0, 4));
				if (value % 241 != 0)
				{
					DialogHandlerFrame.showErrorDialog(invalidKey);
					continue;
				}
			}
			catch(NumberFormatException e)
			{
				DialogHandlerFrame.showErrorDialog(invalidKey);
				continue;
			}
			return value;

		}

	}

}
