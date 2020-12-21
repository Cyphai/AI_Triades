package models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import translation.Messages;

import dataPack.Content;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.util.ArrowFactory;

public abstract class TriadeEditingMousePlugin<V extends BrickVertex, E extends BrickEdge>
		extends
 EditingGraphMousePlugin<V, E>
		implements MouseListener, MouseMotionListener, GraphListener {

	protected Layout<V, E> vertexLocations;
	protected V startVertex;
	protected Point2D down;
	protected BrickView<V, E> modelView;
	
	protected boolean drawArrow;

	final protected QuadCurve2D rawEdge = new QuadCurve2D.Float(0,0,50,35,100,0);
	protected Shape edgeShape;
	protected Shape rawArrowShape;
	protected Shape arrowShape;
	protected Paintable edgePaintable;
	protected Paintable arrowPaintable;
	protected boolean edgeIsDirected;
	protected V mobile = null;

	protected V selectedVertex;
	protected E selectedEdge;
	protected Content activeContent;
	
	protected TreeListener treeListener;

	abstract public Brick getEditedAbstractSchema();

	abstract public void removeSelectedVertex();

	public TriadeEditingMousePlugin(Layout<V, E> vertexLocation) {
		this(MouseEvent.BUTTON1_MASK);
		
		if(vertexLocation == null) {
			throw new IllegalArgumentException("vertexLocation must be not null"); //$NON-NLS-1$
		}
		
		setVertexLocations(vertexLocation);
	}

	/**
	 * create instance and prepare shapes for visual effects
	 * 
	 * @param modifiers
	 */
	public TriadeEditingMousePlugin(int modifiers) {
		super(modifiers, null, null);
		rawArrowShape = ArrowFactory.getNotchedArrow(20, 16, 12);
		edgePaintable = new EdgePaintable();
		arrowPaintable = new ArrowPaintable();
		activeContent = null;
		selectedVertex = null;
//		edgeIsDirected = true;
		
		drawArrow = true;
	}

	public void selectEdge(E newSelection) {
		if (selectedVertex != null) {
			selectedVertex.setSelected(false);
			selectedVertex = null;
		}
		if (selectedEdge != null) {
			selectedEdge.setSelected(false);
			selectedEdge = null;
		}
		if (newSelection != null) {
			selectedEdge = newSelection;
			selectedEdge.setSelected(true);
		}
		
		getModelView().repaint();
	}

	public void selectVertex(V newSelection) {
		if (selectedVertex != null) {
			selectedVertex.setSelected(false);
			selectedVertex = null;
		}
		if (selectedEdge != null) {
			selectedEdge.setSelected(false);
			selectedEdge = null;
		}
		if (newSelection != null) {
			selectedVertex = newSelection;
			selectedVertex.setSelected(true);
		}
		
		getModelView().repaint();
		
		notifyTree(newSelection != null ? newSelection.getContent() : null);		
	}
	
	abstract public void selectVertex(Content content);
	abstract public void notifyTree(Content content);
	
	/**
	 * sets the vertex locations. Needed to place new vertices
	 * 
	 * @param vertexLocations
	 */
	public void setVertexLocations(
Layout<V, E> vertexLocations) {
		this.vertexLocations = vertexLocations;
	}

	public void setContent(Content newContent) {
		activeContent = newContent;
	}

	/**
	 * overrided to be more flexible, and pass events with key combinations. The
	 * default responds to both ButtonOne and ButtonOne+Shift
	 */
	@Override
	public boolean checkModifiers(MouseEvent e) {
		return (e.getModifiers() == modifiers);
	}

	/**
	 * If the mouse is pressed in an empty area, create a new vertex there. If
	 * the mouse is pressed on an existing vertex, prepare to create an edge
	 * from that vertex to another
	 */

	/**
	 * If startVertex is non-null, and the mouse is released over an existing
	 * vertex, create an directed edge from startVertex to the vertex under the
	 * mouse pointer with a transition.
	 */

	/**
	 * If startVertex is non-null, stretch an edge shape between startVertex and
	 * the mouse pointer to simulate edge creation
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void mouseDragged(MouseEvent e) {
		if (checkModifiers(e)) {
			VisualizationViewer<BrickVertex, BrickEdge> vv = (VisualizationViewer<BrickVertex, BrickEdge>) e
			.getSource();

			if (startVertex != null) {
				BrickVertex endVertex = vv.getPickSupport().getVertex(vv.getGraphLayout(), e.getPoint().getX(), e.getPoint().getY());

				Point2D endPoint = e.getPoint();
				if(endVertex != null)
					endPoint = vv.getRenderContext().getMultiLayerTransformer().transform(endVertex.getLocation());
				
			
				transformEdgeShape(down, endPoint);
				transformArrowShape(down, endPoint);
				
				if (down.distance(endPoint) < 20)
					drawArrow = false;
				else
					drawArrow = true;
			
			}
			
			vv.repaint();
		}
	}

	/**
	 * code lifted from PluggableRenderer to move an edge shape into an
	 * arbitrary position
	 */
	protected void transformEdgeShape(Point2D down, Point2D out) {
		float x1 = (float) down.getX();
		float y1 = (float) down.getY();
		float x2 = (float) out.getX();
		float y2 = (float) out.getY();

		AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);

		float dx = x2 - x1;
		float dy = y2 - y1;
		float thetaRadians = (float) Math.atan2(dy, dx);
		xform.rotate(thetaRadians);
		float dist = (float) Math.sqrt(dx * dx + dy * dy);
		xform.scale(dist / rawEdge.getBounds().getWidth(), 1.0);
		edgeShape = xform.createTransformedShape(rawEdge);
	}

	protected void transformArrowShape(Point2D down, Point2D out) {
		float x1 = (float) down.getX();
		float y1 = (float) down.getY();
		float x2 = (float) out.getX();
		float y2 = (float) out.getY();

		AffineTransform xform = AffineTransform.getTranslateInstance(x2, y2);

		float dx = x2 - x1;
		float dy = y2 - y1;
		float thetaRadians = (float) Math.atan2(dy, dx);
		xform.rotate(thetaRadians);
		arrowShape = xform.createTransformedShape(rawArrowShape);
	}

	/**
	 * Used for the edge creation visual effect during mouse drag
	 */
	class EdgePaintable implements Paintable {

		public void paint(Graphics g) {
			if (edgeShape != null && drawArrow) {
				Color oldColor = g.getColor();
				g.setColor(Color.BLACK);
				((Graphics2D) g).draw(edgeShape);
				g.setColor(oldColor);
			}
		}

		public boolean useTransform() {
			return false;
		}
	}

	/**
	 * Used for the directed edge creation visual effect during mouse drag
	 */
	class ArrowPaintable implements Paintable {

		public void paint(Graphics g) {
			if (arrowShape != null && drawArrow) {
				Color oldColor = g.getColor();
				g.setColor(Color.BLACK);
				((Graphics2D) g).fill(arrowShape);
				g.setColor(oldColor);
			}
		}

		public boolean useTransform() {
			return false;
		}
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

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public BrickView<V, E> getModelView() {
		return modelView;
	}

	public void setModelView(BrickView<V, E> modelView) {
		this.modelView = modelView;
	}

	public void setTreeListener(TreeListener treeListener) {
		this.treeListener = treeListener;
	}
}
