package graphicalUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import translation.Messages;

public class TriadeIntroPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3546597040030449553L;

	protected JButton browseAll;
	protected JList exportedSchema;
	protected JButton openExport;
	protected JButton newExport;

	public TriadeIntroPanel() {
		build();
	}

	protected void build() {
		JButton browseAll = new JButton(Messages.getString("TriadeIntroPanel.0")); //$NON-NLS-1$
		browseAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}
}
