package automate;

import edu.uci.ics.jung.graph.impl.*;
import edu.uci.ics.jung.graph.*;
import java.util.*;
import automateGraph.*;


public class Automaton extends AbstractSparseGraph {

	protected Hashtable<DirectedSparseEdge,Transition> transitions;
	protected Hashtable<Vertex,Action> actions;
	
	public Automaton()
	{
		super();
		transitions = new Hashtable<DirectedSparseEdge,Transition>();
		actions = new Hashtable<Vertex,Action>();
	}
	
	public Transition getTransition(Vertex a, Vertex b)
	{
		return transitions.get(new DirectedSparseEdge(a,b));
	}
	
	public void editAction(Vertex vertex)
	{
		Action action = actions.get(vertex);
		if (action == null)
		{
			action = new EmptyAction();
			actions.put(vertex, action);
			
		}
		ActionEditingFrame.editAction(action);
	}
	
	public void editTransition(DirectedSparseEdge edge)
	{
		Transition transition = transitions.get(edge);
		if (transition == null)
		{
			transition = new EpsilonTransition(0);
			transitions.put(edge, transition);
						
		}
		//TransitionEditingFrame.editTransition(transition);
		
	}
}
