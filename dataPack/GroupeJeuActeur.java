package dataPack;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.Serializable;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

public class GroupeJeuActeur extends Groupe implements ObjectInputValidation{
	private static final long serialVersionUID = 7272453862281473980L;

	protected JeuActeur jeuActeur;
	protected String nomJeuActeur;

	GroupeJeuActeur(String nNom, JeuActeur jeuActeur) {
		super(nNom);
		this.jeuActeur = jeuActeur;
		nomJeuActeur = jeuActeur.getNom();
	}

	protected GroupeJeuActeur(String name, Integer idGroup, Integer idFather, String actorSetName)
	{
		super(name, idGroup, idFather);
		nomJeuActeur = actorSetName;
	}

	@Override
	public void changeStringValue(String newString) {
		if (jeuActeur != null)
			jeuActeur.modifierCorps(nom, newString);
		else 
			System.err.println("GroupeJeuActeur.changeStringValue : jeuActeur null, verifier que la validation après chargement s'est déroulée correctement");

	}
//	
//	@Override
//	public String toString() {
//		return super.toString()+"(GJA "+jeuActeur+")";
//	}

	@Override
	public void validateObject() throws InvalidObjectException {
		if (!addedToGroupList) {
			super.validateObject();
		}
		
		
		if (jeuActeur == null)
			jeuActeur = DataPack.getLastLoadedDatapack().getJeuActeurByName(nomJeuActeur);

	}

	protected Object writeReplace()
	{
		return new GroupeJeuActeurProxy(this);
	}

	private static class GroupeJeuActeurProxy implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected String name;
		protected String actorSetName;
		protected Integer idGroup;
		protected Integer idFather;
		protected transient ObjectInputStream ois;

		protected GroupeJeuActeurProxy(GroupeJeuActeur group)
		{
			if (group.nomJeuActeur != null && group.nomJeuActeur.length() > 0)
				actorSetName = group.nomJeuActeur;
			else
				actorSetName = group.jeuActeur.getNom();
			name = group.getNom();
			idGroup = group.getIdGroupe();
			if (group.idFather != null)
			{
				idFather = group.idFather;
			}
			else
			{
				if (group.parent != null)
				{
					idFather = ((Groupe)group).getId();
				}
			}

		}

		private Object readResolve() throws NotActiveException, InvalidObjectException
		{
			GroupeJeuActeur result = new GroupeJeuActeur(name, idGroup, idFather, actorSetName);
			ois.registerValidation(result, 0);
			ois.registerValidation(result, 1);

			return result;
		}

		private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
		{
			this.ois = ois;
			ois.defaultReadObject();
		}

	}


}
