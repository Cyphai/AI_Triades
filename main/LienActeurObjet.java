package main;

import java.util.*;

public class LienActeurObjet {
	
	
	protected static Hashtable<String,String[]> moyens;
	

	protected ObjectifLienPartenaire objectif;
	protected String moyen;
	
	protected static void initTable()
	{
		moyens = new Hashtable<String,String[]>();
		
		String[] concep = {"Analyse"};
		String[] util = {"Application de connaissance", "Découverte de l'objet"};
		String[] recup = {"Observation instrumentée", "Observation non instrumentée"};
		String[] eval = {"Normes"};
		
		moyens.put(ObjectifActeurObjet.conception.toString(), concep);	
		moyens.put(ObjectifActeurObjet.utilisation.toString(), util);	
		moyens.put(ObjectifActeurObjet.recupInfo.toString(),recup );		
		moyens.put(ObjectifActeurObjet.eval.toString(), eval);	
	}
	
	
	public static String[] getStrings(String key)
	{
		if (moyens == null)
			initTable();
		return (moyens.get(key));
	}
	
	


}
