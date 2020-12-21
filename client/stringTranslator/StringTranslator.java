package client.stringTranslator;

import graphicalUserInterface.Program;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map.Entry;

import relations.Mean;
import dataPack.Moyen;
import dataPack.SavableObject;

public class StringTranslator implements Serializable, SavableObject {
	private static final long serialVersionUID = -5366757600960457117L;

	public enum StringType{actorType, groupType, activityType, objectifType, moyenRelationType, moyenObjetType, brickType, stepsType, actionTimesType, actorSetType, statusType}

	protected transient String filepath;

	public Hashtable<Object, String> actors;
	public Hashtable<Object, String> groups;
	public Hashtable<Object, String> activities;
	public Hashtable<Object, String> objectifs;
	public Hashtable<Object, String> moyensRelation;
	public Hashtable<Object, String> moyensObjet;
	public Hashtable<Object, String> bricks;
	public Hashtable<Object, String> steps;
	public Hashtable<Object, String> actionTimes;
	//	final public Hashtable<Translatable, String> actorsSet;
	//	final private Hashtable<String, String> status;

	public StringTranslator() {
		actors = new Hashtable<Object, String>();
		groups = new Hashtable<Object, String>();
		activities = new Hashtable<Object, String>();
		objectifs = new Hashtable<Object, String>();
		moyensRelation = new Hashtable<Object, String>();
		moyensObjet = new Hashtable<Object, String>();
		bricks = new Hashtable<Object, String>();
		steps = new Hashtable<Object, String>();
		actionTimes = new Hashtable<Object, String>();
		//		actorsSet = new Hashtable<Translatable, String>();
		//		status = new Hashtable<String, String>();
		filepath = null;
	}

	private String getTranslatedString(Object object, Hashtable<Object, String> hashTables) {
		return hashTables.get(object);
	}

	public static String getNotTranslatedString(Object object) {
		String noTranlatedString;

		try {
			java.lang.reflect.Method method = object.getClass().getMethod("getNoTranslatedString"); //$NON-NLS-1$
			noTranlatedString = method.invoke(object).toString();
		} catch (Exception e) {
			if(object instanceof String) {
				noTranlatedString = (String)object;
			} else {
				System.out.println("Impossible de trouver le methode \"getNotTranslatedString\" dans l'objet + " + object.getClass().getName()); //$NON-NLS-1$
				e.printStackTrace();
				noTranlatedString = object.getClass().getName();
			}
		}

		return noTranlatedString;
	}

	private String getString(Object object, StringType type) {
		switch(type) {
		case actorType : return getTranslatedString(object, actors);
		case groupType : return getTranslatedString(object, groups);
		case moyenRelationType : return getTranslatedString(object, moyensRelation);
		case moyenObjetType : return getTranslatedString(object, moyensObjet);
		case activityType : return getTranslatedString(object, activities);
		case objectifType : return getTranslatedString(object, objectifs);
		case brickType : return getTranslatedString(object, bricks);
		case stepsType : return getTranslatedString(object, steps);
		case actionTimesType : return getTranslatedString(object, actionTimes);
		//			case actorSetType : return getTranslatedString(translatable, actorsSet);
		//			case statusType : return getTranslatedString(string, status);
		}

		return null;
	}

	public static String getTranslatedString(Object object, StringType type, StringTranslator translator) {
		if(translator == null) {
			return getNotTranslatedString(object);
		} else {
			return translator.getString(object, type);
		}
	}

	public static String getTranslatedString(Object object, StringType type) {
		if(Program.myMainFrame == null || (Program.isTriades() == false && Program.isTriadesLoading() == false)) {
			return getNotTranslatedString(object);
		} else {
			String result;
			if(Program.myMainFrame.datapack != null && Program.myMainFrame.datapack.getCurrentSession() != null) {
				if((result = Program.myMainFrame.datapack.getCurrentSession().getSessionActorName(object)) != null) {
					return result;
				}
			}

			return getDataPackTranslatedString(object, type);
		}
	}

