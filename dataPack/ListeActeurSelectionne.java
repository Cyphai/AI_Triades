package dataPack;

import java.util.Vector;

import models.BrickVertex.VerticeRank;

public class ListeActeurSelectionne {

	protected Vector<ActeurSelectionne> acteurs;

	public ListeActeurSelectionne() {
		acteurs = new Vector<ActeurSelectionne>(5);
	}

	public void ajouterActeurSelectionne(ActeurSelectionne acteur) {
		if (acteur == null)
			return;
		else
			acteurs.add(acteur);
	}
	

	public void ajouterActeurSelectionne(Acteur newActor, VerticeRank rank) {
		ActeurSelectionne nouveau = new ActeurSelectionne(newActor, rank);
		acteurs.add(nouveau);
	}

	public void supprimerActeurSelectionne(ActeurSelectionne acteur) {
		acteurs.remove(acteur);
	}

	public Vector<ActeurSelectionne> getActorsSelection() {
		return acteurs;
	}

}
