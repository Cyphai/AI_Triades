package main;

import java.util.*;

public class LienPartenaire {
	
	
	protected static Hashtable<String,String[]> moyens;
	

	protected ObjectifLienPartenaire objectif;
	protected String moyen;
	
	protected static void initTable()
	{
		moyens = new Hashtable<String,String[]>();
		String[] attAccord = {"Négociation"};
		String[] appAccord = {"Exécution"};
		String[] conAccord = {"Observation de l'activité"};
		
		moyens.put(ObjectifLienPartenaire.appliquerAccord.toString(), appAccord);
		moyens.put(ObjectifLienPartenaire.atteindreAccord.toString(), attAccord);
		moyens.put(ObjectifLienPartenaire.controlerAccord.toString(), conAccord);
	
	}
	
	
	public static String[] getStrings(String key)
	{
		if (moyens == null)
			initTable();
		return (moyens.get(key));
	}
	
	


}
