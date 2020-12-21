package models;

import java.awt.geom.Point2D;
import java.io.Serializable;

import dataPack.Content;

public class SchemaVertex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -643197059838492478L;

	/**
	 * contentId contient un entier indiquant l'acteur ou le type de brique
	 * désigné par ce sommet négatif pour un type de brique (de -10 à -49)
	 * positif pour un acteur Pour connaitre l'acteur
	 * corassociatedContentrespondant (ou la brique correspondante) se référer
	 * au datapack
	 */
	protected Content content;
	private Point2D location;
	protected Integer vertexId;
	protected boolean isSelected;

	public SchemaVertex(Integer id, Content content) {
		vertexId = id;
		location = null;
		this.content = content;
		isSelected = false;
	}

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	public Content getContent() {
		return content;
	}

	public Integer getVertexId() {
		return vertexId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "" + vertexId + " content : " + content;
	}
}
