package models;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JOptionPane;

import client.export.ExportsContextualMenu;

import translation.Messages;

import dataPack.Activite;
import dataPack.Content;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.RankContextualMenu;
import graphicalUserInterface.RelationChooserPopUp;

public class BrickEditingMousePlugin
extends TriadeEditingMousePlugin<BrickVertex, BrickEdge> {

	protected final Brick editedBrick;

	public BrickEditingMousePlugin(Brick _editedBrick,
			TreeListener _treeListener, Layout<BrickVertex, BrickEdge> vertexPosition) {
		super(vertexPosition);
		editedBrick = _editedBrick;
		treeListener = _treeListener;
		treeListener.setGrapheListener(this);
	}

	@Override
	public void removeSelectedVertex() {
		if (selectedVertex != null) {
			if (selectedVertex.getContent() instanceof Activite) {
				DialogHandlerFrame
				.showErrorDialog(Messages.getString("BrickEditingMousePlugin.0")); //$NON-NLS-1$
				return;
			}

			if (DialogHandlerFrame
					.showYesNoDialog(Messages.getString("BrickEditingMousePlugin.1")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
				treeListener.showActor(selectedVertex.getContent());
				editedBrick.removeBrickVertex(selectedVertex);
				selectedVertex = null;
			}

		} else if (selectedEdge != null) {
			if (DialogHandlerFrame
					.showYesNoDialog(Messages.getString("BrickEditingMousePlugin.2")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
				editedBrick.removeModelEdge(selectedEdge);
				selectedEdge = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent e) {
		if (checkModifiers(e)) {
			final VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();
			final Point2D transformedPoint = vv.getRenderContext()
			.getMultiLayerTransformer()
			.inverseTransform(e.getPoint());
			final Point2D p = e.getPoint();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(vertexLocations, p.getX(), p.getY());

				BrickEdge edge = pickSupport.getEdge(vertexLocations, p.getX(), p.getY());

				if (vertex == null && edge != null) 
				{
					
					selectEdge(edge);					
					((RelationChooserPopUp) modelView.getPopUp()).showRelationChooserView(edge, this,Messages.getString("BrickEditingMousePlugin.3")); //$NON-NLS-1$

				} 
				else 
				{
					selectEdge(null);
				}

				if (vertex != null) 
				{ // get ready to make an edge
					if (selectedVertex != vertex)
						selectVertex(vertex);
					startVertex = vertex;
					down = vv.getRenderContext().getMultiLayerTransformer().transform(vertex.getLocation());
					transformEdgeShape(down, down);
					vv.addPostRenderPaintable(edgePaintable);
					edgeIsDirected = true;
					transformArrowShape(down, e.getPoint());
					vv.addPostRenderPaintable(arrowPaintable);
					drawArrow = false;
				} else if (activeContent != null) {
					// On ajoute un sommet au graph en le liant à une nouvelle
					// brick.
					Graph<BrickVertex, BrickEdge> graph = vv.getGraphLayout().getGraph();
					BrickVertex bV = editedBrick.addBrickVertex(activeContent);
					bV.setLocation(p);
					vertexLocations.setLocation(bV, transformedPoint);
					bV.setLocation(transformedPoint);
					modelView.computeCenter();

					Content oldActiveContent = activeContent;
					activeContent = null;
					treeListener.hideActor(oldActiveContent);
					selectVertex(bV);


					for (Iterator<BrickVertex> iterator = graph.getVertices()
							.iterator(); iterator
							.hasNext();) {
						vertexLocations.lock(iterator.next(), true);
					}
					graph.addVertex(bV);
					for (Iterator<BrickVertex> iterator = graph.getVertices()
							.iterator(); iterator
							.hasNext();) {
						vertexLocations.lock(iterator.next(), false);
					}
				}
				else if (edge == null)
				{
					selectVertex((BrickVertex)null);
				}
			}
			vv.repaint();
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
			final Point2D p = e.getPoint();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(
						vertexLocations, p.getX(), p.getY());

				if (vertex != null && startVertex != null) {
					if (vertex != startVertex) {

						if (vv.getGraphLayout().getGraph().isSuccessor(
								startVertex, vertex)) {

							// l'arrete existe deja, on l'édite
							BrickEdge mE = vv.getGraphLayout().getGraph().findEdge(startVertex, vertex);
							if (((RelationChooserPopUp) modelView
									.getPopUp())
									.showRelationChooserView(mE, this,
											Messages.getString("BrickEditingMousePlugin.4")) != -1) //$NON-NLS-1$
								selectEdge(mE);


						} else {
							// Ajout d'une nouvelle arrête

							Graph graph = vv.getGraphLayout().getGraph();
							BrickVertex source = startVertex;
							BrickVertex destination = vertex;
							BrickEdge mEdge = editedBrick
							.addEdge(new BrickEdge(source, destination));


							startVertex = null;
							down = null;


							boolean ok = false;
							if (((RelationChooserPopUp) modelView.getPopUp())
									.showRelationChooserView(mEdge, this,
											Messages.getString("BrickEditingMousePlugin.5")) != -1) //$NON-NLS-1$
								ok = true;



							if (ok) {


								graph.addEdge(mEdge, source, destination);
								selectEdge(mEdge);
							}

							startVertex = null;
							down = null;
							edgeIsDirected = false;
							vv.removePostRenderPaintable(edgePaintable);
							vv.removePostRenderPaintable(arrowPaintable);
							return;

						}

					} else {

						if (activeContent != null) {
							// l'utilisateur veut remplacer le contenu d'un
							// sommet
							if (vertex.getContent() instanceof Activite)
							{
								DialogHandlerFrame.showErrorDialog(Messages.getString("BrickEditingMousePlugin.6")); //$NON-NLS-1$

							}
							else if (JOptionPane
									.showConfirmDialog(null,
											Messages.getString("BrickEditingMousePlugin.7")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
								treeListener.showActor(vertex
										.getContent());
								vertex.setContent(activeContent);
								Content oldContent = activeContent;
								activeContent = null;
								treeListener.hideActor(oldContent);
							}
						}

					}
				} 
				vv.repaint();
			}

			startVertex = null;
			down = null;
			edgeIsDirected = false;
			vv.removePostRenderPaintable(edgePaintable);
			vv.removePostRenderPaintable(arrowPaintable);
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			final VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();
			final Point2D p = e.getPoint();
			GraphElementAccessor<BrickVertex, BrickEdge> pickSupport = vv
			.getPickSupport();
			if (pickSupport != null) {
				final BrickVertex vertex = pickSupport.getVertex(
						vertexLocations, p.getX(), p.getY());
				if (vertex != null)
				{
					if (! (vertex.getContent() instanceof Activite))
					{
						RankContextualMenu menu = new RankContextualMenu(vertex, editedBrick);
						menu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}

		}

	}

	@Override
	public Brick getEditedAbstractSchema() {
		return editedBrick;
	}

	@Override
	public void selectVertex(Content content) {
		selectVertex(editedBrick.getBrickVertexByContent(content));
	}

	@Override
	public void notifyTree(Content content) {

	}
}
