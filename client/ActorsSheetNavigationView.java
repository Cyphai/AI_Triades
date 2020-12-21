package client;

import graphicalUserInterface.Program;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import dataPack.Acteur;

public class ActorsSheetNavigationView extends JPanel {
	private static final long serialVersionUID = 1556255029321249517L;
	
	ActorSheetView actorSheetView;
	ActorSheetNavigationTree actorSheetTree;
	Session session;
	
	public ActorsSheetNavigationView(Session session, Acteur actor) {
		super();
		setLayout(new BorderLayout());
		this.session = session;
		actorSheetView = new ActorSheetView();
		actorSheetView.setActorSheet(session.getActorSheet(actor));
		actorSheetTree = new ActorSheetNavigationTree(session, actor, actorSheetView);
		
		add(actorSheetTree, BorderLayout.WEST);
		add(actorSheetView, BorderLayout.CENTER);
		Program.myMainFrame.validate();
		Program.myMainFrame.repaint();
	}
	
	public void SetActorSelected(Acteur actor) {
		actorSheetView.setActorSheet(session.getActorSheet(actor));
		actorSheetTree.setSelectedActor(actor);
	}

	public ActorSheetView getActorSheetView() {
		return actorSheetView;
	}

	public ActorSheetNavigationTree getActorSheetTree() {
		return actorSheetTree;
	}
}