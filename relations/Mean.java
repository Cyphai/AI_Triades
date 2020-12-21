package relations;

import java.io.Serializable;

import client.stringTranslator.StringTranslator;

public class Mean implements Comparable<Mean>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5819830618061598223L;
	public String name;
	
	public Mean(String name)
	{
		this.name = name;
	}
	
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public boolean equals(Mean other)
	{
		return other.name.trim().equalsIgnoreCase(name.trim());
	}
	
	@Override
	public String toString()
	{
		String result = StringTranslator.getTranslatedString(this, StringTranslator.StringType.moyenRelationType);
		return result;
	}

	public String getNoTranslatedString() {
		return name;
	}
	
	@Override
	public int compareTo(Mean o) {

		return name.compareTo(o.name);
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
		if (!(obj instanceof Mean))
			return false;
		Mean other = (Mean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
