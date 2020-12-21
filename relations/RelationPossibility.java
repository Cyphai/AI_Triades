package relations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;

import translation.Messages;

public class RelationPossibility implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4953398953739613952L;
	/**
	 * Toutes les possibilités sont contenues dans cette map Les objectifs
	 * correspondent aux clées et les moyens sont stockés dans l'arrayList
	 * correspondant
	 */

	public static final int RELATIONREELLE = 1;
	public static final int RELATIONSTRUCTURELLE = 0;

	public static Mean UNDEFINED_MEAN = new Mean(Messages.getString("RelationPossibility.0")); //$NON-NLS-1$
	public static Mean NORELATION_MEAN = new Mean(Messages.getString("RelationPossibility.1")); //$NON-NLS-1$
	public static Goal UNDEFINED_GOAL= new Goal(Messages.getString("RelationPossibility.0")); //$NON-NLS-1$
	public static Goal NORELATION_GOAL = new Goal(Messages.getString("RelationPossibility.1")); //$NON-NLS-1$
	public static String NORELATION_STRING = Messages.getString("RelationPossibility.1"); //$NON-NLS-1$
	public static String UNDEFINED_STRING =  Messages.getString("RelationPossibility.0"); //$NON-NLS-1$
//	HashMap<String, ArrayList<String>> possibility;
	TreeMap<Goal, ArrayList<Mean> > possibility;

	public RelationPossibility() {
//		possibility = new HashMap<String, ArrayList<String>>();
		possibility = new TreeMap<Goal, ArrayList<Mean>>();
	}

	public void addAll(RelationPossibility newPossibility) {
		
		for (Goal objectif : newPossibility.possibility.keySet()) {
			ArrayList<Mean> newMeanings = newPossibility.possibility.get(objectif);
			ArrayList<Mean> meanings = possibility.get(objectif);
			if(possibility.get(objectif) != null) {
				for (Mean meaning : newMeanings) {
					if(meanings.contains(meaning) == false) {
						meanings.add(meaning);
					}
				}
			} else {
				possibility.put(objectif, newMeanings);
			}
		}
	}

//	public HashMap<String, ArrayList<String>> getMap() {
//		return possibility;
//	}
	
	public TreeMap<Goal, ArrayList<Mean> > getMap() {
		return possibility;
	}

	public void modifyKey(Goal oldKey, Goal newKey) {
		if (newKey.equals(oldKey))
			return;

		if (possibility.containsKey(newKey)) {
			throw new IllegalArgumentException(
					Messages.getString("RelationPossibility.2")); //$NON-NLS-1$
		}

		possibility.put(newKey, possibility.remove(oldKey));
	}
	
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		ObjectInputStream.GetField fields = in.readFields();
		Object possibility = fields.get("possibility", new TreeMap<Goal, ArrayList<Mean> >()); //$NON-NLS-1$
		
		if (possibility instanceof TreeMap<?,?>)
		{
			this.possibility = (TreeMap<Goal, ArrayList<Mean>>)possibility;
		}
		else if (possibility instanceof HashMap<?,?>)
		{ 
			HashMap<String, ArrayList<String> > oldPossibility = (HashMap<String, ArrayList<String>>) possibility;
			this.possibility = new TreeMap<Goal, ArrayList<Mean>>();
			for (String o : oldPossibility.keySet())
			{
				ArrayList<Mean> aM = new ArrayList<Mean>();
				for (String m : oldPossibility.get(o))
				{
					aM.add(new Mean(m));
				}
				this.possibility.put(new Goal(o), aM);
			}
			
		}
	}

}
