/**
 * InsertionSort.java
 * 
 * Performs an insertion sort on the provided representation (either
 * adjacency list or matrix) of a graph.
 * 
 * @author MikeYeaple
 *
 */

public class InsertionSort extends Sort {

	/**
	 * InsertionSort()
	 * 
	 * Default constructor.
	 */
	public InsertionSort(){}
	
	/**
	 * sort()
	 * 
	 * Performs an insertion sort on the edges of the graph from the
	 * array of Edges and returns the sorted array.
	 * 
	 * @return - the sorted list of edges.
	 */
	@Override
	protected Edge[] sort(Edge[] a) {		
		for (int i = 0; i < a.length; i++)
		{
			for (int j = i; j > 0; j--)
			{
				if (a[j].getWeight() < a[j-1].getWeight())
				{
					swap(a, j, j-1);
				}
				else break;
			}
		}
		
		return a;
	}

}
