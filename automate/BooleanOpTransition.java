package automate;

import java.util.Hashtable;

public class BooleanOpTransition extends EpsilonTransition {

	public static enum Operator{AND, OR, XOR, NAND} 
	protected Transition[] elements;
	protected Operator type;
	protected int weight;
	
	
	public BooleanOpTransition(Operator _type, Transition[] _elements, int w)
	{
		super(w);
		elements = _elements;
		type = _type;
	}
	
	public boolean evalTransition(Hashtable info) {
		boolean result = false;
		switch (type)
		{
		case AND:
			result = true;
			for (Transition trans:elements)
			{
				result = result && trans.evalTransition(info);
			}
			break;
		case NAND:
			result = true;
			for (Transition trans:elements)
			{
				result = result && trans.evalTransition(info);
			}
			result = !result;
			break;
		case OR:
			result = false;
			for (Transition trans:elements)
			{
				result = result || trans.evalTransition(info);
			}
			break;
		case XOR:
			result = false;
			for (Transition trans:elements)
			{
				if (result && trans.evalTransition(info))
					return false;
				result = result || trans.evalTransition(info);
			}
		}
		
		return result;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	
	public static BooleanOpTransition or(Transition a, Transition b, int w)
	{
		Transition[] temp = new Transition[2];
		temp[0] = a;
		temp[1] = b;
		return new BooleanOpTransition(Operator.OR,temp,w);
	}

	public static BooleanOpTransition and(Transition a, Transition b, int w)
	{
		Transition[] temp = new Transition[2];
		temp[0] = a;
		temp[1] = b;
		return new BooleanOpTransition(Operator.AND,temp,w);
	}
	
	public static BooleanOpTransition nand(Transition a, Transition b, int w)
	{
		Transition[] temp = new Transition[2];
		temp[0] = a;
		temp[1] = b;
		return new BooleanOpTransition(Operator.NAND,temp,w);
	}
	
	public static BooleanOpTransition xor(Transition a, Transition b, int w)
	{
		Transition[] temp = new Transition[2];
		temp[0] = a;
		temp[1] = b;
		return new BooleanOpTransition(Operator.XOR,temp,w);
	}
	
}
