package dataPack;

import java.io.Serializable;
import java.util.Vector;

public class ActivitesEntreprise implements Serializable {
	private static final long serialVersionUID = 5347414089243015128L;

	final static Integer DEBUTINDICEID = -100;

	protected Vector<Activite> activities;

	public ActivitesEntreprise() {
		activities = new Vector<Activite>(0);
	}

	public Activite ajouterActivite(String nom, int iconId) {
		
		Activite newActivity = new Activite(nom, iconId);
		activities.add(newActivity);

		return newActivity;
	}

	public void supprimerActivite(Activite activite) {
		activities.remove(activite);
	}

	public Vector<Activite> getActivities() {
		return activities;
	}

	public int getNbActivites() {
		return activities.size();
	}

	public boolean renameActivity(String oldName, String newName) {
		for (Activite act : activities)
		{
			if (act.toString().equals(newName))
				return false;
		}

		for (Activite act : activities)
		{
			if (act.toString().equals(oldName))
			{
				act.setNom(newName);
				return true;
			}	
		}		
		
		return false;
	}

}
