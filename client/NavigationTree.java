package client;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import translation.Messages;

import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import models.BrickView;
import client.export.ExportModel;
import client.export.ExportsContextualMenu;
import client.stringTranslator.StringTranslator;
import client.stringTranslator.StringTranslator.StringType;
import dataPack.Content;
import dataPack.Groupe;
import dataPack.MyDefaultMutableTreeNode;
import dataPack.MyMutableJTree;
import dataPack.TreeActorsCellRendere;
import dataPack.TreeListener;

public class NavigationTree extends MyMutableJTree implements TreeCellRenderer , MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2949464319099111849L;

	protected DefaultTreeModel treeModel;
	protected JPanel schemaView;
	protected String currentStep;
	protected BrickView<?, ?> currentBrickView;
	protected Hashtable<String, BrickView<?,?>> brickViews;
	final protected Hashtable<Content, DefaultMutableTreeNode> exportGroups;
	
	protected Session session;

	public NavigationTree() {
		//TODO a virer une fois ces arbre ne seront plus sauvé
		super();
		exportGroups = null;
	}

	public NavigationTree(Session session, JPanel schemaView) {
//		System.out.println("NavigationTree.NavigationTree()");
		this.schemaView = schemaView;
		this.session = session;
		brickViews = new Hashtable<String, BrickView<?,?>>();
		exportGroups = new Hashtable<Content, DefaultMutableTreeNode>();

		session.setNavigationTree(this);
		
		setEditable(false);
		
		buildTree();

		getModel().addTreeModelListener(this);
	}

	private void buildTree() {
		currentStep = ""; //$NON-NLS-1$

		DefaultMutableTreeNode root = new Groupe("Root"); //$NON-NLS-1$

		treeModel = new DefaultTreeModel(root);
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		setModel(treeModel);
		setCellRenderer(this);
		addMouseListener(this);
		setRootVisible(false);

		/* construction du model avec la seesion */
		/* ajoute les schemas */
		for(int i = 0 ; i < session.getDatapack().getSteps().size() ; i++) {
			Brick navigationSchema = session.getBrickList().elementAt(i);
			String step = session.getDatapack().getSteps().elementAt(i);
			if(navigationSchema.getVertices().size() != 0) {
				// ajoute les dossiers d'étape
				DefaultMutableTreeNode stepGroup = new Groupe(StringTranslator.getTranslatedString(step, StringType.stepsType));
				stepGroup.setUserObject(navigationSchema);
				root.add(stepGroup);
				expandPath(new TreePath(stepGroup.getPath()));

				Groupe exportGroup = new Groupe(Messages.getString("Version_0_9_9_9.NavigationTree.0")); //$NON-NLS-1$
				exportGroups.put(navigationSchema, exportGroup);
				//ajouter les sous schema
				for(BrickVertex vertex : navigationSchema.getVertices()) {
					Brick brick = (Brick)vertex.getContent();
					stepGroup.add(new DefaultMutableTreeNode(brick));
					exportGroups.put(brick, exportGroup);
//					System.out.println("Put : " + brick);

					Vector<ExportModel> brickExported = session.getExports(brick);
					if (brickExported != null) {
						for (ExportModel exportModel : brickExported) {
							// ajoute les schemas axportés dans le dossier
							DefaultMutableTreeNode export = new DefaultMutableTreeNode(
									exportModel);
							exportGroup.add(export);
							export.setUserObject(exportModel);
						}
					}
				}

				/* Ajoute le dossier des exports */
				stepGroup.add(exportGroup);
			}
		}

		treeModel.reload();
		validate();
		repaint();
	}

	public void addExport(ExportModel newExportModel, Brick schema) {
		DefaultMutableTreeNode exportGroup = getExportGroup(schema);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(newExportModel);
		exportGroup.add(node);
		makeVisible(new TreePath(node.getPath()));

		treeModel.reload();
		validate();
	}
	
	public DefaultMutableTreeNode getExportGroup(Content content) {
//		System.out.println("Get " + schemaId);
		return exportGroups.get(content);
	}

	protected DefaultMutableTreeNode getSelectedNode() {
		TreePath path = getSelectionPath();
		if (path == null) {
			return null;
		}

		return (DefaultMutableTreeNode) path.getLastPathComponent();
	}

	protected Brick getSelectedSchema() {
		try{
			return (Brick)getSelectedNode().getUserObject();
		} catch(Exception e) {
			return null;
		}
	}

	protected ExportModel getSelectedExportModel() {
		try{
			return (ExportModel)getSelectedNode().getUserObject();
		} catch (Exception e) {
			return null;
		}
	}

	protected String getSelectedStep() {
		try{
			DefaultMutableTreeNode selectedNode = getSelectedNode();
			
			if(((DefaultMutableTreeNode)getModel().getRoot()).isNodeChild(selectedNode)) {
				return selectedNode.toString();
			}
			
			return selectedNode.getParent().toString();
		} catch(Exception e) {
			e.printStackTrace();
			return ""; //$NON-NLS-1$
		}		
	}

	protected int getSelectedStepIndex() {
		String stepString = getSelectedStep();
		for(int i = 0 ; i < session.getDatapack().getSteps().size() ; i++) {
			String translatedStep = StringTranslator.getTranslatedString(session.getDatapack().getSteps().elementAt(i), StringType.stepsType);
			if(translatedStep.equalsIgnoreCase(stepString))
				return i;
		}

		return -1;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		try{
			MyDefaultMutableTreeNode myNode = (MyDefaultMutableTreeNode)value;
			return myNode.getJComponent(selected, expanded, leaf, row, hasFocus);
		} catch(Exception e) {
			DefaultMutableTreeNode myNode = (DefaultMutableTreeNode)value;
			JLabel label = TreeActorsCellRendere.createDefaultLabel(myNode.getUserObject().toString(), null, selected);
			return label;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			JTree navigationTree = (NavigationTree)e.getSource();

			TreePath nodePath = navigationTree.getPathForLocation(e.getX(), e.getY());
			if(nodePath == null) {
				return;
			}

			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)nodePath.getLastPathComponent();
			if(selectedNode != null) {
				navigationTree.setSelectionPath(new TreePath(selectedNode.getPath()));
			}
		}

		Brick schema = getSelectedSchema();
		if (schema != null) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if(schema.isNavigationBrick() == false) {
					ExportsContextualMenu menu = new ExportsContextualMenu(schema, this);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			} else if (e.getClickCount() == 1) {
				if (getSelectedStep() != currentStep) {
					BrickView<?,?> bV = brickViews.get(getSelectedStep());
					if (bV == null) {
						TreeListener treeListener = new TreeListener(this);
						bV = new BrickView<BrickVertex, BrickEdge>(session
								.getBrickList().elementAt(
										getSelectedStepIndex()), null, 
										Program.myMainFrame.getDataPack(), treeListener);
						brickViews.put(getSelectedStep(), bV);
					}
					
					schemaView.removeAll();
					schemaView.add(bV);
					schemaView.validate();
					schemaView.repaint();
					currentStep = getSelectedStep();
					currentBrickView = bV;
				}
			}else if (e.getClickCount() > 1) {
				if (!schema.isNavigationBrick())
					Program.myMainFrame.addTab(schema);
			}
		} else if(e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1 && getSelectedNode() !=null && getSelectedNode().getUserObject() instanceof ExportModel) {
			Program.myMainFrame.addTab(getSelectedNode().getUserObject());
		} else if(e.getButton() == MouseEvent.BUTTON3 && getSelectedNode() != null && getSelectedNode().getUserObject() instanceof ExportModel)
		{
			getDeleteExportContextualMenu((ExportModel) getSelectedNode().getUserObject()).show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	public void setSelectedContent(Content content) {
	}
	
	protected JPopupMenu getDeleteExportContextualMenu(final ExportModel export)
	{
		JPopupMenu result = new JPopupMenu();
		JMenuItem delete = new JMenuItem(Messages.getString("Version_1_0_2.NavigationTree.0")); //$NON-NLS-1$
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (DialogHandlerFrame.showYesNoCancelDialog(Messages.getString("Version_1_0_2.NavigationTree.1")+export.getName()+Messages.getString("Version_1_0_2.NavigationTree.2")) == JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
				{
					session.removeExport(export.getBaseSchema(), export);
					((DefaultMutableTreeNode)getSelectedNode()).removeFromParent();
					reloadModel();
					NavigationTree.this.repaint();
				}
				
			}
		});
		result.add(delete);
		return result;
		
	}
}
