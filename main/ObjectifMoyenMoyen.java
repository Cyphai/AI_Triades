package main;

import translation.Messages;

public enum ObjectifMoyenMoyen implements Objectif {

	determineNorme(Messages.getString("TriadeKernel.ObjectifMoyenMoyen.0")), evaluePerti( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifMoyenMoyen.1")); //$NON-NLS-1$

	public String nom;

	private ObjectifMoyenMoyen(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}
}
