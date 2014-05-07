/**
 * MinPQ.java
 * 
 * This is a minimum priority queue, implemented as
 * a binary heap. This is only a basic minimum priority queue 
 * with the sufficient functions to use for Prim's Algorithm. 
 * 
 * @author MikeYeaple
 *
 */

import java.util.NoSuchElementException;
import java.util.HashSet;

public class MinPQ {

	private int[][] pq;	// Items in the PQ, stored from indices 1 to N
	private int[] qp;	// Location of Vertex i (i is index in qp) in pq.
	private int N;		// Number of items in the priority queue
	
	private final int NUM_NODE_PROPERTIES = 3; // Vertex, Weight, Parent
	private final int VERTEX_PROP = 0; // Index of Vertex Property
	private final int WEIGHT_PROP = 1; // Index of Weight(Priority) Property
	private final int PARENT_PROP = 2; // Index of Parent Property
	
	private final int NO_PARENT = -1;
	
	/**
	 * MinPQ()
	 * 
	 * Initializes an empty priority queue of size 1.
	 */
	public MinPQ() {
		this(1);
	}
	
	/**
	 * MinPQ()
	 * 
	 * Initializes the priority queue with the given capacity.
	 * 
	 * @param capacity - the initial capacity of the priority queue
	 */
	public MinPQ(int capacity) {
		pq = new int[capacity + 1][NUM_NODE_PROPERTIES];
		qp = new int[capacity];
		N = 0;
		
		for (int i = 0; i < capacity; i++)
			qp[i] = -1;
	}
	
	/**
	 * MinPQ()
	 * 
	 * Initializes the priority queue using a given array
	 * of keys.
	 * 
	 * @param keys - the keys to construct the priority queue from.
	 */
	public MinPQ(Vertex[] keys) {
		N = keys.length;
		pq = new int[keys.length + 1][NUM_NODE_PROPERTIES];
		
		int max = 0;
		for (int i = 0; i < keys.length; i++)
		{
			if (keys[i].getName() > max)
				max = keys[i].getName();
		}
		qp = new int[max + 1];
		
		// Fill in the pq from 1 to N with the given keys.
		for (int i = 1; i <= N; i++)
		{
			pq[i][VERTEX_PROP] = keys[i-1].getName();
			pq[i][WEIGHT_PROP] = Integer.MAX_VALUE;
			pq[i][PARENT_PROP] = NO_PARENT;
			qp[keys[i-1].getName()] = i;
		}
		
		heapify();
	}
	
	/**
	 * heapify()
	 * 
	 * Heapifies the minimimum PQ.
	 */
	public void heapify()
	{
		// Sink the necessary elements in the first half of the
		// priority queue to heapify it.
		for (int k = N/2; k >= 1; k--)
			sink(k);
	}
	
	/**
	 * isEmpty()
	 * 
	 * Returns true if the priority queue has no elements.
	 * 
	 * @return - true if there are no elements in the priority queue.
	 */
	public boolean isEmpty() {
		return N == 0;
	}
	
	/**
	 * size()
	 * 
	 * Returns the number of elements in the priority queue.
	 * 
	 * @return - the number of elements in the priority queue.
	 */
	public int size() {
		return N;
	}
	
	/**
	 * min()
	 * 
	 * Returns the minimum element in the priority queue.
	 * 
	 * @return - the minimum element in the priority queue.
	 */
	public int[] min() {
		if (isEmpty())
			throw new NoSuchElementException("No elements in PQ!");
		
		return pq[1];
	}
	
	/**
	 * resize()
	 * 
	 * Resizes the priority queue to the given capacity.
	 * 
	 * @param capacity
	 */
	private void resize(int capacity){
		int[][] temp = new int[capacity][NUM_NODE_PROPERTIES];
		
		for (int i = 1; i <= N; i++)
			temp[i] = pq[i];
		
		pq = temp;
	}
	
