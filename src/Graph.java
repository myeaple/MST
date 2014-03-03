/**
 * Graph.java
 * 
 * The purpose of this class is to represent an undirected, weighted graph.
 * 
 * @author Michael Yeaple
 *
 */

import java.util.*;

public class Graph {

	private final int MIN = 1;
	
	private int numVertices = 0;
	private long seed = 0;
	private double p = 0.0;
	
	private Vertex[] vertices;
	private ArrayList<ArrayList<Integer>> gAdjList;
	private ArrayList<Edge> edges; // Edges in the adjacency list.
	private long[][] gMatrix;
	
	private int[] predecessors;
	
	// Amount of time it took to generate the graph, in milliseconds.
	private long generationTime = 0;
	
	// Sort times for the adjacency list.
	private long quickSortListTime = 0;
	private long insertionSortListTime = 0;
	private long countSortListTime = 0;
	
	// Sort times for the matrix.
	private long quickSortMatrixTime = 0;
	private long insertionSortMatrixTime = 0;
	private long countSortMatrixTime = 0;
	
	/*
	 * Do NOT use the default constructor. Instead, use the specific
	 * constructor.
	 */
	public Graph() { }
	
	/**
	 * Graph()
	 * 
	 * Sets the graph variables and generates a graph based on the inputs.
	 * 
	 * @param numV - number of vertices in the graph.
	 * @param seed - a seed number for generating random numbers.
	 * @param p - the probability of any two vertices being connected.
	 */
	public Graph(int numV, long seed, double p)
	{
		this.numVertices = numV;
		this.seed = seed;
		this.p = p;
		
		vertices = new Vertex[this.numVertices];
		gAdjList = new ArrayList<ArrayList<Integer>>();
		gMatrix = new long[this.numVertices][this.numVertices];
		edges = new ArrayList<Edge>();
		
		generate();
	}
	
	/**
	 * generate()
	 * 
	 * Generates the graph based upon the variables from the input file.
	 */
	public void generate()
	{
		// Time how long it takes to generate the graph.
		generationTime = System.currentTimeMillis();
		
		// Generate vertices for our adjacency list
		for (int i = 0; i < numVertices; i++)
		{
			vertices[i] = new Vertex(i);
			gAdjList.add(new ArrayList<Integer>());
		}
		
		Random rConnect = new Random(seed);
		Random wConnection = new Random(seed * 2);
		// For each pair of vertices, determine if they are connected.
		for (int i = 0; i < numVertices; i++)
		{
			// j = i + 1 because we don't want to try a pair twice.
			for (int j = i + 1; j < numVertices; j++)
			{
				double connected = rConnect.nextDouble();
				if (connected <= p)
				{
					// int range = max - min + 1
					int range = numVertices - MIN + 1;
					int weight = MIN + wConnection.nextInt(range);
					Edge eNew = new Edge(vertices[i], vertices[j], weight);
					
					// Add edges to our vertices.
					try 
					{
						vertices[i].addEdge(vertices[j], weight);
						vertices[j].addEdge(vertices[i], weight);
						vertices[i].addEdge(eNew);
						vertices[j].addEdge(eNew);
						edges.add(eNew);
					} catch (VertexException e) 
					{
						MST.exitWithError(e);
					}
					
					// Add the edge to both vertices in our adjacency list.
					gAdjList.get(i).add(j);
					gAdjList.get(j).add(i);
					
					// Add the weighted edge to our matrix.
					gMatrix[i][j] = weight;
					gMatrix[j][i] = weight;
 				}
			}
		}
		
		generationTime = System.currentTimeMillis() - generationTime;
		
		if (!isConnectedGraph())
		{
			// We only want a connected graph. Try again.
			resetGraphs();
			generate();
		}
	}
	
	/**
	 * getGenerationTime()
	 * 
	 * Gets the time it took to generate the graph in milliseconds.
	 * 
	 * @return - time taken to generate the graph in milliseconds.
	 */
	public long getGenerationTime()
	{
		return generationTime;
	}
	
	/**
	 * isConnectedGraph()
	 * 
	 * Checks to see if the entire graph is connected by doing a
	 * depth-first search.
	 * 
	 * @return - true if connected; otherwise, false.
	 */
	public boolean isConnectedGraph()
	{
		boolean isConnected = false;
		
		if (numVertices == countVertices())
			isConnected = true;
		
		return isConnected;
	}
	
