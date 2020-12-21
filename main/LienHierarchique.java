package main;

import java.util.*;

public class LienHierarchique {
	
	
	public static Hashtable<String,String[]> moyens;
		
	//Moyens(Objectif) pour relation de type N->N-1
	
	static protected void initTable() 
	{
	moyens = new Hashtable<String,String[]>();
	String[] defReg = {"Ennoncer les règles","Faire découvrir les règles","Rappel d'un retour d'expérience"};
	String[] evalComp = {"Restitution active du passé","Formes décisionnelles du sujet acteur","Evaluation 360°"};
	String[] donConsigne = {"Donner un ordre","Faire participer à la décision","Suggérer une consigne","Suggestion implicite de la consigne", "Autre"};
	String[] defPoli = {"Ennonciation de la politique"};
	String[] evalPoli = {"Evalutation de la satisfaction du client","Contrôle des résultats"};
	String[] delegation = {"Lettre de mission écrite","Délégation orale","Délégation avec mode d'évaluation","Délégation avec procédure"};
	String[] controle = {"Observation des résultats", "Observation de l'état", "Formes décisionnelles du sujet acteur","Etude de satisfaction"};
	String[] devComp = {"Formation,stage,...","Forme décisionnelle du sujet acteur"};
	//String[] resProb = {"Brainstorming", "Triades", "Outils usuels"};
	String[] resConflit = {"Négociation","Médiation rationnelle","Questionnement sur l'action","Formes décisionnelles du sujet acteur","Outils de résolution de problème"};
	//String[] vide = {""};
	
	String[] nego = {"Questionnement sur la conception des outils et/ou des activités", "Evaluation des compétences"};
	String[] compRend = {"Description des activités", "Description de l'usage des outils", "Description des procédures"};
	
	
	moyens.put(ObjectifLienHierarchique.defRegles.toString(),defReg);
	moyens.put(ObjectifLienHierarchique.evalCompetences.toString(),evalComp);
	moyens.put(ObjectifLienHierarchique.donnerConsigne.toString(),donConsigne);
	moyens.put(ObjectifLienHierarchique.definitionPolitique.toString(),defPoli);
	moyens.put(ObjectifLienHierarchique.evalutationPolitique.toString(),evalPoli);
	moyens.put(ObjectifLienHierarchique.delegationActivite.toString(),delegation);
	moyens.put(ObjectifLienHierarchique.delegationMission.toString(),delegation);
	moyens.put(ObjectifLienHierarchique.controleExecutionActivite.toString(),controle);
	moyens.put(ObjectifLienHierarchique.controleUsageOutils.toString(),controle);
	moyens.put(ObjectifLienHierarchique.developpementCompetences.toString(),devComp);
	moyens.put(ObjectifLienHierarchique.resolutionConflit.toString(),resConflit);
	moyens.put(ObjectifLienHierarchique.resolutionProbleme.toString(),resConflit);
	
	moyens.put(ObjectifRetourLienHierarchique.negociation.toString(), nego);
	moyens.put(ObjectifRetourLienHierarchique.compteRendu.toString(), compRend);
	
	
	
	}
	
	//Moyens(Objectif) retour
	
	
	
	
	protected ObjectifLienHierarchique objectif;
	protected ObjectifRetourLienHierarchique objectifRetour;
	protected String moyen;

	public static String[] getStrings(String key)
	{
		if (moyens == null)
			initTable();
		return moyens.get(key);
	}
	
}
