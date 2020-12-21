//.setBackground(new Color(169,211,247));

package models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.JeuRelation;
import main.RelationComplete;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import translation.Messages;
import client.SchemaEditingMousePlugin;
import dataPack.Acteur;
import dataPack.Activite;
import dataPack.Content;
import dataPack.DataPack;
import dataPack.Moyen;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.annotations.Annotation;
import edu.uci.ics.jung.visualization.annotations.AnnotationManager;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.QuadCurve;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Positioner;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.PopUpView;
import graphicalUserInterface.Program;

public class BrickView<V extends BrickVertex, E extends BrickEdge> extends
JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5832896318413480189L;



	protected Brick brick;
	protected DirectedSparseGraph<V, E> graph;
	protected VisualizationViewer<V, E> vv;
	protected Layout<V, E> vertexLocations;
	protected GraphZoomScrollPane panel;
	protected TriadeEditingMousePlugin<V, E> mousePlugin;
	protected EditingModalGraphMouse<V, E> graphMouse;
	protected PopUpView popUp;
	protected TreeListener treeListener;
	protected DataPack datapack;
	protected Annotation<String> title;
//	protected int titleLength;
	protected AnnotationManager aM;
	protected int vertexShownLevel = 1;
	protected PluggableRenderContext<V, E> pr;
	
	protected Point2D center;
	protected int radius; //Used only for navigationBrick
	


	boolean rotatedEdge = false;

	public BrickView(PopUpView pUV, final DataPack datapack,
			TreeListener treeListener) {
		popUp = pUV;
		this.treeListener = treeListener;
		this.datapack = datapack;
		graph = new DirectedSparseGraph<V, E>();

	}

	@SuppressWarnings("unchecked")
	public BrickView(Brick brick, PopUpView pUV, final DataPack datapack,
			TreeListener treeListener) {
		this(pUV, datapack, treeListener);


		this.brick = brick;



		vertexLocations = new StaticLayout<V, E>(graph);
		vertexLocations.setSize(new Dimension(600, 600));





		if (brick.isNavigationBrick()) {
			mousePlugin = new NavigationMousePlugin<V, E>(treeListener, brick, (Layout<BrickVertex, BrickEdge>) vertexLocations);
		} else if (Program.isTriades()) {
			mousePlugin = (TriadeEditingMousePlugin<V, E>) new SchemaEditingMousePlugin(
					treeListener, brick, (Layout<BrickVertex, BrickEdge>) vertexLocations);
		} else {
			mousePlugin = (TriadeEditingMousePlugin<V, E>) new BrickEditingMousePlugin(
					brick, treeListener, (Layout<BrickVertex, BrickEdge>) vertexLocations);
		}

		buildComponent();

		if (brick.isNavigationBrick()) {
			organizeLayout();
		}

		buildGraph();
	}



	protected void buildComponent()
	{
		final BrickView<V, E> access = this;

		vv = new VisualizationViewer<V, E>(vertexLocations);

		pr = (PluggableRenderContext<V, E>) vv.getRenderContext();
		pr.setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<V, E>(
				14, 20, 7));

		pr.setVertexLabelRenderer(new VertexLabelRenderer() {

			@Override
			public <T> Component getVertexLabelRendererComponent(JComponent vv,
					Object value, Font font, boolean isSelected, T vertex) {
				return access.getVertexLabelRendererComponent(vv, value, font, isSelected, (V)vertex);
			}
		});

		pr.setVertexFontTransformer(new Transformer<V, Font>() {
			@Override
			public Font transform(V vertex) {
				return access.getVertexFont(vertex);
			}
		});

		pr.setVertexIconTransformer(new Transformer<V, Icon>() {
			@Override
			public Icon transform(V vertex) {
				return access.getVertexIcon(vertex);
			}

		});

		pr.setVertexLabelTransformer(new Transformer<V, String>() {

			@Override
			public String transform(V vertex) {
				return access.getVertexLabel(vertex);
			}
		});

		vv.setVertexToolTipTransformer(new Transformer<V, String>() {

			@Override
			public String transform(V vertex) {
				return access.getVertexToolTipText(vertex);
			}
		});

		pr.setEdgeDrawPaintTransformer(new Transformer<E, Paint>() {

			@Override
			public Paint transform(E edge) {
				return access.getEdgeDrawPaint(edge);
			}
		});

		vv.setEdgeToolTipTransformer(new Transformer<E, String>() {

			@Override
			public String transform(E e) {
				return access.getEdgeToolTipLabel(e);
			}
		});

		pr.setEdgeIncludePredicate(new Predicate<Context<Graph<V,E>,E>>() {

			@Override
			public boolean evaluate(
					Context<Graph<V,E>,E> arg0) {
				return access.getEdgeIncludePredicate(arg0);

			}
		});

		pr.setVertexIncludePredicate(new Predicate<Context<Graph<V,E>,V>>() {

			@Override
			public boolean evaluate(
					Context<Graph<V,E>,V> arg0) {
				return access.getVertexIncludePredicate(arg0);

			}
		});

		pr.setEdgeLabelTransformer(new Transformer<E, String>() {
			BrickView<V, E> brickView = access;

			@Override
			public String transform(E e) {
				return brickView.getEdgeLabel(e);
			}
		});
		pr.setEdgeFontTransformer(new Transformer<E, Font>() {
			BrickView<V, E> brickView = access;

			@Override
			public Font transform(E e) {
				return brickView.getEdgeFont(e);
			}
		});

		pr.setEdgeLabelRenderer(new EdgeLabelRenderer() {
			BrickView<V, E> brickView = access;

			@Override
			public void setRotateEdgeLabels(boolean state) {
			}

			@Override
			public boolean isRotateEdgeLabels() {
				return true;
			}

			@Override
			public <T> Component getEdgeLabelRendererComponent(JComponent vv,
					Object value, Font font, boolean isSelected, T edge) {
				return brickView.getEdgeLabelRendererComponent(vv, value, font, isSelected, (E)edge);
			}
		});


		if (!brick.isNavigationBrick())
			pr.setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<V, E>(20, 16, 12));
		else
			pr.setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer<V, E>(0, 0, 0));



		vv.setBounds(new Rectangle(-300, -300, 600, 600));
		vv.setBackground(Color.white);
		vv.setPickSupport(new ShapePickSupport<V, E>(vv));

		QuadCurve<V, E> shape = new QuadCurve<V, E>();
		shape.setControlOffsetIncrement(45);		
		pr.setEdgeShapeTransformer(shape);

		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);

		vv.getRenderer().getVertexLabelRenderer().setPositioner(new Positioner() {

			@Override
			public Position getPosition(float x, float y, Dimension d) {
				double dX = - center.getX() + x ;
				double dY = center.getY() - y ;
				if (dX == 0)
				{
					if (dY >= 0)
						return Position.N;
					else
						return Position.S;
				}
				if (dX >= 0)
				{
					double ratio = dY/dX;
					if (ratio < Math.tan(-Math.PI/3))
						return Position.S;
					else if (ratio < Math.tan(-Math.PI/6))
						return Position.SE;
					else if (ratio < Math.tan(Math.PI/6))
						return Position.E;
					else if (ratio < Math.tan(Math.PI/3))
						return Position.NE;
					else
						return Position.N;
				}
				else
				{
					double ratio = dY/dX;
					if (ratio < Math.tan(-Math.PI/3))
						return Position.N;
					else if (ratio < Math.tan(-Math.PI/6))
						return Position.NW;
					else if (ratio < Math.tan(Math.PI/6))
						return Position.W;
					else if (ratio < Math.tan(Math.PI/3))
						return Position.SW;
					else
						return Position.S;
				}


			}
		});

		pr.setVertexShapeTransformer(new EllipseVertexShapeTransformer<V>(new Transformer<V, Integer>() {

			@Override
			public Integer transform(V arg0) {
				return 60;
			}
		},
		new Transformer<V, Float>() {

			@Override
			public Float transform(V arg0) {
				return 1.f;
			}
		}));

		vv.addMouseListener(mousePlugin);
		vv.addMouseMotionListener(mousePlugin);

		setLayout(new BorderLayout());

		this.add(vv, BorderLayout.CENTER);

		if (Program.isTriades() && !brick.isNavigationBrick() && this.getClass() == BrickView.class)
		{
			vertexShownLevel = datapack.getCurrentSession().getBrickShownedLevel(brick).intValue();
			final JButton more = new JButton(Messages.getString("BrickView.0")); //$NON-NLS-1$
			final JButton less = new JButton(Messages.getString("BrickView.1")); //$NON-NLS-1$

			if (vertexShownLevel == 1)
			{
				less.setEnabled(false);
			}
			if (vertexShownLevel == 3)
			{
				more.setEnabled(false);
				less.setText(Messages.getString("BrickView.4")); //$NON-NLS-1$
			}

			more.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (vertexShownLevel == 1)
					{
						more.setText(Messages.getString("BrickView.2")); //$NON-NLS-1$
						less.setText(Messages.getString("BrickView.3")); //$NON-NLS-1$
						less.setEnabled(true);
						vertexShownLevel++;
						datapack.getCurrentSession().setBrickShownedLevel(brick, new Integer(vertexShownLevel));
						access.repaint();
					}
					else if (vertexShownLevel == 2)
					{
						more.setEnabled(false);
						less.setText(Messages.getString("BrickView.4")); //$NON-NLS-1$
						vertexShownLevel++;
						datapack.getCurrentSession().setBrickShownedLevel(brick, new Integer(vertexShownLevel));
						access.repaint();
					}
				}
			});
			less.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (vertexShownLevel == 2)
					{
						more.setText(Messages.getString("BrickView.5")); //$NON-NLS-1$
						less.setEnabled(false);
						vertexShownLevel--;
						access.repaint();
					}
					else if (vertexShownLevel == 3)
					{
						more.setText(Messages.getString("BrickView.6")); //$NON-NLS-1$
						more.setEnabled(true);
						less.setText(Messages.getString("BrickView.8")); //$NON-NLS-1$
						vertexShownLevel--;
						access.repaint();
					}
				}
			});
			JPanel shownLevelButtons = new JPanel();
			shownLevelButtons.setLayout(new BoxLayout(shownLevelButtons, BoxLayout.X_AXIS));
			shownLevelButtons.add(less);
			shownLevelButtons.add(Box.createGlue());
			shownLevelButtons.add(more);
			this.add(shownLevelButtons, BorderLayout.SOUTH);

		}
		else
		{
			vertexShownLevel = 4;
		}

		mousePlugin.setModelView(this);
		graphMouse = new ModelsEditingModalGraphMouse<V, E>(mousePlugin, pr);

		vv.setGraphMouse(graphMouse);
	/*	if (!brick.isNavigationBrick())
			vv.getRenderContext().getMultiLayerTransformer().getTransformer(
					Layer.VIEW).setTranslate(300, 300);
		else {

			vv.getRenderContext().getMultiLayerTransformer().getTransformer(
					Layer.VIEW).setTranslate(150, 150);
		}*/

		aM = new AnnotationManager(vv.getRenderContext());
		Point2D point = new Point2D.Double(30, 30);
		point = vv.getRenderContext().getMultiLayerTransformer()
				.inverseTransform(point);
		title = new Annotation<String>("", Annotation.Layer.LOWER, Color.black, false, point); //$NON-NLS-1$
		
		modifyTitle(brick.toString() + "\n("+brick.getStep()+")"); //$NON-NLS-1$ //$NON-NLS-2$
		aM.add(Annotation.Layer.UPPER, title);

		vv.addPreRenderPaintable(aM
				.getAnnotationPaintable(Annotation.Layer.LOWER));
		vv.addPostRenderPaintable(aM
				.getAnnotationPaintable(Annotation.Layer.UPPER));

		vv.repaint();

		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				translateAndZoom();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				translateAndZoom();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		

	}

	protected Font getEdgeFont(E e) {
		return new Font("Helvetica", Font.PLAIN, 10); //$NON-NLS-1$
	}

	protected Component getEdgeLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, E edge) {

		JLabel result = new JLabel();
		result.setFont(font);
		result.setText(value.toString());

		return result;
	}

	protected String getEdgeLabel(E e) {

		if(brick.isNavigationBrick()) {
			return ""; //$NON-NLS-1$
		} else {
			if (Program.isTriades())
				return prepareEdgeLabel(e.getCompleteRelation().getEnsembleRelation(), datapack.getProgramSettings().getDefaultEdgeLabel());
			else
				return prepareEdgeLabel(e.getCompleteRelation().getEnsembleRelation(), 0);
		}

	}


	protected String prepareEdgeLabel(Vector<JeuRelation> rels, int labelType)
	{
		String result = ""; //$NON-NLS-1$

		if (labelType < 2)
		{
			for(JeuRelation relation : rels) {
				{
					if(relation.objectifReel.toString().length() <= 4) {
						System.out.println("Objectif Réel : \"" + relation.objectifReel.toString() + "\"");
					}

					if(relation.objectifStructurel.toString().length() <= 4) {
						System.out.println("Objectif structurel : \"" + relation.objectifStructurel.toString() + "\"");
					}

					if (labelType == 0) {
//						result += relation.objectifStructurel.toString().substring(0,4)+". "; //$NON-NLS-1$
						result += (relation.objectifStructurel.toString().length() >4 ? (relation.objectifStructurel.toString().substring(0,4)+".") : ("#" + relation.objectifStructurel.toString() + "#")) + " "; //$NON-NLS-1$
					} else if (labelType == 1) {
//						result += relation.objectifReel.toString().substring(0, 4)+". "; //$NON-NLS-1$
						result += (relation.objectifReel.toString().length() > 4 ? (relation.objectifReel.toString().substring(0, 4)+".") : relation.objectifReel.toString()) + " "; //$NON-NLS-1$
					}
				}
			}
		}
		else
		{
			labelType -= 2;
			JeuRelation relation = rels.get(labelType);
			result += relation.objectifStructurel.toString().substring(0,6)+". - "+relation.objectifReel.toString().substring(0, 6)+"."; //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		
		
		return result;
		
		
	}

	protected String getEdgeToolTipLabel(E e) {
		return  e.getStringDescription();
	}

	protected void buildGraph()
	{
		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			vertexLocations.lock(iterator.next(), true);

		}



		brick.graph = (DirectedSparseGraph<BrickVertex, BrickEdge>) graph;
		for (BrickVertex brickVertex : brick.vertices) {
			graph.addVertex((V) brickVertex);
			brickVertex.setSelected(false);
			// if (!brick.isNavigationBrick())
			vertexLocations.setLocation((V) brickVertex, brickVertex
					.getLocation());
		}

		Vector<BrickEdge> toRemove = new Vector<BrickEdge>();
		for (BrickEdge mEdge : brick.edges) {
			BrickVertex src = mEdge.source;
			BrickVertex dest = mEdge.destination;
			mEdge.setSelected(false);

			try {
				graph.addEdge((E) mEdge, (V) src, (V) dest);

			} catch (IllegalArgumentException e) {
				DialogHandlerFrame
				.showErrorDialog(Messages.getString("BrickView.7")); //$NON-NLS-1$

				toRemove.add(mEdge);
			}
		}
		for (BrickEdge edge : toRemove) {
			brick.edges.remove(edge);
		}

		vertexLocations.initialize();
		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			vertexLocations.lock(iterator.next(), false);
		}
		computeCenter();

	}

	@Override
	public void repaint() {
		if (vv != null && title != null) {
			Point2D point = new Point2D.Double(30, 30);
			point = vv.getRenderContext().getMultiLayerTransformer()
					.inverseTransform(point);

			title.setLocation(point);
		}
		validate();
		super.repaint();
	}






	public TriadeEditingMousePlugin<V, E> getMousePlugin() {
		return mousePlugin;
	}

	public PopUpView getPopUp() {
		return popUp;
	}

	public DirectedSparseGraph<V, E> getGraph() {
		return graph;
	}

	public Component getVertexLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, V vertex) {
		JLabel result = new JLabel();
		result.setFont(font);
		result.setText(value.toString());
		result.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray), BorderFactory.createEmptyBorder(0, 2, 0, 2)));
		if (vertex.isSelected()) {
			result.setForeground(Color.blue);
		}

		if(brick.isNavigationBrick()) {
			switch (((Brick)vertex.content).getState()) {
			case EMPTY:
			case UNCOMPLETED:
				result.setForeground(Color.blue);
				break;
			case CORRECT:
				result.setForeground(new Color(100, 205, 10));
				break;
			case GAPPED:
				result.setForeground(new Color(196,111,0));
				break;
			}
		}
		return result;
	}

	protected Font getVertexFont(V v) {
		if (v.isSelected()) {
			return new Font("Helvetica", Font.ITALIC, 13); //$NON-NLS-1$
		} else {
			new Font("Helvetica", Font.PLAIN, 12); //$NON-NLS-1$
		}
		return new Font("Helvetica", Font.PLAIN, 10); //$NON-NLS-1$
	}

	public Paint getEdgeDrawPaint(E mE) {
		if (mE != null) {
			if (mE.isSelected())
				return Color.CYAN;


			if (Program.isTriades())
			{
				int state = mE.getCompleteRelation().getState();
				switch (state) {
				case RelationComplete.RELATION_OK:
					return new Color(30, 245, 30);
				case RelationComplete.RELATION_INCOMPLETE:
					return Color.BLUE;
				case RelationComplete.RELATION_ECART_OBJECTIF:
					return new Color(196, 111,0);
				case RelationComplete.RELATION_ECART_MOYEN:
					return new Color(210, 10, 200);
				default:
					return Color.BLACK;
				}
			}
			else
			{
				int state = mE.getCompleteRelation().getState();
				switch (state){
				case RelationComplete.RELATION_OK:
					return Color.BLUE;
				case RelationComplete.RELATION_INCOMPLETE :
					return Color.RED;
				default:
					return Color.black;

				}
			}
		}

		return Color.black;
	}

	public boolean getEdgeIncludePredicate(Context<Graph<V, E>, E> arg0) 
	{
		if (arg0.element instanceof BrickEdge)
		{
			return (!((BrickEdge)arg0.element).getCompleteRelation().isNoRelation()) || (((BrickEdge)arg0.element).isSelected());

		}
		return true;
	}

	public boolean getVertexIncludePredicate(Context<Graph<V, E>, V> arg0) {
		if (!Program.isTriades() || brick.isNavigationBrick() || arg0.element.getClass() != BrickVertex.class)
			return true;
		else if (!(arg0.element.getContent() instanceof Acteur))
			return true;
		else if (arg0.element.getRank() != null && arg0.element.getRank().getValue() <= vertexShownLevel)		
			return true;
		else if (vertexShownLevel >= 3)
			return true;
		else
			return false;
	}

	public Icon getVertexIcon(V v) {

		Content content = v.getContent();
		boolean selected = v.isSelected();
		if (content instanceof Activite) {
			return IconDatabase.vectorIconActivities
					.elementAt(((Activite)content).getIconId() + (selected?1:0));
		}
		if (content instanceof Moyen)
			return IconDatabase.vectorIconMoyens.elementAt(((Moyen)content).getIdGenerique().intValue()
					* 2 + (selected?1:0));
		if (content instanceof Acteur) {
			if(v.isSelected())
				return IconDatabase.vectorIconActorsBig.firstElement();

			return ((Acteur)content).getIcon(true);
		}
		if (content instanceof Brick) {
			if(brick.isNavigationBrick()) {
				switch (brick.getState()) {
				case EMPTY:
				case UNCOMPLETED:
					return IconDatabase.vectorIconActivitiesNoCompleted.elementAt(((Brick)content).getActivity().getIconId());
				case CORRECT:
					return IconDatabase.vectorIconActivitiesValide.elementAt(((Brick)content).getActivity().getIconId());
				case GAPPED:
					return IconDatabase.vectorIconActivitiesUnvalide.elementAt(((Brick)content).getActivity().getIconId());
				}
			} else {
				return IconDatabase.vectorIconActivities.elementAt(((Brick)content).getActivity().getIconId());
			}
		}

		return IconDatabase.iconRemoveActor0;
	}

	public Paint getVertexDrawPaint(V v) {
		return Color.black;
	}

	public String getVertexLabel(V v) {
		String result = v.getContent().toString();
		if (result == null) {
			return Messages.getString("BrickView.11"); //$NON-NLS-1$
		}
		return result;
	}

	public String getVertexToolTipText(V bV) {
		return bV.getStringDescription();

	}

	public String getEdgeToolTipText(E e) {
		return e.getStringDescription();
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		return ((JComponent) event.getSource()).getToolTipText();
	}

	public TreeListener getTreeListener() {
		return treeListener;
	}

	public void setTreeListener(TreeListener treeListener) {
		this.treeListener = treeListener;
		mousePlugin.setTreeListener(treeListener);
	}


	protected void organizeLayout()
	{
		double oX = 350;
		double oY = 300;
		if (!brick.isNavigationBrick())
		{
			System.err.println("BrickView.organizeLayout() must be call only on a navigationBrick."); //$NON-NLS-1$
			return;
		}

		ArrayList<BrickVertex> primaryVertices = new ArrayList<BrickVertex>();
		ArrayList<BrickVertex> secondaryVertices = new ArrayList<BrickVertex>();
		ArrayList<BrickVertex> remainingVertices = new ArrayList<BrickVertex>();
		for (BrickVertex bV : brick.getVertices())
		{
			switch (bV.getRank())
			{
			case primary :
				primaryVertices.add(bV);
				break;
			case secondary :
				secondaryVertices.add(bV);
				break;
			case remaining :
				remainingVertices.add(bV);
				break;
			default :
				System.out.println("Vertex with no rank in a navigationBrick (BrickView.organizeLayout)."); //$NON-NLS-1$
				System.out.println("Vertex info : "+bV.getStringDescription()); //$NON-NLS-1$
				remainingVertices.add(bV);
			}
		}

		radius = 30 * (primaryVertices.size());
		if (radius <= 50)
			radius = 0;
		else
		{
			Ellipse2D shape = new Ellipse2D.Double(oX-radius, oY-radius, 2*radius, 2*radius);
			Annotation.Layer layer = Annotation.Layer.LOWER;
			Annotation<Shape> annotation =
					new Annotation<Shape>(shape, layer, Color.GRAY, false, new Point2D.Double(oX+radius, oY+radius));
			aM.add(layer, annotation);
		}
		int count = 0;

		double delta = Math.PI*2;
		if (primaryVertices.size() > 0)
			delta /= primaryVertices.size();
		for (BrickVertex bV : primaryVertices)
		{
			double x = oX+radius*Math.cos(0.1+delta * count);
			double y = oY+radius*Math.sin(0.1+delta * count);
			bV.setLocation(new Point2D.Double(x, y));
			count ++;

		}
		count = 0;
		radius += 100;
		delta = Math.PI*2;
		{
			Ellipse2D shape = new Ellipse2D.Double(oX-radius, oY-radius, 2*radius, 2*radius);
			Annotation.Layer layer = Annotation.Layer.LOWER;
			Annotation<Shape> annotation =
					new Annotation<Shape>(shape, layer, Color.GRAY, false, new Point2D.Double(oX+radius, oY+radius));
			aM.add(layer, annotation);
		}
		if (secondaryVertices.size() > 0)
			delta /= secondaryVertices.size();
		for (BrickVertex bV : secondaryVertices)
		{
			double x = oX + radius*Math.cos(-0.3+delta * count);
			double y = oY + radius*Math.sin(-0.3+delta * count);
			bV.setLocation(new Point2D.Double(x, y));
			count ++;

		}

		count = 0;
		radius += 100;
		delta = Math.PI*2;

		{
			Ellipse2D shape = new Ellipse2D.Double(oX-radius, oY-radius, 2*radius, 2*radius);
			Annotation.Layer layer = Annotation.Layer.LOWER;
			Annotation<Shape> annotation =
					new Annotation<Shape>(shape, layer, Color.GRAY, false, new Point2D.Double(oX+radius, oY+radius));
			aM.add(layer, annotation);
		}

		if (remainingVertices.size() > 0)
			delta /= remainingVertices.size();

		for (BrickVertex bV : remainingVertices)
		{
			double x = oX + radius*Math.cos(0.3+delta * count);
			double y = oY + radius*Math.sin(0.3+delta * count);
			bV.setLocation(new Point2D.Double(x, y));
			count ++;

		}
	}

	public void modifyTitle(String newTitle)
	{
		int titleLength = 0;
		if (newTitle.length() == 0)
		{
			title.setAnnotation("");
			titleLength = 0;
			return;
		}
		String[] splittedString = newTitle.split("\n"); //$NON-NLS-1$
		String newAnnotation = ""; //$NON-NLS-1$
		titleLength = 0;
		newAnnotation += "<html><TABLE border=1><TR><TD><center><font size=4>"; //$NON-NLS-1$
		boolean first = true;
		for (String s : splittedString)
		{
			if (first)
				first = false;
			else
				newAnnotation += "<br>"; //$NON-NLS-1$
			newAnnotation += s;
			if (titleLength < s.length())
				titleLength = s.length();
		}
		newAnnotation += "</center></font></TD></TR></TABLE></html>"; //$NON-NLS-1$
		title.setAnnotation(newAnnotation);
		titleLength *= 6;
	}

	protected void computeCenter()
	{
		center = new Point2D.Double();
		for (BrickVertex bV : brick.getVertices())
		{
			center.setLocation(center.getX() + bV.getLocation().getX(), center.getY()+bV.getLocation().getY());
		}
		int size = brick.getVertices().size();
		center.setLocation(center.getX()/size, center.getY()/size);
	}

	public void translateAndZoom()
	{
		int width = vv.getWidth();
		int height = vv.getHeight();
		MutableTransformer transformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		Layout<V, E> layout = vv.getGraphLayout();
		
		transformer.setToIdentity();
		//		Point2D center = graph.getCenter();
		//	Point2D scale = graph.getScale();

		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		double x = minX, y = minY;
		boolean first = true;

		if (!brick.isNavigationBrick())
		{
		
		for (V i : vv.getGraphLayout().getGraph().getVertices())
		{
			Point2D p;
			p = transformer.getTransform().transform(layout.transform(i), null);
			if (first)
			{
				first = false;
				minX = p.getX();
				maxX = p.getX();
				minY = p.getY();
				maxY = p.getY();
				x = minX;
				y = minY;

			}


			if (minX > p.getX())
				minX = p.getX();
			if (maxX < p.getX())
				maxX = p.getX();
			if (minY > p.getY())
				minY = p.getY();
			if (maxY < p.getY())
				maxY = p.getY();
		}
		if (graph.getVertexCount() <= 1)
		{
			minX -= 150;
			minY -= 150;
			maxX += 150;
			maxY += 150;
		}
		
		minY -= (40+height/10);
		maxY += height /20;
		maxX += 20 +width / 15;
		minX -= (20+width / 15);
		
		x = maxX + minX;
		x /= 2;
		y = maxY + minY;
		y /= 2;
		}
		else
		{
			x = width / 5;
			y = height / 4; //cf organizeLayout function, constant defined as oX and oY
			minX = x -radius - width/15;
			minY = y -radius - height/10;
			maxX = x + radius + width/15;
			maxY = y + radius + height / 20;
		}


		if (maxX != minX && maxY != minY)
		{

			double scaleX = ((double)width)/(maxX - minX);
			double scaleY = ((double)height)/(maxY - minY);
			double scale = scaleX < scaleY ? scaleX : scaleY;
			scale *= 0.95;
			transformer.translate(width/2-x, height / 2 - y);
			if (width > 0 && height > 0)
				transformer.scale(scale, scale, new Point2D.Double(width /2, height/2));
			else
				System.err.println("No scale, height = "+height+" and width = "+width);


		}
		repaint();
	}

	
	
	@SuppressWarnings("unchecked")
	public E getReturnRelation(E modelEdge) {
		return graph.findEdge((V)(modelEdge.getDestination()), (V)(modelEdge.getSource()));
	}

}
