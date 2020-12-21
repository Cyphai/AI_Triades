package main;

import translation.Messages;

public enum ObjectifMoyenActeur implements Objectif {

	evalComp(Messages.getString("TriadeKernel.ObjectifMoyenActeur.0")), defNormes( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifMoyenActeur.1")), securiseAction( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifMoyenActeur.2")); //$NON-NLS-1$

	public String nom;

	private ObjectifMoyenActeur(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}
}
