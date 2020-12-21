package client;

import java.io.Serializable;

import dataPack.Acteur;

public class ActorSheet implements Serializable {
	private static final long serialVersionUID = -8186284219822784179L;

	protected Acteur actor;
	protected String actorName;
	protected String firstText;
	protected String secondText;
	protected String noteText;
	
	public ActorSheet(Acteur actor) {
		this.actor = actor;
		firstText = ""; //$NON-NLS-1$
		secondText = ""; //$NON-NLS-1$
	}

	public String getFirstText() {
		return firstText;
	}

	public void setFirstText(String firstText) {
		this.firstText = firstText;
	}

	public String getSecondText() {
		return secondText;
	}

	public void setSecondText(String secondText) {
		this.secondText = secondText;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getNoteText() {
		return noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public Acteur getActor() {
		return actor;
	}

	public void setActor(Acteur actor) {
		this.actor = actor;
	}
}
