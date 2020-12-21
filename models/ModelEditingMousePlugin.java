package models;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JOptionPane;

import dataPack.TreeListener;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.SimpleSparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import graphicalUserInterface.DialogHandlerFrame;

public class ModelEditingMousePlugin extends TriadeEditingMousePlugin {


	protected TreeListener treeListener;
	protected Model editedModel;

	public ModelEditingMousePlugin(TreeListener _treeListener, Model _editedModel)
	{
		super();
		editedModel = _editedModel;
		treeListener = _treeListener;
		treeListener.setGrapheListener(this);

	}
	
	@Override
	public void removeSelectedVertex()
	{
		if (selectedVertex != null)
		{
			ModelVertex modelVertex = (ModelVertex)selectedVertex.getUserDatum(ModelVertex.class);
			if (modelVertex != null)
			{
				
				
				if (DialogHandlerFrame.showYesNoDialog("Voulez vous vraiment supprimer le sommet sélectionné?") == JOptionPane.YES_OPTION)
				{	
					editedModel.removeModelVertex(modelVertex);
					selectedVertex = null;
					treeListener.showActor(modelVertex.contentId);
				}
			}
		}
		else if (selectedEdge != null)
		{
			ModelEdge modelEdge = (ModelEdge)selectedEdge.getUserDatum(ModelEdge.class);
			if (DialogHandlerFrame.showYesNoDialog("Voulez vous vraiment supprimer l'arête sélectionnée?") == JOptionPane.YES_OPTION)
			{	
				editedModel.removeModelEdge(modelEdge);
				selectedEdge = null;
			}
		}
	}


	public void mousePressed(MouseEvent e) {
		if(checkModifiers(e)) {
			final VisualizationViewer vv =
				(VisualizationViewer)e.getSource();
			final Point2D p = vv.inverseViewTransform(e.getPoint());
			PickSupport pickSupport = vv.getPickSupport();
			if(pickSupport != null) {
				final Vertex vertex = pickSupport.getVertex(p.getX(), p.getY());
				if(vertex != null) { // get ready to make an edge
					startVertex = vertex;
					down = e.getPoint();
					transformEdgeShape(down, down);
					vv.addPostRenderPaintable(edgePaintable);
					edgeIsDirected = true;
					transformArrowShape(down, e.getPoint());
					vv.addPostRenderPaintable(arrowPaintable);

				} else { // make a new vertex
					if (activeContent != null)
					{
						Graph graph = vv.getGraphLayout().getGraph();
						Vertex newVertex = new SimpleSparseVertex();
						vertexLocations.setLocation(newVertex, vv.inverseTransform(e.getPoint()));

						ModelVertex mV = editedModel.addModelVertex(activeContent, newVertex);
						mV.setLocation(vv.inverseTransform(e.getPoint()));
						newVertex.addUserDatum(ModelVertex.class,mV , new UserDataContainer.CopyAction.Shared());
						newVertex.addUserDatum("nom",editedModel.dataPack.getStringById(activeContent),new UserDataContainer.CopyAction.Clone());
						newVertex.addUserDatum("selection",new Integer(0),new UserDataContainer.CopyAction.Clone());
						newVertex.addUserDatum("id",mV.contentId,new UserDataContainer.CopyAction.Clone());
						Integer oldContent = activeContent;
						activeContent = null;
						treeListener.hideActor(oldContent);
						
						Layout layout = vv.getGraphLayout();
						for(Iterator<?> iterator=graph.getVertices().iterator(); iterator.hasNext(); ) {
							layout.lockVertex((Vertex)iterator.next());
						}
						graph.addVertex(newVertex);
						vv.getModel().restart();
						for(Iterator<?> iterator=graph.getVertices().iterator(); iterator.hasNext(); ) {
							layout.unlockVertex((Vertex)iterator.next());
						}
						vv.repaint();
					}
				}
			}
		}
	}

	/**
	 * If startVertex is non-null, and the mouse is released over an
	 * existing vertex, create an undirected edge from startVertex to
	 * the vertex under the mouse pointer. If shift was also pressed,
	 * create a directed edge instead.
	 */
	public void mouseReleased(MouseEvent e) {
		if(checkModifiers(e)) {
			final VisualizationViewer vv =
				(VisualizationViewer)e.getSource();
			final Point2D p = vv.inverseViewTransform(e.getPoint());
			PickSupport pickSupport = vv.getPickSupport();
			if(pickSupport != null) {
				final Vertex vertex = pickSupport.getVertex(p.getX(), p.getY());
				if(vertex != null && startVertex != null) {
					if (vertex != startVertex)
					{
						if (startVertex.getSuccessors().contains(vertex))
						{
							
							Edge edge = startVertex.findEdge(vertex);
							ModelEdge mE = (ModelEdge)edge.getUserDatum(ModelEdge.class);
							 if (modelView.getPopUp().showRelationChooserView(mE,this,"Edition d'une relation") != -1)
							 	 selectEdge(edge);
							 
							
						}
						else
						{
						Graph graph = vv.getGraphLayout().getGraph();
						Edge edge = new DirectedSparseEdge(startVertex,vertex);
						ModelVertex source = (ModelVertex)startVertex.getUserDatum(ModelVertex.class);
						ModelVertex destination = (ModelVertex)vertex.getUserDatum(ModelVertex.class);
						Integer sourceID;
						Integer destinationID;

						//Normalement dans un model, ni source, ni destination ne devrait etre null toutefois, les test sont conservé en cas de besoin
						if (source == null)
							sourceID = (Integer)startVertex.getUserDatum("id");
						else
							sourceID = source.vertexId;

						if (destination == null)
							destinationID = (Integer)startVertex.getUserDatum("id");
						else
							destinationID = destination.vertexId;

						if (destinationID == null || sourceID == null)
						{
							System.err.println(destinationID+" : "+destination+"  --  "+sourceID+" : "+source);
							System.err.println("Erreur lors de l'ajout d'une arrête, un des sommets est mal construit");

							startVertex = null;
							down = null;
							edgeIsDirected = false;
							vv.removePostRenderPaintable(edgePaintable);
							vv.removePostRenderPaintable(arrowPaintable);
							return;
						}

						ModelEdge mEdge = editedModel.addModelEdge(sourceID, destinationID, edge);
						if (modelView.getPopUp().showRelationChooserView(mEdge,this,"Création d'une relation") != -1)
						{
							edge.addUserDatum(ModelEdge.class, mEdge, new UserDataContainer.CopyAction.Shared());
							edge.addUserDatum("selection", new Integer(1), new UserDataContainer.CopyAction.Clone());
							graph.addEdge(edge);
							selectEdge(edge);
						}
						else
						{
							editedModel.removeModelEdge(mEdge);
						}
						
						}
					}
					else
					{
						if (vertex != selectedVertex)
						{
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
	}

	@Override
	public AbstractSchema getEditedAbstractSchema() {
		return editedModel;
	}


}
