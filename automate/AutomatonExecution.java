package automate;

import main.*;
import edu.uci.ics.jung.graph.*;
import java.util.*;

public class AutomatonExecution {

	protected Vertex activeState;
	protected Automaton automate;
	
	
	public Vertex nextState(Hashtable info)
	{

		Vertex result = null;
		if (activeState != null)
		{
		int weight = -1;
		Set<Vertex> successeurs = activeState.getOutEdges();
		for (Vertex successeur:successeurs)
		{
			if(automate.getTransition(activeState,successeur).evalTransition(info))
			{
				if (automate.getTransition(activeState,successeur).getWeight() > weight)
				{
					result = successeur;
					weight = automate.getTransition(activeState,successeur).getWeight();
				}
			}
		}
		}
		else
		{
			System.out.println("Cette execution à déjà été achévée.");
		}
		activeState = result;
		return result;
		
	}
	
	public Vertex getActiveState()
	{
		return activeState;
	}
}
