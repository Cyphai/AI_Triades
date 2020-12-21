package relations;

import java.io.Serializable;

import client.stringTranslator.StringTranslator;

public class Goal implements Comparable<Goal>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2333173474899739781L;
	public String name;
	
	public Goal(String name)
	{
		this.name = name;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public boolean equals(Goal other)
	{
		return other.name.equals(name);
	}
	
	public String getNoTranslatedString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Goal))
			return false;
		Goal other = (Goal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return StringTranslator.getTranslatedString(this, StringTranslator.StringType.objectifType);
	}

	@Override
	public int compareTo(Goal o) {
		return name.compareTo(o.name);
	}
}
