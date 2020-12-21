package dataPack;

import graphicalUserInterface.ExecutionMode;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;

import translation.Messages;
import client.stringTranslator.StringTranslator;
import client.stringTranslator.StringTranslator.StringType;


public class ActeurBase extends Acteur implements ObjectInputValidation{
	private static final long serialVersionUID = 1L;

	private final static int BASE_GROUPE_ID = 20;
	private final static int ACTEUR_MILIEU_GROUPE_ID = 30;

	protected JeuActeur jeuActeur;
	protected String corpsMetier;
	protected ActeurBase base;
	protected boolean manuallyRenamed;

	protected transient String jeuActeurName;
	protected transient Integer baseId;

	private ActeurBase(String n_poste, Integer statut, Integer groupe,
			JeuActeur jeuActeur, String corpsMetier, ActeurBase base) {
		super(n_poste, statut, groupe);

		this.base = base;
		this.corpsMetier = corpsMetier;
		this.jeuActeur = jeuActeur;
		manuallyRenamed = false;
	}

	/**
	 * Base
	 * 
	 * @param poste
	 * @param statut
	 * @param jeuActeur
	 */
	public ActeurBase(String poste, Integer statut, JeuActeur jeuActeur) {
		this(poste, statut, BASE_GROUPE_ID, jeuActeur, null, null);
	}

	/**
	 * Corps
	 * 
	 * @param corpsMetier
	 *            : corps de l'acteur
	 * @param base
	 *            : Base de l'acteur
	 */
	public ActeurBase(String corpsMetier, ActeurBase base) {
		this(null, base.getIdStatut(), ACTEUR_MILIEU_GROUPE_ID, base.jeuActeur,
				corpsMetier, base);
	}

	protected ActeurBase(String poste, Integer id, Integer idStatut, Integer idGroupe, String nomJeuActeur, String corps, Integer baseId, boolean manuallyRenamed)
	{
		super(poste, id, idStatut, idGroupe);
		base = null;
		jeuActeur = null;
		corpsMetier = corps;
		jeuActeurName = nomJeuActeur;
		this.baseId = baseId;
		this.manuallyRenamed = manuallyRenamed;
	}


	public ActeurBase getBase() {
		return base;
	}

//	public boolean equals(ActeurBase acteurBase) {
//		if (super.equals(acteurBase))
//				
//				
//				&& jeuActeur == acteurBase.jeuActeur
//				&& ((corpsMetier == null &&  acteurBase.corpsMetier == null)))
//				return true;
//				
//		if (corpsMetier != null && corpsMetier.equals(acteurBase.corpsMetier)))
//						&& base == acteurBase.base;
//	}

	public boolean isBase() {
		return base == null;
	}

	@Override
	public MyTreeNodeType getType() {
		if (isBase()) {
			return MyTreeNodeType.BaseType;
		} else {
			return MyTreeNodeType.CorpsType;
		}
	}

	public JeuActeur getJeuActeur() {
		return jeuActeur;
	}

	public String getCorpsMetier() {
		return corpsMetier;
	}

	public void setCorpsMetier(String corpsMetier) {
		this.corpsMetier = corpsMetier;
	}

	@Override
	public void setPoste(String newPoste) {
		manuallyRenamed = true;
		super.setPoste(newPoste);
	}

	@Override
	public void changeStringValue(String newString) {
		setPoste(newString);
	}

	@Override
	public Integer getIdStatut() {
		if (isBase())
			return idStatut;
		else
			return base.getIdStatut();
	}

	@Override
	public String getNoTranslatedString() {
		if (poste != null)
			return poste;

		if (base == null) {
			return Messages.getString("ActeurBase.0"); //$NON-NLS-1$
		} else {
			return base + " " + corpsMetier; //$NON-NLS-1$
		}
	}

	@Override
	public String toString() {
		String result = StringTranslator.getTranslatedString(this, StringType.actorType);
//		if (ExecutionMode.isDebug()) {
//			result += " id : "+getId() +" corps : "+getCorpsMetier();
//		}
//		if (getBase() != null) {
//			result += " base : "+getBase().poste;
//		}

		return result;
	}


	protected Object writeReplace() 
	{
		return new ActeurBaseProxy(this);
	}
	
	@Override
	public void validateObject() throws InvalidObjectException {

		super.validateObject();
		if (jeuActeurName != null && jeuActeurName.length() > 0)
		{
			jeuActeur = DataPack.getLastLoadedDatapack().getJeuActeurByName(jeuActeurName);
		}


		if (baseId != null)
		{
			for (ActeurBase b : jeuActeur.getListeBase())
			{
				if (b.getId().equals(baseId))
					base = b;
			}
			if (base == null)
			{
				System.err.println("ActeurBase.validateObject : baseId != null but all base of jeuActor have different ids");
			}
		}
	}


	protected static class ActeurBaseProxy extends ActeurProxy{


			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			protected String nomJeuActeur;
			protected String corpsMetier;
			protected Integer baseId;
			protected boolean manuallyRenamed;


			protected transient ObjectInputStream ois;

			protected ActeurBaseProxy(ActeurBase acteur) {
				super(acteur);
				if (acteur.parent != null)
				{
					idGroupe = ((Groupe)acteur.parent).getId();
				}
				nomJeuActeur = acteur.getJeuActeur().getNom();
				corpsMetier = acteur.corpsMetier;
				if (acteur.base != null)
					baseId = acteur.base.getId();
				else
					baseId = null;
				manuallyRenamed = acteur.manuallyRenamed;
			}

			private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
			{
				this.ois = ois;
				ois.defaultReadObject();
			}

			protected Object readResolve() throws NotActiveException, InvalidObjectException
			{
				ActeurBase result = new ActeurBase(poste, idActeur, idStatut, idGroupe, nomJeuActeur, corpsMetier, baseId, manuallyRenamed);
				ois.registerValidation(result, 0);
				return result;
			}

		}




	}
