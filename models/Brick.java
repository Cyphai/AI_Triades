package models;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import translation.Messages;

import main.RelationComplete;
import client.stringTranslator.StringTranslator;
import client.stringTranslator.StringTranslator.StringType;
import dataPack.Acteur;
import dataPack.ActeurBase;
import dataPack.Activite;
import dataPack.Content;
import dataPack.DataPack;
import dataPack.JeuActeur;
import edu.uci.ics.jung.graph.DirectedGraph;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

public class Brick implements Serializable,  Content {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3672273059633190927L;

	public enum BrickState {EMPTY, UNCOMPLETED, CORRECT, GAPPED};

	protected Vector<BrickVertex> vertices;
	protected String name;
	protected String step;
	protected Vector<BrickEdge> edges;

	protected Vector<BrickVertex> genericActors;

	protected transient DataPack datapack;

	protected transient DirectedGraph<BrickVertex, BrickEdge> graph;

	protected Activite activity;

	protected boolean navigationBrick;

	protected Brick() {
		vertices = null;

		graph = null;
	}

	private Brick(String _step, String _nom, DataPack _datapack) {
		step = _step;
		vertices = new Vector<BrickVertex>(5);
		edges = new Vector<BrickEdge>(10);
		name = _nom;
		datapack = _datapack;
		graph = null;
		genericActors = new Vector<BrickVertex>();
		navigationBrick = false;
		activity = null;
	}

	public Brick(String _step, String _nom, DataPack _datapack,
			Activite _activity) {
		this(_step, _nom, _datapack);
		if (_activity != null) {
			activity = _activity;
			BrickVertex activityVertex = addBrickVertex(activity);
			activityVertex.setLocation(new Point2D.Double(0, 0));
			navigationBrick = false;
		} else {
			this.navigationBrick = true;
		}
	}

	public Brick(Brick brick) {
		step = brick.step;
		name = brick.name;
		datapack = brick.datapack;
		graph = null;
		activity = brick.activity;
		navigationBrick = brick.navigationBrick;
		genericActors = new Vector<BrickVertex>(brick.genericActors);

		HashMap<BrickVertex, BrickVertex> oldNew = new HashMap<BrickVertex, BrickVertex>();

		vertices = new Vector<BrickVertex>(brick.vertices.size());
		for(BrickVertex brickVertex : brick.vertices) {
			BrickVertex value = new BrickVertex(brickVertex);
			oldNew.put(brickVertex, value);
			vertices.add(value);
		}

		edges = new Vector<BrickEdge>(brick.edges.size());
		for(BrickEdge brickEdge: brick.edges) {
			edges.add(new BrickEdge(oldNew.get(brickEdge.getSource()), oldNew.get(brickEdge.getDestination()) ,new RelationComplete(brickEdge.getCompleteRelation())));
		}
	}

	public BrickVertex addBrickVertex(Content newContent) {
		BrickVertex temp = new BrickVertex(datapack);
		temp.setContent(newContent);
		vertices.add(temp);

		if (newContent instanceof ActeurBase
				&& ((ActeurBase) newContent).isBase()) {
			genericActors.add(temp);
		}

		if (graph != null)
		{
			graph.addVertex(temp);
		}
		return temp;
	}

	public BrickEdge addEdge(BrickEdge newEdge) {
		edges.add(newEdge);
		return newEdge;
	}

	public void removeContent(Content oldContent) {
		//Y'aurai pas moyen de simplifier ici?
		
		for (int i = 0; i < vertices.size();) {
			if (vertices.elementAt(i).getContent().equals(oldContent)) {
				if (oldContent instanceof ActeurBase	&& ((ActeurBase) oldContent).isBase())
				{
					genericActors.remove(vertices.elementAt(i));
				}
				removeBrickVertex(vertices.elementAt(i));
			} else
				i++;
		}
	}

	//TODO problème lors de la suppression d'un acteur générique, il semble rester dans le vector genericActors
	