	/**
	 * countVertices()
	 * 
	 * Performs a DFS to count the number of vertices in the graph.
	 * 
	 * @return - number of vertices in the graph.
	 */
	private int countVertices()
	{
		resetDFSLists();
		
		int count = countVerticesHelper(vertices[0], null, 0);
		
		resetVertices();
		
		return count;
	}
	
	/**
	 * countVerticesHelper()
	 * 
	 * Recursive function to do a depth-first search to count
	 * the number of vertices.
	 * 
	 * @param count
	 * @return
	 */
	private int countVerticesHelper(Vertex currVertex, Vertex prev, int count)
	{
		Vertex current = currVertex;
		
		// If we've already visited the vertex, skip it.
		if (!current.isVisited())
		{
			current.visit(); // Mark the vertex as visited.
			count++; // Increment our counter
			
			if (prev != null)
				predecessors[currVertex.getName()] = prev.getName();
			
			ArrayList<Vertex> next = getNextVertices(current);
			
			for (int i = 0; i < next.size(); i++)
			{
				count = countVerticesHelper(next.get(i), currVertex, count);
			}
		}
		
		return count;
	}
	
	/**
	 * getNextVertices()
	 * 
	 * Gets an ArrayList of the vertices connected to the one passed in.
	 * 
	 * @param current - the vertex whose connected vertices you want to get.
	 * @return - the connected vertices.
	 */
	private ArrayList<Vertex> getNextVertices(Vertex current)
	{
		ArrayList<Vertex> vNext = new ArrayList<Vertex>();
		
		// Map of weights by the connected node name.
		HashMap<Integer, Long> edges = current.getEdgeMap();
		
		for (int i = 0; i < numVertices; i++)
		{
			if (edges.containsKey(i))
			{
				vNext.add(vertices[i]);
			}
		}
		
		return vNext;
	}
	
	/**
	 * performEdgeSorts()
	 * 
	 * Performs insertion, count, and quick sort on the edges of the graph
	 * for both the matrix and the adjacency list, and prints the
	 * results.
	 */
	public void performEdgeSorts()
	{
		final String adjListRepStr = "LIST";
		final String matrixRepStr = "MATRIX";
		
		final String countSortStr = "COUNT SORT";
		final String quickSortStr = "QUICKSORT";
		final String insertionSortStr = "INSERTION SORT";
		
		// Matrix sorts...
//		printDivider();
//		printSortedEdges(
//				insertionSortMatrix(), 
//				matrixRepStr, 
//				insertionSortStr,
//				insertionSortMatrixTime);
//		
//		printDivider();
//		printSortedEdges(
//				countSortMatrix(), 
//				matrixRepStr, 
//				countSortStr,
//				countSortMatrixTime);
//		
//		printDivider();
//		printSortedEdges(
//				quickSortMatrix(), 
//				matrixRepStr, 
//				quickSortStr,
//				quickSortMatrixTime);
		
		// Adjacency List sorts...
		printDivider();
		printSortedEdges(
				insertionSortList(), 
				adjListRepStr, 
				insertionSortStr,
				insertionSortListTime);
		
		printDivider();
		printSortedEdges(
				countSortList(), 
				adjListRepStr, 
				countSortStr,
				countSortListTime);
		
		printDivider();
		printSortedEdges(
				quickSortList(), 
				adjListRepStr, 
				quickSortStr,
				quickSortListTime);
		
	}
	
	/* ---------------- Edge Sorting Functions ---------------- */
	
	/**
	 * insertionSortList()
	 * 
	 * Performs an insertion sort on the edges of the graph from the
	 * adjacency list and returns the sorted list.
	 * 
	 * @return - the sorted list of edges.
	 */
	public Edge[] insertionSortList()
	{
		// Time how long it takes to sort the edges.
		insertionSortListTime = System.currentTimeMillis();
		
		Edge[] a = edges.toArray(new Edge[edges.size()]);
		
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
		
		insertionSortListTime = 
			System.currentTimeMillis() - insertionSortListTime;
		
		return a;
	}
	
