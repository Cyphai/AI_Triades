package main;

import translation.Messages;

public enum ObjectifObjetMoyen implements Objectif {

	evaluePerti(Messages.getString("TriadeKernel.ObjectifObjetMoyen.0")), valideProc(Messages.getString("TriadeKernel.ObjectifObjetMoyen.1")); //$NON-NLS-1$ //$NON-NLS-2$

	public String nom;

	private ObjectifObjetMoyen(String _nom) {
		nom = _nom;
	}

	public String toString() {
		return nom;
	}
}
