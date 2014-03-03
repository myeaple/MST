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

	private Vertex vLeft;
	private Vertex vRight;
	private int weight;
	
	/**
	 * Edge()
	 * 
	 * Default constructor - don't bother using it.
	 */
	public Edge() {}
	
	public Edge(Vertex vLeft, Vertex vRight, int weight)
	{
		this.vLeft = vLeft;
		this.vRight = vRight;
		this.weight = weight;
	}
	
	/**
	 * getConnectedVertex()
	 * 
	 * Gets the vertex connected to the one passed in. Returns null if
	 * an incorrect vertex is passed in.
	 * 
	 * @param v - the current vertex.
	 * @return - the vertex connected to the one passed in.
	 */
	public Vertex getConnectedVertex(Vertex v)
	{
		if (v == vLeft)
			return vRight;
		else if (v == vRight)
			return vLeft;
		else
			return null;
	}
	
	/**
	 * lessThan()
	 * 
	 * Compares this edge to the one passed in. This is to be
	 * used in the event that two edges are of the same weight.
	 * 
	 * @return - true if this edge is "less than" the one passed in.
	 */
	public boolean lessThan(Edge e)
	{
		// First, compare the weights.
		if (weight < e.getWeight())
		{
			return true;
		}
		else
		{
			// Else, compare the left vertex name.
			if (vLeft.getName() < e.getLeftVertex().getName())
			{
				return true;
			}
			else if (vLeft.getName() == e.getLeftVertex().getName())
			{
				// Else, compare the right vertex name.
				if (vRight.getName() < e.getRightVertex().getName())
					return true;
				else
					return false;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * getWeight()
	 * 
	 * Gets the weight of the edge.
	 * 
	 * @return - the weight of the edge.
	 */
	public int getWeight()
	{
		return weight;
	}
	
	/**
	 * getLeftVertex()
	 * 
	 * Gets the left vertex (v1).
	 * 
	 * @return - the left vertex (v1).
	 */
	public Vertex getLeftVertex()
	{
		return vLeft;
	}
	
	/**
	 * getRightVertex()
	 * 
	 * Gets the right vertex (v2).
	 * 
	 * @return - the right vertex (v2).
	 */
	public Vertex getRightVertex()
	{
		return vRight;
	}
	
}
