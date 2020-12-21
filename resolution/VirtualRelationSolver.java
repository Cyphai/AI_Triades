package resolution;

import java.util.Vector;

import main.Statut;
import models.Model;

import dataPack.DataPack;

public class VirtualRelationSolver {

	protected int nbTypeBrick;
	protected Vector<VirtualRelationInformation> entrante;
	protected Vector<VirtualRelationInformation> sortante;
	protected DataPack datapack;
	protected Model model;


	public VirtualRelationSolver(int nb, DataPack _datapack, Model _model)
	{
		datapack = _datapack;
		nbTypeBrick = nb;
		entrante = new Vector<VirtualRelationInformation>(nb);
		sortante = new Vector<VirtualRelationInformation>(nb);
		model = _model;
	}


	public VirtualRelationSolver(VirtualRelationSolver source) {
		datapack = source.datapack;
		model = source.model;
		nbTypeBrick = source.nbTypeBrick;
		entrante = new Vector<VirtualRelationInformation>(source.entrante);
		sortante = new Vector<VirtualRelationInformation>(source.sortante);
	}


	public void addVirtualEdge(Integer typeBrick, Integer orig, Integer dest,boolean entrant)
	{
		if (entrant)
		{

			for (VirtualRelationInformation infoEdge: entrante)
			{
				if (infoEdge.brickType.equals(typeBrick))
				{
					infoEdge.addInformation(orig,dest);
					return;
				}	
			}
		}
		else
		{
			for (VirtualRelationInformation infoEdge: sortante)
			{
				if (infoEdge.brickType.equals(typeBrick))
				{
					infoEdge.addInformation(orig,dest);
					return;
				}	
			}
		}
		
		VirtualRelationInformation temp = new VirtualRelationInformation(typeBrick);
		temp.addInformation(orig, dest);
		if (entrant)
			entrante.add(temp);
		else
			sortante.add(temp);
	}


	public Integer solveVirtualSource(Integer brickType, Integer destination) {
		//Ici le brickType est contenu dans source
		//destination contient soit un acteur (positif) soit une autre brique.
		if (destination.intValue()>= 0)
		{
			if (Statut.values()[datapack.getActors().getActeur(destination).getIdStatut().intValue()].rang >= 0)
			{
				//On cherche une relation sortante allant vers un manager
				return getSource(brickType,new Integer(-1));
			}
			else
			{
				//On cherche une relation entrante allant vers un partenaire
				return getSource(brickType,new Integer(-2));
			}
		}
		else
			return getSource(brickType,destination);


	}
	
	public Integer solveVirtualDestination(Integer brickType, Integer source) {
		//Ici le brickType est contenu dans source
		//destination contient soit un acteur (positif) soit une autre brique.
		if (source.intValue()>= 0)
		{
			if (Statut.values()[datapack.getActors().getActeur(source).getIdStatut().intValue()].rang >= 0)
			{
				//On cherche une relation sortante allant vers un manager
				return getDestination(brickType,new Integer(-1));
			}
			else
			{
				//On cherche une relation entrante allant vers un partenaire
				return getDestination(brickType,new Integer(-2));
			}
		}
		else
			return getDestination(brickType,source);


	}

	public Integer getSource(Integer brickType, Integer destination)
	{
		for (VirtualRelationInformation info: sortante )
		{
			if (info.brickType.equals(brickType))
			{
				return info.getSource(destination);
			}
		}

		return null;
	}


	public Integer getDestination(Integer brickType, Integer source) {
		for (VirtualRelationInformation info: entrante )
		{
			if (info.brickType.equals(brickType))
			{
				return info.getDestination(source);
			}
		}
		return null;
	}
}
