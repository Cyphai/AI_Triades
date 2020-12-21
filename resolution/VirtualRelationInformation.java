package resolution;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class VirtualRelationInformation {
	
	protected final Integer brickType;
	protected Vector<Arete> infos;
	
	
	
	
	public VirtualRelationInformation(Integer typeId)
	{
		brickType = typeId;
		infos = new Vector<Arete>();
	}
	
	public void addInformation(Integer source, Integer destination)
	{
		infos.add(new Arete(source,destination));
	}

	public Integer getSource(Integer destination) {
		for (Arete arete:infos)
		{
			if (arete.dest.equals(destination))
				return arete.source;
		}
		return null;
	}
	
	public Integer getDestination(Integer source){
		
		for (Arete arete:infos)
		{
			if (arete.source.equals(source))
				return arete.dest;
		}
		return null;
		
	}

}
