package dataPack;

import graphicalUserInterface.Program;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import translation.Messages;

public class MoyenEntreprise implements Serializable {

	private static final long serialVersionUID = -1013542159489711501L;

	final static Integer DEBUTINDICEID = -50;

	protected Hashtable<Integer, Moyen> moyens;

	public MoyenEntreprise() {
		moyens = new Hashtable<Integer, Moyen>(0);
	}

	public Moyen ajouterMoyen(Integer idGenerique, String nom) {
		Integer id = new Integer(MoyenEntreprise.DEBUTINDICEID - moyens.size());
		Moyen newMoyen = new Moyen(nom, id, idGenerique);
		moyens.put(id, newMoyen);

		Program.myMainFrame.getDataPack().getActorsModel().ajouterMoyen(
				newMoyen);

		return newMoyen;
	}

	public void supprimerMoyen(Integer idMoyen) {
		Program.myMainFrame.getDataPack().getActorsModel()
				.removeNodeFromParent(getMoyen(idMoyen));
		moyens.remove(idMoyen);
	}

	public Vector<Moyen> getMoyenVector() {
		return new Vector<Moyen>(moyens.values());
	}

	public Moyen getMoyen(Integer idMoyen) {
		return moyens.get(idMoyen);
	}

	public int getNbMoyen() {
		return moyens.size();
	}

	public String getName(Integer id) {
		Moyen moyen = moyens.get(id);
		if (moyen == null) {
			System.err.println("!!! getName sur un Moyen inexistant !!!"); //$NON-NLS-1$
			return Messages.getString("MoyenEntreprise.1"); //$NON-NLS-1$
		}

		return moyens.get(id).getNom();
	}
}
