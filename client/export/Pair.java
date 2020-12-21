package client.export;

import java.io.Serializable;

public class Pair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3912777682364629562L;
	protected final Integer a;
	protected final Integer b;

	public Pair(Integer a, Integer b) {
		this.a = a;
		this.b = b;
	}

	public Pair(Pair other) {
		a = other.a;
		b = other.b;
	}

	@Override
	public boolean equals(Object object) {
		{
			if (object instanceof Pair) {
				Pair other = (Pair) object;

				return a.equals(other.a) && b.equals(other.b);
			}
		}
		return false;
	}
	
	public int hashCode()
	{
		return 100000 * a.intValue() + b.intValue();
	}
}
