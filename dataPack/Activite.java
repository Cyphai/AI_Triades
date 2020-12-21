package dataPack;

import java.io.Serializable;

import client.stringTranslator.StringTranslator;

public class Activite implements Serializable, Content {

	private static final long serialVersionUID = 5295995249408229062L;


	protected String nom;

	protected int iconId;

	@SuppressWarnings("unused")
	private Activite() {
		this(null, -1);
	}

	public Activite(String nom, int iconId) {
		this.nom = nom;
		this.iconId = iconId;
	}

	public int getIconId() {
		return iconId;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Activite))
			return false;
		Activite other = (Activite) obj;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	public String getNoTranslatedString() {
		return nom;
	}
	
	@Override
	public String toString() {
		return StringTranslator.getTranslatedString(this, StringTranslator.StringType.activityType);
	}
}
