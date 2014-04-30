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

import java.util.Comparator;
import java.util.NoSuchElementException;

public class MinPQ {

	private Key[] a;	// Items in the PQ, stored from indices 1 to N
	private int N;		// Number of items in the priority queue
	private Comparator<Key> comparator;
	
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
		a = (Key[]) new Object[capacity + 1];
		N = 0;
	}
	
	/**
	 * MinPQ()
	 * 
	 * Initializes the priority queue with the given capacity and
	 * the given comparator.
	 * 
	 * @param capacity - the initial capacity of the priority queue.
	 * @param comparator - the comparator to be used.
	 */
	public MinPQ(int capacity, Comparator<Key> comparator) {
		this.comparator = comparator;
		a = (Key[]) new Object[capacity + 1];
		N = 0;
	}
	
	/**
	 * MinPQ()
	 * 
	 * Initializes the priority queue with a capacity of 1 and
	 * the given comparator.
	 * 
	 * @param comparator - the comparator to be used.
	 */
	public MinPQ(Comparator<Key> comparator) {
		this(1, comparator);
	}
	
	/**
	 * MinPQ()
	 * 
	 * Initializes the priority queue using a given array
	 * of keys.
	 * 
	 * @param keys - the keys to construct the priority queue from.
	 */
	public MinPQ(Key[] keys) {
		N = keys.length;
		a = (Key[]) new Object[keys.length + 1];
		
		// Fill in the pq from 1 to N with the given keys.
		for (int i = 1; i <= N; i++)
			a[i] = keys[i-1];
		
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
	public Key min() {
		if (isEmpty())
			throw new NoSuchElementException("No elements in PQ!");
		
		return a[1];
	}
	
	/**
	 * resize()
	 * 
	 * Resizes the priority queue to the given capacity.
	 * 
	 * @param capacity
	 */
	private void resize(int capacity){
		Key[] temp = (Key[]) new Object[capacity];
		
		for (int i = 1; i <= N; i++)
			temp[i] = a[i];
		
		a = temp;
	}
	
	/**
	 * insert()
	 * 
	 * Inserts a new element into the priority queue.
	 * 
	 * @param e
	 */
	public void insert(Key e) {
		// If we're out of space, resize the priority queue.
		if (N == a.length - 1)
			resize(2 * a.length);
		
		// Insert e, then have it swim up to an appropriate position.
		a[++N] = e;
		swim(e);
	}
	
	public Key popMin() {
		if (isEmpty())
			throw new NoSuchElementException("Priority queue has no elements!");
		
		// Swap the first and last element.
		swap(1, N);
		
		// Keep a reference to the first element.
		Key min = a[N--];
		
		// Sink the elemeent swapped to the front.
		sink(1);
		
		// Remove the last element.
		a[N+1] = null;
		
		// If necessary, resize the priority queue.
		if ((N > 0) && (N == (a.length - 1) / 4))
			resize(a.length / 2);
		
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
			k=j;
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
		if (comparator == null) {
			return ((Comparable<Key>) a[i]).compareTo(a[j]) > 0;
		}
		else {
			return comparator.compare(a[i], a[j]) > 0;
		}
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
		Key exch = a[i];
		a[i] = a[j];
		a[j] = exch;
	}
	
}
