package models;

import graphicalUserInterface.Program;

import java.awt.geom.Point2D;
import java.io.Serializable;

import translation.Messages;
import client.Session;
import dataPack.Acteur;
import dataPack.Activite;
import dataPack.Content;
import dataPack.DataPack;
import dataPack.Moyen;

public class BrickVertex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2411539979667875828L;
	protected Content content;
	protected Point2D location;
	protected boolean selected;
	protected VerticeRank rank;
	
	public enum VerticeRank implements Comparable<VerticeRank> 
	{
		primary(1), secondary(2), remaining(3), undefined(4);
		
		private int r;
		
		private VerticeRank(int r)
		{
			this.r = r;
		}
		
		public int getValue()
		{
			return r;
		}
		
	};

	protected BrickVertex(DataPack _dataPack) {
		content = null;
		selected = false;
		rank = VerticeRank.remaining;
	}
	
	protected BrickVertex(BrickVertex brickVertex) {
		content = brickVertex.content;
		location = brickVertex.location;
		rank = brickVertex.rank;
	}

	protected void setActor(Content newContent) {
		content = newContent;
	}

	public void setLocation(Point2D newLocation) {
		location = newLocation;
	}

	public Point2D getLocation() {
		return location;
	}
	
	public void setRank(VerticeRank r)
	{
		rank = r;
	}
	
	public VerticeRank getRank()
	{
		
		if (rank == null)
			rank = VerticeRank.remaining;
		
		if (Program.isTriades())
		{
			Session cS = Program.myMainFrame.getDataPack().getCurrentSession();
			if (cS != null)
			{
				VerticeRank r = cS.getActorsList().get(content);
				
				if (r != null && r.getValue() < rank.getValue())
					return r;
			}
		}
		return rank;
	}
	


	public String getStringDescription() {

		if (content instanceof Moyen)
			return Messages.getString("BrickVertex.0") + content; //$NON-NLS-1$
		else if (content instanceof Acteur)
			return Messages.getString("BrickVertex.1") + content; //$NON-NLS-1$
		else if (content instanceof Activite)
			return Messages.getString("BrickVertex.2") + content; //$NON-NLS-1$
		else if (content instanceof Brick)
			return Messages.getString("BrickVertex.4") + content; //$NON-NLS-1$
		else			
			return Messages.getString("BrickVertex.3"); //$NON-NLS-1$
	}

	public Content getContent() {
		return content;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BrickVertex) {
			return ((BrickVertex) other).content.equals(content);
		}
		return false;
	}

	@Override
	public String toString() {
		return "content : " + content; //$NON-NLS-1$
	}

	public void setContent(Content newContent) {
		content = newContent;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
