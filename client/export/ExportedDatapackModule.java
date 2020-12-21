package client.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import models.BrickVertex.VerticeRank;
import translation.Messages;
import client.Session;
import dataPack.Content;
import dataPack.DataPack;
import dataPack.MyDefaultMutableTreeNode;

public class ExportedDatapackModule implements Serializable {

	private static final long serialVersionUID = -120958692936379007L;
	protected Session fullSession;
	protected ArrayList<Session> sessions;
	protected DataPack datapack;

	public ExportedDatapackModule(DataPack datapack)
	{
		this.datapack = datapack;
		sessions = new ArrayList<Session>();
		
		HashMap<Content, VerticeRank> allActorSet = new HashMap<Content, VerticeRank>();
		for (MyDefaultMutableTreeNode a : datapack.getActorsModel().getAllActors())
		{
			allActorSet.put(a, VerticeRank.remaining);
		}
		
		fullSession = new Session(allActorSet, Messages.getString("ExportedDatapackModule.0"), datapack); //$NON-NLS-1$
		fullSession.initSession();
	}


	
	public Session addNewSession(Session newSession) {
		newSession.setDatapack(datapack);
		sessions.add(newSession);
		return newSession;
	}
	

	public ArrayList<Session> getSessionList() {
		return sessions;
	}

	public Session getFullSession() {

		return fullSession;
	}

	public void upSession(Session selectedSession) {
		int index = sessions.indexOf(selectedSession);
		if (index > 0) {
			sessions.remove(index);
			sessions.add(0, selectedSession);
		}
	}

}
