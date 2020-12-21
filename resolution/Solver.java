package resolution;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import models.Brick;
import models.BrickVertex;
import models.Model;
import models.ModelVertex;
import models.Schema;

import dataPack.DataPack;
import dataPack.ListeActeurSelectionne;

public class Solver {
	
	protected DataPack datapack;
	protected Vector<Brick> possibleBricks;
	protected Vector<Model> possibleModels;
	protected Set<Integer> avaibleContent;
	protected ListeActeurSelectionne list;
	
	public Solver(DataPack _datapack,ListeActeurSelectionne _list)
	{
		datapack = _datapack;
		list = _list;
		possibleBricks = new Vector<Brick>();
		possibleModels = new Vector<Model>();
		avaibleContent = new TreeSet<Integer>();
		avaibleContent.addAll(list.getIdSet());
		
	}
	
	public Vector<Schema> generateSchema(String _name)
	{
		selectPossibleBricks();
		selectPossibleModels();
		

		Vector<Schema> bestSchema = null;
		for (int i = 0; i< possibleModels.size();i++)
		{
			SchemaGenerator schemGen = new SchemaGenerator(possibleModels.elementAt(i),_name,datapack,this);
			Vector<Schema> result = schemGen.generateSchema();
			if (result != null )
			{
				if (bestSchema != null)
				{
					if (result.firstElement().getFitness() == bestSchema.firstElement().getFitness() )
					{	
						bestSchema.addAll(result);
					}	
					else if (result.firstElement().getFitness() > bestSchema.firstElement().getFitness())
					{
						bestSchema = result;
					}
				}
				else 
				{
					bestSchema = result;
				}
			}
		}
		return bestSchema;
	}
	
	public void selectPossibleBricks()
	{
		boolean ok;
		for (Brick brick:datapack.getBrickList())
		{
			ok = true;
			Iterator<BrickVertex> iterator = brick.getVertices().iterator();
			while (ok && iterator.hasNext())
			{
				BrickVertex vertex = iterator.next();

				if (vertex.getPossiblesContents().iterator().next().intValue()>=0 && !isUnionNotEmpty(avaibleContent,vertex.getPossiblesContents()))
					ok = false;	
			}
			if (ok)
			{	
			
				possibleBricks.add(brick);
				avaibleContent.add(brick.getType().getBrickTypeId());
			}
			
			
			
		}
	}
	
	public void selectPossibleModels()
	{
		boolean ok;
		for (Model model:datapack.getModelList())
		{
			ok = true;
			Iterator<ModelVertex> iterator = model.getVertices().iterator();
			while (ok && iterator.hasNext())
			{
				ModelVertex vertex = iterator.next();
				if (!avaibleContent.contains(vertex.getContentId()))
				{
					ok = false;
				}
			}
			if (ok)
			{
				possibleModels.add(model);
			}
		}
	}
	
	public boolean isUnionNotEmpty(Collection<?> a, Collection<?> b)
	{
		for (Object objA:a)
		{
			for (Object objB:b)
			{
				if (objA.equals(objB))
					return true;
				
			}
		}
		
		
		return false;
	}
	
	public ListeActeurSelectionne getListeActeurSelectionne() {
		return list;
	}
	
}