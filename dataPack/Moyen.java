package dataPack;

import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.Program;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JLabel;

import main.StatutObjet;
import translation.Messages;
import client.stringTranslator.StringTranslator;

public class Moyen extends MyDefaultMutableTreeNode implements Serializable,
		Content {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3631023820177794653L;

	protected String nom;
	protected Integer idMoyen;
	protected Integer idGenerique;
	protected Integer idFather;
	
	protected static boolean oldHashCodeMode = false;

	public Moyen(String nom, Integer id, Integer generique) {
		this.nom = nom;
		idMoyen = id;
		idGenerique = generique;
	}

	@Override
	public Integer getId() {
		return idMoyen;
	}

	public Integer getIdGenerique() {
		return idGenerique;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nouveauNom) {
		nom = nouveauNom;
	}

	public String getNomGenerique() {
		if (idGenerique == null || idGenerique.intValue() < 0
				|| idGenerique.intValue() > StatutObjet.values().length) {
			return Messages.getString("Moyen.0") + idGenerique + Messages.getString("Moyen.1"); //$NON-NLS-1$ //$NON-NLS-2$
		} else
			return StatutObjet.values()[idGenerique.intValue()].toString();
	}

	@Override
	public JLabel getJComponent(boolean selected, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		return TreeActorsCellRendere.createDefaultLabel(toString(), getIcon(false), selected);
	}

	@Override
	public Icon getIcon(boolean bigIcon) {
		return IconDatabase.vectorIconMoyensMin.get(idGenerique.intValue()
				% IconDatabase.vectorIconMoyensMin.size());
	}

	@Override
	public MyTreeNodeType getType() {
		return MyTreeNodeType.MoyenType;
	}

	@Override
	public void changeStringValue(String newString) {
		if (newString.compareTo(this.toString()) != 0) {
			setNom(newString);
		}
	}

	@Override
	public int hashCode() {
		if (oldHashCodeMode)
		{
			return super.hashCode();
		}
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idGenerique == null) ? 0 : idGenerique.hashCode());
		result = prime * result + ((idMoyen == null) ? 0 : idMoyen.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Moyen))
			return false;
		Moyen other = (Moyen) obj;
		if (idGenerique == null) {
			if (other.idGenerique != null)
				return false;
		} else if (!idGenerique.equals(other.idGenerique))
			return false;
		if (idMoyen == null) {
			if (other.idMoyen != null)
				return false;
		} else if (!idMoyen.equals(other.idMoyen))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}
	
	

	public String getNoTranslatedString() {
		return nom;
	}
	
	public static void setOldHashCodeMode(boolean newValue)
	{
		oldHashCodeMode = newValue;
	}
	
	@Override
	public String toString() {
		String result = "";
		if (Program.isTriades())
			result =  StringTranslator.getTranslatedString(this, StringTranslator.StringType.moyenObjetType);
		else
			result = StringTranslator.getTranslatedString(this, StringTranslator.StringType.moyenObjetType) + " (" + getNomGenerique() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	return result;
	}

	public static boolean isOldModeSet() {
		return oldHashCodeMode;
	}
	
	private Object writeReplace()
	{
		Integer idFather = null;
		if (parent != null)
			idFather = ((Groupe)parent).getIdGroupe();
		
		return new MoyenProxy(nom, idMoyen, idGenerique, idFather);
	}
	

	@Override
	public void validateObject() throws InvalidObjectException {
		Groupe father = DataPack.getLastLoadedDatapack().getActorsModel().getGroupeMoyens();
		if (father != null)
		{
		father.add(this);
		
		}
		else
		{
			System.out.println("Moyen.validateObject() : groupeMoyens null");
		}
	}
	
	
	protected static class MoyenProxy implements Serializable, ObjectInputValidation{

		private static final long serialVersionUID = 1L;

		protected String nom;
		protected Integer idMoyen;
		protected Integer idGenerique;
		protected Integer idFather;
		
		protected transient ObjectInputStream ois;
		
		protected static transient HashMap<Integer, Moyen> alreadyLoadedMoyen = null;
		
		public MoyenProxy(String nom, Integer idMoyen, Integer idGenerique, Integer idFather) {
			super();
			this.nom = nom;
			this.idMoyen = idMoyen;
			this.idGenerique = idGenerique;
			this.idFather = idFather;
		}
		
		private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
		{
			this.ois = ois;
			ois.defaultReadObject();
			
			if (alreadyLoadedMoyen == null)
			{
				alreadyLoadedMoyen = new HashMap<Integer, Moyen>();
				ois.registerValidation(this, -10);
			}
			
		}
		
		
		private Object readResolve() throws NotActiveException, InvalidObjectException
		{
			if (alreadyLoadedMoyen.containsKey(idMoyen))
				return alreadyLoadedMoyen.get(idMoyen);
			
			Moyen result = new Moyen(nom, idMoyen, idGenerique);
			alreadyLoadedMoyen.put(idMoyen, result);
			ois.registerValidation(result, 0);
			return result;
		}

		@Override
		public void validateObject() throws InvalidObjectException {
			alreadyLoadedMoyen = null;
			
		}
	}


	
}
