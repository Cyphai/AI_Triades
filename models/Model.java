package models;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import dataPack.*;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class Model implements Serializable, AbstractSchema{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4211763759080896241L;

	protected final DataPack dataPack;
	protected Vector<ModelEdge> edges;
	protected String name;
	protected Vector<ModelVertex> vertices;
	protected transient Graph graph;
	
	public Model(DataPack _dataPack, String _nom)
	{
		dataPack = _dataPack;
		edges = new Vector<ModelEdge>();
		vertices = new Vector<ModelVertex>();
		name = _nom;
	}
	
	public ModelVertex addModelVertex(Integer actorId,Vertex vertex)
	{
		ModelVertex result = new ModelVertex(new Integer(getIdMax().intValue()+1),actorId);
		result.setAssociatedVertex(vertex);
		vertices.add(result);
		return result;
	}
	
	public ModelEdge addModelEdge(Integer source, Integer destination, Edge associatedEdge)
	{
		ModelEdge result = new ModelEdge(source,destination);
		edges.add(result);
		result.setAssociatedEdge(associatedEdge);
		return result;
	}
	
	public synchronized void removeModelEdge(ModelEdge modelEdge)
	{
		if (edges.contains(modelEdge))
		{
			edges.remove(modelEdge);
			Graph graph = (Graph) modelEdge.getAssociatedEdge().getGraph();
			if (graph != null)
				graph.removeEdge(modelEdge.getAssociatedEdge());
		}
	}
	
	public synchronized void removeModelVertex(ModelVertex modelVertex)
	{
		if (vertices.contains(modelVertex))
		{
			System.out.println("removeModelVertex");
			
			vertices.remove(modelVertex);
	
//			Enumeration<Edge> iterator = modelVertex.getAssociatedVertex().getInEdges().iterator();						
//			while (modelVertex.getAssociatedVertex().getInEdges().size() < 0)
//			{
//			
//				//Edge edge = iterator.;
//				ModelEdge modelEdge = (ModelEdge)edge.getUserDatum(ModelEdge.class);
//				removeModelEdge(modelEdge);
//			}
//			
			if (modelVertex.getAssociatedVertex() != null)
			{
			for(Object edgeObj:modelVertex.getAssociatedVertex().getInEdges())
			{
				Edge edge = (Edge)edgeObj;
				ModelEdge modelEdge = (ModelEdge)edge.getUserDatum(ModelEdge.class);
				removeModelEdge(modelEdge);
			}
			for(Object edgeObj:modelVertex.getAssociatedVertex().getOutEdges())
			{
				Edge edge = (Edge)edgeObj;
				ModelEdge modelEdge = (ModelEdge)edge.getUserDatum(ModelEdge.class);
				removeModelEdge(modelEdge);
			}
			graph.removeVertex(modelVertex.associatedVertex);
			}
			else
			{
				for (ModelEdge mE: edges)
				{
					if (mE.source.equals(modelVertex.vertexId) || mE.destination.equals(modelVertex.vertexId))
							removeModelEdge(mE);
				}
				
			}
		}
	}
	
	public void removeActor(Integer content)
	{
		//Iterator<ModelVertex> iterator = vertices.iterator();
		
		for (int i = 0; i< vertices.size();)
		{
			if (vertices.elementAt(i).contentId.intValue() == content.intValue())
			{
				removeModelVertex(vertices.elementAt(i));
			}
			else
			{
				i++;
			}
		}
	}
	
	
	protected Integer getIdMax()
	{
		Integer result = new Integer(-1);
		for (ModelVertex vertex:vertices)
		{
			if (vertex.vertexId > result)
				result = vertex.vertexId;
			
		}
		
		return result;
	}
	
	public ModelVertex getModelVertex(Integer id)
	{
		
		if (id.intValue() < vertices.size() && vertices.elementAt(id.intValue()).vertexId == id)
			return vertices.elementAt(id.intValue());
		else
			for (ModelVertex vertex:vertices)
				if (vertex.vertexId == id)
					return vertex;
		
		System.err.println("Aucun sommet trouvé correspondant à l'id fourni (Model.getModelVertex avec l'id:"+id+")");
		return null;
	}

	public DataPack getDataPack() {
		return dataPack;
	}

	public Vector<ModelVertex> getVertices() {
		return vertices;
	}

	public String toString()
	{
		return name;
	}

	@Override
	public void removeElement(Object element) {
		if (element.getClass() == ModelVertex.class)
			removeModelVertex((ModelVertex)element);
		else
			System.err.println("Demande de suppression d'un objet qui n'est pas un ModelVertex dans un Model");
		
	}

	public Vector<ModelEdge> getEdges() {
		return edges;
	}

	public ModelVertex getVertexByContent(Integer contentId)
	{
		for (ModelVertex vertex:vertices)
		{
			if (vertex.contentId.equals(contentId))
				return vertex;
		}
		return null;
	}

	public String getName() {
		
		return name;
	}
}
