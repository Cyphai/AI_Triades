package dataPack;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.Dimension;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.TransferHandler;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import models.TriadeEditingMousePlugin;
import translation.Messages;

/* 
 * Affiche la hiéarchie des acteurs dans leur entreprise et permet dans cacher certain ou de les afficher.
 * Ainsi seul les acteurs pouvant étre manipuler sont visible, ce qui facilite la visibilité et la manipulation.
 * De plus elle ne touche pas au model et les changement sont mis a jours automatiquement en cas de changement sur ce model.
 */

public class JTreeActors extends MyMutableJTree implements Externalizable, CellEditorListener {
	private static final long serialVersionUID = -5070764781286563264L;

	protected TreeActorsCellRendere treeCellRenderer;
	protected TreeListener treeListener;

	protected FiltreActeursEntreprise filtreActeurs;
	protected Vector<JeuActeur> jeuxActeur;
	protected DataPack dataPack;

	protected Vector<Acteur> hidedBaseActors;
	protected boolean isHidedBases;

	protected Brick brick;
	
	protected TriadeEditingMousePlugin<BrickVertex, BrickEdge> triadeEditingMousePlugin;

	protected Groupe otherGroup;
	
	public JTreeActors() {
		//TODO trouver comment ne pas sauvegarder cette arbre
		treeListener = null;
	}
	
	public JTreeActors(DataPack dataPack, boolean isDataPack) {
		treeListener = new TreeListener(dataPack.getActorsModel(), null);
		if(!Program.isTriades()) {
			setToolTipText(Messages.getString("JTreeActors.0")); //$NON-NLS-1$
		}
		addMouseListener(treeListener);
		build(dataPack, isDataPack);
		initJTree();
	//	setPreferredSize(new Dimension(330, 400));
	}
	
	public JTreeActors(DataPack dataPack) {
		this(dataPack, true);
	}
	
	public JTreeActors(DataPack dataPack, Brick brick) {
		this(dataPack, false);

		this.brick = brick;
		
		for (JeuActeur jeuActeur : Program.myMainFrame.getDataPack()
				.getJeuxActeur()) {
			ajouterJeuActeur(jeuActeur);
		}

		// les acteurs present dans la brique doivent étre cacher
		// les moyen sont affichés pour les ajouter a la brique

		Vector<Content> actorsSet = new Vector<Content>();

		// BrickType brickType =
		// brick.getDatapack().getBrickTypeById(brick.getType().getBrickTypeId());

		// récupére tout les acteurs existant dans la brique
		for (BrickVertex brickVertex : brick.getVertices()) {
			actorsSet.add(brickVertex.getContent());
		}

		// cache les acteurs deja present
		for (Content actor : actorsSet) {
			if(actor instanceof MyDefaultMutableTreeNode)
				hideActor(actor);
		}

		// femre les dossier des Jeux d'acteurs
		for (JeuActeur jeuActeur : jeuxActeur) {
			collapsePath(new TreePath(jeuActeur.getGroupeJeuActeur().getPath()));
		}
	}

	private void build(DataPack dataPack, boolean isDataPack) {
		filtreActeurs = new FiltreActeursEntreprise(dataPack.getActorsModel(), isDataPack);
		jeuxActeur = new Vector<JeuActeur>();
		this.dataPack = dataPack;

		hidedBaseActors = new Vector<Acteur>();
		isHidedBases = false;
	
		setModel(filtreActeurs);
		
		getModel().addTreeModelListener(this);
		filtreActeurs.addTreeModelListener(this);
	}

