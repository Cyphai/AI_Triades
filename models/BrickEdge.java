package models;

import java.io.Serializable;

import translation.Messages;

import main.RelationComplete;
import client.export.ExportVertexData;

public class BrickEdge implements Serializable {
	private static final long serialVersionUID = -5466333956926991387L;

	protected BrickVertex source;
	protected BrickVertex destination;
	protected RelationComplete relations;

	protected boolean selected;

	public BrickEdge(BrickVertex _source, BrickVertex _destination) {
		source = _source;
		destination = _destination;
		relations = new RelationComplete();
		selected = false;
	}

	public BrickEdge(BrickVertex source2, BrickVertex destination2,
			RelationComplete relationComplete) {
		source = source2;
		destination = destination2;
		relations = new RelationComplete(relationComplete);
	}
	
	public BrickEdge(BrickEdge brickEdge) {
		this(brickEdge.source, brickEdge.destination, brickEdge.relations);
	}
	
	public BrickEdge(BrickEdge baseEdge, boolean doNotClone) {
		source = baseEdge.source;
		destination = baseEdge.destination;
		if (doNotClone)
			relations = baseEdge.relations;
		else
			relations = new RelationComplete(baseEdge.relations);
	}

	public BrickEdge(ExportVertexData source2, ExportVertexData destination2,
			RelationComplete completeRelation, boolean doNotClone) {
		source = source2;
		destination = destination2;
		
		if (doNotClone)
			relations = completeRelation;
		else
			relations = new RelationComplete(completeRelation);
	}

	public BrickVertex getSource() {
		return source;
	}
	

	public void setSource(BrickVertex newVertex) {
		source = newVertex;
		
	}

	public BrickVertex getDestination() {
		return destination;
	}
	
	public void setDestination(BrickVertex newVertex)
	{
		destination = newVertex;
	}

	public RelationComplete getCompleteRelation() {
		return relations;
	}

	public boolean equals(BrickEdge other) {
		return (source.equals(other.source) && destination
				.equals(other.destination));
	}

	public String getStringDescription() {
		return relations.getStringDescription();
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "src = " + source + ", dst = " + destination;  //$NON-NLS-1$ //$NON-NLS-2$
	}

	

}
