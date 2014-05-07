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
	
	private final String adjListRepStr = "LIST";
	private final String matrixRepStr = "MATRIX";
	
	private final String countSortStr = "COUNT SORT";
	private final String quickSortStr = "QUICKSORT";
	private final String insertionSortStr = "INSERTION SORT";
	
	private int numVertices = 0;
	private long seed = 0;
	private double p = 0.0;
	
	private Vertex[] vertices;
	private ArrayList<ArrayList<Integer>> adjList;
	private int[][] matrix;
	
	private int[] predecessors;
	
	// Amount of time it took to generate the graph, in milliseconds.
	private long generationTime = 0;
	
	private long kruskalTime = 0;
	private long primTime = 0;
	
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
		adjList = new ArrayList<ArrayList<Integer>>();
		matrix = new int[this.numVertices][this.numVertices];
		
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
			adjList.add(new ArrayList<Integer>());
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
					vertices[i].addEdge(eNew);
					vertices[j].addEdge(eNew);
					
					
					// Add the edge to both vertices in our adjacency list.
					adjList.get(i).add(j);
					adjList.get(j).add(i);
					
					// Add the weighted edge to our matrix.
					matrix[i][j] = weight;
					matrix[j][i] = weight;
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
		ArrayList<Edge> edges = current.getEdges();
