package main;

import java.util.*;

public class FormesDecisionnelles {

	protected static Hashtable<String,String[]> moyens;
	
	protected static void initTable()
	{
		moyens = new Hashtable<String,String[]>();
		
		String[] sujetAct = { "Rechercher plus d’informations sur l’acteur", "Demander de décrire avec plus de précision une séquence d’événements","Rechercher les décisions de la personne qui raconte une séquence d’actions","Demander le type d’effet d’une action","S’enquérir de la cohérence entre objectif et moyen utilisé pour l’atteindre","Interroger ce qu’elle a pris en compte pour les décisions","Demander quels sont les importants qui dirigent l’action","Demande de la hiérarchisation faite pour prendre sa décision"};
		String[] sujetDec =  {"Ordonner un acte","Enoncer la loi ou une règle convenue","Donner des informations","Donner des consignes","Annoncer une stratégie d’action"};
		String[] sujetRai = {"Ecouter","Appropriation du problème de l’autre en les rapportant à son expérience","Interpréter ce qui est dit, fait (actions ou comportements)","Evaluer dires et faire en les comparant à une norme (sociale ou individuelle)","Reformuler","Rechercher plus d’informations sur la situation","Enoncer un constat ou un effet","Faire des hypothèses de causes (qui expliquent les événements)","Emettre des hypothèses de solution à un problème ou difficulté","Rechercher une causalité","Explique (ce qu’il faut faire ou pourquoi les événements se sont passés de la sorte)","Demander des explications pour comprendre une idée ou une situation","Donner un conseil"};
		String[] def =  {"Proférer une menace ou mettre en garde", "Se mettre en défense (justification,fuite,passivité,mise en œuvre d’un mécanisme de défense psychique)"};
		String[] univRel = { "Entrer en relation selon un des quatre objectifs de relation","Partager toutes les informations ou en cacher quelques unes.","Prendre l’autre dans sa totalité ou le considérer selon une partie de son identité.","Soumettre, dominer, rechercher une relation d’égalité"};
	
		moyens.put(ObjectifFormesDecisionnelles.sujetActeur.toString(),sujetAct);
		moyens.put(ObjectifFormesDecisionnelles.sujetDecideur.toString(),sujetDec);
		moyens.put(ObjectifFormesDecisionnelles.sujetRaisonneur.toString(),sujetRai);
		moyens.put(ObjectifFormesDecisionnelles.defensive.toString(),def);
		moyens.put(ObjectifFormesDecisionnelles.universRelation.toString(),univRel);
	
	
	}
	
	public static String[] getStrings(String key)
	{
		if (moyens == null)
			initTable();
		return (moyens.get(key));
	}
	
}
