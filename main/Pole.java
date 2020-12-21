package main;

import java.io.Serializable;

import edu.uci.ics.jung.graph.impl.SimpleSparseVertex;

public class Pole extends SimpleSparseVertex implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5185453429018761925L;
	public static int ACTEUR = 1;
	public static int OBJET = 2;
	protected static int idMax = 1;
	protected int id;
	
	public Pole()
	{
		super();
	}
	
	public int getType()
	{
		return 0;
	}
	
	public void setId(int newId)
	{
		id = newId;
		if (newId > idMax)
			idMax = newId;
	}
	
	public int getId()
	{
		return id;
	}
	
	public static int getIdMax()
	{
		return idMax;
	}
	
	
}
