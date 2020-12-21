package main;

import translation.Messages;

public enum ObjectifRetourLienHierarchique implements Objectif {

	negociation(
			Messages.getString("TriadeKernel.ObjectifRetourLienHierarchique.0")), compteRendu( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifRetourLienHierarchique.1")); //$NON-NLS-1$

	public String nom;

	private ObjectifRetourLienHierarchique(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}

}
