package client.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import models.Brick;
import models.TriadeEditingMousePlugin;
import translation.Messages;
import dataPack.Content;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import graphicalUserInterface.DialogHandlerFrame;

public class ExportingMousePlugin extends
		TriadeEditingMousePlugin<ExportVertexData, ExportEdgeData> {

	// protected Model editedModel;
	protected ExportModel currentExport;
	protected ExportPopUp popUp;

	public ExportingMousePlugin(TreeListener _treeListener,
			ExportModel _curentShema,
			ExportPopUp popUp, Layout<ExportVertexData, ExportEdgeData> vertexPosition) {
		super(vertexPosition);
		this.popUp = popUp;
		currentExport = _curentShema;
		treeListener = _treeListener;
		treeListener.setGrapheListener(this);
	}

	@Override
	public void selectEdge(ExportEdgeData newSelection) {
		if (newSelection != null)
			popUp.selectNewObject(newSelection);
		else
			popUp.unselectEdge();
		
		super.selectEdge(newSelection);
	}

	@Override
	public void selectVertex(ExportVertexData vertex) {
		
		if (vertex != null)
			popUp.selectNewObject(vertex);
		else
			popUp.unselectVertex();
		super.selectVertex(vertex);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (checkModifiers(e)) {
			final VisualizationViewer<ExportVertexData, ExportEdgeData> vv = (VisualizationViewer<ExportVertexData, ExportEdgeData>) e
					.getSource();
			GraphElementAccessor<ExportVertexData, ExportEdgeData> pickSupport = vv
					.getPickSupport();
			if (pickSupport != null) {
				final ExportVertexData vertex = pickSupport.getVertex(vv
						.getGraphLayout(), e.getPoint().getX(), e.getPoint()
						.getY());
				
				
				ExportEdgeData edge = pickSupport.getEdge(vertexLocations, e.getPoint().getX(), e.getPoint().getY());

				if (vertex == null && edge != null) 
				{
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
					selectVertex((ExportVertexData)null);
			}
		}
	}

	/**
	 * If startVertex is non-null, and the mouse is released over an existing
	 * vertex, create an undirected edge from startVertex to the vertex under
	 * the mouse pointer. If shift was also pressed, create a directed edge
	 * instead.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (checkModifiers(e)) {
			final VisualizationViewer<ExportVertexData, ExportEdgeData> vv = (VisualizationViewer<ExportVertexData, ExportEdgeData>) e
					.getSource();
			final Point2D p = e.getPoint();
			GraphElementAccessor<ExportVertexData, ExportEdgeData> pickSupport = vv
					.getPickSupport();
			if (pickSupport != null) {
				final ExportVertexData vertex = pickSupport.getVertex(vv
						.getGraphLayout(), p.getX(),
						p.getY());
				if (vertex != null && startVertex != null) {
					if (vertex != startVertex) {
						if (vv.getGraphLayout().getGraph().isSuccessor(
								startVertex, vertex)) {

							ExportEdgeData edge = vv.getGraphLayout()
									.getGraph().findEdge(startVertex, vertex);
							selectEdge(edge);

						} else {
							DialogHandlerFrame
									.showErrorDialog(Messages.getString("ExportingMousePlugin.0")); //$NON-NLS-1$
						}
					} else {
						if (vertex != selectedVertex) {
							selectVertex(vertex);
						}
					}
				} else if (vertex == null && startVertex == null)
					selectVertex((ExportVertexData)null);
			}
			startVertex = null;
			down = null;
			edgeIsDirected = false;
			vv.removePostRenderPaintable(edgePaintable);
			vv.removePostRenderPaintable(arrowPaintable);
			vv.repaint();
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			if (modelView instanceof ExportView) {
				final ExportView view = (ExportView) modelView;
				JPopupMenu menu = JpegGenerator.getGenerationMenu(view);
				menu.addSeparator();
				JMenuItem previousStep = new JMenuItem(Messages.getString("ExportingMousePlugin.1")); //$NON-NLS-1$
				previousStep.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (view.getCurrentApparitionStep() > 1)
						{
							view.setCurrentApparitionStep(view.getCurrentApparitionStep() - 1);
							}
						else
							DialogHandlerFrame.showErrorDialog(Messages.getString("ExportingMousePlugin.2")); //$NON-NLS-1$
					}
				});
				
				JMenuItem nextStep = new JMenuItem(Messages.getString("ExportingMousePlugin.3")); //$NON-NLS-1$
				nextStep.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (view.getCurrentApparitionStep() < currentExport.getApparitionStepCount())
						{
							view.setCurrentApparitionStep(view.getCurrentApparitionStep() + 1);
							}
						else
							DialogHandlerFrame.showErrorDialog(Messages.getString("ExportingMousePlugin.4")); //$NON-NLS-1$
					}
					
				});
				menu.add(previousStep);
				menu.add(nextStep);
				
				menu.show(view, e.getX(), e.getY());
			}
		}
	}

	@Override
	public Brick getEditedAbstractSchema() {
		System.err
				.println("!!! Abstract schema indisponible dans un shemaExportingMousePlugin !!!"); //$NON-NLS-1$
		return null;
	}

	@Override
	public void removeSelectedVertex() {
		System.err
				.println("!!! Impossible de supprimer un sommet depuis un ShemaView !!!"); //$NON-NLS-1$
	}

	@Override
	public void selectVertex(Content content) {
		selectVertex(currentExport.getExportVertexDataByContent(content));
	}

	@Override
	public void notifyTree(Content content) {
		treeListener.setSelectedContent(content);

		
	}
	
	public void setExportView(ExportView theView)
	{
		popUp.setView(theView);
		super.setModelView(theView);
	}

}

// cacher/afficher
//
