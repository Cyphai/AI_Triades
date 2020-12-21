package automate;

import java.util.Hashtable;

public class EpsilonTransition implements Transition {

	protected int weight;
	
	public EpsilonTransition(int w)
	{
		weight = w;
	}
	
	public boolean evalTransition(Hashtable info) {
		return true;
	}
	
	public int getWeight()
	{
		return weight;
	}

}
