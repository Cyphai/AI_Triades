package models;

import java.awt.geom.Point2D;
import java.util.Vector;

import main.RelationComplete;
import dataPack.ActeurSelectionne;
import dataPack.Content;
import dataPack.DataPack;
import dataPack.SavableObject;
import edu.uci.ics.jung.graph.SparseGraph;

public class Schema implements AbstractSchema, SavableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 56L;
	protected final DataPack dataPack;
	protected Vector<BrickEdge> edges;
	protected String nom;
	protected Vector<BrickVertex> vertices;
	protected Brick associatedBrick;
	protected transient SparseGraph<BrickVertex, BrickEdge> graph;
	protected transient String path;
	protected Integer sessionId;

	/**
	 * Indique si le schéma représente une étape globale (vrai) ou si il
	 * représente une brique simple (faux)
	 */
	protected boolean mainSchema;

	public Schema(DataPack _dataPack, String _nom, boolean isMainSchema,
			Brick associatedBrick, Integer sessionId) {
		dataPack = _dataPack;
		edges = new Vector<BrickEdge>();
		vertices = new Vector<BrickVertex>();
		nom = _nom;
		graph = null;
		path = null;
		mainSchema = isMainSchema;
		this.associatedBrick = associatedBrick;
		this.sessionId = sessionId;
	}

	public Schema(Schema schema) {
		dataPack = schema.dataPack;
		edges = new Vector<BrickEdge>(schema.edges);
		vertices = new Vector<BrickVertex>(schema.vertices);
		nom = new String(schema.nom);
		graph = schema.graph;
		path = null;
		mainSchema = schema.mainSchema;
		associatedBrick = schema.associatedBrick;
		this.sessionId = schema.sessionId;
	}

	public BrickVertex addSchemaVertex(Content content) {
		BrickVertex result = new BrickVertex(new Integer(
				getIdMax()
				.intValue() + 1), content);
		result.setLocation(new Point2D.Double());
		vertices.add(result);
		return result;
	}

	public BrickEdge addModelEdge(BrickVertex source, BrickVertex destination,
			RelationComplete relationComplete) {
		BrickEdge result = new BrickEdge(source, destination, relationComplete);
		edges.add(result);
		return result;
	}

	public void addModelEdge(BrickEdge newEdge) {
		newEdge.setAssociatedEdge(null);
		edges.add(newEdge);

	}

	protected Integer getIdMax() {
		Integer result = new Integer(-1);
		for (BrickVertex vertex : vertices) {
			if (vertex.vertexId > result)
				result = vertex.vertexId;

		}

		return result;
	}

	public BrickVertex getSchemaVertex(Integer id) {
		for (BrickVertex vertex : vertices) {
			if (vertex.getVertexId().equals(id)) {
				return vertex;
			}
		}
		System.out
				.println("Aucun sommet trouvé correspondant à l'id fourni (Schema.getSchemaVertex avec l'id:"
						+ id + ")");
		System.out.println(vertices);
		return null;
	}

	protected BrickVertex getSchemaVertexByContent(Integer id) {

		// if (vertices.elementAt(id.intValue()).vertexId.equals(id))
		// return vertices.elementAt(id.intValue());
		// else
		for (BrickVertex vertex : vertices)
			if (vertex.associatedContent.equals(id))
				return vertex;

		System.err
				.println("Aucun sommet trouvé correspondant au content fourni (Schema.getSchemaVertexByContent avec l'id:"
						+ id + ")");
		System.out.println(vertices);
		return null;
	}

	public BrickVertex getVertexByActor(ActeurSelectionne selectedActor) {
		for (BrickVertex vertex : vertices) {
			if (selectedActor.getIdActeur().equals(vertex.getContent()))
				return vertex;
		}
		return null;
	}

	public Vector<BrickVertex> getVertices() {
		return vertices;
	}

	public void setVertices(Vector<BrickVertex> vertices) {
		this.vertices = vertices;
	}

	public Vector<BrickEdge> getEdges() {
		return edges;
	}

	public DataPack getDataPack() {
		return dataPack;
	}

	@Override
	public void removeElement(Object Element) {

	}

	@Override
	public void removeModelEdge(BrickEdge edge) {

	}

	public String getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(String _path) {
		path = _path;

	}

	public boolean isMainSchema() {
		return mainSchema;
	}

	public void setMainSchema(boolean mainSchema) {
		this.mainSchema = mainSchema;
	}

	public Brick getAssociatedBrick() {
		return associatedBrick;
	}

	public void setAssociatedBrick(Brick associatedBrick) {
		this.associatedBrick = associatedBrick;
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public String getName() {
		return nom;
	}
	
	@Override
	public String toString() {
		return nom;
	}

}
