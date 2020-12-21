package resolution;

public class Arete implements Cloneable, Comparable<Arete>{
	public Integer source;
	public Integer dest;

	public Arete(Integer src, Integer dst)
	{
		source = src;
		dest = dst;
	}

	//@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Arete)
			return source.equals(((Arete)obj).source) && dest.equals(((Arete)obj).dest);

		System.err.println("Impossible de comparer " + obj.getClass() + "a une Arete");
		return false;
	}

	@Override
	public int compareTo(Arete o) {

		if (source.equals(source.equals(o.source)))
			return (dest.compareTo(o.dest));
		else
			return (source.compareTo(o.source));

	}
}