	/**
	 * insertionSortMatrix()
	 * 
	 * Performs an insertion sort on the edges of the graph from the
	 * matrix and returns the sorted list.
	 * 
	 * @return - the sorted list of edges.
	 */
	public Edge[] insertionSortMatrix()
	{
		// Time how long it takes to sort the edges.
		insertionSortMatrixTime = System.currentTimeMillis();
		
		// TODO: Insertion sort on the matrix.
		
		insertionSortMatrixTime = 
			System.currentTimeMillis() - insertionSortMatrixTime;
		
		return null;
	}
	
	/**
	 * countSortList()
	 * 
	 * Performs a count sort on the edges of the graph from the 
	 * adjacency list and returns the sorted list.
	 * 
	 * @return - the sorted list of edges.
	 */
	public Edge[] countSortList()
	{
		// Time how long it takes to sort the edges.
		countSortListTime = System.currentTimeMillis();
		
		Edge[] a = edges.toArray(new Edge[edges.size()]);
		Edge[] aux = new Edge[a.length];
		int count[];
		int r = 0;
		
		// Determine the max weight (R).
		int max = 0;
		Iterator<Edge> iter = edges.iterator();
		while(iter.hasNext())
		{
			Edge current = iter.next();
			int weight = current.getWeight();
			
			if (max < weight)
				max = weight;
		}
		r = max + 1;
		
		count = new int[r];
		
		// Fill the count array.
		for (int i = 0; i < a.length; i++)
			count[a[i].getWeight() + 1]++;
		
		// Calculate the sums in the count array.
		for (int i = 0; i < count.length - 1; i++)
			count[i + 1] += count[i];
		
		// Sort the Edges into the aux array.
		for (int i = 0; i < a.length; i++)
			aux[count[a[i].getWeight()]++] = a[i];
		
		countSortListTime = 
			System.currentTimeMillis() - countSortListTime;
		
		return aux;
	}
	
	/**
	 * countSortMatrix()
	 * 
	 * Performs a count sort on the edges of the graph from the 
	 * matrix and returns the sorted list.
	 * 
	 * @return - the sorted list of edges.
	 */
	public Edge[] countSortMatrix()
	{
		// Time how long it takes to sort the edges.
		countSortMatrixTime = System.currentTimeMillis();
		
		// TODO: Perform count sort on the matrix.
		
		countSortMatrixTime = 
			System.currentTimeMillis() - countSortMatrixTime;
		
		return null; // return aux;
	}
	
	/**
	 * quickSortList()
	 * 
	 * Performs a quick sort on the edges of the graph from the
	 * adjacency list and returns the sorted list.
	 * 
	 * @return - the sorted list of edges.
	 */
	public Edge[] quickSortList()
	{
		// Time how long it takes to sort the edges.
		quickSortListTime = System.currentTimeMillis();
		
		Edge[] a = edges.toArray(new Edge[edges.size()]);
		
		shuffle(a);
		quickSort(a, 0, a.length - 1);
		
		quickSortListTime = 
			System.currentTimeMillis() - quickSortListTime;
		
		return a;
	}
	
	/**
	 * quickSortMatrix()
	 * 
	 * Performs a quick sort on the edges of the graph from the
	 * matrix and returns the sorted list.
	 * 
	 * @return - the sorted list of edges.
	 */
	public Edge[] quickSortMatrix()
	{
		// Time how long it takes to sort the edges.
		quickSortMatrixTime = System.currentTimeMillis();
		
		quickSortMatrixTime = 
			System.currentTimeMillis() - quickSortMatrixTime;
		
		return null;
	}
	
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
		if (hi == lo) return;
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
	
	/* ---------------- Sort Helper Functions ---------------- */
	
	/**
	 * swap()
	 * 
	 * Swaps two elements in the given array.
	 * 
	 * @param arr - the array whose elements you want to swap.
	 * @param i, j - the position of one of the elements to swap
	 */
	private void swap(Edge[] arr, int i, int j)
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
	private void shuffle(Edge[] a)
	{
		Random rand = new Random();
		for (int i = 0; i < a.length; i++)
		{
			int r = i + rand.nextInt(a.length - 1);
			swap(a, i, r);
		}
	}
	
