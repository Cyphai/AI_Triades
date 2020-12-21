package models;

import java.awt.geom.Point2D;
import java.io.Serializable;


import edu.uci.ics.jung.graph.Vertex;

public class ModelVertex implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -643197059838492478L;
	
	/**
	 * contentId contient un entier indiquant l'acteur ou le type de brique désigné par ce sommet
	 * négatif pour un type de brique (de -10 à -49) positif pour un acteur
	 * Pour connaitre l'acteur correspondant (ou la brique correspondante) se référer au datapack
	 */
	protected Integer contentId;
	protected Point2D location;
	protected Integer vertexId;

	protected transient Vertex associatedVertex;
	
	
	public ModelVertex(Integer id, Integer _contentId)
	{
		vertexId = id;
		contentId = _contentId;
		location = null;
	}
	
	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	public Vertex getAssociatedVertex() {
		return associatedVertex;
	}

	public void setAssociatedVertex(Vertex associatedVertex) {
		this.associatedVertex = associatedVertex;
	}

	public Integer getContentId() {
		return contentId;
	}

	public Integer getVertexId() {
		return vertexId;
	}

	
}
