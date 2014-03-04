/**
 * CountSort.java
 * 
 * Performs a count sort on the provided representation (either
 * adjacency list or matrix) of a graph.
 * 
 * @author MikeYeaple
 *
 */

public class CountSort extends Sort {

	/**
	 * CountSort()
	 * 
	 * Default constructor.
	 */
	public CountSort(){}
	
	/**
	 * sort()
	 * 
	 * Performs a count sort on the edges of the graph from the 
	 * array of Edges and returns the sorted array.
	 * 
	 * @return - the sorted list of edges.
	 */
	@Override
	protected Edge[] sort(Edge[] a) {
		Edge[] aux = new Edge[a.length];
		int count[];
		int r = 0;
		
		// Determine the max weight (R).
		int max = 0;
		for(int i = 0; i < a.length; i++)
		{
			Edge current = a[i];
			int weight = current.getWeight();
			
			if (max < weight)
				max = weight;
		}
		r = max + 1;
		
		count = new int[r + 1];
		
		// Fill the count array.
		for (int i = 0; i < a.length; i++)
			count[a[i].getWeight() + 1]++;
		
		// Calculate the sums in the count array.
		for (int i = 0; i < count.length - 1; i++)
			count[i + 1] += count[i];
		
		// Sort the Edges into the aux array.
		for (int i = 0; i < a.length; i++)
			aux[count[a[i].getWeight()]++] = a[i];
		
		return aux;
	}

}