	/* ---------------- Print Functions ---------------- */
	/**
	 * printAdjacencyMatrix()
	 * 
	 * Prints the graph represented as an adjacency matrix.
	 */
	public void printAdjacencyMatrix()
	{
		// Only print if there are <= 10 vertices
		if (numVertices > 10)
			return;
		
		System.out.println("\nThe graph as an adjacency matrix:");
		
		for (int i = 0; i < numVertices; i++)
		{
			String currLine = "\n ";
			
			for (int j = 0; j < numVertices; j++)
			{
				String spacing = "   ";
				
				if (gMatrix[i][j] == 10)
					spacing = "  ";
				
				currLine += spacing + Long.toString(gMatrix[i][j]);
			}
			
			System.out.println(currLine);
		}
	}
	
	/**
	 * printAdjacencyList()
	 * 
	 * Prints the graph represented as an adjacency list.
	 */
	public void printAdjacencyList()
	{
		// Only print if there are <= 10 vertices
		if (numVertices > 10)
			return;
		
		System.out.println("\nThe graph as an adjacency list:");
		for (int i = 0; i < gAdjList.size(); i++)
		{
			Vertex currVertex = vertices[i];
			
			HashMap<Integer, Long> edges = currVertex.getEdgeMap();
			
			String currLine = currVertex.nameToString() + "->";
			
			for (int j = 0; j < numVertices; j++)
			{
				if (edges.containsKey(j))
				{
					currLine += " " + Integer.toString(j) 
						+ String.format("(%s)", Long.toString(edges.get(j)));
				}
			}
			
			System.out.println(currLine);
		}
	}
	
	/**
	 * printDFSInfo()
	 * 
	 * Prints the information related to the depth-first search.
	 */
	public void printDFSInfo()
	{
		// Only print if there are <= 10 vertices
		if (numVertices > 10)
			return;
		
		String verticesStr = "";		
		String predecessorsStr = "";
		for (int i = 0; i < predecessors.length; i++)
		{			
			if (i > 0)
				predecessorsStr += " ";
			
			verticesStr += " " + Integer.toString(i);
			predecessorsStr += Integer.toString(predecessors[i]);
		}
		
		System.out.println("\nDepth-First Search:\nVertices:");
		System.out.println(verticesStr);
		System.out.println("Predecessors:");
		System.out.println(predecessorsStr);
	}
	
	/**
	 * printSortedEdges()
	 * 
	 * Prints an array of sorted edges, along with relevant info.
	 * 
	 * @param a - the array of sorted edges.
	 * @param gRep - graph representation upon which the sort was performed.
	 * @param sortName - name of the sort (all caps) used.
	 * @param runtime - runtime of the sort (stored in private vars upon sort).
	 */
	public void printSortedEdges(
			Edge[] a, 
			String gRep, 
			String sortName, 
			long runtime)
	{
		System.out.printf("SORTED EDGES WITH %s USING %s\n",
				gRep,
				sortName);
		
		int totalWeight = 0;
		for (int i = 0; i < a.length; i++)
		{
			totalWeight += a[i].getWeight();
			
			System.out.printf("%d %d weight = %d\n", 
					a[i].getLeftVertex(), 
					a[i].getRightVertex(), 
					a[i].getWeight());
		}
		
		System.out.printf("\nTotal weight = %d\n", totalWeight);
		System.out.printf("Runtime: %d milliseconds", runtime);
	}
	
	/**
	 * printDivider()
	 * 
	 * Prints a divider for breaking up sections of the program output.
	 */
	private void printDivider()
	{
		System.out.println("===================================");
	}
	
	/* ---------------- Reset Functions ---------------- */
	
	/**
	 * resetVertices()
	 * 
	 * Sets each of the vertices to unvisited.
	 */
	private void resetVertices()
	{
		for (int i = 0; i < gAdjList.size(); i++)
		{
			vertices[i].reset();
		}
	}
	
	/**
	 * resetGraphs()
	 * 
	 * Resets both the matrix and the adjacency list.
	 */
	private void resetGraphs()
	{
		// Reset the adjacency list.
		gAdjList = new ArrayList<ArrayList<Integer>>();
		
		// Reset the matrix.
		for (int i = 0; i < numVertices; i++)
		{
			for (int j = 0; j < numVertices; j++)
			{
				gMatrix[i][j] = 0;
			}
		}
	}
	
	/**
	 * resetDFSLists()
	 * 
	 * Resets the lists containing data from the DFS.
	 */
	private void resetDFSLists()
	{
		predecessors = new int[numVertices];
		predecessors[0] = -1;
	}
	
}
