/**
 * QuickSort.java
 * 
 * Performs a quick sort on the provided representation (either
 * adjacency list or matrix) of a graph.
 * 
 * @author MikeYeaple
 *
 */

public class QuickSort extends Sort {

	/**
	 * QuickSort()
	 * 
	 * Default constructor.
	 */
	public QuickSort(){}
	
	/**
	 * sort()
	 * 
	 * Performs a quick sort on the edges of the graph from the
	 * array of Edges and returns the sorted array.
	 * 
	 * @return - the sorted list of edges.
	 */
	@Override
	protected Edge[] sort(Edge[] a) {		
		shuffle(a);
		quickSort(a, 0, a.length - 1);
		
		return a;
	}
	
	/* ----------------- Helper Functions -------------- */
	/**
	 * quickSort()
	 * 
	 * The recursive function to actually perform the quicksort
	 * on the list of edges.
	 * 
	 * @param a - the list of edges to be sorted.
	 * @param lo - the index of the low value in a.
	 * @param hi - the index of the high value in a.
	 */
	private void quickSort(Edge[] a, int lo, int hi)
	{		
		if (hi <= lo) return;
		int j = partition(a, lo, hi);
		quickSort(a, lo, j - 1);
		quickSort(a, j+1, hi);
	}
	
	/**
	 * partition()
	 * 
	 * Performs the partitioning for quick sort.
	 * 
	 * @param a - the array of Edges to be sorted.
	 * @param lo - the low value in the array.
	 * @param hi - the high value in the array.
	 * @return - the index of the item which is now in place.
	 */
	private int partition(Edge[] a, int lo, int hi)
	{
		int i = lo;
		int j = hi + 1;
		
		while(true)
		{
			while (a[++i].lessThan(a[lo]))
				if (i == hi) break;
				
			while (a[lo].lessThan(a[--j]))
				if (j == lo) break;
			
			if (i >= j) break;
			swap(a, i, j);
		}
		
		swap(a, lo, j);
		return j;
	}

}
