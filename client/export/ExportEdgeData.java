package client.export;

import java.awt.Color;
import java.util.Hashtable;

import main.RelationComplete;
import models.BrickEdge;

/*
 * contient les modification d'éxportation d'une arrete
 */

public class ExportEdgeData extends BrickEdge implements ExportDataInterface {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3158403916349233462L;

	protected Hashtable<Integer, ExportTimeEdgeData> changeByActionTime;
	protected Color edgeColor;
	protected ExportData exportData;

	public ExportEdgeData(BrickEdge baseEdge) {
		super(baseEdge, true);
		exportData = new ExportData();
		edgeColor = null;
	}

	public ExportEdgeData(ExportVertexData source,
			ExportVertexData destination,
			RelationComplete completeRelation) {
		super(source, destination, completeRelation, true);
		exportData = new ExportData();
	}

	public ExportTimeEdgeData getChangeForActionTime(Integer actionTime) {
		return changeByActionTime.get(actionTime);
	}

	public Color getColor() {
		return edgeColor;
	}
	
	public void setEdgeColor(Color color) {
		edgeColor = color;
	}
	
	@Override
	public ExportData getExportData() {

		return exportData;
	}

}