//		HashMap<Integer, Long> edges = current.getEdgeMap();
		
		for (int i = 0; i < edges.size(); i++)
		{
			vNext.add(edges.get(i).getConnectedVertex(current));
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
		final String actionStr = "SORTED EDGES";
		Sort iSort = new InsertionSort();
		Sort cSort = new CountSort();
		Sort qSort = new QuickSort();
		
		// Matrix sorts...
		printDivider();
		printEdges(
				iSort.sort(matrix),
				actionStr,
				matrixRepStr, 
				insertionSortStr,
				iSort.getSortTimeMatrix());
		
		printDivider();
		printEdges(
				cSort.sort(matrix), 
				actionStr,
				matrixRepStr, 
				countSortStr,
				cSort.getSortTimeMatrix());
		
		printDivider();
		printEdges(
				qSort.sort(matrix), 
				actionStr,
				matrixRepStr, 
				quickSortStr,
				qSort.getSortTimeMatrix());
		
		// Adjacency List sorts...
		printDivider();
		printEdges(
				iSort.sort(adjList, vertices), 
				actionStr,
				adjListRepStr, 
				insertionSortStr,
				iSort.getSortTimeList());
		
		printDivider();
		printEdges(
				cSort.sort(adjList, vertices), 
				actionStr,
				adjListRepStr, 
				countSortStr,
				cSort.getSortTimeList());
		
		printDivider();
		printEdges(
				qSort.sort(adjList, vertices), 
				actionStr,
				adjListRepStr, 
				quickSortStr,
				qSort.getSortTimeList());
	}
	
	/**
	 * performKruskal()
	 * 
	 * Performs Kruskal's Algorithm to generate an MST using the different
	 * sorts (Insertion, Count, and Quick) and representations of the Graph
	 * (Matrix, List) and prints the results.
	 */
	public void performKruskal()
	{
		final String actionStr = "KRUSKAL";
		
		// Kruskal with Matrix...
		printDivider();
		printEdges(
				kruskalMST(SortType.Insertion, GraphType.Matrix),
				actionStr,
				matrixRepStr, 
				insertionSortStr,
				kruskalTime);
		
		printDivider();
		printEdges(
				kruskalMST(SortType.Count, GraphType.Matrix), 
				actionStr,
				matrixRepStr, 
				countSortStr,
				kruskalTime);
		
		printDivider();
		printEdges(
				kruskalMST(SortType.Quick, GraphType.Matrix), 
				actionStr,
				matrixRepStr, 
				quickSortStr,
				kruskalTime);
		
		// Kruskal with Adjacency List...
		printDivider();
		printEdges(
				kruskalMST(SortType.Insertion, GraphType.List), 
				actionStr,
				adjListRepStr, 
				insertionSortStr,
				kruskalTime);
		
		printDivider();
		printEdges(
				kruskalMST(SortType.Count, GraphType.List),  
				actionStr,
				adjListRepStr, 
				countSortStr,
				kruskalTime);
		
		printDivider();
		printEdges(
				kruskalMST(SortType.Quick, GraphType.List), 
				actionStr,
				adjListRepStr, 
				quickSortStr,
				kruskalTime);
	}
	
	/**
	 * kruskalMST()
	 * 
	 * Creates an MST of the graph by using Kruskal's algorithm.
	 * 
	 * @return the MST as an array of Edges.
	 */
	private Edge[] kruskalMST(SortType sType, GraphType gType)
	{
		kruskalTime = System.currentTimeMillis();
		
		ArrayList<Edge> mst = new ArrayList<Edge>();
		
		// Sort Edges in non-decreasing order by weight.
		Edge[] sorted = null;
		Sort iSort = new InsertionSort();
		Sort cSort = new CountSort();
		Sort qSort = new QuickSort();
		
		switch(sType.getValue())
		{
			case 0: // Insertion Sort
				if (gType == GraphType.Matrix)
					sorted = iSort.sort(matrix);
				else
					sorted = iSort.sort(adjList, vertices);
				break;
				
			case 1: // Count Sort
				if (gType == GraphType.Matrix)
					sorted = cSort.sort(matrix);
				else
					sorted = cSort.sort(adjList, vertices);
				break;
				
			case 2: // Quick Sort
				if (gType == GraphType.Matrix)
					sorted = qSort.sort(matrix);
				else
					sorted = qSort.sort(adjList, vertices);
				break;
				
			default: // Shouldn't happen unless I suck at writing code.
				MST.exitWithMessage("The MST sort type was not specified.");
				break;
		}
		
		// Initialize a partition structure.
		int[] partition = new int[numVertices];
		int[] rank = new int[numVertices];
		for (int i = 0; i < partition.length; i++)
		{
			partition[i] = i; // Set p(v) = v.
			rank[i] = 0; // All Vertices start with rank of 0.
		}
		
		int includedCount = 0;
		int index = 0;
		
		while (includedCount < numVertices - 1) {
			if (index >= sorted.length)
				MST.exitWithMessage("Index is greater than sorted array size.");
			
			Edge curr = sorted[index];
			
			int u = curr.getLeftVertex().getName();
			int v = curr.getRightVertex().getName();
			
			int root1 = find(u, partition);
			int root2 = find(v, partition);
			
			if (root1 != root2)
			{
				//System.out.printf("Added edge: %s %s\n", u, v);
				
				// Add edge to MST.
				mst.add(curr);
				includedCount++;
				// Union root1 and root2.
				union(root1, root2, partition, rank);
			}
			
			index++;
		}
		
		kruskalTime = System.currentTimeMillis() - kruskalTime;
		
		return mst.toArray(new Edge[mst.size()]);
	}
	
	/**
	 * find()
	 * 
	 * Performs union find with path compression on the given Vertex.
	 * 
	 * @param v - the Vertex to find the root of.
	 * @param p - the partition to use to find the root of v.
	 * @return - the Vertex that is the root of v.
	 */
	private int find(int v, int[] p)
	{		
		// The root is where the parent is itself.
		if (v != p[v])
		{
			p[v] = find(p[v], p);
		}
		
		return p[v];
	}
	
	/**
	 * union()
	 * 
	 * Performs the Union-by-Rank operation on the two given Vertices.
	 * 
	 * @param u - one of the Vertices to be unioned.
	 * @param v - one of the Vertices to be unioned.
	 * @param p - the partition for the Vertices.
	 * @param rank - an array of the Vertices' ranks.
	 */
	private void union (int u, int v, int[] p, int[] rank)
	{
		if (rank[u] > rank[v])
		{
			p[v] = u;
		}
		else
		{
			p[u] = v;
			if (rank[u] == rank[v])
				rank[v]++;
		}
	}
	
	/**
	 * performPrim()
	 * 
	 * Performs Prim's Algorithm to generate an MST using the different
	 * representations of the Graph (Matrix, List) and prints the results.
	 */
	public void performPrim()
	{
		final String actionStr = "PRIM";
		
		// Kruskal with Matrix...
		printDivider();
		printEdges(
				primMST(GraphType.Matrix),
				actionStr,
				matrixRepStr, 
				null,
				primTime);
		
		printDivider();
		printEdges(
				primMST(GraphType.List), 
				actionStr,
				adjListRepStr, 
				null,
				kruskalTime);
	}
	
	/**
	 * primMST()
	 * 
	 * Creates a MST from the Graph using Prim's algorithm.
	 * 
	 * @return - the minimum spanning tree of the graph as an array of Edges.
	 */
	private Edge[] primMST(GraphType gType)
	{
		primTime = System.currentTimeMillis();
		
		ArrayList<Edge> mst = new ArrayList<Edge>();
		int[] parent = new int[numVertices];
		
		// Get Edges from the appropriate representation.
		Edge[] edges;
		if (gType == GraphType.List)
		{
			edges = getEdgesFrom(adjList, vertices);
		}
		else
		{
			edges = getEdgesFrom(matrix, vertices);
		}
		
		Vertex[] verts = new Vertex[numVertices];
		for (int i = 0; i < edges.length; i++)
		{
			verts[edges[i].getLeftVertex().getName()] = edges[i].getLeftVertex();
			verts[edges[i].getRightVertex().getName()] = edges[i].getRightVertex();
		}
		
		// Initialize all priorities and weights to "infinity."
		for (int i = 1; i < verts.length; i++)
		{
			verts[i].setPriority(Integer.MAX_VALUE);
			verts[i].setWeight(Integer.MAX_VALUE);
		}
		
		// We want to start with Vertex 0.
		verts[0].setPriority(0);
		parent[0] = 0; // Vertex 0 is its own parent.
		
		MinPQ pq = new MinPQ(verts);
		
		while (!pq.isEmpty())
		{
			// Remove the min element.
			Vertex u = pq.deleteMin();
			
			// If it's any Vertex other than Vertex 0...
			if (u.getName() != 0)
			{
				// Add the edge between u and its parent.
				mst.add(new Edge(
						verts[parent[u.getName()]], 
						u, 
						u.getEdge(parent[u.getName()]).getWeight()));
			}
			
			ArrayList<Edge> vEdges = u.getEdges();
			Iterator<Edge> iter = vEdges.iterator();
			
			// For every edge v adjacent to u...
			while (iter.hasNext())
			{
				Edge currEdge = iter.next();
				try {
					Vertex v = currEdge.getOtherVertex(u);
					
					// If v isn't in the MST already, and
					// weight(u,v) < priority of v in PQ...
					if (pq.contains(v.getName()) && 
							currEdge.getWeight() < v.getPriority())
					{
						// Update the parent and priority.
						parent[v.getName()] = u.getName();
						v.setPriority(currEdge.getWeight());
						pq.heapify();
					}
				
				} catch(VertexException e) {
					MST.exitWithError(e);
				}
			}
		}
		
		Edge[] mstArr = new Edge[numVertices - 1];
		for (int i = 0; i < mst.size(); i++)
		{
			Edge e = mst.get(i);
			mstArr[e.getRightVertex().getName() - 1] = e;
		}
		
		primTime = System.currentTimeMillis() - primTime;
		
		return mstArr;
	}
	
	/**
	 * getEdgesFrom()
	 * 
	 * Gets an array of edges from an adjacency list representation
	 * of a graph.
	 * 
	 * @param adjList - the adjacency list to get the edges from.
	 * @return - an array of the Edges in the graph.
	 */
	private Edge[] getEdgesFrom(
			ArrayList<ArrayList<Integer>> adjList,
			Vertex[] vertices)
	{
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		for (int i = 0; i < adjList.size(); i++)
		{
			for (int j = 0; j < adjList.get(i).size(); j++)
			{
				// Get the edge from each vertex...
				Edge currEdge = vertices[i].getEdgeByVRight(adjList.get(i).get(j));
				
				// Add the edges to our ArrayList.
				// This will ignore duplicate edges.
				if (currEdge != null && !edges.contains(currEdge))
					edges.add(currEdge);
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
	private Edge[] getEdgesFrom(int[][] matrix, Vertex[] vertices)
	{
		HashMap<Integer, HashSet<Integer>> visited = 
			new HashMap<Integer, HashSet<Integer>>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
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
						Vertex left = vertices[i];
						Vertex right = vertices[j];
												
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
		
		// Convert the ArrayList of Edges to an array and return it.
		return edges.toArray(new Edge[edges.size()]);
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
				
				if (matrix[i][j] == 10)
					spacing = "  ";
				
				currLine += spacing + Long.toString(matrix[i][j]);
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
		for (int i = 0; i < adjList.size(); i++)
		{
			Vertex currVertex = vertices[i];
			
			ArrayList<Edge> edges = currVertex.getEdges();
			
			String currLine = currVertex.nameToString() + "->";
			
			for (int j = 0; j < edges.size(); j++)
			{
				Edge edge = edges.get(j);
				currLine += " " 
					+ Integer.toString(
							edge.getConnectedVertex(currVertex).getName()
							) 
					+ String.format("(%d)", edge.getWeight());
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
	 * printEdges()
	 * 
	 * Prints an array of sorted edges, along with relevant info.
	 * 
	 * @param a - the array of sorted edges.
	 * @param actionStr - the action performed (i.e. KRUSKAL, SORTED EDGES)
	 * @param gRep - graph representation upon which the sort was performed.
	 * @param sortName - name of the sort (all caps) used.
	 * @param runtime - runtime of the sort (stored in private vars upon sort).
	 */
	public void printEdges(
			Edge[] a,
			String actionStr,
			String gRep, 
			String sortName, 
			long runtime)
	{
		if (sortName != null)
			System.out.printf("%s WITH %s USING %s\n",
					actionStr,
					gRep,
					sortName);
		else
			System.out.printf("%s WITH %s\n",
					actionStr,
					gRep);
		
		int totalWeight = 0;
		for (int i = 0; i < a.length; i++)
		{
			totalWeight += a[i].getWeight();
			
			if (numVertices <= 10)
			{
				System.out.printf("%d %d weight = %d\n", 
						a[i].getLeftVertex().getName(), 
						a[i].getRightVertex().getName(), 
						a[i].getWeight());
			}
		}
		
		if (actionStr.equals("KRUSKAL"))
		{
			System.out.printf("\nTotal weight of MST using Kruskal: %d\n", 
					totalWeight);			
		}
		else
			System.out.printf("\nTotal weight = %d\n", totalWeight);
		
		System.out.printf("Runtime: %d milliseconds\n\n", runtime);
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
		for (int i = 0; i < adjList.size(); i++)
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
		adjList = new ArrayList<ArrayList<Integer>>();
		
		// Reset the matrix.
		for (int i = 0; i < numVertices; i++)
		{
			for (int j = 0; j < numVertices; j++)
			{
				matrix[i][j] = 0;
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
