package resolution;

import java.awt.geom.Point2D;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import models.Brick;
import models.BrickVertex;
import models.Model;
import models.BrickEdge;
import models.ModelVertex;
import models.Schema;
import models.SchemaVertex;
import dataPack.DataPack;
import dataPack.ActeurSelectionne;
import graphicalUserInterface.DialogHandlerFrame;
import main.ActionTimeListe;

public class SchemaGenerator implements Cloneable {


	protected Set<Integer> unfoldedContent;
	protected Set<Integer> avaibleActors;
	protected Brick currentBrick;
	protected Point2D currentBrickLocation;
	protected int currentBrickVertexId;
	protected Hashtable<Integer,SchemaVertex> brickToVertexAssociation;
	protected VirtualRelationSolver virtualRelationSolver;
	protected Schema builtSchema;
	protected final Model model;
	protected final Solver solver;

	public SchemaGenerator(Model workingModel, String schemaName, DataPack datapack, Solver _solver)
	{
		model = workingModel;
		solver = _solver;
		//hypothesisList = new Stack<Hypothesis>();
		unfoldedContent = new TreeSet<Integer>();
		avaibleActors = new TreeSet<Integer>(solver.avaibleContent);

		virtualRelationSolver = new VirtualRelationSolver(5,datapack,workingModel);
		builtSchema = new Schema(datapack,schemaName);
		currentBrickLocation = null;
		brickToVertexAssociation = null;
		currentBrickVertexId = 0;
		currentBrick = null;
		for (ModelVertex vertex:model.getVertices())
		{
			unfoldedContent.add(vertex.getContentId());
		}


	}

	public SchemaGenerator(SchemaGenerator source)
	{
		model = source.model;
		builtSchema = new Schema(source.builtSchema);
		currentBrick = source.currentBrick;
		currentBrickVertexId = source.currentBrickVertexId;
		currentBrickLocation = source.currentBrickLocation;
		if (source.brickToVertexAssociation != null)
			brickToVertexAssociation = new Hashtable<Integer,SchemaVertex>(source.brickToVertexAssociation);
		virtualRelationSolver = new VirtualRelationSolver(source.virtualRelationSolver);
		unfoldedContent = new TreeSet<Integer>(source.unfoldedContent);
		solver = source.solver;
		avaibleActors = new TreeSet<Integer>(source.avaibleActors);

	}

	protected SchemaVertex addActor(Integer idActor)
	{
		if (idActor.intValue()>=0)
		{
			if (avaibleActors.contains(idActor))
			{
				for (ActeurSelectionne selectedActor:solver.list.getActorsSelection())
				{
					if (selectedActor.getIdActeur().equals(idActor))
					{
						if (builtSchema.getVertexByActor(selectedActor)!= null)
						{
							return null;
						}
						else
						{
							avaibleActors.remove(idActor);
							return builtSchema.addSchemaVertex(idActor, null);

						}
					}
				}
			}
		}
		else
		{
			return builtSchema.addSchemaVertex(idActor, null);
		}
		System.out.println("Echec lors de l'ajout d'un acteur d'id : "+idActor);
		return null;
	}