	public static String getDataPackTranslatedString(Object object, StringType type) {
		if(Program.myMainFrame.getDataPack() == null) {
			return getNotTranslatedString(object)+ " øøø"; //$NON-NLS-1$
		}

		StringTranslator translator = Program.myMainFrame.getDataPack().getStringTranslator();
		String result = getTranslatedString(object, type, translator);
		if(result == null) {
			return getNotTranslatedString(object);
		}
		return result;		
	}

	private void printTable(String string, Hashtable<? extends Object, String> table) {
		for (Object object : table.keySet()) {
			System.out.println(string + " : " + getNotTranslatedString(object) + " -> " + table.get(object)); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public String getFilePath() {
		return filepath;
	}

	@Override
	public void setFilePath(String _filePath) {
		filepath = _filePath;		
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException{
		//boolean skipObjectives = false;
		//Object first = stream.readObject();
		//System.out.println("StringTranslator.readObject first.getClass : "+first.getClass());
		actionTimes = (Hashtable<Object, String>)stream.readObject();
		activities = (Hashtable<Object, String>)stream.readObject();
		actors = (Hashtable<Object, String>)stream.readObject();
		bricks = (Hashtable<Object, String>)stream.readObject();
		groups = (Hashtable<Object, String>)stream.readObject();
		
		if (Moyen.isOldModeSet())
		{
			Hashtable<Object, String> oldMoyen = (Hashtable<Object, String>) stream.readObject();
			Moyen.setOldHashCodeMode(false);
			moyensObjet = new Hashtable<Object, String>();
			moyensRelation = new Hashtable<Object, String>();
			for (Entry<Object, String> entry : oldMoyen.entrySet())
			{
				if (entry.getKey() instanceof Mean)
				{
					moyensRelation.put(entry.getKey(), entry.getValue());
				}
				else if (entry.getKey() instanceof Moyen)
				{
					moyensObjet.put(entry.getKey(), entry.getValue());
				}
				else
				{
					System.err.println("StringTranslator.readObject() : Unrecognized type of key : "+entry.getKey().getClass());
				}
			}

		}
		else
		{
			moyensObjet = (Hashtable<Object, String>)stream.readObject();
			moyensRelation = (Hashtable<Object, String>)stream.readObject();
		}
		//			try
		//			{
		//				while (true)
		//				{
		//					Object o = stream.readObject();
		//					Object v = stream.readObject();
		//					String s = null;
		//					if (v instanceof String)
		//						s = (String)v;
		//					if (s == null)
		//					{
		//						System.err.println("StringTranslator.readObject() : Means reconstitution : One of the value is not a String (class = "+v.getClass()+")");
		//						break;
		//					}
		//					if (o instanceof Mean)
		//					{
		//						moyensRelation.put(o, s);
		//					}
		//					else if (o instanceof Moyen)
		//					{
		//						moyensObjet.put(o,s);
		//					}
		//					else
		//					{
		//						System.err.println("StringTranslator.readObject() : Means reconstitution : One of the key is neither a Mean (relation), neither a Moyen (object) key class : "+o.getClass());
		//						break;
		//					}
		//				}
		//			}
		//			catch (OptionalDataException e)
		//			{
		//				System.err.println("Optional data length = "+e.length);
		//				System.err.println("Skipped bytes : "+stream.skipBytes(e.length+1)+" read return : "+stream.read());
		//				stream.defaultReadObject();
		//				
		//			}

		//			if (o.getClass().equals(Hashtable.class))
		//			{			
		//				objectifs = (Hashtable<Object, String>)o;
		//				skipObjectives = true;
		//			}

		objectifs = (Hashtable<Object, String>)stream.readObject();
		try {
		steps = (Hashtable<Object, String>)stream.readObject();
		} catch (EOFException error)
		{
			steps = new Hashtable<Object, String>();
		}
		

	}
	
	public boolean isEmpty()
	{
		if (!actors.isEmpty())
			return false;
		if (!groups.isEmpty())
			return false;
		if (!activities.isEmpty())
			return false;
		if (!objectifs.isEmpty())
			return false;
		if (!moyensObjet.isEmpty())
			return false;
		if (!moyensObjet.isEmpty())
			return false;
		if (!bricks.isEmpty())
			return false;
		if (!steps.isEmpty())
			return false;
		if (!actionTimes.isEmpty())
			return false;
		return true;
		
	}
}
