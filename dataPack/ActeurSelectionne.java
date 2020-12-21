package dataPack;

import translation.Messages;
import models.BrickVertex.VerticeRank;

public class ActeurSelectionne {

	final static int NOACTIVITE = -1;

	protected Acteur acteur; // indexé a leur idActeur

	protected VerticeRank rank;
	// protected Integer idActivite;

	public ActeurSelectionne(Acteur acteur) {
		this.acteur = acteur;

		rank = VerticeRank.primary;

		// idActivite = new Integer(NOACTIVITE);
	}

	public ActeurSelectionne(Acteur acteur, VerticeRank rank) {
		this.acteur = acteur;
		this.rank = rank;
		// idActivite = activite;
	}

public void setRank(VerticeRank newRank)
		{
	rank = newRank;
		}

	public VerticeRank getRank()
	{
		return rank;
	}
	

	public Acteur getActeur() {
		return acteur;
	}


	public String toString() {
		String r;
		if (rank==VerticeRank.primary)
			r = Messages.getString("ActeurSelectionne.0"); //$NON-NLS-1$
		else if (rank == VerticeRank.secondary)
			r = Messages.getString("ActeurSelectionne.1"); //$NON-NLS-1$
		else if (rank == VerticeRank.remaining)
			r = Messages.getString("ActeurSelectionne.2"); //$NON-NLS-1$
		else
			r = ""; //$NON-NLS-1$
		return acteur.toString()+ r;
	}


}
