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

public class MinPQ {

	private Vertex[] pq;	// Items in the PQ, stored from indices 1 to N
	private int[] qp;	// Location of Vertex i (i is index in qp) in pq.
	private int N;		// Number of items in the priority queue
	
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
		pq = (Vertex[]) new Object[capacity + 1];
		N = 0;
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
		pq = (Vertex[]) new Object[keys.length + 1];
		
		// Fill in the pq from 1 to N with the given keys.
		for (int i = 1; i <= N; i++)
			pq[i] = keys[i-1];
		
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
	public Vertex min() {
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
		Vertex[] temp = (Vertex[]) new Object[capacity];
		
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
		pq[++N] = e;
		swim(N);
	}
	
	public Vertex popMin() {
		if (isEmpty())
			throw new NoSuchElementException("Priority queue has no elements!");
		
		// Swap the first and last element.
		swap(1, N);
		
		// Keep a reference to the first element.
		Vertex min = pq[N--];
		
		// Sink the elemeent swapped to the front.
		sink(1);
		
		// Remove the last element.
		pq[N+1] = null;
		
		// If necessary, resize the priority queue.
		if ((N > 0) && (N == (pq.length - 1) / 4))
			resize(pq.length / 2);
		
		return min;
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
		if (pq[i].getPriority() > pq[j].getPriority())
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
		Vertex exch = pq[i];
		pq[i] = pq[j];
		pq[j] = exch;
	}
	
}
