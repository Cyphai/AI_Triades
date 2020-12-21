package client.export;

import java.io.Serializable;

public class ExportInformations implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1111877328457634887L;
	protected String title;
	protected int edgeLabelSize;
	protected int edgeLabelMode;
	protected int vertexLabelSize;
	
	public ExportInformations(String title)
	{
		this.title = title;
		edgeLabelSize = 10;
		edgeLabelMode = 0;
		vertexLabelSize = 12;
	}
	
	public ExportInformations(String title, int edgeLabelSize,
			int edgeLabelMode, int vertexLabelSize) {
		super();
		this.title = title;
		this.edgeLabelSize = edgeLabelSize;
		this.edgeLabelMode = edgeLabelMode;
		this.vertexLabelSize = vertexLabelSize;
	}

	public ExportInformations(ExportInformations source) {
		title = source.title;
		edgeLabelMode = source.edgeLabelMode;
		edgeLabelSize = source.edgeLabelSize;
		vertexLabelSize = source.vertexLabelSize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getEdgeLabelSize() {
		return edgeLabelSize;
	}

	public void setEdgeLabelSize(int edgeLabelSize) {
		this.edgeLabelSize = edgeLabelSize;
	}

	public int getEdgeLabelMode() {
		return edgeLabelMode;
	}

	public void setEdgeLabelMode(int edgeLabelMode) {
		this.edgeLabelMode = edgeLabelMode;
	}

	public int getVertexLabelSize() {
		return vertexLabelSize;
	}

	public void setVertexLabelSize(int vertexLabelSize) {
		this.vertexLabelSize = vertexLabelSize;
	}
	
	

}
