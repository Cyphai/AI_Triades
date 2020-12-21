package main;

import translation.Messages;


public enum ObjectifFormesDecisionnelles implements Objectif {

/*	sujetRaisonneur(Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.0")), sujetActeur( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.1")), sujetDecideur( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.2")), defensive( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.3")), universRelation( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.4")); //$NON-NLS-1$
*/
	sujetActeur("Se centrer sur l'acteur"); //$NON-NLS-1$
	
	protected String nom;

	private ObjectifFormesDecisionnelles(String _nom) {
		nom = _nom;
	}

	@Override
	public String toString() {
		return nom;
	}
}
