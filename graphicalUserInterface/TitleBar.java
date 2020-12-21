package graphicalUserInterface;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import translation.Messages;

public class TitleBar extends JPanel {

	private static final long serialVersionUID = 8802351875147413451L;

	public TitleBar(ImageIcon icon, String title) {
		JToolBar toolBar = new JToolBar();
		JButton minimiser = new JButton(IconDatabase.iconMinimize);
		JButton fermer = new JButton(IconDatabase.iconClose);

		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);

		toolBar.setFloatable(false);
		minimiser.setFocusable(false);
		minimiser.setToolTipText(Messages.getString("TitleBar.0")); //$NON-NLS-1$
		fermer.setFocusable(false);
		fermer.setToolTipText(Messages.getString("TitleBar.1")); //$NON-NLS-1$

		minimiser
				.addActionListener(Program.myMainFrame.controller.actionMinimize);
		if (title.equals(Messages.getString("TitleBar.2"))) //$NON-NLS-1$
			fermer
					.addActionListener(Program.myMainFrame.controller.actionTreeVisibility);
		else
			fermer
					.addActionListener(Program.myMainFrame.controller.actionSchemaVisibility);

		add(new JLabel(icon));
		add(new JLabel(" " + title)); //$NON-NLS-1$
		add(Box.createHorizontalStrut(100)); // Afin de fixer la largeur
												// minimale de l'arborescence
		add(Box.createGlue());
		//toolBar.add(minimiser);
		//toolBar.add(fermer);
//		add(toolBar);

		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}
}
