package models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;


import client.export.ExportingMousePlugin;
import dataPack.DataPack;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.PopUpView;
import graphicalUserInterface.Program;

public class SchemaView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4352715056596473408L;

	private static final int NOMODE = -1; // mode non initialiser, tout est
											// affiché
	private static final int STARMODE = 0; // affiche seulement les acteur
											// voisin d'un acteur défini par
											// startTargetId
	private static final int CONFLICTMODE = 1; // affiche seulement les lieu ou
												// il y a conflit (+
												// possiblement l'étoile de
												// cette acteur
	private static final int ACTIONTIMEMODE = 2; // affiche seulement les
													// element actif a un
													// certain temps d'action
	private static final int FREEMODE = 3; // l'utilisateur est libre de faire
											// ce qu'il veut.

	private int mode;

	private Integer starTargetId; // id de l'element centre de l'etoile
	private Integer actionTime; // temps

	protected SparseGraph<SchemaVertex, BrickEdge> graph;
	protected VisualizationViewer<SchemaVertex, BrickEdge> vv;
	protected edu.uci.ics.jung.algorithms.layout.StaticLayout<SchemaVertex, BrickEdge> vertexLocations;

	protected TriadeEditingMousePlugin mousePlugin;
	protected final EditingModalGraphMouse<SchemaVertex, BrickEdge> graphMouse;
	protected PopUpView popUp;

	protected Schema shema;

	// protected ModelExport modelExport; // modification a effectuer sur les
	// element visible du shema

	@SuppressWarnings("unchecked")
	private SchemaView(TriadeEditingMousePlugin _mousePlugin, PopUpView pUV,
			final DataPack datapack) {
		mode = NOMODE;

		graph = new SparseGraph<SchemaVertex, BrickEdge>();
		popUp = pUV;
		vertexLocations = new StaticLayout<SchemaVertex, BrickEdge>(graph);

		vv = new VisualizationViewer<SchemaVertex, BrickEdge>(vertexLocations);

		PluggableRenderContext<SchemaVertex, BrickEdge> pr = (PluggableRenderContext<SchemaVertex, BrickEdge>) vv
				.getRenderer();
		// pr.setVertexLabelCentering(true);
		pr.setEdgeArrowTransformer(new DirectionalEdgeArrowTransformer(14, 20,
				7));
		pr.setVertexLabelRenderer(new VertexLabelRenderer() {

			boolean rotatedEdge = false;

			public <T> Component getVertexLabelRendererComponent(
					JComponent vv,
					Object value, Font font, boolean isSelected,
 T vertex) {

				JLabel result = new JLabel();
				result.setFont(font);
				result.setText((String) value);
				result.setBorder(BorderFactory.createEmptyBorder(55, 0, 0, 0));

				if (vertex instanceof SchemaVertex
						&& ((SchemaVertex) vertex).isSelected()) {

					result.setForeground(Color.blue);
				}

				return result;
			}


		});
		pr.setVertexFontTransformer(new Transformer<SchemaVertex, Font>() {

			@Override
			public Font transform(SchemaVertex v) {
				
					if (v.isSelected()) {
						return new Font("Helvetica", Font.ITALIC, 13);
					} else
						new Font("Helvetica", Font.PLAIN, 12);

			}

		});

		pr.setEdgeDrawPaintTransformer(new Transformer<BrickEdge, Paint>() {

			@Override
			public Paint transform(BrickEdge mE) {
				if (mE != null
						&& (mE.source.intValue() < 0 && mE.source.intValue() > -50)
						|| (mE.destination.intValue() < 0 && mE.destination
								.intValue() > -50))
					if (mE.isSelected())
						return Color.cyan;
					else
						return Color.blue;

				if (mE.isSelected())
					return Color.green;
				return Color.black;
			}
		});
		// pr.setEdgeFillPaintTransformer(new Transformer<BrickEdge, Paint>() {
		//
		// @Override
		// public Paint transform(BrickEdge mE) {
		// return pr.
		// }
		// Voir si il faut mettre quelque chose ici
		// });

		pr.setVertexIconTransformer(new Transformer<SchemaVertex, Icon>() {


			@Override
			public Icon transform(SchemaVertex e) {

				Integer id = e.getVertexId(); // <=  Attention ! il faut utiliser contentId
				boolean selected = e.isSelected();
				if (id != null) {
					int i = id.intValue();
					if (i <= -50) {
						return IconDatabase.vectorIconActivities
								.elementAt(datapack.getActivities()
										.getActivite(id).getIconId()
										+ (selected?1:0));
						// if (selected != null && selected.intValue() == 1)
						// return IconDatabase.iconActivitySelected;
						// return IconDatabase.iconActivity;
					}

					if ((i <= -10 && i > -50) || i == -1 || i == -2) {
						if (selected)
							return IconDatabase.iconArrowOutSelected;
						return IconDatabase.iconArrowOut;
					}

				}

				if (selected )
					return IconDatabase.iconHumanActorSelected;
				return IconDatabase.iconHumanActor;
			}
		});

		pr.setVertexDrawPaintTransformer(new Transformer<SchemaVertex, Paint>() {
			@Override
			public Paint transform(SchemaVertex v) {
				return Color.black;
			}
		
				});

			
pr
				.setVertexFillPaintTransformer(new Transformer<SchemaVertex, Paint>() {

					@Override
					public Paint transform(SchemaVertex v) {
						Integer id = v.getVertexId();
				if (id != null) {
					int i = id.intValue();
					if (i <= -50)
						return Color.yellow;

					if (i <= -10 && i > -50)
						return Color.blue;

					if (i == -1 || i == -2)
						return Color.cyan;
				}

						if (v.isSelected())
					return Color.white;

				return Color.red;
			}

		});

		pr.setVertexIncludePredicate(new Predicate<Context<Graph<SchemaVertex, BrickEdge>, SchemaVertex>>() {

			@Override
			public boolean evaluate(
					Context<Graph<SchemaVertex, BrickEdge>, SchemaVertex> arg0) {
				if (arg0 == null)
					return false;

				if (arg0.getClass() == DirectedSparseEdge.class) {
					DirectedSparseEdge edge = (DirectedSparseEdge) arg0;

					Integer idEdge = (Integer) edge.getUserDatum("id");

					if (idEdge == null)
						return false;
				return false;
			}

		});

		this.graphLayout = new StaticLayout(graph);
		graphLayout.initialize(new Dimension(600, 600), vertexLocations);


		vv.setBounds(new Rectangle(-300, -300, 600, 600));
		vv.setBackground(Color.white);
		vv.setPickSupport(new ShapePickSupport(50));
		pr.setVertexShapeFunction(new EllipseVertexShapeFunction(
				new ConstantVertexSizeFunction(60),
				new ConstantVertexAspectRatioFunction(1.0f)));
		pr.setEdgeShapeFunction(new EdgeShape.QuadCurve());
		pr.setVertexStringer(new VertexStringer() {

			public String getLabel(ArchetypeVertex v) {

				String result = (String) v.getUserDatum("nom");
				if (result == null) {
					return "Sommet non nommé";
				}
				return result;
			}
		});

		vv.setToolTipFunction(new ToolTipFunction() {

			public String getToolTipText(Vertex v) {

				BrickVertex bV = (BrickVertex) v
						.getUserDatum(BrickVertex.class);
				if (bV != null)
					return bV.getStringDescription();

				Integer id = (Integer) v.getUserDatum("id");
				if (id != null) {
					if (id.intValue() > -50)
						return "Sommet virtuel permettant de représenter\n des relations sortant de la brique";
					else {
						DataPack dataPack = (DataPack) graph
								.getUserDatum(DataPack.class);
						if (dataPack != null)
							return "Sommet représentant l'activité centrale de la brique : "
									+ dataPack.getStringById(id);
						else
							return "Sommet représentant l'activité centrale de la brique\n";

					}
				}
				return "Sommet étrange";
			}

			public String getToolTipText(Edge e) {
				Object obj = e.getUserDatum("nom");
				if (obj != null && obj.getClass() == String.class) {
					return (String) obj;
				}
				return "Arête";
			}

			@Override
			public String getToolTipText(MouseEvent event) {
				return ((JComponent) event.getSource()).getToolTipText();
			}
		});

		vv.addMouseListener(_mousePlugin);
		vv.addMouseMotionListener(_mousePlugin);
		setLayout(new BorderLayout());

		this.add(vv, BorderLayout.CENTER);
		mousePlugin = _mousePlugin;
		// mousePlugin.setModelView(this);
		graphMouse = new ModelsEditingModalGraphMouse(mousePlugin);

		// the EditingGraphMouse will pass mouse event coordinates to the
		// vertexLocations function to set the locations of the vertices as
		// they are created
		graphMouse.setVertexLocations(vertexLocations);
		vv.setGraphMouse(graphMouse);
		// graphMouse.add(new EditingPopupGraphMousePlugin(vertexLocations));

	}

	// public SchemaView(Schema schema, TreeListener _treeListener, PopUpView
	// pUV) {
	// this(new ExportingMousePlugin(_treeListener, schema), pUV,
	// Program.myMainFrame.getDataPack());
	//
	// this.shema = schema;
	//
	// // ajoute tout les element du temps d'action au shema.
	// // il sont tous visible et peuvent étre manipuler par la suite.
	//
	// Iterator<SchemaVertex> vertexIterator = shema.getVertices().iterator();
	//
	// while (vertexIterator.hasNext()) {
	// SchemaVertex curent = vertexIterator.next();
	//
	// Vertex curentVertex = new SimpleSparseVertex();
	// vertexLocations.setLocation(curentVertex, curent.getLocation());
	// curentVertex.addUserDatum("id", curent.getVertexId(),
	// new UserDataContainer.CopyAction.Clone());
	// curentVertex.addUserDatum("contentId", curent.getContent(),
	// new UserDataContainer.CopyAction.Clone());
	// curentVertex.addUserDatum("selection", new Integer(0),
	// new UserDataContainer.CopyAction.Clone());
	// curentVertex.addUserDatum(SchemaVertex.class, curent,
	// new UserDataContainer.CopyAction.Shared());
	//
	// String nom = null;
	// if (curent.getContent() <= -100)
	// // activite
	// nom = new String(schema.dataPack.getActivite(
	// curent.getContent()).toString());
	// else if (curent.getContent() >= 0)
	// // acteur
	// nom = new String(schema.dataPack.getActorsModel().getActorName(
	// curent.getContent()));
	// else if (curent.getContent() <= -50)
	// // moyen
	// nom = new String(shema.dataPack.getMoyen(curent.getContent())
	// .toString());
	// else
	// nom = new String("Contenue de l'indice bizarre");
	//
	// curentVertex.addUserDatum("nom", nom,
	// new UserDataContainer.CopyAction.Clone());
	//
	// curent.setAssociatedVertex(curentVertex);
	//
	// graph.addVertex(curentVertex);
	// }
	//
	// Iterator<BrickEdge> edgeIterator = shema.getEdges().iterator();
	//
	// while (edgeIterator.hasNext()) {
	// BrickEdge mEdge = edgeIterator.next();
	//
	// Vertex src = schema.getSchemaVertex(mEdge.source)
	// .getAssociatedVertex();
	// Vertex dst = schema.getSchemaVertex(mEdge.destination)
	// .getAssociatedVertex();
	//
	// DirectedSparseEdge edge = new DirectedSparseEdge(src, dst);
	//
	// mEdge.setAssociatedEdge(edge);
	//
	// edge.addUserDatum(BrickEdge.class, mEdge,
	// new UserDataContainer.CopyAction.Shared());
	// edge.addUserDatum("selection", new Integer(0),
	// new UserDataContainer.CopyAction.Clone());
	//
	// graph.addEdge(edge);
	// }
	//
	// mode = NOMODE;
	// }

	public void setStarMode(Integer idActor, Integer actionTime) {

		starTargetId = idActor;

		mode = STARMODE;
	}

	public TriadeEditingMousePlugin getMousePlugin() {
		return mousePlugin;
	}

	public PopUpView getPopUp() {
		return popUp;
	}

}
