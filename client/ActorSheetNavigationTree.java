package client;

import graphicalUserInterface.Program;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import models.Brick;
import models.BrickVertex;
import models.BrickVertex.VerticeRank;
import translation.Messages;
import dataPack.Acteur;
import dataPack.Content;
import dataPack.Groupe;
import dataPack.MyDefaultMutableTreeNode;
import dataPack.MyMutableJTree;

public class ActorSheetNavigationTree extends MyMutableJTree implements TreeCellRenderer , MouseListener{
	private static final long serialVersionUID = 1125262905601022125L;

	protected ActorSheetView sheetView;
	protected Session session;
	
	protected HashMap<Acteur, DefaultMutableTreeNode> allActors;
	
	public ActorSheetNavigationTree(Session session, Acteur actor, ActorSheetView sheetView) {
		this.sheetView = sheetView;
		this.session = session;
		allActors = new HashMap<Acteur, DefaultMutableTreeNode>();
		
		DefaultMutableTreeNode root = new Groupe("Root"); //$NON-NLS-1$
		
		DefaultMutableTreeNode selectedNode = null;

		Vector<Content> allContents = new Vector<Content>();
		
		ArrayList<Content> allPrimary;
		ArrayList<Content> allSecondary;
		ArrayList<Content> allOther;
		
		allPrimary = new ArrayList<Content>();
		allSecondary = new ArrayList<Content>();
		allOther = new ArrayList<Content>();
		
		for(Content currentActor : session.actorsList.keySet()) {
			Acteur actorTemp = Program.myMainFrame.datapack.getActorsModel().getActorById(((Acteur)currentActor).getId());
			if(currentActor.equals(actor)) {
				sheetView.setActorSheet(session.getActorSheet(actorTemp));
			}
			
			if(session.getActorsList().get(currentActor) == VerticeRank.primary) {
				allPrimary.add(actorTemp);
			} else if(session.getActorsList().get(currentActor) == VerticeRank.secondary) {
				allSecondary.add(actorTemp);
			} else {
				allOther.add(actorTemp);
			}
			
			allContents.add(actorTemp);
		}
		
		if(session.isValid()) {
			for(Brick brick : session.getBrickList()) {
				for(BrickVertex vertex : brick.getVertices()) {
					if(allContents.contains(vertex.getContent()) == false) {
						if(vertex.getContent() instanceof Acteur) {
							Acteur actorTemp = Program.myMainFrame.datapack.getActorsModel().getActorById(((Acteur)vertex.getContent()).getId());
							System.out.println(Messages.getString("Version_0_9_9_9.ActorSheetNavigationTree.1") + vertex.getContent()); //$NON-NLS-1$
							allOther.add(actorTemp);
							allContents.add(actorTemp);
						}
					}
				}
			}
		}
		
		//create groups
		Groupe primaryActors = new Groupe(Messages.getString("Version_0_9_9_9.ActorSheetNavigationTree.2")); //$NON-NLS-1$
		Groupe secondaryActors = new Groupe(Messages.getString("Version_0_9_9_9.ActorSheetNavigationTree.3")); //$NON-NLS-1$
		Groupe otherActors = new Groupe(Messages.getString("Version_0_9_9_9.ActorSheetNavigationTree.4")); //$NON-NLS-1$
		
		Comparator<Content> comparator = new Comparator<Content>() {
			@Override
			public int compare(Content o1, Content o2) {
				return Collator.getInstance().compare(o1.toString(), o2.toString());
			}
		}; 
		
		Collections.sort(allPrimary, comparator); 
		Collections.sort(allSecondary, comparator);
		Collections.sort(allOther, comparator);
		
		Acteur actorTemp = Program.myMainFrame.datapack.getActorsModel().getActorById(actor.getId());
		selectedNode = addVectorIntoGroup(allPrimary, primaryActors, selectedNode == null ? actorTemp : null);
		selectedNode = addVectorIntoGroup(allSecondary, secondaryActors, selectedNode == null ? actorTemp : null);
		selectedNode = addVectorIntoGroup(allOther, otherActors, selectedNode == null ? actorTemp : null);
		
		if(primaryActors.getChildCount() > 0) {
			root.add(primaryActors);
		}

		if(secondaryActors.getChildCount() > 0) {
			root.add(secondaryActors);
		}
		
		if(otherActors.getChildCount() > 0) {
			root.add(otherActors);
		}
		
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setModel(treeModel);
		setRootVisible(false);
		
		setCellRenderer(this);
		addMouseListener(this);
		
		if(selectedNode != null) {
			setSelectionPath(new TreePath(selectedNode.getPath()));
		}
	}
	
	private DefaultMutableTreeNode addVectorIntoGroup(ArrayList<Content> contents, DefaultMutableTreeNode group, Object actor) {
		DefaultMutableTreeNode selectedNode = null;
		for(int i = 0 ; i < contents.size() ; i++) {
			Content content = contents.get(i);

			if(content instanceof Acteur) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(content);
				group.add(node);
				
					allActors.put((Acteur)content, node);
				
				if(content.equals(actor)) {
					selectedNode = node;
				}
			}
		}
		
		return selectedNode;
	}
	
	public ActorSheetNavigationTree(Session session, ActorSheetView sheetView) {
		this(session, null, sheetView);
	}
	
	protected DefaultMutableTreeNode getSelectedNode() {
		TreePath path = getSelectionPath();
		if (path == null) {
			return null;
		}

		return (DefaultMutableTreeNode) path.getLastPathComponent();
	}
	
	protected Acteur getSelectedVertex() {
		try{
			return (Acteur)getSelectedNode().getUserObject();
		} catch(Exception e) {
			return null;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			Acteur selectedActor = getSelectedVertex();
			
			if(selectedActor != null) {
				sheetView.setActorSheet(session.getActorSheet(Program.myMainFrame.datapack.getActorsModel().getActorById(selectedActor.getId())));
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		
		try{
			MyDefaultMutableTreeNode myNode = (MyDefaultMutableTreeNode)((DefaultMutableTreeNode)value).getUserObject();
			return myNode.getJComponent(selected, expanded, leaf, row, hasFocus);
		} catch(Exception e) {
			MyDefaultMutableTreeNode myNode = (MyDefaultMutableTreeNode)(value);
			return myNode.getJComponent(selected, expanded, leaf, row, hasFocus);
		}
	}

	public void setSelectedActor(Acteur actor) {
		if(allActors.get(actor) != null) {
			setSelectionPath(new TreePath(allActors.get(actor).getPath()));
		}
	}
}
