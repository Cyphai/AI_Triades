package client.export;

import graphicalUserInterface.Program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import translation.Messages;

import models.Brick;
import client.NavigationTree;
import client.Session;

public class ExportsContextualMenu extends JPopupMenu{

	private static final long serialVersionUID = 6895482137669411024L;
	protected Brick baseSchema;
	NavigationTree navigationTree;

	public ExportsContextualMenu(Brick brick) {
		super(Messages.getString("ExportsContextualMenu.0")); //$NON-NLS-1$
		baseSchema = brick;
		fillMenu();
		navigationTree = Program.myMainFrame.datapack.getCurrentSession().getNavigationTree();
	}

	public ExportsContextualMenu(Brick schema, NavigationTree navigationTree) {
		this(schema);

		this.navigationTree = navigationTree;
	}

	protected void fillMenu() {
		final Session session = Program.myMainFrame.getDataPack()
		.getCurrentSession();

		JMenuItem newExport = new JMenuItem(Messages.getString("ExportsContextualMenu.1")); //$NON-NLS-1$
		newExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(Program.myMainFrame,
						Messages.getString("ExportsContextualMenu.2"), //$NON-NLS-1$
						Messages.getString("ExportsContextualMenu.3") //$NON-NLS-1$
								+ baseSchema.getName()
								+ " (" //$NON-NLS-1$
								+ new SimpleDateFormat("dd MMM yyyy kk'h'mm") //$NON-NLS-1$
				.format(new Date()) + ")"); //$NON-NLS-1$
				if(name != null && name.compareTo("") != 0) { //$NON-NLS-1$
					ExportModel newExport = new ExportModel(name, baseSchema);
					session.addExport(baseSchema, newExport);
					if(navigationTree != null) 
						navigationTree.addExport(newExport, baseSchema);
					else 
						System.out.println("NavigtionTree null dans ExportsContextualMenu, c'est surement pour ca que l'arbre ne ce met pas a jour quand on creer une nouvelle image depuis ici."); //$NON-NLS-1$
					Program.myMainFrame.addTab(newExport);
				}
			}
		});
	
		add(newExport);
		add(new JSeparator());
		final JPopupMenu baseMenu = this;
		Vector<ExportModel> exportList = session.getExports(baseSchema);
		if (exportList != null)
			for (final ExportModel export : exportList) {
				JMenuItem oldExport = new JMenuItem(export.getName());
				oldExport.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Program.myMainFrame.addTab(export);

					}
				});
				baseMenu.add(oldExport);

			}

	}


}
