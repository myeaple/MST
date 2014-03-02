/**
 * Edge.java
 * 
 * The purpose of this class is to represent an edge between
 * two vertices in a graph.
 * 
 * @author MikeYeaple
 *
 */

public class Edge {

	private Vertex v1;
	private Vertex v2;
	private int weight;
	
	/**
	 * Edge()
	 * 
	 * Default constructor - don't bother using it.
	 */
	public Edge() {}
	
	public Edge(Vertex v1, Vertex v2, int weight)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}
	
	/**
	 * GetConnectedVertex()
	 * 
	 * Gets the vertex connected to the one passed in. Returns null if
	 * an incorrect vertex is passed in.
	 * 
	 * @param v - the current vertex.
	 * @return - the vertex connected to the one passed in.
	 */
	public Vertex GetConnectedVertex(Vertex v)
	{
		if (v == v1)
			return v2;
		else if (v == v2)
			return v1;
		else
			return null;
	}
	
}
