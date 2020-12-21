package main;

import translation.Messages;

public enum StatutObjet {
	nonDef(Messages.getString("TriadeKernel.StatutObjet.0"), new Integer(0)), outils(Messages.getString("TriadeKernel.StatutObjet.1"), new Integer(1)), methode( //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("TriadeKernel.StatutObjet.2"), new Integer(2)), stocks(Messages.getString("TriadeKernel.StatutObjet.3"), //$NON-NLS-1$ //$NON-NLS-2$
			new Integer(3));

	public String nom;
	public Integer id;

	private StatutObjet(String _nom, Integer _id) {
		nom = _nom;
		id = _id;
	}

	public String toString() {
		return nom;
	}

}
