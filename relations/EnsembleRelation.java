package relations;

import graphicalUserInterface.DialogHandlerFrame;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import main.Liens;
import main.ObjectifActeurMoyen;
import main.ObjectifActeurObjet;
import main.ObjectifFormesDecisionnelles;
import main.ObjectifLienHierarchique;
import main.ObjectifLienPartenaire;
import main.ObjectifMoyenActeur;
import main.ObjectifMoyenMoyen;
import main.ObjectifMoyenObjet;
import main.ObjectifObjetActeur;
import main.ObjectifObjetMoyen;
import main.ObjectifRetourLienHierarchique;
import translation.Messages;
import dataPack.Acteur;
import dataPack.Activite;
import dataPack.Content;
import dataPack.Moyen;
import dataPack.SavableObject;

public class EnsembleRelation implements SavableObject {
	private static final long serialVersionUID = -2385124293655813004L;
	HashSet<RelationDescription> table;

	public EnsembleRelation() {
		table = new HashSet<RelationDescription>();
	}

	public void addRelationDescription(RelationDescription newRelDesc) {
		table.add(newRelDesc);

	}

	public HashSet<RelationDescription> getRelationDescriptions() {
		return table;
	}

	public RelationPossibility getRelationPossibility(Content first, Content second,
			boolean realRelation) {
		Integer a = null;
		Integer b = null;
		if (first instanceof Acteur)
		{
			a = ((Acteur)first).getIdStatut();
		}
		else if (first instanceof Moyen)
		{
			a = new Integer(18 + ((Moyen) first).getIdGenerique().intValue());
		}
		else if (first instanceof Activite)
		{
			a = new Integer(22);
		}

		if (second instanceof Acteur) {
			b = ((Acteur) second).getIdStatut();
		} else if (second instanceof Moyen) {
			b = new Integer(18 + ((Moyen) second).getIdGenerique().intValue());
		} else if (second instanceof Activite) {
			b = new Integer(22);
		}

		if (a == null || b == null) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("EnsembleRelation.0")); //$NON-NLS-1$
			return null;
		}


		RelationPossibility result = new RelationPossibility();
		for (RelationDescription relation : table) {
			if (relation.allow(a, b, realRelation)) {
				result.addAll(relation.getPossibility());
			}
		}

		return result;
	}

	public void addEnsembleRelation(EnsembleRelation newSet) {
		table.addAll(newSet.table);
	}

	@Override
	public String getFilePath() {
		return null;
	}

	@Override
	public void setFilePath(String filePath) {

	}

	static public EnsembleRelation getDefaultRelations() {
		EnsembleRelation ensembleRelation = new EnsembleRelation();

		//Acteurs 0-7
		//Partenaires 8-17
		//Objectifs 18-21
		//Activité 22


		//Objectif Acteur Objectif
		RelationDescription relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.1")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(0, 17, 18, 21, true);
		//ajoute les relations à choisir
		for(ObjectifActeurMoyen objectif : ObjectifActeurMoyen.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);

		//Objectif Acteur Activité
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.2")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(0, 17, 22, 22, true);
		//ajoute les relations à choisir
		for(ObjectifActeurObjet objectif : ObjectifActeurObjet.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);		

		//Objectif formesDecisionnelles (relation réels acteur)
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.3")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRealRelation(true);
		relationDescription.setRect(0, 17, 0, 17, true);
		//ajoute les relations à choisir
		for(ObjectifFormesDecisionnelles objectif : ObjectifFormesDecisionnelles.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif lien hierarchique 
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.4")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setTriangleSup(0, 0, 8, true);
		//ajoute les relations à choisir
		for(ObjectifLienHierarchique objectif : ObjectifLienHierarchique.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif lien partenaire 
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.5")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(0, 17, 0, 17, true);
		//ajoute les relations à choisir
		for(ObjectifLienPartenaire objectif : ObjectifLienPartenaire.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		//ObjectifFormesDecisionnelles formesDecisionnelles = ObjectifFormesDecisionnelles.values()[1];
		//relationDescription.getPossibility().getMap().put(formesDecisionnelles.toString(), Liens.getStrings(formesDecisionnelles));

		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif objectif acteur
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.6")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(18, 21, 0, 17, true);
		//ajoute les relations à choisir
		for(ObjectifMoyenActeur objectif : ObjectifMoyenActeur.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);			

		//Objectif objectif objectif
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.7")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(18, 21, 18, 21, true);
		//ajoute les relations à choisir
		for(ObjectifMoyenMoyen objectif : ObjectifMoyenMoyen.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif objectif activite
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.8")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(18, 21, 22, 22, true);
		//ajoute les relations à choisir
		for(ObjectifMoyenObjet objectif : ObjectifMoyenObjet.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif activite acteur
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.9")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(22, 22, 0, 17, true);
		//ajoute les relations à choisir
		for(ObjectifObjetActeur objectif : ObjectifObjetActeur.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif activite objectif
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.10")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setRect(22, 22, 18, 21, true);
		//ajoute les relations à choisir
		for(ObjectifObjetMoyen objectif : ObjectifObjetMoyen.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Objectif hierarchique retour
		relationDescription = new RelationDescription(Messages.getString("EnsembleRelation.11")); //$NON-NLS-1$
		//ajoute les relations possible
		relationDescription.setTriangleInf(0, 0, 8, true);
		//ajoute les relations à choisir
		for(ObjectifRetourLienHierarchique objectif : ObjectifRetourLienHierarchique.values()) {
			relationDescription.getPossibility().getMap().put(new Goal(objectif.toString()), Liens.getStrings(objectif));
		}
		ensembleRelation.addRelationDescription(relationDescription);	

		//Non defini
		ArrayList<Mean> undefStrings = new ArrayList<Mean>();
		undefStrings.add(RelationPossibility.UNDEFINED_MEAN);
		ArrayList<Mean> noRelationStrings = new ArrayList<Mean>();
		noRelationStrings.add(RelationPossibility.NORELATION_MEAN);
		relationDescription = new RelationDescription(RelationPossibility.UNDEFINED_STRING+" & "+RelationPossibility.NORELATION_STRING); //$NON-NLS-1$
		relationDescription.setRect(0, 22, 0, 22, true);
		relationDescription.getPossibility().getMap().put(RelationPossibility.UNDEFINED_GOAL, undefStrings);
		relationDescription.getPossibility().getMap().put(RelationPossibility.NORELATION_GOAL, noRelationStrings);
		ensembleRelation.addRelationDescription(relationDescription);	

		return ensembleRelation;	
	}

	public String getAllGoalOfMean(String meanName) {
		Set<String> allGoal = new HashSet<String>();
		String result= Messages.getString("EnsembleRelation.12") + meanName + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		for(RelationDescription relation : table) {
			for(Goal goal : relation.getPossibility().getMap().keySet()) {
				for(Mean mean : relation.getPossibility().getMap().get(goal)) {
					if(meanName.equals(mean.toString())) {
						if(allGoal.contains(goal.toString()) == false) {
							allGoal.add(goal.toString());
							result += "  - "+ goal.toString() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			}
		}

		if(allGoal.size() == 0) {
			result += Messages.getString("EnsembleRelation.16"); //$NON-NLS-1$
		}

		System.out.println(result);
		return result;
	}

	public void printRelations() {

		//Acteur ->
		System.out.println("\\section{Relation sortant d'un acteur}");
		//vers un acteur
		System.out.println("\\subsection{Vers un autre acteur}");
		printRelationCategory(0, 0);

		System.out.println("\\subsection{Vers un moyen}");
		printRelationCategory(0, 1);

		System.out.println("\\subsection{Vers une activité}");
		printRelationCategory(0, 2);

		System.out.println("\\section{Relation sortant d'un moyen}");

		System.out.println("\\subsection{Vers un acteur}");
		printRelationCategory(1, 0);

		System.out.println("\\subsection{Vers un autre moyen}");
		printRelationCategory(1, 1);

		System.out.println("\\subsection{Vers une activité}");
		printRelationCategory(1, 2);

		System.out.println("\\section{Relation sortant d'une activité}");
		System.out.println("\\subsection{Vers un acteur}");
		printRelationCategory(2, 0);

		System.out.println("\\subsection{Vers un moyen}");
		printRelationCategory(2, 1);

		System.out.println("\\subsection{Vers une autre activité}");
		printRelationCategory(2, 2);
	}

	private void printRelationCategory(int source, int destination)
	{
		System.out.println("\\begin{itemize}");
		Vector< Entry<Goal, ArrayList<Mean> > > goals = new Vector<Map.Entry<Goal,ArrayList<Mean>>>();

		for (RelationDescription rD : table)
		{
			if (rD.relationCategory(0, 0))
			{
				for (Entry<Goal, ArrayList<Mean> > entry : rD.getPossibility().getMap().entrySet())

				{
					goals.add(entry);

				}
			}
		}
		Collections.sort(goals,new Comparator< Entry<Goal, ArrayList<Mean> > >() {

			@Override
			public int compare(Entry<Goal, ArrayList<Mean>> o1,
					Entry<Goal, ArrayList<Mean>> o2) {

				return Collator.getInstance().compare(o1.getKey().toString(), o2.getKey().toString());
			}
		});

		for (Entry<Goal, ArrayList<Mean> > entry : goals)
		{

			System.out.println("\\item \\textbf{"+entry.getKey().toString()+"}");
			System.out.println("\\begin{itemize}");
			for (Mean m : entry.getValue())
			{
				if (m.toString().trim().length() > 0)
					System.out.println("\\item "+m.toString());
			}
			System.out.println("\\\\ \n \\end{itemize}");

		}


		System.out.println("\\end{itemize}");
	}
}
