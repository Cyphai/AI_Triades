package dataPack;

import graphicalUserInterface.DataPackView;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import translation.Messages;

public class CompanyInfoView extends JFrame {
	private static final long serialVersionUID = -4610853408447255737L;

	public CompanyInfoView(final CompanyInfo info, final DataPackView datapackView) {
		super();
		
		setTitle(Messages.getString("CompanyInfoView.0")); //$NON-NLS-1$
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JLabel nameLabel = new JLabel(Messages.getString("CompanyInfoView.1")); //$NON-NLS-1$
		JLabel phoneLabel = new JLabel(Messages.getString("CompanyInfoView.2")); //$NON-NLS-1$
		JLabel emailLabel = new JLabel(Messages.getString("CompanyInfoView.3")); //$NON-NLS-1$
		JLabel managerLabel = new JLabel(Messages.getString("CompanyInfoView.4")); //$NON-NLS-1$
		
		final JTextField nameField = new JTextField(info.getName());
		final JTextField phoneField = new JTextField(info.getPhone());
		final JTextField emailField = new JTextField(info.getEmail());
		final JTextField managerField = new JTextField(info.getManager());

		final CompanyInfoView access = this;
		
		JButton okButton = new JButton(Messages.getString("CompanyInfoView.5")); //$NON-NLS-1$
		JButton cancelButton = new JButton(Messages.getString("CompanyInfoView.6")); //$NON-NLS-1$
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				info.setEmail(emailField.getText());
				info.setManager(managerField.getText());
				info.setPhone(phoneField.getText());
				info.setName(nameField.getText());
				datapackView.updateView();
				access.setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				access.setVisible(false);
			}
		});
		
		JPanel infoPanel = new JPanel(new GridLayout(0, 2));
		JPanel buttonsPanel = new JPanel();

		infoPanel.add(nameLabel);
		infoPanel.add(nameField);
		infoPanel.add(phoneLabel);
		infoPanel.add(phoneField);
		infoPanel.add(emailLabel);
		infoPanel.add(emailField);
		infoPanel.add(managerLabel);
		infoPanel.add(managerField);
		
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(infoPanel);
		panel.add(Box.createVerticalGlue());
		panel.add(buttonsPanel);

		add(panel);
		
		validate();
		setVisible(true);
	}
	
}
