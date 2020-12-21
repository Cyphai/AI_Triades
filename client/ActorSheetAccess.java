package client;

import graphicalUserInterface.Program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import translation.Messages;
import dataPack.Acteur;

public class ActorSheetAccess extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9025212489239805952L;
	protected Acteur actor;
	static public final String ActorsSheetTabName = Messages.getString("ActorSheetAccess.2"); //$NON-NLS-1$
	
	public ActorSheetAccess(Acteur actor)
	{
		super(Messages.getString("ActorSheetAccess.0")); //$NON-NLS-1$
		this.actor = actor;
		fillMenu();
	}

	protected void fillMenu()
	{
		JMenuItem access = new JMenuItem(Messages.getString("ActorSheetAccess.1")); //$NON-NLS-1$
		access.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Program.myMainFrame.showTabByName(ActorsSheetTabName)) {
					System.out.println("Déja affiché"); //$NON-NLS-1$
					Program.myMainFrame.datapack.getCurrentSession().getActorsSheetView().SetActorSelected(actor);
				} else {
					System.out.println("Create actorsSheet"); //$NON-NLS-1$
					Program.myMainFrame.datapack.getCurrentSession().showActorSheet(actor);
				}
				
			}
		});
		add(access);
	}
}
