package client.export;

import graphicalUserInterface.Program;

import java.util.Hashtable;

import javax.swing.Icon;

import models.BrickVertex;
import dataPack.Content;

/*
 * contient les modification a effectuer sur un sommet 
 */

public class ExportVertexData extends BrickVertex implements
		ExportDataInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6976012649418320957L;

	protected Hashtable<Integer, ExportTimeVertexData> changeByActionTime;

	protected ExportData exportData;
	protected ExportImageData image;
	protected Content content;
	
	public ExportVertexData(BrickVertex baseVertex) {
		super(baseVertex);
		exportData = new ExportData();
		exportData.visible = true;
		exportData.text = null;

		image = new ExportImageData();
		content = baseVertex.getContent();
	}

	public Icon getIcon() {
		Icon icon = null;
		if (image != null)
			icon = image.getIcon();

		if(icon != null) {
			return icon;
		}
		
		return Program.myMainFrame.getDataPack().getCurrentSession().getDefaultImage(content);
	}
	
	public ExportTimeVertexData getChangeByActionTime(Integer actionTime) {
		return changeByActionTime.get(actionTime);
	}

	@Override
	public ExportData getExportData() {
		return exportData;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof BrickVertex) {
			return ((BrickVertex)other).getContent().equals(content);
		} 
	
		return false;
	}
	
	

	public void setImageData(ExportImageData data) {
		image.set(data);
	}
}