	protected void initJTree() {
		treeListener.setTree(this);

		filtreActeurs.setTreeListener(treeListener);

		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		setRootVisible(false);
		
		expandPath(new TreePath((((DefaultMutableTreeNode)getModel()
				.getRoot())
				.getPath())));

		treeCellRenderer = new TreeActorsCellRendere();
		setCellRenderer(treeCellRenderer);
		TreeCellEditor cellEditor = new TreeCellEditor();
		setCellEditor(cellEditor);
		cellEditor.addCellEditorListener(this);

		dataPack.initDataPackActorsView(filtreActeurs);

		if (Program.isTriadesLoading()) {
			for (JeuActeur jeuActeur : dataPack.getJeuxActeur()) {
				hideBases(jeuActeur);
			}

		filtreActeurs.hideNode(dataPack.getActorsModel().getGroupeMoyens());
		}

		expendAllNode();

		treeListener.setSelectedNode(dataPack.getActorsModel()
				.getGroupeActeurs());
		// setDragEnabled(true);
		// setDropMode(DropMode.ON_OR_INSERT);
		// setTransferHandler(new TreeTransferHandler());
		//Attention, TreeTransfertHandler sorti du buildpath
		
		
		setDragEnabled(true);
		setTransferHandler(new TransferHandler(""));
		
	}

	
	
	protected void expendAllNode(DefaultMutableTreeNode node) {
		if (node == null)
			return;

		expendNode(node);

		int nbChild = node.getChildCount();
		for (int i = 0; i < nbChild; i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
			.getChildAt(i);

			expendAllNode(child);
		}
	}
	
	protected void expendNode(DefaultMutableTreeNode node) {
		// tree.expandPath(new TreePath(node.getPath()));
		makeVisible(new TreePath(node.getPath()));
	}

	protected void collapseNode(DefaultMutableTreeNode node) {
		collapsePath(new TreePath(node.getPath()));
	}

	public void expendAllNode() {
		expendAllNode(getRoot());
	}
	
	protected MyDefaultMutableTreeNode getSelectedNode() {
		TreePath path = getSelectionPath();
		if (path == null) {
			return null;
		}

		MyDefaultMutableTreeNode selectedNode = (MyDefaultMutableTreeNode) path
				.getLastPathComponent();

		return selectedNode;
	}

	public void hideActor(Content content) {
		hideActor((MyDefaultMutableTreeNode)content);
	}

	public void hideActor(MyDefaultMutableTreeNode actor) {
		filtreActeurs.hideNode(actor);

		// si l'"acteur est une base ou un corps il faut cacher les corps ou les
		// bases
		if (MyDefaultMutableTreeNode.isCorps(actor)) {
			hideBases(((ActeurBase) actor).jeuActeur);
			hidedBaseActors.add((ActeurBase) actor);
		} else if (MyDefaultMutableTreeNode.isBase(actor)) {
			hideAllBases(((ActeurBase) actor).jeuActeur);
			hideCorps(((ActeurBase) actor).jeuActeur);
			hidedBaseActors.add((ActeurBase) actor);
		}
	}

	public void hideAll() {
		hideAll(dataPack.getActorsModel().getRoot());
	}

