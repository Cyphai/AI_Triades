package dataPack;

import graphicalUserInterface.ExecutionMode;
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

import main.Statut;
import client.stringTranslator.StringTranslator;

/*
 * défini un acteur pour la liste de selection des acteur
 */

public class Acteur extends MyDefaultMutableTreeNode implements Content,
		Serializable, Comparable<Acteur> {

	private static final long serialVersionUID = -7013072226067306012L;

	protected String poste;
	protected Integer idActeur;
	protected Integer idStatut;
	protected Integer idGroupe;

	public Acteur(String n_poste, Integer n_idStatut, Integer groupe) {
		super();
		poste = n_poste;
		idActeur = Program.myMainFrame.getDataPack().getNewActorId();

		if (n_idStatut == null)
			System.out.println(n_idStatut);
		else
			idStatut = n_idStatut;

		idGroupe = groupe;

		Program.myMainFrame.getDataPack().addAnActor(this);
	}
	
	

	
	
	public Acteur(String poste, Integer idActeur, Integer idStatut,
			Integer idGroupe) {
		super();
		this.poste = poste;
		this.idActeur = idActeur;
		this.idStatut = idStatut;
		this.idGroupe = idGroupe;
		
	}





	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getPoste() {
		return poste;
	}

	public void setIdActeur(Integer idActeur) {
		this.idActeur = idActeur;
	}

	

	public Statut getStatut() {
		return main.Statut.values()[getIdStatut()];
	}

	public void setIdStatut(Integer idStatut) {
		this.idStatut = idStatut;
	}

	public Integer getIdStatut() {
		return idStatut;
	}

	public void setIdGroupe(Integer groupe) {
		idGroupe = groupe;
	}

	public Integer getIdGroupe() {
		return idGroupe;
	}

	public boolean estDansGroupe(Integer groupe) {
		return (idGroupe == groupe);
	}

	public int compareTo(Acteur acteur) {
		if (acteur.getId().equals(idActeur))
			return 0;
		else if (acteur.getId().intValue() < idActeur.intValue())
			return 1;
		else {
			return -1;
		}
	}

	

	@Override
	public JLabel getJComponent(boolean selected, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		return TreeActorsCellRendere.createDefaultLabel(this.toString(),
				getIcon(false), selected);
	}

	@Override
	public Icon getIcon(boolean bigIcon) {
		if(bigIcon) {
			return IconDatabase.vectorIconActorsBig.get((idGroupe
					% (IconDatabase.vectorIconActorsBig.size()-1))+1);
		} else {
			return IconDatabase.vectorIconActorsMin.get(idGroupe
					% IconDatabase.vectorIconActorsMin.size());
		}
	}

	@Override
	public MyTreeNodeType getType() {
		return MyTreeNodeType.ActorType;
	}

	@Override
	public void changeStringValue(String newString) {
		if (newString.compareTo(this.toString()) != 0) {
			setPoste(newString);
		}
	}

	@Override
	public Integer getId() {
		return idActeur;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idActeur == null) ? 0 : idActeur.hashCode());
		result = prime * result
				+ ((idStatut == null) ? 0 : idStatut.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Acteur) {
			return ((Acteur) object).idActeur.equals(idActeur);
		}
		return false;
	}


	public String getNoTranslatedString() {
		if (Program.isTriades())
			return poste;
		else 
			return poste + "(" + getStatut().toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String toString() {
		String result = StringTranslator.getTranslatedString(this, StringTranslator.StringType.actorType);
//		if (ExecutionMode.isDebug()) {
//			result+= " id : "+getId().intValue(); //$NON-NLS-1$
//		}
		return result;
	}
	
	private Object writeReplace()
	{
		return new ActeurProxy(this);
	}
	
	
	public static class ActeurProxy implements Serializable, ObjectInputValidation{
		

		private static final long serialVersionUID = 1L;
		protected String poste;
		protected Integer idActeur;
		protected Integer idStatut;
		protected Integer idGroupe;
		
		protected static HashMap<Integer, Acteur> alreadyLoadedActors = null;
		
		protected transient ObjectInputStream ois;
		
		protected ActeurProxy(Acteur acteur)
		{
			poste = acteur.poste;
			idActeur = acteur.idActeur;
			idStatut = acteur.idStatut;
			idGroupe = acteur.idGroupe;
		}
		
		private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
		{
			this.ois = ois;
			ois.defaultReadObject();
			
			if (alreadyLoadedActors == null)
			{
				ois.registerValidation(this, -10);
				alreadyLoadedActors = new HashMap<Integer, Acteur>();
			}
		}
		
		private Object readResolve() throws NotActiveException, InvalidObjectException
		{
			if (alreadyLoadedActors.containsKey(idActeur))
				return alreadyLoadedActors.get(idActeur);
			
			Acteur result = new Acteur(poste,idActeur,idStatut, idGroupe);
			alreadyLoadedActors.put(idActeur, result);
			ois.registerValidation(result, 0);
			return result;
		
		}

		@Override
		public void validateObject() throws InvalidObjectException {
			alreadyLoadedActors = null;
			
		}
		
	}


	@Override
	public void validateObject() throws InvalidObjectException {
		Groupe father = DataPack.getLastLoadedDatapack().getActorsModel().getGroupById(idGroupe);
		if (father != null)
		{
			father.add(this);
		}
		else
		{
			throw new IllegalStateException("An actor have no father during validation");
		}
	}
	
}
