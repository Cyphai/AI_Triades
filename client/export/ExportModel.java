package client.export;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import dataPack.Content;
import edu.uci.ics.jung.visualization.annotations.Annotation;

/*
 * cette classe enregiste tout les changement visuel effectué par l'utilisateur lors de l'exporte d'un schema.
 * Elle posséde pour chaque element du graphe un dataExporte qui contient les changement
 */

public class ExportModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8579249503567790228L;

	protected Vector<ExportVertexData> vertexData; // regroupe les
																// modifications
																// à effectuer
																// sur les
																// sommets
	protected Vector<ExportEdgeData> edgeData; // modification sur
															// les arretes
	
	protected Vector<Annotation<String>> annotations;

	protected ExportInformations informations;
	
	protected String name;
	protected Brick baseSchema;
	protected int apparitionStepCount;
	
	public ExportModel(String name, Brick schema) {
		this.name = name;
		this.baseSchema = schema;
		vertexData = new Vector<ExportVertexData>();
		edgeData = new Vector<ExportEdgeData>();
		apparitionStepCount = 1;
		informations = new ExportInformations(name);
		
		HashMap<BrickVertex, ExportVertexData> oldNew = new HashMap<BrickVertex, ExportVertexData>();

		for (BrickVertex vertex : schema.getVertices()) {
			ExportVertexData newVertex = new ExportVertexData(vertex);
			oldNew.put(vertex, newVertex);
			vertexData.add(newVertex);
		}
		
		for(BrickEdge edge : schema.getEdges()) {
			edgeData.add(new ExportEdgeData(oldNew.get(edge.getSource()), oldNew.get(edge.getDestination()), edge.getCompleteRelation()));
		}
	}

	public ExportModel(ExportModel modelExport) {
		vertexData = new Vector<ExportVertexData>(
				modelExport.vertexData);
		edgeData = new Vector<ExportEdgeData>(modelExport.edgeData);
		
		this.name = modelExport.name;
		this.baseSchema = modelExport.baseSchema;
		this.informations = new ExportInformations(modelExport.getExportInformations());
	}

	public ExportVertexData addVertexData(BrickVertex newVertex) {

		if (newVertex == null)
			return null;

		ExportVertexData data = new ExportVertexData(newVertex);
		vertexData.add(data);
		return data;
	}

	public ExportEdgeData addEdgeData(BrickEdge newEdge) {
		ExportEdgeData data = new ExportEdgeData(newEdge);
		edgeData.add(data);

		return data;
	}




	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Brick getBaseSchema() {
		return baseSchema;
	}
	
	public ExportVertexData getExportVertexDataByContent(Content content) {
		if(content != null) {
			for(ExportVertexData exportVertexData : vertexData) {
				Content currentContent = exportVertexData.getContent();
				if(content.equals(currentContent)) {
					return exportVertexData;
				}
			}
		} else {
			System.out.println("Content null !"); //$NON-NLS-1$
		}
		
		return null;
	}
	
	public Vector<ExportVertexData> getVertexData() {
		return vertexData;
	}

	public void setVertexData(Vector<ExportVertexData> vertexData) {
		this.vertexData = vertexData;
	}

	public Vector<ExportEdgeData> getEdgeData() {
		return edgeData;
	}

	public void setEdgeData(Vector<ExportEdgeData> edgeData) {
		this.edgeData = edgeData;
	}

	public int getApparitionStepCount() {
		return apparitionStepCount;
	}

	public void setApparitionStepCount(int apparitionStepCount) {
		this.apparitionStepCount = apparitionStepCount;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public ExportInformations getExportInformations()
	{
		if (informations == null)
			informations = new ExportInformations(name);
		
		return informations;
	}
}
