package main;


public class Objet extends Pole{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5593038862364253170L;
	public String nom;
	public StatutObjet statut;
	
	public Objet(String _nom,StatutObjet _statut)
	{
		super();
		nom = _nom;
		statut = _statut;
	}
	
	public Objet()
	{
		this("Non défini",StatutObjet.nonDef);
	}

	public String toString()
	{
		return nom;
	}
	
	public int getType()
	{
		return Pole.OBJET;
	}

}
