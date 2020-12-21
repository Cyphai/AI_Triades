package models;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import translation.Messages;
import client.export.ExportsContextualMenu;
import dataPack.Content;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

public class NavigationMousePlugin<V extends BrickVertex, E extends BrickEdge>
extends TriadeEditingMousePlugin<V, E> {

	protected Brick editedBrick;

	public NavigationMousePlugin(TreeListener _treeListener,
			Brick _editedSchema, Layout<BrickVertex, BrickEdge> vertexPosition) {
		super((Layout<V, E>) vertexPosition);
		editedBrick = _editedSchema;

		treeListener = _treeListener;
		if (treeListener != null)
			treeListener.setGrapheListener(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}


	@Override
	@SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
			.getSource();
			final Point2D p = e.getPoint();
			GraphElementAccessor<V, E> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {

				if (pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY()) != null)
				{
					final Brick brick = (Brick) pickSupport.getVertex(
							vv.getGraphLayout(), p.getX(), p.getY()).getContent();
					if (brick != null) {
						ExportsContextualMenu menu = new ExportsContextualMenu(
								brick);
						menu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}

		if (checkModifiers(e)) {

			final VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();
			final Point2D p = e.getPoint();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(
						vv.getGraphLayout(), p.getX(), p.getY());
				if (vertex != null) {
					if (e.getClickCount() > 1
							&& e.getButton() == MouseEvent.BUTTON1) {
						Content content = vertex.getContent();
						if (content instanceof Brick) {

							Program.myMainFrame.addTab(content);
						}

					}

				}
			}
			vv.repaint();
		}
	}

	@Override
	public Brick getEditedAbstractSchema() {
		return editedBrick;
	}

	@Override
	public void removeSelectedVertex() {
		DialogHandlerFrame.showErrorDialog(Messages.getString("NavigationMousePlugin.0")); //$NON-NLS-1$
	}

	@Override
	public void selectVertex(Content content) {
		selectVertex((V)editedBrick.getBrickVertexByContent(content));
	}

	@Override
	public void notifyTree(Content content) {
		treeListener.setSelectedContent(content);
	}

}
