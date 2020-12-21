package main;

import translation.Messages;

public enum ObjectifObjetActeur implements Objectif {
	evalCompetences(Messages.getString("TriadeKernel.ObjectifObjetActeur.0")); //$NON-NLS-1$

	public String nom;

	private ObjectifObjetActeur(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}

}
