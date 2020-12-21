package dataPack;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.MainFrameDatapackCreator;
import graphicalUserInterface.Program;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import models.Brick;
import models.GraphListener;
import translation.Messages;
import client.NavigationTree;

public class TreeListener implements Externalizable, MouseListener {
	static private final long serialVersionUID = 123L;

	protected ModelActeursEntreprise modelActeur;
	protected GraphListener graphe;
	protected JTreeActors tree;

	private NavigationTree navigationTree;

	public TreeListener() {

	}

	public TreeListener(ModelActeursEntreprise modelActeur, JTreeActors tree) {
		this.modelActeur = modelActeur;
		this.tree = tree;
		graphe = null;
	}

	public TreeListener(NavigationTree navigationTree) {
		this.navigationTree = navigationTree;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TreePath courant = acteurs.getTree().getPathForLocation(e.getX(),
		// e.getY());

		// récupére le path du noeud séléctioné
		TreePath path = ((JTree) e.getSource()).getPathForLocation(e.getX(), e
				.getY());

		if (path == null) {
			// rien n'est séléctioné
			return;
		}

		// récupére le noeud
		MyDefaultMutableTreeNode selectedNode = (MyDefaultMutableTreeNode) path
		.getLastPathComponent();

		if(e.getButton() != MouseEvent.BUTTON1) {
			if(!Program.isTriades()) {
				tree.setEditable(true);
				tree.startEditing(new TreePath(selectedNode.getPath()));
				return;
			}
		}
		
		if(graphe != null) {
			if(!MyDefaultMutableTreeNode.isGroupe(selectedNode)) {
//				System.out.println("Set content (TreeListener) : " + selectedNode.toString()); //$NON-NLS-1$
				graphe.setContent(selectedNode);
			} else {
				graphe.setContent(null);
			}
		}
	}

	public void setGrapheListener(GraphListener n_graphe) {
		graphe = n_graphe;
	}

	public void ajouterJeuAceteur(JeuActeur jeuActeur) {
		tree.ajouterJeuActeur(jeuActeur);
	}

	public void hideAllBases(JeuActeur jeuActeur) {
		tree.hideAllBases(jeuActeur);
		tree.validate();
	}

	public void hideAllCorps(JeuActeur jeuActeur) {
		tree.hideAllCorps(jeuActeur);
		tree.validate();
	}

	public void ajouterJeuxActeur(Vector<JeuActeur> jeuxActeur) {
		for (JeuActeur jeuActeur : jeuxActeur) {
			ajouterJeuAceteur(jeuActeur);
		}
	}

	public Acteur ajouterActeur(Integer statutId, String nom) {

		if (checkOpenTab() != JOptionPane.YES_OPTION)
			return null;

		MyDefaultMutableTreeNode selectedNode = tree.getSelectedNode();

		if (selectedNode == null
				|| selectedNode.isNodeAncestor(modelActeur.getGroupeActeurs()) == false) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("TreeListener.1")); //$NON-NLS-1$
			return null;
		}

		Groupe fatherGroupe = null;

