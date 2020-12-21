package main;

import translation.Messages;

public enum ObjectifActeurMoyen implements Objectif {

	utilise(Messages.getString("TriadeKernel.ObjectifActeurMoyen.0")), applique(Messages.getString("TriadeKernel.ObjectifActeurMoyen.1")); //$NON-NLS-1$ //$NON-NLS-2$

	public String nom;

	private ObjectifActeurMoyen(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}
}
