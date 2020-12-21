package main;

import translation.Messages;

public enum ObjectifLienPartenaire implements Objectif {
	atteindreAccord(Messages.getString("TriadeKernel.ObjectifLienPartenaire.0")), appliquerAccord( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifLienPartenaire.1")), controlerAccord(Messages.getString("TriadeKernel.ObjectifLienPartenaire.2")); //$NON-NLS-1$ //$NON-NLS-2$

	public String nom;
	public String[] moyens;

	private ObjectifLienPartenaire(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}
}
