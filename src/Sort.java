/**
 * Sort.java
 * 
 * An abstract class to standardize and simplify the sorting of edges
 * within a graph.
 * 
 * @author MikeYeaple
 *
 */

import java.util.*;

public abstract class Sort {

	private long sortTimeList;
	private long sortTimeMatrix;
	
	/**
	 * sort()
	 * 
	 * Creates a list of edges and sorts them from an adjacency list
	 * representation of a graph.
	 * 
	 * @param adjList - the adjacency list whose edges need to be sorted.
	 * @return - an array of sorted Edges.
	 */
	protected Edge[] sort(ArrayList<ArrayList<Vertex>> adjList)
	{
		sortTimeList = System.currentTimeMillis();
		Edge[] result;
		
		// Create an array of Edges from the adjacency list.
		Edge[] a = getEdgesFrom(adjList);
		
		// Sort them.
		result = sort(a);
		
		sortTimeList = System.currentTimeMillis() - sortTimeList;
		
		return result;
	}
	
	/**
	 * sort()
	 * 
	 * Creates a list of edges and sorts them from a matrix
	 * representation of a graph.
	 * 
	 * @param matrix - the matrix whose edges need to be sorted.
	 * @return - an array of sorted Edges.
	 */
	protected Edge[] sort(int[][] matrix)
	{
		sortTimeMatrix = System.currentTimeMillis();
		Edge[] result;
		
		// Create an array of Edges from the matrix.
		Edge[] a = getEdgesFrom(matrix);
		
		// Sort them.
		result = sort(a);
		
		sortTimeMatrix = System.currentTimeMillis() - sortTimeMatrix;
		
		return result;
	}
	
	/**
	 * sort()
	 * 
	 * Performs the actual sorting of the Edges passed in.
	 * 
	 * @param a - the array of Edges to be sorted.
	 * @return - an array of sorted Edges.
	 */
	protected abstract Edge[] sort(Edge[] a);
	
	/* ---------------------- Helper Functions ---------------------- */
	
	/**
	 * getEdgesFrom()
	 * 
	 * Gets an array of edges from an adjacency list representation
	 * of a graph.
	 * 
	 * @param adjList - the adjacency list to get the edges from.
	 * @return - an array of the Edges in the graph.
	 */
	protected Edge[] getEdgesFrom(ArrayList<ArrayList<Vertex>> adjList)
	{
		// Using a HashSet to weed out duplicate Edges.
		HashSet<Edge> edges = new HashSet<Edge>();
		
		for (int i = 0; i < adjList.size(); i++)
		{
			for (int j = 0; j < adjList.get(i).size(); j++)
			{
				// Get the edges from each vertex...
				ArrayList<Edge> currEdges = adjList.get(i).get(j).getEdges();
				
				// Add the edges to our HashSet.
				edges.addAll(currEdges);
			}
		}
		
		// Convert the HashSet to an array of Edges and return it.
		return edges.toArray(new Edge[edges.size()]);
	}
	
	/**
	 * getEdgesFrom()
	 * 
	 * Gets an array of edges from a matrix representation
	 * of a graph.
	 * 
	 * @param matrix - the matrix to get the edges from.
	 * @return - an array of the Edges in the graph.
	 */
	protected Edge[] getEdgesFrom(int[][] matrix)
	{
		HashMap<Integer, HashSet<Integer>> visited = 
			new HashMap<Integer, HashSet<Integer>>();
		HashSet<Edge> edges = new HashSet<Edge>();
		
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				// Check to see if we've already created the edge.
				// We only need half of the graph because it is symmetric.
				if (visited.containsKey(j) && visited.get(j).contains(i))
					continue;
				else
				{
					int weight = matrix[i][j];
					
					// Only create an edge if one exists (weight > 0).
					if (weight > 0)
					{
						Vertex left = new Vertex(i);
						Vertex right = new Vertex(j);
						
						// Add the new Edge.
						edges.add(new Edge(left, right, weight));
						
						// Add this to our list of visited edges.
						if (visited.containsKey(i))
						{
							visited.get(i).add(j);
						}
						else
						{
							HashSet<Integer> visNew = new HashSet<Integer>();
							visNew.add(j);
							visited.put(i, visNew);
						}
					}
				}
			}
		}
		
		// Convert the HashSet of Edges to an array and return it.
		return edges.toArray(new Edge[edges.size()]);
	}
	
	/**
	 * swap()
	 * 
	 * Swaps two elements in the given array.
	 * 
	 * @param arr - the array whose elements you want to swap.
	 * @param i, j - the position of one of the elements to swap
	 */
	protected void swap(Edge[] arr, int i, int j)
	{
		Edge temp = arr[j];
		arr[j] = arr[i];
		arr[i] = temp;
	}
	
	/**
	 * shuffle()
	 * 
	 * Shuffles an array of Edges using a Knuth shuffle.
	 * 
	 * @param a - the array of Edges you want to shuffle.
	 */
	protected void shuffle(Edge[] a)
	{
		Random rand = new Random();
		for (int i = 0; i < a.length; i++)
		{
			int r = rand.nextInt(a.length - 1);
			if (i != r)
				swap(a, i, r);
		}
	}
	
	/* ---------------------- Accessors ---------------------- */
	
	/**
	 * getSortTimeList()
	 * 
	 * Gets the time it took to sort the edges of the adjacency list
	 * representation of the graph.
	 * 
	 * @return - time elapsed during the sort.
	 */
	public long getSortTimeList()
	{
		return sortTimeList;
	}
	
	/**
	 * getSortTimeMatrix()
	 * 
	 * Gets the time it took to sort the edges of the matrix
	 * representation of the graph.
	 * 
	 * @return - time elapsed during the sort.
	 */
	public long getSortTimeMatrix()
	{
		return sortTimeMatrix;
	}
	
}