	public void removeBrickVertex(BrickVertex brickVertex) {
		if (vertices.contains(brickVertex)) {
			for (int i = 0; i < edges.size();) {
				if (edges.elementAt(i).source.equals(brickVertex)
						|| edges.elementAt(i).destination
						.equals(brickVertex))
					removeModelEdge(edges.elementAt(i));
				else
					i++;
			}
			vertices.remove(brickVertex);

			if (brickVertex.getContent() instanceof ActeurBase)
			{
				if (((ActeurBase)brickVertex.getContent()).isBase())
				{
					while (genericActors.contains(brickVertex))
						genericActors.remove(brickVertex);
				}
			}

			if (graph != null) {
				graph.removeVertex(brickVertex);
			}
		} else {
			System.out.println("Brick.removeBrickVertex failed"); //$NON-NLS-1$
		}
	}

	public void removeModelEdge(BrickEdge modelEdge) {
		if (edges.contains(modelEdge)) {
			edges.remove(modelEdge);
			if (graph != null)
			{
				graph.removeEdge(modelEdge);
			}
		}
	}

	public void switchActivity(Activite oldActivity, Activite newActivity)
	{
		BrickVertex oldVertex = getBrickVertexByContent(oldActivity);
		if (oldVertex == null)
		{
			System.err.println("Brick.switchActivity : no vertex correspond to the oldActivity, switch aborted"); //$NON-NLS-1$
		}

		BrickVertex newVertex = addBrickVertex(newActivity);
		for (BrickEdge bE : edges)
		{
			if (bE.getSource().equals(oldVertex))
			{
				bE.setSource(newVertex);
				if (graph != null)
				{
					graph.removeEdge(bE);
					graph.addEdge(bE, newVertex, bE.getDestination());
				}
			}

			if (bE.getDestination().equals(oldVertex))
			{
				bE.setDestination(newVertex);
				if (graph != null)
				{
					graph.removeEdge(bE);
					graph.addEdge(bE, bE.getSource(), newVertex);
				}
			}
		}
		newVertex.setLocation(oldVertex.getLocation());
		removeBrickVertex(oldVertex);
		activity = newActivity;
	}

	public BrickVertex getBrickVertexByContent(Content lookedContent) {
		for (BrickVertex vertex : vertices)
			if (vertex.getContent().equals(lookedContent))
				return vertex;

		System.out
		.println("Aucun sommet trouvé correspondant à l'id fourni (Brick.getBrickVertexByContent avec le contentId:" //$NON-NLS-1$
				+ lookedContent + ")"); //$NON-NLS-1$

		System.out.println(vertices);
		return null;
	}

	public Vector<BrickVertex> getVertices() {
		return vertices;
	}

	public DataPack getDatapack() {
		if (datapack == null)
			datapack = Program.myMainFrame.getDataPack();
		return datapack;
	}

	public Vector<BrickEdge> getEdges() {
		return edges;
	}

	public Activite getActivity() {
		return activity;
	}

	public String getName() {
		return name;
	}

	public String getStep() {
		return StringTranslator.getTranslatedString(step, StringType.stepsType);
	}
	
	public String getNoTranslatedStep()
	{
		return step;
	}
	
	public boolean isGeneric() {
		if (genericActors == null) {
			genericActors = new Vector<BrickVertex>();

			//récupére la liste des acteur générique dans la brick
			for (BrickVertex vertex : vertices) {
				Content vertexContent = vertex.getContent();

				if (vertexContent instanceof ActeurBase
						&& ((ActeurBase) vertexContent).isBase()) {
					genericActors.add(vertex);
				}
			}
		}

		return genericActors.size() > 0;
	}

	private void replaceBaseByCorps(BrickVertex vertex, String nomCorps) {
		if (((ActeurBase)vertex.getContent()).isBase())
		{
			Content newContent = ((ActeurBase) vertex.getContent())
			.getJeuActeur().getRealActor(
					(ActeurBase) vertex.getContent(), nomCorps);
			vertex.setContent(newContent);
		}
	}

