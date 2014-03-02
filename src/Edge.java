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
		if (v == vLeft)
			return vRight;
		else if (v == vRight)
			return vLeft;
		else
			return null;
	}
	
	/**
	 * LessThan()
	 * 
	 * Compares this edge to the one passed in. This is to be
	 * used in the event that two edges are of the same weight.
	 * 
	 * @return - true if this edge is "less than" the one passed in.
	 */
	public boolean LessThan(Edge e)
	{
		if (vLeft.GetName() < e.GetLeftVertex().GetName())
		{
			return true;
		}
		else if (vLeft.GetName() == e.GetLeftVertex().GetName())
		{
			if (vRight.GetName() < e.GetRightVertex().GetName())
				return true;
			else
				return false;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * GetWeight()
	 * 
	 * Gets the weight of the edge.
	 * 
	 * @return - the weight of the edge.
	 */
	public int GetWeight()
	{
		return weight;
	}
	
	/**
	 * GetLeftVertex()
	 * 
	 * Gets the left vertex (v1).
	 * 
	 * @return - the left vertex (v1).
	 */
	public Vertex GetLeftVertex()
	{
		return vLeft;
	}
	
	/**
	 * GetRightVertex()
	 * 
	 * Gets the right vertex (v2).
	 * 
	 * @return - the right vertex (v2).
	 */
	public Vertex GetRightVertex()
	{
		return vRight;
	}
	
}
