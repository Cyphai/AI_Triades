package main;

import translation.Messages;

public enum ObjectifMoyenObjet implements Objectif {

	estAdapteEff(Messages.getString("TriadeKernel.ObjectifMoyenObjet.0")), faciliRea(Messages.getString("TriadeKernel.ObjectifMoyenObjet.1")), cadreRea( //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.ObjectifMoyenObjet.2")), securiseRea(Messages.getString("TriadeKernel.ObjectifMoyenObjet.3")); //$NON-NLS-1$ //$NON-NLS-2$

	String nom;

	ObjectifMoyenObjet(String _nom) {
		nom = _nom;
	}

	@Override
	public String toString() {
		return nom;		
	}
}