	public Vector<Brick> createNoGenericBrick() {
		if (isGeneric() == false) {
			throw new RuntimeException("Demande de générer les briques non générique d'une brique deja non générique"); //$NON-NLS-1$
		}

		Vector<Brick> noGenericBricks = new Vector<Brick>();
		JeuActeur genericJeuActeur = ((ActeurBase) genericActors.firstElement()
				.getContent()).getJeuActeur();

		
		boolean checkActors = false;
		
		for(String nomCorps : genericJeuActeur.getListeCorps()) {
			//Créer une brique pour chaques corps du jeu d'acteur identique a celle la (constructeur par recopi)
			Brick newBrick = new Brick(this);
			newBrick.name += " " + nomCorps; //$NON-NLS-1$
			newBrick.genericActors.clear();

			
			for (BrickVertex genericVertex : genericActors) {
				// récupére l'id du corps de la base
				BrickVertex vertexToBeChanged = newBrick
				.getBrickVertexByContent(genericVertex.getContent());

				//remplacer les acteur generique par les acteur non generique de la base
				if (vertexToBeChanged != null)
					newBrick.replaceBaseByCorps(vertexToBeChanged, nomCorps);
				else
				{
					System.out.println("Brick.createNoGenericBrick() :: Attention, un sommet générique du vector genericActors n'est plus présent dans la brique.");
					checkActors = true;
				}
			}

			noGenericBricks.add(newBrick);
		}

		if (checkActors)
			DialogHandlerFrame.showErrorDialog("Attention, un acteur générique est encore présent dans les infos d'une brique\nalors qu'il semble avoir été précédemment supprimé, veuillez contacter les développeurs\npour corriger ce problème.");
		
		return noGenericBricks;
	}

	public boolean isNavigationBrick() {
		return navigationBrick;
	}

	public void setNavigationBrick(boolean navigationBrick) {
		this.navigationBrick = navigationBrick;
	}

	public String getNoTranslatedString() {
		return name + (Program.isTriades() ? "" : " (" + step + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public String toString() {

		if (!navigationBrick)
			return StringTranslator.getTranslatedString(this, StringTranslator.StringType.brickType);
		else
			return Messages.getString("SchemaGenerator.0") //$NON-NLS-1$
					+ StringTranslator.getTranslatedString(step, StringTranslator.StringType.stepsType);
	}

	public void setStep(String newName) {
		step = newName;
	}

	public void setName(String newName) {
		name = newName;
	}

	public void resetGraph() {
		graph = null;
	}

	public boolean contains(Content content) {
		for(BrickVertex vertex : vertices) {
			if(vertex.getContent().equals(content)) {
				return true;
			}
		}
		
		return false;
	}
	
	public BrickState getState() {
		boolean isEcartBrick = false;
		boolean isCompletedRelation = false;
		boolean isUncompletedRelation = false;
		
		for(BrickEdge edge : getEdges()) {
			if(edge.getCompleteRelation().getState() != RelationComplete.RELATION_INCOMPLETE) {
				isCompletedRelation = true;
			} else
				isUncompletedRelation = true;
			
			if(edge.getCompleteRelation().getState() == RelationComplete.RELATION_ECART_MOYEN || edge.getCompleteRelation().getState() == RelationComplete.RELATION_ECART_OBJECTIF) {
				isEcartBrick = true;
			}
		}
		if (!isCompletedRelation)
			return BrickState.EMPTY;
		else if (isUncompletedRelation)
			return BrickState.UNCOMPLETED;
		else if(isEcartBrick) {
			return BrickState.GAPPED;
		} else {
			return BrickState.CORRECT;
		}
	}

	public boolean isActorStatePresent(Acteur actor, int requestedState) {
		
		if (!contains(actor))
			return false;
		
		
		for (BrickEdge edge : getEdges())
		{
			if (edge.getSource().getContent().equals(actor) || edge.getDestination().getContent().equals(actor))
			{
				if (edge.getCompleteRelation().hasState(requestedState))
					return true;
			}
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (navigationBrick ? 1231 : 1237);
		result = prime * result + ((step == null) ? 0 : step.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Brick))
			return false;
		Brick other = (Brick) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (navigationBrick != other.navigationBrick)
			return false;
		if (step == null) {
			if (other.step != null)
				return false;
		} else if (!step.equals(other.step))
			return false;
		return true;
	}

	public Vector<BrickVertex> getGenericActors() {

		return genericActors;
	}
}