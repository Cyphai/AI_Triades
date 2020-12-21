package Mailing;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tools.ConfigCreator;
import tools.ConfigTriades;
import translation.Messages;

public class MailView extends JFrame implements ActionListener {
	private static final long serialVersionUID = 9022335411694658914L;

	private final JTextField subject;
	private final JTextArea text;
	private final JTextField mail;
	private final JComboBox formType;
	private final MailForm mailForm;
	
	private final JButton cancelButton;
	private final JButton okButton;

	public MailView() {
		this(Messages.getString("MailView.0"), null); //$NON-NLS-1$
	}

	public MailView(String title) {
		this(title, null);
	}
	
	public MailView(String viewTitle, MailForm mailForm) {
		super(viewTitle);
		this.mailForm = mailForm;
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		subject = new JTextField(mailForm.getInitSubject()); //$NON-NLS-1$
		subject.setPreferredSize(new Dimension(200, 25));
		text = new JTextArea(mailForm.getInitText()); //$NON-NLS-1$
		text.setPreferredSize(new Dimension(200, 400));
//		mail = new JTextField(Program.myMainFrame.getDataPack().getCompanyInfo().getEmail());
		if (Program.isTriades())
			mail = new JTextField(ConfigTriades.getInstance().getMail());
		else
			mail = new JTextField(ConfigCreator.getInstance().getMail());
		mail.setPreferredSize(new Dimension(200, 25));
		formType = new JComboBox(MailFormFactory.getFormTypes());
		formType.setPreferredSize(new Dimension(200, 25));

		JScrollPane textScrollPane = new JScrollPane(text);

		JLabel comboBoxLabel = new JLabel(Messages.getString("MailView.4")); //$NON-NLS-1$
		comboBoxLabel.setPreferredSize(new Dimension(200, 25));
		
		JLabel subjectLabel = new JLabel(Messages.getString("MailView.5")); //$NON-NLS-1$
		subjectLabel.setPreferredSize(new Dimension(200, 25));
		JLabel textLabel = new JLabel(Messages.getString("MailView.6")); //$NON-NLS-1$
		textLabel.setPreferredSize(new Dimension(200, 25));
		JLabel mailLabel = new JLabel(Messages.getString("MailView.7")); //$NON-NLS-1$
		mailLabel.setPreferredSize(new Dimension(200, 25));
		JLabel title = new JLabel(Messages.getString("MailView.8")); //$NON-NLS-1$
		
		cancelButton = new JButton(Messages.getString("MailView.9")); //$NON-NLS-1$
		okButton = new JButton(Messages.getString("MailView.10")); //$NON-NLS-1$

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		JPanel comboBoxPanel = new JPanel(new GridBagLayout());
		
		JPanel subjectPanel = new JPanel();
		subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.X_AXIS));
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		
		JPanel mailPanel = new JPanel();
		mailPanel.setLayout(new BoxLayout(mailPanel, BoxLayout.X_AXIS));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		JPanel titlePanel = new JPanel(new GridBagLayout());
				
		comboBoxPanel.add(comboBoxLabel);
		comboBoxPanel.add(Box.createVerticalGlue());
		comboBoxPanel.add(formType);
		
		titlePanel.add(title);
		
		mailPanel.add(mailLabel);
		mailPanel.add(Box.createVerticalGlue());
		mailPanel.add(mail);
		
		subjectPanel.add(subjectLabel);
		subjectPanel.add(Box.createVerticalGlue());
		subjectPanel.add(subject);
		
		textPanel.add(textLabel);
		textPanel.add(Box.createVerticalGlue());
		textPanel.add(textScrollPane);
		
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		
		if(mailForm == null) {
			add(comboBoxPanel);
		}
		
		add(titlePanel);
		add(mailPanel);
		add(subjectPanel);
		add(textPanel);
		add(buttonsPanel);

		setSize(new Dimension(800, 600));
		setVisible(true);

		validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == okButton) {
			if(mail.getText().trim().equalsIgnoreCase("")) { //$NON-NLS-1$
				System.out.println("MailVide"); //$NON-NLS-1$
				DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.MailView.3")); //$NON-NLS-1$
			} else {
				System.out.println("Mail pas vide"); //$NON-NLS-1$
				ConfigTriades.getInstance().setMail(mail.getText());
				mailForm.setContent(subject.getText(), text.getText(), mail.getText());
				if(mailForm.sendMail()) {
					setVisible(false);
					mailForm.endAction();
				}
				
				System.out.println("ok"); //$NON-NLS-1$
			}
		} else if(arg0.getSource() == cancelButton) {
			setVisible(false);
			System.out.println("cancel"); //$NON-NLS-1$
			mailForm.endAction();
		}
	}
	/*
	public static void main(String[] args) {
		JFrame frame = new JFrame();
//		frame.setLayout(new GridLayout());
		frame.setSize(800,600);
		frame.setVisible(true);
		
		frame.add(new MailView());
		
		frame.validate();
	}*/
}