		if (MyDefaultMutableTreeNode.isActor(selectedNode)) {
			fatherGroupe = (Groupe) selectedNode.getParent();
		} else if (selectedNode == modelActeur.getGroupeActeurs()) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("TreeListener.2")); //$NON-NLS-1$
			return null;
		} else if (MyDefaultMutableTreeNode.isGroupe(selectedNode)) {
			fatherGroupe = (Groupe) selectedNode;
		} else {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("TreeListener.3")); //$NON-NLS-1$
			return null;
		}

		Acteur newActor = new Acteur(nom, statutId, fatherGroupe.getIdGroupe());
		modelActeur.ajouterActeur(newActor, fatherGroupe);

		tree.expendNode(newActor);
		setSelectedNode(newActor);
		return newActor;
	}

	private int checkOpenTab() {
		// afin que les arbres soit bien mis a jour il faut que tout les onglet
		// soit fermer pour ne pas crée d'incoérence

		if (Program.myMainFrame.getTabLength().intValue() > 1)
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("TreeListener.0")); //$NON-NLS-1$
			if (!Program.isTriades())
				((MainFrameDatapackCreator)Program.myMainFrame).cleanTabAndGraphs();
		}
		return JOptionPane.YES_OPTION;
	}

	public Content deleteActor() {
		// supprime l'acteur/brique selectioné

		MyDefaultMutableTreeNode selectedNode = tree.getSelectedNode();

		if (selectedNode == null)
			return null;

		if (checkOpenTab() != JOptionPane.YES_OPTION)
			return null;

		if (selectedNode.isNodeAncestor(modelActeur.getGroupeActeurs()) == true
				&& selectedNode != modelActeur.getGroupeActeurs()) {
			if (MyDefaultMutableTreeNode.isActor(selectedNode)) {
				Acteur actor = (Acteur) selectedNode;

//				if (DialogHandlerFrame
//						.showYesNoDialog(Messages.getString("TreeListener.5")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
//					return null;

				DefaultMutableTreeNode newSelectedNode = (DefaultMutableTreeNode) actor
				.getParent();

				// l'acteur selectioné est supprimé
				modelActeur.supprimerAnyActeur(actor);

				setSelectedNode(newSelectedNode);

				return actor;
			} else if (MyDefaultMutableTreeNode.isGroupe(selectedNode)) {
				Groupe groupe = (Groupe) selectedNode;
				

				// si le groupe contient des acteurs ou des groupes on demande
				// confirmation pour les supprimer
//				if (selectedNode.getChildCount() > 0) {
//					confirmation = DialogHandlerFrame
//					.showYesNoDialog(Messages.getString("TreeListener.6")); //$NON-NLS-1$
//				} else {
//					confirmation = DialogHandlerFrame
//					.showYesNoDialog(Messages.getString("TreeListener.7")); //$NON-NLS-1$
//				}

//				if (confirmation != JOptionPane.YES_OPTION)
//					return null;

				DefaultMutableTreeNode newSelectedNode = (DefaultMutableTreeNode) groupe
				.getParent();

				modelActeur.supprimerGroupe(groupe);

				setSelectedNode(newSelectedNode);

				return groupe;
			}
		} else {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("TreeListener.8")); //$NON-NLS-1$
		}

		return null;
	}

	public Groupe ajouterGroupe(String nom) {

		if (checkOpenTab() != JOptionPane.YES_OPTION)
			return null;

		MyDefaultMutableTreeNode selectedNode = tree.getSelectedNode();

		MyDefaultMutableTreeNode fatherGroupe = null;

		if (selectedNode == null) {
			int choix = DialogHandlerFrame
			.showYesNoDialog(Messages.getString("TreeListener.9")); //$NON-NLS-1$
			if (choix == JOptionPane.YES_OPTION) {
				fatherGroupe = modelActeur.getGroupeActeurs();
			} else {
				return null;
			}
		} else if (selectedNode.isNodeAncestor(modelActeur.getGroupeActeurs()) == false) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("TreeListener.10")); //$NON-NLS-1$
			return null;

		} else {
			if (MyDefaultMutableTreeNode.isActor(selectedNode)) {
				fatherGroupe = (MyDefaultMutableTreeNode) selectedNode
				.getParent();
			} else {
				fatherGroupe = selectedNode;
			}
		}

		Groupe newGroupe = new Groupe(nom);
		modelActeur.ajouterGroupe(newGroupe, fatherGroupe);

		tree.expendNode(newGroupe);
		setSelectedNode(newGroupe);

		return newGroupe;
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void hideActor(Content oldActiveContent) {
		tree.hideActor(oldActiveContent);
	}

	public void hideActors(Set<Content> actorsSet) {
		for (Content actor : actorsSet) {
			hideActor(actor);
		}
	}

	public void showActor(Content content) {
		tree.showActor(content);
	}

	public void showActors(Set<Content> actorsSet) {
		for (Content actorId : actorsSet) {
			showActor(actorId);
		}
	}

	public void setSelectedNode(DefaultMutableTreeNode selectedNode) {
		if (Program.isTriades()) {
			tree.setSelectionPath(new TreePath(selectedNode.getPath()));
			tree.validate();
		}
	}

	public void setTree(JTreeActors tree) {
		this.tree = tree;
	}

	public void setSelectedContent(Content content) {
		if(content == null) {
			if(tree !=null) 
				tree.setSelectionPath(null);
			else 
				System.out.println("Tree null !!"); //$NON-NLS-1$

			return;
		}

		if(content instanceof MyDefaultMutableTreeNode) {
			MyDefaultMutableTreeNode node;
			if(content instanceof Acteur) {
				node = modelActeur.getActorById(((Acteur)content).getId());
			} else {
				node = (MyDefaultMutableTreeNode)content;
			}
			tree.setSelectionPath(new TreePath(node.getPath()));
		} else if (content instanceof Brick) {
			navigationTree.setSelectedContent(content);
		}
	}

	public JTreeActors getTree() {
		return tree;
	}

	public Set<Acteur> getSelectedActors() {
		MyDefaultMutableTreeNode selectedNode = tree.getSelectedNode();

		if (selectedNode == null)
			return null;

		return getAllActorVisible(selectedNode);
	}

	protected Set<Acteur> getAllActorVisible(MyDefaultMutableTreeNode node) {
		/*
		 * retourne tout les acteur visible contenue dans ce groupe et de tout
		 * ses sous groupe
		 */
		TreeSet<Acteur> actors = new TreeSet<Acteur>();

		if (MyDefaultMutableTreeNode.isActor(node) || MyDefaultMutableTreeNode.isBase(node) || MyDefaultMutableTreeNode.isCorps(node)) {
			actors.add((Acteur) node);
			return actors;
		} else if (MyDefaultMutableTreeNode.isGroupe(node)) {
			// if
			// (DialogHandlerFrame.showYesNoDialog("Tout les acteurs du groupe seront ajouter a la liste.\n voulez vous continuer?")
			// != JOptionPane.YES_OPTION)
			// return actors;

			// récupére et retourne tout les acteurs du groupe
			int nbChildren = tree.getModel().getChildCount(node);
			for (int i = 0; i < nbChildren; i++) {
				MyDefaultMutableTreeNode child = (MyDefaultMutableTreeNode) tree
				.getModel().getChild(node, i);
				actors.addAll(getAllActorVisible(child));
			}
			return actors;
		} else
			return null;
	}

	public Acteur getSelectedActor() {
		MyDefaultMutableTreeNode selectedNode = tree.getSelectedNode();

		if (selectedNode == null)
			return null;

		if (MyDefaultMutableTreeNode.isActor(selectedNode)) {
			return (Acteur) selectedNode;
		} else
			return null;
	}

	public void readExternal(ObjectInput in) throws IOException,
	ClassNotFoundException {}

	public void writeExternal(ObjectOutput out) throws IOException {
		throw new RuntimeException("D'ou vient la presence de ce TreeListener ?"); //$NON-NLS-1$
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public Vector<Content> getSelectedContents() {
		return getAllChildren(tree.getSelectedNode());
	}

	private Vector<Content> getAllChildren(MyDefaultMutableTreeNode node) {
		Vector<Content> contents = new Vector<Content>();
		
		if(node !=null) {
			contents.add(node);
			
			for(int i = 0 ; i < node.getChildCount() ; i++) {
				MyDefaultMutableTreeNode child = (MyDefaultMutableTreeNode)node.getChildAt(i);
				contents.addAll(getAllChildren(child));
			}
		}
		
		return contents;
	}
}