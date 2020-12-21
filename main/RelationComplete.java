package main;

import graphicalUserInterface.Program;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Vector;

import relations.Goal;
import relations.Mean;
import relations.RelationPossibility;
import translation.Messages;

public class RelationComplete implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1067545404384529172L;

	public static final int RELATION_OK = 0;
	public static final int RELATION_INCOMPLETE = 1;
	public static final int RELATION_ECART_OBJECTIF = 3;
	public static final int RELATION_ECART_MOYEN = 2;

	protected Vector<JeuRelation> ensembleRelations;

	public RelationComplete() {
		ensembleRelations = new Vector<JeuRelation>(Program.myMainFrame
				.getDataPack().getActionTimeList().size());
		for (int i = 0; i < Program.myMainFrame.getDataPack()
				.getActionTimeList().size(); i++) {
			ensembleRelations.add(new JeuRelation());
		}

	}
	
	public RelationComplete(boolean noRelationFlag)
	{
		ensembleRelations = new Vector<JeuRelation>(Program.myMainFrame
				.getDataPack().getActionTimeList().size());
		for (int i = 0; i < Program.myMainFrame.getDataPack()
				.getActionTimeList().size(); i++) {
			ensembleRelations.add(new JeuRelation(noRelationFlag));
		}
	}

	@SuppressWarnings("unchecked")
	public RelationComplete(RelationComplete relationComplete) {

		ensembleRelations = new Vector<JeuRelation>();
		for (JeuRelation jR : relationComplete.ensembleRelations)
		{
			ensembleRelations.add(new JeuRelation(jR));
		}
	}

	public JeuRelation getJeuRelation(int index) {
		return ensembleRelations.elementAt(index);
	}

	public Vector<JeuRelation> getEnsembleRelation() {
		return ensembleRelations;
	}

	public boolean isEmpty(int mode) {
		for (JeuRelation relationSet : ensembleRelations)
			if (!relationSet.isEmpty(mode))
				return false;
		return true;
	}

	public boolean isNoRelation() {
		
		for (JeuRelation relationSet : ensembleRelations)
			if (!relationSet.isNoRelation())
				return false;
		return true;
	}
	
	public BitSet getActiveTime() {
		BitSet result = new BitSet(ensembleRelations.size());
		result.clear();
		for (int i = 0; i < ensembleRelations.size(); i++) {
			if (!ensembleRelations.elementAt(i).isEmpty(
					RelationPossibility.RELATIONSTRUCTURELLE))
				result.set(i);
		}
		return result;
	}

	public void addJeuRelation(ActionTime newActionTime) {
		ensembleRelations.add(new JeuRelation());
	}

	public int getState() {
		int result = RELATION_OK;
		for (JeuRelation jR : ensembleRelations) {
			int stateJR = jR.getState();
			if (result < stateJR)
				result = stateJR;
		}
		return result;
	}
	
	public Vector<JeuRelation> getUndefineRelations() {
		Vector<JeuRelation> result = new Vector<JeuRelation>();

		for(JeuRelation jeuRelation : ensembleRelations) {
			if(jeuRelation.getState() == RELATION_INCOMPLETE) {
				result.add(jeuRelation);
			}
		}
		
		return result;
	}
	
	public Vector<JeuRelation> getValideRelations() {
		Vector<JeuRelation> result = new Vector<JeuRelation>();

		for(JeuRelation jeuRelation : ensembleRelations) {
			if(jeuRelation.getState() == RELATION_OK) {
				result.add(jeuRelation);
			}
		}
		
		return result;		
	}
	
	public Vector<JeuRelation> getFalseObjectifRelations() {
		Vector<JeuRelation> result = new Vector<JeuRelation>();

		for(JeuRelation jeuRelation : ensembleRelations) {
			if(jeuRelation.getState() == RELATION_ECART_OBJECTIF) {
				result.add(jeuRelation);
			}
		}
		
		return result;		
	}
	
	public Vector<JeuRelation> getFalseMoyenRelation() {
		Vector<JeuRelation> result = new Vector<JeuRelation>();

		for(JeuRelation jeuRelation : ensembleRelations) {
			if(jeuRelation.getState() == RELATION_ECART_MOYEN) {
				result.add(jeuRelation);
			}
		}
		
		return result;		
	}
	
	private String createStringColor(String color, String text) {
		//TODO si possible a virer dans une class d'outils de manipe html
		if(color != null && color.compareTo("") != 0) { //$NON-NLS-1$
			return "<font color=\"" + color + "\">" + text + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			return text;
		}
	}
	
	protected String getStringStructurelRelation() {
		String result = ""; //$NON-NLS-1$
		
		for(JeuRelation relation : ensembleRelations) {
			result += createRelationString(relation, true);// "  Objectif : '" + relation.objectifStructurel + "' - Moyen : '" + relation.moyenStructurel + "' <br/>"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		result += "</br>"; //$NON-NLS-1$
		
		return result;
	}

	private String createRelationString(JeuRelation relation, boolean structurel, String objectifColor, String moyenColor) {
		Goal objectif;
		Mean moyen;
		
		if(structurel) {
			objectif = relation.objectifStructurel;
			moyen = relation.moyenStructurel;
		} else {
			objectif = relation.objectifReel;
			moyen = relation.moyenReel;
		}
		
		String actionTimeName = Program.myMainFrame.getDataPack().getActionTimeList().elementAt(ensembleRelations.indexOf(relation)).toString();
		
		if(structurel) {
			return (Program.isTriades() ? Messages.getString("RelationComplete.0") : actionTimeName + " : " )+ createStringColor(objectifColor, objectif.toString()) + ((relation.objectifStructurel.compareTo(RelationPossibility.UNDEFINED_GOAL) == 0) ? "<br/>" : " (" + createStringColor(moyenColor, moyen.toString()) + ")<br/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		} else {
			return actionTimeName + " : " + createStringColor(objectifColor, objectif.toString()) + ((relation.objectifReel.compareTo(RelationPossibility.UNDEFINED_GOAL) == 0) ? "<br/>" : " (" + createStringColor(moyenColor, moyen.toString()) + ")<br/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}
	
	private String createRelationString(JeuRelation relation, boolean structurel) {
		Goal objectif;
		Mean moyen;
		
		if(structurel) {
			objectif = relation.objectifStructurel;
			moyen = relation.moyenStructurel;
		} else {
			objectif = relation.objectifReel;
			moyen = relation.moyenReel;
		}	
		
		String actionTimeName = Program.myMainFrame.getDataPack().getActionTimeList().elementAt(ensembleRelations.indexOf(relation)).toString();

		if(structurel) {
			return (actionTimeName + " : " )+ objectif.toString() + ((relation.objectifStructurel.compareTo(RelationPossibility.UNDEFINED_GOAL) == 0) ? "<br/>" : " (" + moyen.toString() + ")<br/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else {
			return actionTimeName + " : " + objectif.toString() + ((relation.objectifReel.compareTo(RelationPossibility.UNDEFINED_GOAL) == 0) ? "<br/>" : " (" + moyen.toString() + ")<br/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}
	
	public String getStringDescription() {
		String result = "<html><PRE>"; //$NON-NLS-1$

		int relationCompleteState = getState();

		if(Program.isTriades() == false) {
			result += getStringStructurelRelation();
		}else if(relationCompleteState == RELATION_OK) {
			result += "               " + createStringColor("green", Messages.getString("RelationComplete.3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			result += getStringStructurelRelation();
		} else if(relationCompleteState == RELATION_INCOMPLETE) {
			result += "               " + createStringColor("blue", Messages.getString("RelationComplete.4")) + "<br/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			result += getStringStructurelRelation(); 
		} else {
			//Ecart sur les relations
			result += "               " + createStringColor("red", Messages.getString("RelationComplete.5")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			Vector<JeuRelation> valideRelations = getValideRelations();
			Vector<JeuRelation> undefineRelations = getUndefineRelations();
			Vector<JeuRelation> falseObjectifRelations = getFalseObjectifRelations();
			Vector<JeuRelation> falseMoyenRelations = getFalseMoyenRelation();
			
			if(falseObjectifRelations.size() > 0) {
				result += Messages.getString("RelationComplete.6"); //$NON-NLS-1$
				for(JeuRelation relation : falseObjectifRelations) {
					result += " - " + createRelationString(relation, false, "red", "red");//  "  Objectif : " + createStringColor("red", relation.objectifReel) + " - Moyen : " + createStringColor("red", relation.moyenReel) + "<br/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					result += "    " + createRelationString(relation, true, "", ""); //"  Objectif : " + relation.objectifStructurel + " - Moyen : " + relation.moyenStructurel + "<br/> <br/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
			
			if(falseMoyenRelations.size() > 0) {
				result += Messages.getString("RelationComplete.7"); //$NON-NLS-1$
				for(JeuRelation relation : falseMoyenRelations) {
					result += " - " + createRelationString(relation, false, "", "red");// "  Objectif : " + createStringColor("red", relation.objectifReel) + " - Moyen : " + createStringColor("red", relation.moyenReel) + "<br/>"; //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
					result += "    " + createRelationString(relation, true, "", ""); //"  Objectif : " + relation.objectifStructurel + " - Moyen : " + relation.moyenStructurel + "<br/> <br/>"; //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
				}
			}

			if(undefineRelations.size() > 0) {
				result += createStringColor("blue", undefineRelations.size() + (undefineRelations.size() > 1 ? Messages.getString("RelationComplete.1") : Messages.getString("RelationComplete.2")) + " : <br/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				
				for(JeuRelation relation : undefineRelations) {
					result += " - " + createRelationString(relation, true); //$NON-NLS-1$
				}
			}
			
			if(valideRelations.size() > 0) {
				result += Messages.getString("RelationComplete.3"); //$NON-NLS-1$
				for(JeuRelation relation : valideRelations) {
					result += " - " + createRelationString(relation, true); //"  Objectif : " + relation.objectifStructurel + " - Moyen : " + relation.moyenStructurel + "<br/>"; //$NON-NLS-1$
				}
			}
		}
		
		result += "</PRE></html>"; //$NON-NLS-1$
		return result;
	}

	public void removeJeuRelation(int index) {
		ensembleRelations.removeElementAt(index);
		
	}
	
	static public RelationComplete createNoRelation()
	{
		RelationComplete result = new RelationComplete(true);
		return result;
		
	}

	public boolean hasState(int requestedState) {
		for (JeuRelation relation : ensembleRelations)
		{
			if (relation.getState() == requestedState)
				return true;
		}
		return false;
	}

	public boolean contains(Goal goal) {
		for (JeuRelation relation : ensembleRelations)
		{
			if (relation.objectifStructurel.equals(goal) || relation.objectifReel.equals(goal))
				return true;
		}
		return false;
	}
}