	public Vector<Schema> generateSchema()
	{

		while (unfoldedContent.size() > 0 || currentBrick != null)
		{
			if (currentBrick != null)
			{
				if (currentBrickVertexId < currentBrick.getVertices().size())
				{
					BrickVertex vertex = currentBrick.getVertices().elementAt(currentBrickVertexId);

					if (vertex.getPossiblesContents().size() == 1)
					{
						SchemaVertex newVertex = addActor(vertex.getPossiblesContents().iterator().next());
						if (newVertex == null)
						{
							return null;
						}
						else
						{
							newVertex.setLocation(new Point2D.Double(currentBrickLocation.getX()+currentBrick.getVertices().elementAt(currentBrickVertexId).getLocation().getX()/1.4,currentBrickLocation.getY()+currentBrick.getVertices().elementAt(currentBrickVertexId).getLocation().getY()/1.4));
							brickToVertexAssociation.put(vertex.getVertexId(), newVertex);
							currentBrickVertexId++;
						}
					}
					else
					{
						currentBrickVertexId ++;
						Set<Integer> actorsSet = vertex.getPossiblesContents();
						actorsSet.retainAll(avaibleActors);
						Vector<Schema> bestSchema = null;
						for (Integer id : actorsSet)
						{
							SchemaGenerator schemGen = new SchemaGenerator(this);
							SchemaVertex newVertex = schemGen.addActor(id);
							if (newVertex != null)
							{
								newVertex.setLocation(new Point2D.Double(currentBrickLocation.getX()+currentBrick.getVertices().elementAt(currentBrickVertexId).getLocation().getX(),currentBrickLocation.getY()+currentBrick.getVertices().elementAt(currentBrickVertexId).getLocation().getY()));
								schemGen.brickToVertexAssociation.put(vertex.getVertexId(), newVertex);
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
						}
						return bestSchema;

					}
				}
				else
				{
					//Traitement de fin de brique :D
					//Pour chaque arêtes de la liste, soit on l'ajoute aux arêtes virtuelles (référençant l'id virtuelle et l'id du SchemaVertex)
					SchemaVertex activityVertex = builtSchema.addSchemaVertex(currentBrick.getActivityId(), null);
					activityVertex.setLocation(currentBrickLocation);

					for (BrickEdge edge:currentBrick.getEdges())
					{

						if (edge.getSource().intValue() < 0 && edge.getSource().intValue() > -100)
						{
							if (edge.getDestination().intValue() >= 0)
								virtualRelationSolver.addVirtualEdge(currentBrick.getType().getBrickTypeId(), edge.getSource(), brickToVertexAssociation.get(edge.getDestination()).getVertexId(),true);
							else
								virtualRelationSolver.addVirtualEdge(currentBrick.getType().getBrickTypeId(), edge.getSource(), edge.getDestination(),true);

						}
						else if (edge.getDestination().intValue() < 0 && edge.getDestination().intValue() > -100)
						{
							if (edge.getSource().intValue() >= 0)
								virtualRelationSolver.addVirtualEdge(currentBrick.getType().getBrickTypeId(), brickToVertexAssociation.get(edge.getSource()).getVertexId(),edge.getDestination(),false);
							else
								virtualRelationSolver.addVirtualEdge(currentBrick.getType().getBrickTypeId(), edge.getSource(),edge.getDestination(),false);

						}
						else
						{
							Integer source;
							Integer destination;
							if (edge.getSource().intValue() <= -50)
								source = activityVertex.getVertexId();
							else
								source = brickToVertexAssociation.get(edge.getSource()).getVertexId();

							if (edge.getDestination().intValue() <= -50)
								destination = activityVertex.getVertexId();
							else
								destination = brickToVertexAssociation.get(edge.getDestination()).getVertexId();

							builtSchema.addModelEdge(source,destination , edge.getCompleteRelation(),null);
						}
					}
					brickToVertexAssociation = null;
					currentBrick = null;
					currentBrickLocation = null;
					currentBrickVertexId = 0;

				}
			}
			else
			{
				Integer nextContent = unfoldedContent.iterator().next();
				unfoldedContent.remove(nextContent);
				if (nextContent.intValue() < 0)
				{
					//On récupère un brickType
					Vector<Brick> possiblesBricks = new Vector<Brick>();
					for (Brick brick:solver.possibleBricks)
					{
						if (brick.getType().getBrickTypeId().equals(nextContent))
						{
							possiblesBricks.add(brick);
						}
					}
					if (possiblesBricks.isEmpty())
					{
						return null;
					}
					else if (possiblesBricks.size() == 1)
					{
						currentBrick = possiblesBricks.firstElement();
						currentBrickLocation = model.getVertexByContent(nextContent).getLocation();
						brickToVertexAssociation = new Hashtable<Integer,SchemaVertex>(currentBrick.getVertices().size());
					}
					else
					{
						Vector<Schema> bestSchema = null;
						for (Brick brick:possiblesBricks)
						{					
							currentBrickLocation = model.getVertexByContent(nextContent).getLocation();
							brickToVertexAssociation = new Hashtable<Integer,SchemaVertex>();
							SchemaGenerator schemGen = new SchemaGenerator(this);
							schemGen.currentBrick = brick;
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

				}
				else
				{
					SchemaVertex result = addActor(nextContent);
					if (result != null)
						result.setLocation(new Point2D.Double(model.getVertexByContent(nextContent).getLocation().getX(),model.getVertexByContent(nextContent).getLocation().getY()));
					else
						return null;
				}

			}
		}
		//Résolution des relation du modèle.
		for (BrickEdge edge:model.getEdges())
		{

			ModelVertex src = model.getModelVertex(edge.getSource());
			ModelVertex dst = model.getModelVertex(edge.getDestination());
			Integer source;
			Integer destination;
			if (src.getContentId().intValue()< 0)
				source = virtualRelationSolver.solveVirtualSource(src.getContentId(),dst.getContentId());
			else 
			{
				ActeurSelectionne actor = solver.list.getActorSelectionById(src.getContentId());
				source = builtSchema.getVertexByActor(actor).getVertexId();			
			}
			if (dst.getContentId().intValue()< 0)
				destination = virtualRelationSolver.solveVirtualDestination(dst.getContentId(),src.getContentId());
			else
			{
				ActeurSelectionne actor = solver.list.getActorSelectionById(dst.getContentId());
				destination = builtSchema.getVertexByActor(actor).getVertexId();
			}

			if (destination != null && source != null )
			{
				builtSchema.addModelEdge(source, destination,edge.getCompleteRelation(), null);
			}
		}
		evaluateFitness();
		Vector<Schema> result = new Vector<Schema>();
		result.add(builtSchema);
		return result;
	}



	public SchemaGenerator clone() throws CloneNotSupportedException
	{
		return (SchemaGenerator)super.clone();

	}

	private BitSet getActiveTime(SchemaVertex vertex)
	{
		BitSet result = new BitSet(model.getDataPack().getActionTimeList().size());
		result.clear();
		for (BrickEdge edge:builtSchema.getEdges())
		{
			if (edge.getSource().equals(vertex.getVertexId()) || edge.getDestination().equals(vertex.getVertexId()))
			{
				result.or(edge.getCompleteRelation().getActiveTime());
			}
		}

		return result;
	}

	public void evaluateFitness()
	{


		int fitness = 0;
		for (SchemaVertex vertex : builtSchema.getVertices())
		{
			if (vertex.getContent().intValue()>= 0)
			{
				BitSet activeTime = getActiveTime(vertex);
				BitSet actorActiveTime = solver.list.getActorSelectionById(vertex.getContent()).getActiveTime();
				for (int i = 0; i< model.getDataPack().getActionTimeList().size();i++)
				{
					if (activeTime.get(i) == actorActiveTime.get(i))
						fitness ++;
				}
			}
		}

		for (ActeurSelectionne actor: solver.list.getActorsSelection())
		{
			if (builtSchema.getVertexByActor(actor) == null)
				fitness -= 5;
		}
		}



}
