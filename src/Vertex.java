/**
 * Vertex.java
 * 
 * The purpose of this class is to represent a vertex within an
 * undirected, weighted graph.
 * 
 * @author Michael Yeaple
 *
 */

import java.util.*;

public class Vertex {
	
	private int name; // Name is a number (i.e. 0, 1, etc.)
	// weightByInteger represents the weight between this vertex and 
	// another by the name of the other vertex.
	// K: otherVertex -> V: weight of edge
	private HashMap<Integer, Long> weightByVertexName;
	private boolean visited;
	
	public Vertex(){ }
	
	/**
	 * Vertex() - specific constructor
	 * 
	 * @param name - the "name" of the vertex (an integer).
	 */
	public Vertex(int name)
	{
		this.name = name;
		weightByVertexName = new HashMap<Integer, Long>();
	}
	
	/**
	 * AddEdge()
	 * 
	 * Adds a new edge connected to the provided vertex with the
	 * provided weight.
	 * 
	 * @param vNew - the vertex to connect to via the new edge.
	 * @param weight - the weight of the edge connecting the two vertices.
	 */
	public void AddEdge(Vertex vNew, long weight) throws VertexException
	{
		int vName = vNew.GetName();
		
		if (!weightByVertexName.containsKey(vName))
		{
			weightByVertexName.put(vName, weight);
		}
		else
		{
			throw new VertexException("Edge already exists.");
		}
	}
	
	/**
	 * Visit()
	 * 
	 * Marks the current node as visited.
	 */
	public void Visit()
	{
		visited = true;
	}
	
	/**
	 * Reset()
	 * 
	 * Marks the current node as not visited.
	 */
	public void Reset()
	{
		visited = false;
	}
	
	/**
	 * IsVisited()
	 * 
	 * Returns true if the vertex has already been visited.
	 * 
	 * @return - true: vertex has been visited; false: vertex not visited.
	 */
	public boolean IsVisited()
	{
		return visited;
	}
	
	/**
	 * NameToString()
	 * 
	 * Returns the "name" of the vertex as a string.
	 * 
	 * @return - the "name" of the vertex.
	 */
	public String NameToString()
	{
		return Integer.toString(name);
	}
	
	/**
	 * GetName()
	 * 
	 * Returns the name of the string as an int.
	 * 
	 * @return - the name of the string as an int.
	 */
	public int GetName()
	{
		return name;
	}
	
	/**
	 * GetEdges()
	 * 
	 * Returns the connected vertices mapped to the weights.
	 * 
	 * @return - a hashmap of the weight of each edge by the vertex name.
	 */
	public HashMap<Integer, Long> GetEdges()
	{
		return weightByVertexName;
	}
	
}
