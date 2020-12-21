package main;

import graphicalUserInterface.Program;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import relations.Goal;
import relations.Mean;
import relations.RelationPossibility;
import translation.Messages;

public class JeuRelation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 110214674857996254L;
	public Goal objectifStructurel;
	public Goal objectifReel;

	public Mean moyenReel;
	public Mean moyenStructurel;

	public JeuRelation() {
		objectifStructurel = RelationPossibility.UNDEFINED_GOAL;
		objectifReel = RelationPossibility.UNDEFINED_GOAL;
		moyenReel = RelationPossibility.UNDEFINED_MEAN;
		moyenStructurel = RelationPossibility.UNDEFINED_MEAN;
	}

	public JeuRelation(boolean noRelationFlag)
	{

		objectifStructurel = RelationPossibility.NORELATION_GOAL;
		objectifReel = RelationPossibility.NORELATION_GOAL;
		moyenReel = RelationPossibility.NORELATION_MEAN;
		moyenStructurel = RelationPossibility.NORELATION_MEAN;
	}

	public JeuRelation(JeuRelation jeuRelation) {
		objectifStructurel = jeuRelation.objectifStructurel;
		objectifReel = jeuRelation.objectifReel;
		moyenStructurel = jeuRelation.moyenStructurel;
		moyenReel = jeuRelation.moyenReel;
	}

	public boolean etatInitial() {
		return (objectifStructurel.equals(RelationPossibility.UNDEFINED_GOAL)
				&& objectifReel.equals(RelationPossibility.UNDEFINED_GOAL)
				&& moyenReel.equals(RelationPossibility.UNDEFINED_MEAN) && moyenStructurel.equals(RelationPossibility.UNDEFINED_MEAN));
	}

	public void print() {
		System.out.println("Obj struct : " + objectifStructurel); //$NON-NLS-1$
		System.out.println("Obj reel : " + objectifReel); //$NON-NLS-1$
		System.out.println("Moyen struct : " + moyenStructurel); //$NON-NLS-1$
		System.out.println("Moyen reel : " + moyenReel); //$NON-NLS-1$

	}

	public boolean isEmpty(int mode) {
		if (mode == RelationPossibility.RELATIONSTRUCTURELLE)
			return objectifStructurel.equals(RelationPossibility.UNDEFINED_GOAL)
			&& moyenStructurel.equals(RelationPossibility.UNDEFINED_MEAN);
		else
			return moyenReel.equals(RelationPossibility.UNDEFINED_MEAN)
			&& objectifReel.equals(RelationPossibility.UNDEFINED_GOAL);

	}

	public boolean isNoRelation()
	{
		return (objectifStructurel.equals(RelationPossibility.NORELATION_GOAL)
				&& objectifReel.equals(RelationPossibility.NORELATION_GOAL)
				&& moyenReel.equals(RelationPossibility.NORELATION_MEAN) && moyenStructurel.equals(RelationPossibility.NORELATION_MEAN));

	}

	public int getState()
	{

		if (Program.isTriades())
		{
			if (objectifStructurel.equals(objectifReel) && moyenStructurel.equals(moyenReel))
				return RelationComplete.RELATION_OK;

			if (objectifReel.equals(RelationPossibility.UNDEFINED_GOAL))
				return RelationComplete.RELATION_INCOMPLETE;

			else if(objectifStructurel.equals(objectifReel) == false)
				return RelationComplete.RELATION_ECART_OBJECTIF;
			else 
				return RelationComplete.RELATION_ECART_MOYEN;
		}
		else
		{
			if (objectifStructurel.equals(RelationPossibility.UNDEFINED_GOAL))
				return RelationComplete.RELATION_INCOMPLETE;
			else
				return RelationComplete.RELATION_OK;
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		ObjectInputStream.GetField fields = in.readFields();
		Object oS = fields.get("objectifStructurel", RelationPossibility.UNDEFINED_GOAL); //$NON-NLS-1$
		if (oS instanceof String)
		{
			objectifStructurel = new Goal((String)oS);
			objectifReel = new Goal((String)fields.get("objectifReel", RelationPossibility.UNDEFINED_STRING)); //$NON-NLS-1$
			moyenStructurel = new Mean((String)fields.get("moyenStructurel", RelationPossibility.UNDEFINED_STRING)); //$NON-NLS-1$
			moyenReel = new Mean((String)fields.get("moyenReel", RelationPossibility.UNDEFINED_STRING)); //$NON-NLS-1$
		}
		else if (oS instanceof Goal)

		{
			objectifStructurel = (Goal)oS;
			objectifReel = (Goal)fields.get("objectifReel", RelationPossibility.UNDEFINED_GOAL); //$NON-NLS-1$
			moyenStructurel = (Mean)fields.get("moyenStructurel", RelationPossibility.UNDEFINED_MEAN); //$NON-NLS-1$
			moyenReel = (Mean)fields.get("moyenReel", RelationPossibility.UNDEFINED_MEAN); //$NON-NLS-1$
		}
		else
		{
			System.err.println("Error while loading a JeuRelation, the first object is neither a String, neither a Goal"); //$NON-NLS-1$
			throw new StreamCorruptedException();
		}

	}
}
