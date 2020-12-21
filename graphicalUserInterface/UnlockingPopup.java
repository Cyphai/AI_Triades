package graphicalUserInterface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import translation.Messages;



public class UnlockingPopup extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static UnlockingPopup singleton = new UnlockingPopup();
	
	protected JTextField mail;
	protected JTextField code;
	
	protected boolean result;


	protected UnlockingPopup()
	{
		super(Messages.getString("Version_1_0_3.UnlockingPopup.0")); //$NON-NLS-1$
		setSize(new Dimension(350, 150));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			@Override
		    public void windowClosed(WindowEvent e) {
				closePopup();
			}
		
		});

		buildContentPane();
		pack();

	}

	protected void buildContentPane()
	{
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mail = new JTextField(25);
		code = new JTextField(25);

		JLabel label = new JLabel(Messages.getString("Version_1_0_3.UnlockingPopup.1")); //$NON-NLS-1$
		mainPanel.add(label);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(mail);
		mainPanel.add(code);
		mainPanel.add(Box.createVerticalStrut(10));


		//Buttons : 
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createGlue());
		JButton unlock = new JButton(Messages.getString("Version_1_0_3.UnlockingPopup.2")); //$NON-NLS-1$
		unlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tryUnlockDatapack();	
			}
		});
		buttonPanel.add(unlock);
		buttonPanel.add(Box.createHorizontalStrut(10));
		JButton cancel = new JButton(Messages.getString("Version_1_0_3.UnlockingPopup.3")); //$NON-NLS-1$
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UnlockingPopup.this.setVisible(false);
			}
		});	
		buttonPanel.add(cancel);
		mainPanel.add(buttonPanel);

		setContentPane(mainPanel);
	}

	
	public static boolean showPopup()
	{
		return showPopup(true);
	}
	
	public static boolean showPopup(boolean waitForResult)
	{
		singleton.setVisible(true);
		if (waitForResult)
		{
		try {
			singleton.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return singleton.result;
		}
		return true;
	}
	
	protected void closePopup()
	{
		singleton.notifyAll();
		setVisible(false);
	}
		

	protected void tryUnlockDatapack()
	{

		if (Program.myMainFrame.getDataPack().unlockDatapack(mail.getText(), code.getText()))
		{
			setVisible(false);
			result = true;
		}
		else
		{
			result = false;
		}
		

	}
}
