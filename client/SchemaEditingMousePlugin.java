package client;

import java.awt.event.MouseEvent;

import main.RelationComplete;
import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import models.TriadeEditingMousePlugin;
import translation.Messages;
import client.export.ExportsContextualMenu;
import dataPack.Acteur;
import dataPack.Content;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;
import graphicalUserInterface.RelationChooserPopUp;

public class SchemaEditingMousePlugin extends
TriadeEditingMousePlugin<BrickVertex, BrickEdge> {

	protected Brick editedSchema;
	protected NavigationTree navigationTree;

	public SchemaEditingMousePlugin(TreeListener _treeListener,
			Brick _editedSchema, Layout<BrickVertex, BrickEdge> vertexPosition) {
		super(vertexPosition);
		this.navigationTree = Program.myMainFrame.datapack.getCurrentSession().getNavigationTree();
		editedSchema = _editedSchema;
		setTreeListener(_treeListener);
	}

	@Override
	public void removeSelectedVertex() {
		if (selectedVertex != null) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("SchemaEditingMousePlugin.0")); //$NON-NLS-1$

		} else if (selectedEdge != null) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("SchemaEditingMousePlugin.1")); //$NON-NLS-1$

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent e) {
		if (checkModifiers(e)) {

			final VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(vv
						.getGraphLayout(), e
						.getPoint().getX(), e.getPoint().getY());

				BrickEdge edge = pickSupport.getEdge(vertexLocations, e.getPoint().getX(), e.getPoint().getY());

				if (vertex == null && edge != null) 
				{
					if (((RelationChooserPopUp) modelView.getPopUp()).showRelationChooserView(edge, this,Messages.getString("SchemaEditingMousePlugin.2")) != -1) //$NON-NLS-1$
						selectEdge(edge);					

				} 
				else 
				{
					selectEdge(null);
				}

				if (vertex != null) { // get ready to make an edge
					startVertex = vertex;
					down = e.getPoint();
					transformEdgeShape(down, down);
					vv.addPostRenderPaintable(edgePaintable);
					edgeIsDirected = true;
					transformArrowShape(down, e.getPoint());
					vv.addPostRenderPaintable(arrowPaintable);

				}
				else
				{
					selectVertex((BrickVertex)null);
				}
			}
		}
	}

	/**
	 * If startVertex is non-null, and the mouse is released over an existing
	 * vertex, create an undirected edge from startVertex to the vertex under
	 * the mouse pointer. If shift was also pressed, create a directed edge
	 * instead.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void mouseReleased(MouseEvent e) {
		if (checkModifiers(e)) {
			final VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(vv
						.getGraphLayout(), e
						.getPoint().getX(), e
						.getPoint().getY());
				if (vertex != null && startVertex != null) {
					if (vertex != startVertex) {
						if (vv.getGraphLayout().getGraph().isSuccessor(
								startVertex, vertex)) {

							BrickEdge edge = vv.getGraphLayout().getGraph()
							.findEdge(
									startVertex, vertex);

							if (((RelationChooserPopUp) modelView.getPopUp())
									.showRelationChooserView(
											edge, this,
											Messages.getString("SchemaEditingMousePlugin.3")) != -1) //$NON-NLS-1$
								selectEdge(edge);

						} else {
							Graph<BrickVertex, BrickEdge> graph = vv.getGraphLayout().getGraph();
							BrickVertex source = startVertex;
							BrickVertex destination = vertex;
							BrickEdge mEdge = editedSchema.addEdge(new BrickEdge(source, destination,RelationComplete.createNoRelation()));


							startVertex = null;
							down = null;


							boolean ok = false;
							if (((RelationChooserPopUp) modelView.getPopUp())
									.showRelationChooserView(mEdge, this,
											Messages.getString("SchemaEditingMousePlugin.4")) != -1) //$NON-NLS-1$
								ok = true;



							if (ok) {


								graph.addEdge(mEdge, source, destination);
								selectEdge(mEdge);
							}

						}
					} else {
						if (vertex != selectedVertex) {
							selectVertex(vertex);

						}
					}

				}
			}
			startVertex = null;
			down = null;
			edgeIsDirected = false;
			vv.removePostRenderPaintable(edgePaintable);
			vv.removePostRenderPaintable(arrowPaintable);
			vv.repaint();
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			final VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(vv
						.getGraphLayout(), e
						.getPoint().getX(), e
						.getPoint().getY());

				if (vertex != null && vertex.getContent() instanceof Acteur)
				{
					ActorSheetAccess menu = new ActorSheetAccess((Acteur)vertex.getContent());
					menu.show(e.getComponent(),e.getX(),e.getY());
				}
				else
				{

					ExportsContextualMenu menu = new ExportsContextualMenu(
							editedSchema, navigationTree);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		}

	}

	@Override
	public Brick getEditedAbstractSchema() {
		return editedSchema;
	}

	@Override
	public void selectVertex(Content content) {
		if(content == null) {
			super.selectVertex((BrickVertex)null);
		} else {
			selectVertex(editedSchema.getBrickVertexByContent(content));
		}
	}

	@Override
	public void notifyTree(Content content) {
		treeListener.setSelectedContent(content);
	}

	@Override
	public void setTreeListener(TreeListener treeListener) {
		super.setTreeListener(treeListener);
		if(treeListener != null)
			treeListener.setGrapheListener(this);
	}

	@Override
	public void setContent(Content content) {
		selectVertex(content);
	}
}
