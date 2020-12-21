package client;

import graphicalUserInterface.Program;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import models.TriadeEditingMousePlugin;
import dataPack.Acteur;
import dataPack.JTreeActors;
import dataPack.MyDefaultMutableTreeNode;

public class SchemaTree extends JTreeActors implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8772042230835489826L;

	public SchemaTree() {
		//TODO a virer une fois ces arbre ne seront plus sauvé
		super();
	}

	public SchemaTree(Brick schema, TriadeEditingMousePlugin<BrickVertex, BrickEdge> triadeEditingMousePlugin) {
		super(Program.myMainFrame.datapack);

		this.brick = schema;
		
		setEditable(false);
		setTriadeEditingMousePlugin(triadeEditingMousePlugin);
		addMouseListener(this);
		
		/* cache les sommets qui ne sont pas dans le schema */
		simpleHideAll();

		for(BrickVertex vertex : schema.getVertices()) {
			if(vertex.getContent() instanceof MyDefaultMutableTreeNode)
				simpleShowActor(vertex.getContent());
		}
		
		expendAllNode();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			if(getSelectedNode() instanceof Acteur) {
				ActorSheetAccess menu = new ActorSheetAccess(dataPack.getActorsModel().getActorById(((Acteur)getSelectedNode()).getId()));
				menu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
	}

	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	
	
	protected Object writeReplace()
	{
		return new SchemaTreeProxy();
	}
	
	protected static class SchemaTreeProxy implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected Object readResolve()
		{
			return null;
		}
	}
}
