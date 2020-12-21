package dataPack;

import graphicalUserInterface.Program;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public class JeuActeur implements Serializable, ObjectInputValidation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5603838022343558666L;
	protected Vector<String> corpsMetier;
	protected Vector<ActeurBase> bases;
	protected Map<String, Vector<ActeurBase>> acteurs;
	protected String nom;

	protected Groupe groupeJeuActeur;
	protected Map<String, GroupeJeuActeur> groupesCorps;
	ModelActeursEntreprise modelActeur;

	public JeuActeur(String _nom) {
		nom = _nom;
		corpsMetier = new Vector<String>();
		bases = new Vector<ActeurBase>();
		acteurs = new TreeMap<String, Vector<ActeurBase>>();

		groupeJeuActeur = new GroupeRoot(_nom);
		groupesCorps = new HashMap<String, GroupeJeuActeur>();
		modelActeur = Program.myMainFrame.getDataPack().getActorsModel();
		modelActeur.ajouterGroupe(groupeJeuActeur, modelActeur.getRoot());
	}

public ActeurBase ajouterBase(String nom, Integer idStatut) {
		ActeurBase nouvelleBase = new ActeurBase(nom, idStatut, this);

		for (Entry<String, Vector<ActeurBase>> corps : acteurs.entrySet()) {
			ActeurBase nouveauCorp = new ActeurBase(corps.getKey(),
					nouvelleBase);
			corps.getValue().add(nouveauCorp);

			modelActeur.ajouterCorps(nouveauCorp, this);
		}

		bases.add(nouvelleBase);

		modelActeur.ajouterBase(nouvelleBase, this);
		return nouvelleBase;
	}

	public void supprimerBase(ActeurBase oldBase) {
		int index = bases.indexOf(oldBase);
		if (index != -1) {
			for (Entry<String, Vector<ActeurBase>> corps : acteurs.entrySet()) {
				Vector<ActeurBase> vecteurActeur = corps.getValue();
				if (vecteurActeur.elementAt(index).getBase().equals(oldBase)) {
					vecteurActeur.remove(index);

					modelActeur.removeNodeFromParent(vecteurActeur
							.elementAt(index));
				} else {
					for (ActeurBase acteur : vecteurActeur) {
						if (acteur.getBase().equals(oldBase)) {
							vecteurActeur.remove(acteur);
							modelActeur.removeNodeFromParent(acteur);
							break;
						}
					}
				}

			}
			bases.remove(index);

			modelActeur.removeNodeFromParent(oldBase);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void ajouterCorps(String nom) {
		corpsMetier.add(nom);
		Vector<ActeurBase> nouveauCorps = new Vector<ActeurBase>();

		GroupeJeuActeur newGroupeCorps = new GroupeJeuActeur(nom, this);
		groupesCorps.put(nom, newGroupeCorps);
		modelActeur.ajouterGroupe(newGroupeCorps, groupeJeuActeur);

		for (ActeurBase acteurBase : bases) {
			ActeurBase newCorps = new ActeurBase(nom, acteurBase);
			nouveauCorps.add(newCorps);

			modelActeur.ajouterCorps(newCorps, this);
		}
		acteurs.put(nom, nouveauCorps);
	}

	public boolean modifierCorps(String oldCorps, String newCorps) {
		Vector<ActeurBase> acteursCorps = acteurs.get(oldCorps);
		if (acteursCorps == null)
			return false;

		for (ActeurBase acteur : acteursCorps) {
			acteur.setCorpsMetier(newCorps);

			modelActeur.nodeChanged(acteur);
		}

		int index = corpsMetier.indexOf(oldCorps);
		corpsMetier.remove(index);
		corpsMetier.add(index, newCorps);

		acteurs.remove(oldCorps);
		acteurs.put(newCorps, acteursCorps);

		GroupeJeuActeur groupeCorps = getGroupeCorps(oldCorps);
		groupesCorps.remove(groupeCorps);
		groupeCorps.setNom(newCorps);
		groupesCorps.put(newCorps, groupeCorps);

		return true;
	}

	public void supprimerCorps(String nom) {
		if (acteurs.get(nom) == null) {
			throw new IllegalArgumentException();
		}
		acteurs.remove(nom);

		DefaultMutableTreeNode groupeCorps = groupesCorps.get(nom);
		modelActeur.removeNodeFromParent(groupeCorps);
		groupesCorps.remove(nom);
	}

	public void computeName() {
		for (Entry<String, Vector<ActeurBase>> entry : acteurs.entrySet()) {
			for (ActeurBase aB : entry.getValue()) {
				if (aB.getPoste() == null || !aB.manuallyRenamed) {
					aB.setPoste(aB.base.toString() + " " + aB.corpsMetier); //$NON-NLS-1$
					aB.manuallyRenamed = false;
				}
			}
		}
	}


	public ActeurBase getRealActor(ActeurBase base, String nomCorps) {
		Vector<ActeurBase> selectedCorps = acteurs.get(nomCorps);
		for (ActeurBase acteurBase : selectedCorps) {
			if (acteurBase.getBase().equals(base)) {
				return acteurBase;
			}
		}

		return null;

	}

	@Override
	public String toString() {
		return nom;
	}

	public Vector<ActeurBase> getCorps(String name) {
		return acteurs.get(name);
	}

	public GroupeJeuActeur getGroupeCorps(String name) {
		return groupesCorps.get(name);
	}

	public Collection<GroupeJeuActeur> getGroupesCorps() {
		return groupesCorps.values();
	}

	public Groupe getGroupeBase() {
		return groupeJeuActeur;
	}

	public Groupe getGroupeJeuActeur() {
		return groupeJeuActeur;
	}

	public Vector<String> getListeCorps() {
		return corpsMetier;
	}

	public Vector<ActeurBase> getListeBase() {
		return bases;
	}

	public void setNom(String nouveauNom) {
		nom = nouveauNom;
	}

	public Integer getCorpsId(String nomCorps, Integer baseId) {
		Vector<ActeurBase> corps = getCorps(nomCorps);

		if (corps == null) {
			throw new IllegalArgumentException(nomCorps + " n'est pas un corps de ce jeu d'acteur"); //$NON-NLS-1$
		}

		for (ActeurBase actor : corps) {
			if (actor.getBase().getId().equals(baseId)) {
				return actor.getId();
			}
		}

		throw new IllegalArgumentException("Impossible de trouver le corps de cette base : nomCorps = " + nomCorps + ", baseId = " + baseId); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public int getBaseIndexByName(String baseName) {
		for (int i = 0; i < bases.size(); i++) {

			if (bases.elementAt(i).getPoste().equals(baseName))
				return i;
		}
		return -1;
	}

	public String getNom() {
		return nom;
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
		ois.registerValidation(this, 1);
	}

	@Override
	public void validateObject() throws InvalidObjectException {
		DataPack.getLastLoadedDatapack().getActorsModel().getRoot().add(groupeJeuActeur);
		
	}
}