package main;

import translation.Messages;

public enum ObjectifActeurObjet implements Objectif {

	conception(Messages.getString("TriadeKernel.ObjectifActeurObjet.0")), utilisation(Messages.getString("TriadeKernel.ObjectifActeurObjet.1")), recupInfo( //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.ObjectifActeurObjet.2")), eval(Messages.getString("TriadeKernel.ObjectifActeurObjet.3")); //$NON-NLS-1$ //$NON-NLS-2$

	public String nom;

	private ObjectifActeurObjet(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}

}
