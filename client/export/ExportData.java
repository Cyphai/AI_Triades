package client.export;

import java.io.Serializable;

public class ExportData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5832206653696002697L;
	boolean visible;
	protected String text; // texte à afficher
	protected int textSize;
	protected int apparitionStep;
	
	public ExportData() {
		visible = true;
		apparitionStep = 1;

		textSize = -1;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	
	public void setApparitionStep(int newStep)
	{
		apparitionStep = newStep;
	}
	
	public int getApparitionStep()
	{
		return apparitionStep;
	}
}
