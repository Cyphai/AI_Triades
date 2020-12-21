package main;


public class ActeurGenerique extends Pole{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1644787848967220296L;
	public Statut statut;
	public String nom;
	public String role;
	
	public ActeurGenerique(String _nom, Statut _statut)
	{
		nom = _nom;
		statut = _statut;
	}
	
	public ActeurGenerique()
	{
		this("Non-défini",Statut.nonDef);
	}
	
	public String toString()
	{
		return (nom+"  -  "+statut.toString());
	}
	
	public int getType()
	{
		return Pole.ACTEUR;
	}

}