	public void hideAll(DefaultMutableTreeNode father) {
		if(father.isLeaf() == false) {
			for(Enumeration<?> enumeration = father.children() ; enumeration.hasMoreElements() ;)
				hideAll((DefaultMutableTreeNode)enumeration.nextElement());
		} 

		try{
			Acteur acteur = (Acteur)father;
			hideActor(acteur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//			filtreActeurs.hideNode(node);
		//			hidedBaseActors.add(new Acteur(null, null, null)); // hack pour corriger le fait que ce tableau n'est pas rempli lors d'un hideall
		//			isHidedBases = true;
	}		

	public void showActor(Content content) {
		MyDefaultMutableTreeNode actor = (MyDefaultMutableTreeNode)content;
		
		hidedBaseActors.remove(actor);
		if (hidedBaseActors.size() == 0) {
			showAllBases();
			showAllCorps();
		}

		filtreActeurs.shwoNode(actor);
	}

	public void simpleHideAll() {
		filtreActeurs.hideNodes(dataPack.getActorsModel().getAllActors());
	}

	public MyDefaultMutableTreeNode simpleShowActor(Content content) {
		MyDefaultMutableTreeNode actor = (MyDefaultMutableTreeNode)content;
		filtreActeurs.shwoNode(actor);
		return actor;
	}

	public void hideCorps(JeuActeur jeuActeur) {
		filtreActeurs.hideNodes(jeuActeur.getGroupesCorps());
	}

	public void hideBases(JeuActeur jeuActeur) {
		filtreActeurs.hideNodes(jeuActeur.getListeBase());
	}

	public void hideAllBases(JeuActeur jeuActeurReference) {
		if (isHidedBases == false) {

			for (JeuActeur jeuActeur : jeuxActeur) {
				if (jeuActeur != jeuActeurReference) {
					hideBases(jeuActeur);
				}
			}
		}
	}

	public void hideAllCorps(JeuActeur jeuActeurReference) {
		for (JeuActeur jeuActeur : jeuxActeur) {
			if (jeuActeur != jeuActeurReference) {
				filtreActeurs.hideNodes(jeuActeur.getGroupesCorps());
			}
		}
	}

	public void showAllBases() {
		isHidedBases = false;
		for(JeuActeur jeuActeur : jeuxActeur) {
			filtreActeurs.shwoNodes(jeuActeur.getListeBase());
		}
	}

	public void showAllCorps() {
		for (JeuActeur jeuActeur : jeuxActeur) {
			filtreActeurs.shwoNodes((jeuActeur.getGroupesCorps()));

			//pour chaque groupe corps ajoute tout les acteurs
			for (String corps : jeuActeur.getListeCorps()) {
				filtreActeurs.shwoNodes(jeuActeur.getCorps(corps));
			}
		}
	}

	public TreeListener getListener() {
		return treeListener;
	}

	public void ajouterJeuActeur(JeuActeur jeuActeur) {
		jeuxActeur.add(jeuActeur);
	}

	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) getModel().getRoot();
	}

	@Override
	public DefaultTreeModel getModel() {
		return (DefaultTreeModel)super.getModel();
	}

	public void readExternal(ObjectInput in) throws IOException,
	ClassNotFoundException {
	}

	public void writeExternal(ObjectOutput out) throws IOException {
	}

	public void startEditing() {
		startEditing(getSelectionPath());
	}
	
	public void startEditing(TreePath selectionPath) {
		Object obj = selectionPath.getLastPathComponent();
		
		if (obj instanceof MyDefaultMutableTreeNode) {

			MyDefaultMutableTreeNode selection = (MyDefaultMutableTreeNode) obj;
			ModelActeursEntreprise model = dataPack.getActorsModel();


			if (model.getGroupeActeurs().isNodeDescendant(selection) || model.getGroupeMoyens().isNodeDescendant(selection)) {

				startEditingAtPath(getSelectionPath());
			} else {
				DialogHandlerFrame.showInformationDialog("Vous ne pouvez pas renommer un élément d'un jeu d'acteur comme cela.\nVeuillez passez par l'interface de modification des jeux d'acteurs");
			}
		}
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		String newValue = ((DefaultCellEditor)e.getSource()).getCellEditorValue().toString();
		if(getSelectedNode() instanceof ActeurBase) {
			DialogHandlerFrame.showErrorDialog(Messages.getString("JTreeActors.1")); //$NON-NLS-1$
			setEditable(false);
			return;
		}
		getSelectedNode().changeStringValue(newValue);
		setEditable(false);
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
		setEditable(false);
	}

	public void setTriadeEditingMousePlugin(TriadeEditingMousePlugin<BrickVertex, BrickEdge> triadeEditingMousePlugin) {
		this.triadeEditingMousePlugin = triadeEditingMousePlugin;
	}
	
	public void setVertexSelected(Content content) {
		if(triadeEditingMousePlugin != null) {
			triadeEditingMousePlugin.selectVertex(content);
		}
	}
	
	public void addContent(Content content) {
		if(otherGroup == null) {
			otherGroup = new Groupe("Autres"); //$NON-NLS-1$
			getModel().insertNodeInto(otherGroup, getRoot(), getRoot().getChildCount());
		}
		getModel().insertNodeInto(new DefaultMutableTreeNode(content), otherGroup, otherGroup.getChildCount());
	}
}