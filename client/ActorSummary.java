package client;

import graphicalUserInterface.Program;

import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import translation.Messages;

import main.RelationComplete;
import models.Brick;
import dataPack.Acteur;

public class ActorSummary extends JEditorPane implements HyperlinkListener, ActorSheetSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7905664357195268284L;

	protected Session session;
	protected TreeMap<String, Brick> linkMap;
	protected int linkCounter;

	public ActorSummary()
	{
		super("text/html",""); //$NON-NLS-1$ //$NON-NLS-2$
		setEditable(false);
		addHyperlinkListener(this);
		linkMap = new TreeMap<String, Brick>();
		linkCounter = 0;
		session = Program.myMainFrame.datapack.getCurrentSession();
		if (!session.isValid())
		{
			throw new IllegalStateException("Invalid session when building an ActorSummary"); //$NON-NLS-1$
		}


	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

			String s = e.getDescription().toString();
			if (linkMap.containsKey(s))
			{
				Program.myMainFrame.addTab(linkMap.get(s));
			}
			else
			{
				System.err.println("ActorSummary.hyperlinkUpdate() : Lien absent de la table de redirection, id du lien : "+s); //$NON-NLS-1$
			}

		}

	}

	protected String buildText(Acteur actor)
	{
		String result = "<html>"; //$NON-NLS-1$
		Vector<Brick> corrects = new Vector<Brick>();
		Vector<Brick> uncomplete = new Vector<Brick>();
		Vector<Brick> gapped = new Vector<Brick>();


		for (Brick brick : session.getBrickList())
		{
			if (brick.contains(actor))
			{
				boolean uncompleteBool = brick.isActorStatePresent(actor, RelationComplete.RELATION_INCOMPLETE);
				boolean gappedBool = brick.isActorStatePresent(actor, RelationComplete.RELATION_ECART_OBJECTIF) || brick.isActorStatePresent(actor, RelationComplete.RELATION_ECART_MOYEN);

				if (!uncompleteBool && !gappedBool)
					corrects.add(brick);

				if (uncompleteBool)
					uncomplete.add(brick);

				if (gappedBool)
					gapped.add(brick);
			}
		}			

		int titleSize = 5;
		
		if (gapped.size() > 0)
		{
			result += "<font color=\"red\"><font size=\""+titleSize+"\"><b>\n"; //$NON-NLS-1$ //$NON-NLS-2$

			if (gapped.size() == 1)
				result+= Messages.getString("Version_0_9_9_9.ActorSummary.7"); //$NON-NLS-1$
			else if (gapped.size() > 1) 
				result += Messages.getString("Version_0_9_9_9.ActorSummary.8"); //$NON-NLS-1$

			result += "</b></font><ul>"; //$NON-NLS-1$

			for (Brick b : gapped)
			{
				result += buildLink(b);
			}
			result +="<br>"; //$NON-NLS-1$
			result += "</ul></font>"; //$NON-NLS-1$
		}
		if (uncomplete.size() > 0)
		{

			result += "<font color=\"blue\"><font size=\""+titleSize+"\"><b>\n"; //$NON-NLS-1$ //$NON-NLS-2$
			if (uncomplete.size() == 1)
				result += Messages.getString("Version_0_9_9_9.ActorSummary.14"); //$NON-NLS-1$
			else if (uncomplete.size() > 1)
				result += Messages.getString("Version_0_9_9_9.ActorSummary.15"); //$NON-NLS-1$

			result += "</b></font><ul>"; //$NON-NLS-1$
			
			for (Brick b : uncomplete)
			{
				result += buildLink(b);
			}

			


			result += "<br>"; //$NON-NLS-1$
			result += "</ul></font>"; //$NON-NLS-1$
		}

		if (corrects.size() > 0)
		{
			result += "<font color=\"green\"><font size=\""+titleSize+"\"><b>\n"; //$NON-NLS-1$ //$NON-NLS-2$

			if (corrects.size() == 1)
				result += Messages.getString("Version_0_9_9_9.ActorSummary.21"); //$NON-NLS-1$
			else if (corrects.size() > 1)
				result += Messages.getString("Version_0_9_9_9.ActorSummary.22");  //$NON-NLS-1$

			result += "</b></font><ul>"; //$NON-NLS-1$
			
			for (Brick b :corrects)
			{
				result += buildLink(b);
			}

			result += "</ul></font>"; //$NON-NLS-1$
		}
		result += "</html>"; //$NON-NLS-1$
		return result;
	}

	protected String buildLink(Brick b)
	{
		String link = linkCounter+""; //$NON-NLS-1$
		linkCounter ++;
		linkMap.put(link, b);
		return "<li><a href=\""+link+"\">"+b.toString()+" ("+b.getStep()+")</a><br></li>\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	}

	@Override
	public void updateSelection(ActorSheet newActorSheet) {
		linkMap.clear();
		linkCounter = 0;
		Acteur actor = newActorSheet.getActor();
		if (actor != null)
			setText(buildText(actor));

	}

}
