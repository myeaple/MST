/**
 * Edge.java
 * 
 * The purpose of this class is to represent a weighted edge connecting 
 * two vertices within an undirected, weighted graph.
 * 
 * @author MikeYeaple
 *
 */

public class Edge {

	private Vertex v1;
	private Vertex v2;
	private int weight;
	
	/**
	 * Do NOT use the default constructor.
	 * Only create edges with the specific constructor.
	 */ 
	public Edge(){ }
	
	/**
	 * The specific constructor to create an Edge.
	 * 
	 * @param v1 - one of the two vertices to be connected by the edge.
	 * @param v2 - the other vertex to be connected by the edge.
	 * @param weight - the weight of the edge.
	 */
	public Edge(Vertex v1, Vertex v2, int weight)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}
	
	/**
	 * GetConnectedVertex()
	 * 
	 * Returns the other vertex connected by the edge.
	 * 
	 * @param vCurrent - the current vertex.
	 * @return - the found vertex.
	 * @throws VertexException - vCurrent is not part of the current edge.
	 */
	public Vertex GetConnectedVertex(Vertex vCurrent) throws VertexException
	{
		Vertex vFound = null;
		
		if (vCurrent == v1)
		{
			vFound = v2;
		}
		else if (vCurrent == v2)
		{
			vFound = v1;
		}
		
		if (vFound == null)
			throw new VertexException("The vertex passed in was not one " +
					"of the two vertices connected by the edge.");
		
		return vFound;
	}
	
	/**
	 * GetWeight()
	 * 
	 * Gets the weight of the current edge.
	 * 
	 * @return - the weight of the edge
	 */
	public int GetWeight()
	{
		return weight;
	}
	
}
