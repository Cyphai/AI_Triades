package client.export;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.BrickView;

import org.apache.commons.collections15.Predicate;

import translation.Messages;
import dataPack.TreeListener;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class ExportView extends BrickView<ExportVertexData, ExportEdgeData> {
	private static final long serialVersionUID = -7652301569114706302L;

	protected ExportModel exportModel;
	protected int currentApparitionStep;
	protected JLabel level;
	protected String shownLevelLabelPrefix;
	
	public ExportView(ExportModel exportModel, TreeListener treeListener,
			ExportPopUp pUV) {
		super(pUV, Program.myMainFrame.datapack, treeListener);
		
		brick = exportModel.getBaseSchema();
		currentApparitionStep = exportModel.apparitionStepCount;

		KKLayout<ExportVertexData, ExportEdgeData> kkLayout = null;
		if(exportModel.getBaseSchema().isNavigationBrick()) {
			kkLayout = new KKLayout<ExportVertexData, ExportEdgeData>(graph);
			kkLayout.setLengthFactor(1);
			vertexLocations = kkLayout;
		} else {
			vertexLocations = new StaticLayout<ExportVertexData, ExportEdgeData>(graph);
		}
		
		mousePlugin = new ExportingMousePlugin(treeListener, exportModel, pUV, vertexLocations);
		
		this.exportModel = exportModel;
				
		buildComponent();
		
		buildGraph(exportModel);

		if(kkLayout != null) {
			kkLayout.initialize();
			kkLayout.adjustForGravity();
		}
		
		}

	
	
	public VisualizationViewer<ExportVertexData, ExportEdgeData> getVisualizationViewer() {
		return vv;
	}

	@Override
	protected void buildComponent() {
		super.buildComponent();
		final ExportView access = this;
		//ajouter les predicate
		pr.setVertexIncludePredicate(new Predicate<Context<Graph<ExportVertexData,ExportEdgeData>,ExportVertexData>>() {
			
			@Override
			public boolean evaluate(Context<Graph<ExportVertexData, ExportEdgeData>, ExportVertexData> arg0) {
					return access.getVertexIncludePredicate(arg0);
			}
		});
		
		pr.setEdgeIncludePredicate(new Predicate<Context<Graph<ExportVertexData,ExportEdgeData>,ExportEdgeData>>() {

			@Override
			public boolean evaluate(
					Context<Graph<ExportVertexData, ExportEdgeData>, ExportEdgeData> arg0) {
				return access.getEdgeIncludePredicate(arg0);
				
			}
		});
		
		final JButton more = new JButton(Messages.getString("ExportView.0"));  //$NON-NLS-1$
		final JButton less = new JButton(Messages.getString("ExportView.1")); //$NON-NLS-1$
		shownLevelLabelPrefix = Messages.getString("ExportView.2"); //$NON-NLS-1$
		level = new JLabel(shownLevelLabelPrefix+currentApparitionStep);
		
		more.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentApparitionStep < exportModel.getApparitionStepCount())
				{
				
					currentApparitionStep++;
					level.setText(shownLevelLabelPrefix+currentApparitionStep);
					access.repaint();
				}
				else
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("ExportPopUp.18")); //$NON-NLS-1$
				}
			}
		});
		less.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentApparitionStep > 1)
				{
					currentApparitionStep--;
					level.setText(shownLevelLabelPrefix+currentApparitionStep);
				
					access.repaint();
				}
				else
				{

					DialogHandlerFrame.showErrorDialog(Messages.getString("ExportPopUp.16")); //$NON-NLS-1$
				}
				
			}
		});
		JPanel shownLevelButtons = new JPanel();
		shownLevelButtons.setLayout(new BoxLayout(shownLevelButtons, BoxLayout.X_AXIS));
		shownLevelButtons.add(less);
		shownLevelButtons.add(Box.createGlue());
		shownLevelButtons.add(level);
		shownLevelButtons.add(Box.createGlue());
		shownLevelButtons.add(more);
		this.add(shownLevelButtons, BorderLayout.SOUTH);
		
		modifyTitle(exportModel.getExportInformations().getTitle());
	}
	
	protected void buildGraph(ExportModel exportModel) {

		Layout<ExportVertexData, ExportEdgeData> layout = vv.getGraphLayout();
		boolean restartModel = false;
		
		/* ajoute les donneés d'export des sommets */
		for (ExportVertexData vertexData : exportModel.getVertexData()) {
			graph.addVertex(vertexData);
			vertexData.setSelected(false);

				restartModel = true;
				
			if(exportModel.getBaseSchema().isNavigationBrick() == false) {
				layout.lock(vertexData, true);

				/* place les sommets correctement suivant les valeurs de l'export */

				vertexLocations.setLocation(vertexData, vertexData.getLocation());

				layout.lock(vertexData, false);
			}
		}

		if(restartModel) {
			vv.repaint();
		}

		/* ajoute les données d'export des arête */
		for (ExportEdgeData edgeData : exportModel.getEdgeData()) {
			edgeData.setSelected(false);
			graph.addEdge(edgeData, (ExportVertexData) edgeData.getSource(),
					(ExportVertexData) edgeData.getDestination());
			
		}
		
		computeCenter();
	}
	
	
	

	/*******************/
	/** Vertex export **/
	/*******************/
	/* Icon du sommet */
	@Override
	public Icon getVertexIcon(ExportVertexData vertexData) {
		Icon icon = null;
		if(vertexData == null || (icon = vertexData.getIcon()) == null) {
			return super.getVertexIcon(vertexData);
		}

		return icon;
	}

	/* Texte du sommet */
	@Override
	public String getVertexLabel(ExportVertexData vertexData) {
		String label;
		if(vertexData != null && (label = vertexData.getExportData().getText()) !=null && label.compareTo("") != 0) //$NON-NLS-1$
			return label;
		else 
			return super.getVertexLabel(vertexData);
	}
	
	@Override
	public Component getVertexLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, ExportVertexData vertex) {
		JLabel label = (JLabel)super.getVertexLabelRendererComponent(vv, value, font, isSelected,
				vertex);
		
		int size = vertex.getExportData().getTextSize();
		if (size == -1)
			size = exportModel.getExportInformations().getVertexLabelSize();
		
		label.setFont(new Font(font.getName(), font.getStyle(),size));
		
		return label;
	}
	
	@Override
	protected Component getEdgeLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, ExportEdgeData edge) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel)super.getEdgeLabelRendererComponent(vv, value, font, isSelected, edge);
		
		int size = edge.getExportData().getTextSize();
		if (size == -1)
			size = exportModel.getExportInformations().getEdgeLabelSize();
		
		
		label.setFont(new Font(font.getName(), font.getStyle(),size));
		
		return label;
	}
	
	@Override
	public String getEdgeLabel(ExportEdgeData edgeData) {
		String text = edgeData.getExportData().getText(); 
		if(text != null && !text.isEmpty()) {
			return text;
		}
		
		int mode = exportModel.getExportInformations().getEdgeLabelMode();
		if (mode == -1)
		{
			return ""; //$NON-NLS-1$
		}
		else if (mode >= 0 && mode < 2 + Program.myMainFrame.getDataPack().getActionTimeList().size() )
		{
			return prepareEdgeLabel(edgeData.getCompleteRelation().getEnsembleRelation(), mode);
		}
		else
		{ 
			System.err.println("The edge label mode is inadequate (value = "+mode+" and count of different possible mode : "+(2+Program.myMainFrame.getDataPack().getActionTimeList().size())+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return "øøø"; //$NON-NLS-1$
		}
			
		
		
	}
	
	@Override
	public Font getVertexFont(ExportVertexData vertexData) {
		return super.getVertexFont(vertexData);//new Font("Helvetica", Font.BOLD, 23);
	}

	
	@Override
	public boolean getVertexIncludePredicate(Context<Graph<ExportVertexData, ExportEdgeData>, ExportVertexData> arg0) {
		return arg0.element.getExportData().getApparitionStep() <= currentApparitionStep && arg0.element.getExportData().isVisible() && super.getVertexIncludePredicate(arg0);
	}

	/*****************/
	/** edge export **/
	/*****************/
	/* Couleur de l'arête */
	@Override
	public Paint getEdgeDrawPaint(ExportEdgeData edgeData) {
		Color color;
		if(edgeData != null && (color = edgeData.getColor()) !=null && edgeData.isSelected() == false)
			return color;
		else 
			return super.getEdgeDrawPaint(edgeData);
	}
	
	@Override
	public boolean getEdgeIncludePredicate(Context<Graph<ExportVertexData, ExportEdgeData>, ExportEdgeData> arg0) 
	{
		return arg0.element.getExportData().getApparitionStep() <= currentApparitionStep && arg0.element.getExportData().isVisible() && super.getEdgeIncludePredicate(arg0);
	}

	/* Type de fléche à afficher */
	//TODO Type de fléche à afficher à coder en trouvant où le modifier et si on le fait

	public ExportModel getExportModel() {
		return exportModel;
	}

	public int getCurrentApparitionStep() {
		return currentApparitionStep;
	}

	public void setCurrentApparitionStep(int currentApparitionStep) {
		this.currentApparitionStep = currentApparitionStep;
		level.setText(shownLevelLabelPrefix+currentApparitionStep);
		repaint();
	
	}
	
	public void setCorrectSizeForBoundingBox()
	{
		int minX = 1, minY = 1;
		int maxX = 0, maxY = 0;
		for (ExportVertexData eVD : exportModel.getVertexData())
		{
			
			if (eVD.getExportData().isVisible())
			{
			Point2D p = eVD.getLocation();
			if (minX == 1 && maxX == 0)
				minX = maxX = (int) p.getX();
			if (minY == 1 && maxY == 0)
				minY = maxY = (int) p.getY();
			
			if (minX > p.getX())
				minX = (int) p.getX();
			if (maxX < p.getX())
				maxX = (int) p.getX();
			
			if (minY > p.getY())
				minY = (int) p.getY();
			if (maxY < p.getY())
				maxY = (int) p.getY();
			}
		}
		
		
		minX -= 175;
		minY -= 130;
		maxX += 175;
		maxY += 75;
		
		vv.getRenderContext().getMultiLayerTransformer().getTransformer(
				Layer.VIEW).setTranslate(-minX, -minY); //(maxX -minX)/2 , (maxY - minY)/2);

		
		vv.setBounds(minX, minY, maxX- minX, maxY-minY);
		computeCenter();
		title.setLocation(new Point2D.Double(minX+20,minY+20));
		
			
		vv.repaint();
			}
	
}
