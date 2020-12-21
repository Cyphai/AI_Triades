package main;

import java.io.Serializable;

import client.stringTranslator.StringTranslator;
import client.stringTranslator.StringTranslator.StringType;

public class ActionTime implements Serializable {

	// action1("time 1", 0), action2("time 2", 0), action3("time 2", 0),
	// action4("time 2", 0);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5435459194851632383L;
	public String nom;
	public Integer id;

	public ActionTime(String n_nom, Integer n_id) {
		nom = n_nom;
		id = n_id;
	}

	public String getNoTranslatedString() {
		return nom;
	}
	
	@Override
	public String toString() {
		return StringTranslator.getTranslatedString(this, StringType.actionTimesType);
	}

	public void setName(String newName) {
		nom = newName;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ActionTime))
			return false;
		ActionTime other = (ActionTime) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
