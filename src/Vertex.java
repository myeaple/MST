import java.util.*;

/**
 * Vertex.java
 * 
 * The purpose of this class is to represent a vertex within an
 * undirected, weighted graph.
 * 
 * @author MikeYeaple
 *
 */

public class Vertex {
	
	private int name; // Name is a number (i.e. 0, 1, etc.)
	private ArrayList<Edge> edges;
	private boolean visited;
	
	public Vertex()
	{
		edges = new ArrayList<Edge>();
	}
	
	/**
	 * Vertex() - specific constructor
	 * 
	 * @param name - the "name" of the vertex (an integer).
	 */
	public Vertex(int name)
	{
		this.name = name;
		edges = new ArrayList<Edge>();
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
	public void AddEdge(Vertex vNew, int weight)
	{
		edges.add(new Edge(this, vNew, weight));
	}
	
	/**
	 * GetEdges()
	 * 
	 * Gets a list of the edges connected to this vertex.
	 * 
	 * @return - the edges connected to this vertex.
	 */
	public ArrayList<Edge> GetEdges()
	{
		return edges;
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
	
}
