package main;

import translation.Messages;

public enum ObjectifLienHierarchique implements Objectif {

	defRegles(Messages.getString("TriadeKernel.ObjectifLienHierarchique.0")), evalCompetences( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.1")), donnerConsigne(Messages.getString("TriadeKernel.ObjectifLienHierarchique.2")), evalutationPolitique( //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.3")), definitionPolitique(Messages.getString("TriadeKernel.ObjectifLienHierarchique.4")), delegationActivite( //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.5")), delegationMission(Messages.getString("TriadeKernel.ObjectifLienHierarchique.6")), controleExecutionActivite( //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.7")), controleUsageOutils( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.8")), developpementCompetences( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.9")), resolutionConflit( //$NON-NLS-1$
			Messages.getString("TriadeKernel.ObjectifLienHierarchique.10")), resolutionProbleme(Messages.getString("TriadeKernel.ObjectifLienHierarchique.11")); //$NON-NLS-1$ //$NON-NLS-2$

	public String nom;

	private ObjectifLienHierarchique(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}

}
