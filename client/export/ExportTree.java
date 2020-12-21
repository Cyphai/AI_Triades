package client.export;

import graphicalUserInterface.Program;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import models.Brick;
import models.BrickVertex;
import models.TriadeEditingMousePlugin;
import translation.Messages;
import dataPack.Acteur;
import dataPack.Content;
import dataPack.JTreeActors;
import dataPack.MyDefaultMutableTreeNode;

public class ExportTree extends JTreeActors implements TreeCellRenderer, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870470942564510295L;

	//TreeModel treeModel;
	ExportModel exportModel;
	ExportPopUp popup;
	
	public ExportTree() {
		//TODO a virer une fois ces arbre ne seront plus sauvé
		super();
		setEditable(false);
	}

	public ExportTree(ExportModel exportModel, ExportPopUp popup, TriadeEditingMousePlugin triadeEditingMousePlugin) {
		super(Program.myMainFrame.datapack);
		
		this.popup = popup;
		this.exportModel = exportModel;

		setTriadeEditingMousePlugin(triadeEditingMousePlugin);
		
		addMouseListener(this);
		setCellRenderer(this);
		setEditable(false);

		/* cache les sommets qui ne sont pas dans le schema à exporter */
		simpleHideAll();
//		DataPack dataPack = Program.myMainFrame.datapack;
		brick = exportModel.getBaseSchema();
		for(BrickVertex vertex : brick.getVertices()) {
			if(vertex.getContent() instanceof Brick) {
			} else {
				Content content = vertex.getContent();
				if(content instanceof MyDefaultMutableTreeNode) {
					simpleShowActor(content);
				} 
			} 
		}

		expendAllNode();
	}

	@Override
	protected MyDefaultMutableTreeNode getSelectedNode() {
		TreePath path = getSelectionPath();
		if (path == null) {
			return null;
		}

		return (MyDefaultMutableTreeNode) path.getLastPathComponent();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		MyDefaultMutableTreeNode myNode = (MyDefaultMutableTreeNode) value;
		JLabel label;
		
		if(myNode instanceof Acteur) {
			Acteur actor = dataPack.getActorsModel().getActorById(((Acteur)myNode).getId());
			label = (JLabel)treeCellRenderer.getTreeCellRendererComponent(tree, actor, selected, expanded, leaf, row, hasFocus);
			ExportVertexData vertexData = exportModel.getExportVertexDataByContent(actor);
			if(vertexData != null && vertexData.getExportData().isVisible() == false) {
				if(selected == false) {
					label.setBackground(Color.lightGray);
				}
				label.setText(label.getText() + Messages.getString("ExportTree.0")); //$NON-NLS-1$
			} else {
				String labelText = label.getText();
				//TODO trouver comment afficher correctement les labels sans devoir utiliser les espaces
				label.setText(labelText); //$NON-NLS-1$
			}
		} else {
			label = (JLabel)treeCellRenderer.getTreeCellRendererComponent(tree, myNode, selected, expanded, leaf, row, hasFocus);
		}
		
		return label;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MyDefaultMutableTreeNode selectedNode = getSelectedNode();
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(selectedNode != null && selectedNode instanceof Acteur) {
				//Acteur actor = dataPack.getActorsModel().getActorById(((Acteur)selectedNode).getId());
				ExportVertexData vertexData = exportModel.getExportVertexDataByContent(selectedNode);
				setVertexSelected(selectedNode);
				if(vertexData != null) {
					popup.selectNewObject(vertexData);
				} else {
					popup.selectNewObject(null);
				}
			} else {
				setVertexSelected(null);
				popup.selectNewObject(null);
			}
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
	
	protected Object writeReplace()
	{
		return new ExportTreeProxy();
	}

	protected static class ExportTreeProxy implements Serializable
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