	/**
	 * insert()
	 * 
	 * Inserts a new element into the priority queue.
	 * 
	 * @param e
	 */
	public void insert(Vertex e) {
		// If we're out of space, resize the priority queue.
		if (N == pq.length - 1)
			resize(2 * pq.length);
		
		// Insert e, then have it swim up to an appropriate position.
		pq[++N][VERTEX_PROP] = e.getName();
		pq[N][WEIGHT_PROP] = Integer.MAX_VALUE; // Initialize to "infinity."
		pq[N][PARENT_PROP] = NO_PARENT; // Initialize to no parent.
		
		// If the new Vertex name is > qp.length + 1, resize it.
		if (e.getName() > qp.length + 1)
		{
			int[] temp = new int[e.getName() + 1];
			
			for (int i = 0; i < qp.length; i++)
			{
				temp[i] = qp[i];
			}
			
			qp = temp;
		}
		qp[e.getName()] = N;
		swim(N);
	}
	
	/**
	 * deleteMin()
	 * 
	 * Deletes the minimum element from the PQ and returns it.
	 * 
	 * @return - the minimum element in the PQ.
	 */
	public int[] deleteMin() {
		if (isEmpty())
			throw new NoSuchElementException("Priority queue has no elements!");
		
		// Swap the first and last element.
		swap(1, N);
		
		// Keep a reference to the first element.
		qp[pq[N][VERTEX_PROP]] = -1; // Mark it as no longer in PQ.
		int[] min = pq[N--];
		
		// Sink the element swapped to the front.
		sink(1);
		
		// Remove the last element.
		pq[N+1] = null;
		
		// If necessary, resize the priority queue.
		if ((N > 0) && (N == (pq.length - 1) / 4))
			resize(pq.length / 2);
		
		return min;
	}
	
	/**
	 * contains()
	 * 
	 * Returns true if PQ contains Vertex i
	 * 
	 * @param i - the name of the Vertex to check for.
	 * @return - true if Vertex i is in PQ.
	 */
	public boolean contains(int i)
	{
		return qp[i] != -1;
	}
	
	/**
	 * setPriority()
	 * 
	 * Sets the priority of a given vertex with a new weight and
	 * parent.
	 * 
	 * @param vertex - the vertex to update.
	 * @param weight - the new weight.
	 * @param parent - the new parent.
	 */
	public void setPriority(int vertex, int weight, int parent)
	{
		// Set the new values.
		pq[qp[vertex]][WEIGHT_PROP] = weight;
		pq[qp[vertex]][PARENT_PROP] = parent;
		
		// Re-heapify.
		heapify();
	}
	
	/**
	 * getPriority()
	 * 
	 * Gets the priority of the specified vertex.
	 * 
	 * @param vertex - the Vertex whose priority you want to retrieve.
	 * @return - the priority of the specified vertex.
	 */
	public int getPriority(int vertex)
	{
		return pq[qp[vertex]][WEIGHT_PROP];
	}
	
	/* Binary Heap Helper Functions */
	
	/**
	 * swim()
	 * 
	 * The standard swim function for the heap.
	 * 
	 * @param k - the index of the element to swim.
	 */
	private void swim(int k) {
		while (k > 1 && greater(k/2, k)) {
			swap(k, k/2);
			k = k/2;
		}
	}
	
	/**
	 * sink()
	 * 
	 * The standard sink function for the heap.
	 * 
	 * @param k - the index of the element to sink.
	 */
	private void sink(int k) {
		while (2*k <= N) {
			int j = 2*k;
			
			if (j < N && greater(j, j+1))
				j++;
			
			if (!greater(k, j))
				break;
			
			swap(k, j);
			k = j;
		}
	}
	
	/* Other Helper Functions */
	
	/**
	 * greater()
	 * 
	 * Returns true if element at i is greater than element at j.
	 * 
	 * @param i - index of the first element to be compared.
	 * @param j - index of the second element to be compared.
	 */
	private boolean greater(int i, int j) {
		if (pq[i][WEIGHT_PROP] > pq[j][WEIGHT_PROP])
			return true;
		
		return false;
	}
	
	/**
	 * swap()
	 * 
	 * Swaps the two elements at the given indices.
	 * 
	 * @param i - index of the first element to be swapped.
	 * @param j - index of the second element to be swapped.
	 */
	private void swap(int i, int j) {
		// Update qp first...
		qp[pq[i][VERTEX_PROP]] = j;
		qp[pq[j][VERTEX_PROP]] = i;
		
		int[] exch = pq[i];
		pq[i] = pq[j];
		pq[j] = exch;
	}
	
}
