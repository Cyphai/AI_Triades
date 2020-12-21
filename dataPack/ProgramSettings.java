package dataPack;

import java.io.Serializable;

public class ProgramSettings implements Serializable {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -9004176656852621202L;

	protected boolean helpEditing;
	
	/**
	 * The default content of edge label
	 * If the value = 0 the structural relation are displayed
	 * If the value = 1 the real relation are displayed
	 */
	protected int defaultEdgeLabel;
	
	public final int structuralRelationMode = 0;
	public final int realRelationMode = 1;
	
	public ProgramSettings()
	{
		helpEditing = false;
		defaultEdgeLabel = 0;
	}
	
	public boolean isHelpEditing() {
		return helpEditing;
	}

	public void setHelpEditing(boolean helpEditing) {
		this.helpEditing = helpEditing;
	}

	public int getDefaultEdgeLabel() {
		return defaultEdgeLabel;
	}

	public void setDefaultEdgeLabel(int defaultEdgeLabel) {
		this.defaultEdgeLabel = defaultEdgeLabel;
	}
	
}
