package main;

import translation.Messages;

public enum Statut {
	nonDef(0, Messages.getString("TriadeKernel.Statut.0")), directeurGeneral(1, Messages.getString("TriadeKernel.Statut.1"), 1), directeur( //$NON-NLS-1$ //$NON-NLS-2$
			2, Messages.getString("TriadeKernel.Statut.2"), 2), chefDeService(3, Messages.getString("TriadeKernel.Statut.3"), 3), chefEquipe( //$NON-NLS-1$ //$NON-NLS-2$
			4, Messages.getString("TriadeKernel.Statut.4"), 4), cadreIntermediaire(5, //$NON-NLS-1$
			Messages.getString("TriadeKernel.Statut.5"), 5), technicienSuperieur(6, //$NON-NLS-1$
			Messages.getString("TriadeKernel.Statut.6"), 6), ouvrierSpecialise(7, //$NON-NLS-1$
			Messages.getString("TriadeKernel.Statut.7"), 7), deleguePersonnel(8, //$NON-NLS-1$
			Messages.getString("TriadeKernel.Statut.8")), client(9, Messages.getString("TriadeKernel.Statut.9")), auditeur(10, //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.Statut.10")), psychologue(11, Messages.getString("TriadeKernel.Statut.11")), conseilJuridique(12, //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.Statut.12")), orgaSyndicale(13, Messages.getString("TriadeKernel.Statut.13")), partenaire( //$NON-NLS-1$ //$NON-NLS-2$
			14, Messages.getString("TriadeKernel.Statut.14")), formateur(15, Messages.getString("TriadeKernel.Statut.15")), medecinTravail(16, //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.Statut.16")), chefProjet(17, Messages.getString("TriadeKernel.Statut.17")); //$NON-NLS-1$ //$NON-NLS-2$

	public String nom;
	public int rang;
	public Integer id;

	private Statut(int _id, String _nom) {
		this(_id, _nom, -1);
	}

	private Statut(int _id, String _nom, int _rang) {
		nom = _nom;
		rang = _rang;
		id = new Integer(_id);
	}

	public String toString() {
		return nom;
	}

}
