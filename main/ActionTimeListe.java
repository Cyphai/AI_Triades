package main;

import java.io.Serializable;
import java.util.Vector;

public class ActionTimeListe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1062344209989758490L;
	protected Vector<ActionTime> actionTimes;
	
	//TODO supprimer cette ligne lorsque l'on aura plus besoin de récupérer les anciens temps d'actions.
	protected Vector<ActionTime>actionsTime;
	
	public ActionTimeListe() {
		actionTimes = new Vector<ActionTime>();
	}

	public ActionTime addActionTime(String name) {
		ActionTime newActionTime = new ActionTime(name, actionTimes.size());
		actionTimes.add(newActionTime);
		return newActionTime;
	}

	public Vector<ActionTime> values() {
		return actionTimes;
	}

	public void removeActionTime(ActionTime aT) {
		actionTimes.remove(aT);
	}
	
	public ActionTime getActionTimeByName(String name)
	{
		for (ActionTime aT : actionTimes)
		{
			if (aT.toString().equals(name))
				return aT;
		}
		return null;
	}

	public void restoreFromOldVersion() {
		
		actionTimes = actionsTime;
		actionsTime = null;
		
	}

}
