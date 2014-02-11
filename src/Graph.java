/**
 * Graph.java
 * 
 * The purpose of this class is to represent an undirected, weighted graph.
 * 
 * @author MikeYeaple
 *
 */

import java.util.*;

public class Graph {

	private int numVertices = 0;
	private long seed = 0;
	private double p = 0.0;
	
	private ArrayList<Vertex> gAdjList;
	private long[][] gMatrix;
	
	private ArrayList<Integer> dfsVertices;
	private ArrayList<Integer> predecessors;
	
	// Amount of time it took to generate the graph, in milliseconds.
	private long generationTime = 0;
	
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
		
		gAdjList = new ArrayList<Vertex>();
		gMatrix = new long[this.numVertices][this.numVertices];
		
		
		Generate();
	}
	
	/**
	 * Generate()
	 * 
	 * Generates the graph based upon the variables from the input file.
	 */
	public void Generate()
	{
		// Time how long it takes to generate the graph.
		generationTime = System.currentTimeMillis();
		
		// Generate vertices for our adjacency list
		for (int i = 0; i < numVertices; i++)
		{
			gAdjList.add(new Vertex(i));
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
					int range = numVertices;
					int weight = wConnection.nextInt(range);
					
					// Add the edge to both vertices in our adjacency list.
					try 
					{
						gAdjList.get(i).AddEdge(gAdjList.get(j), weight);
						gAdjList.get(j).AddEdge(gAdjList.get(i), weight);
					} catch (VertexException e) 
					{
						MST.ExitWithError(e);
					}
					
					// Add the weighted edge to our matrix.
					gMatrix[i][j] = weight;
				}
			}
		}
		
		generationTime = System.currentTimeMillis() - generationTime;
		
		if (!IsConnectedGraph())
		{
			// We only want a connected graph. Try again.
			ResetGraphs();
			Generate();
		}
	}
	
	/**
	 * GetGenerationTime()
	 * 
	 * Gets the time it took to generate the graph in milliseconds.
	 * 
	 * @return - time taken to generate the graph in milliseconds.
	 */
	public long GetGenerationTime()
	{
		return generationTime;
	}
	
	/**
	 * IsConnectedGraph()
	 * 
	 * Checks to see if the entire graph is connected by doing a
	 * depth-first search.
	 * 
	 * @return - true if connected; otherwise, false.
	 */
	public boolean IsConnectedGraph()
	{
		boolean isConnected = false;
		
		if (numVertices == CountVertices())
			isConnected = true;
		
		return isConnected;
	}
	
	/**
	 * PrintAdjacencyMatrix()
	 * 
	 * Prints the graph represented as an adjacency matrix.
	 */
	public void PrintAdjacencyMatrix()
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
				currLine += "   " + Long.toString(gMatrix[i][j]);
			}
			
			System.out.println(currLine);
		}
	}
	
	/**
	 * PrintAdjacencyList()
	 * 
	 * Prints the graph represented as an adjacency list.
	 */
	public void PrintAdjacencyList()
	{
		// Only print if there are <= 10 vertices
		if (numVertices > 10)
			return;
		
		System.out.println("\nThe graph as an adjacency list:");
		for (int i = 0; i < gAdjList.size(); i++)
		{
			Vertex currVertex = gAdjList.get(i);
			
			HashMap<Integer, Long> edges = currVertex.GetEdges();
			
			String currLine = currVertex.NameToString() + "->";
			
			for (int j = 0; j < numVertices; j++)
			{
				if (edges.containsKey(j))
				{
					currLine += " " + Integer.toString(j) 
					+ String.format("({0})", Long.toString(edges.get(j)));
				}
			}
		}
	}
	
	/**
	 * PrintDFSInfo()
	 * 
	 * Prints the information related to the depth-first search.
	 */
	public void PrintDFSInfo()
	{
		String verticesStr = "";
		for (int i = 0; i < dfsVertices.size(); i++)
		{
			verticesStr += " " + Integer.toString(dfsVertices.get(i));
		}
		
		String predecessorsStr = "";
		for (int i = 0; i < predecessors.size(); i++)
		{
			predecessorsStr += " " + Integer.toString(predecessors.get(i));
		}
		
		System.out.println("Depth-First Search:\nVertices:");
		System.out.println(verticesStr);
		System.out.println("Predecessors:");
		System.out.println(predecessorsStr);
	}
	
	/**
	 * CountVertices()
	 * 
	 * Performs a DFS to count the number of vertices in the graph.
	 * 
	 * @return - number of vertices in the graph.
	 */
	private int CountVertices()
	{
		ResetDFSLists();
		
		int count = CountVerticesHelper(gAdjList.get(0), null, 0);
		
		ResetVertices();
		
		return count;
	}
	
	/**
	 * CountVerticesHelper()
	 * 
	 * Recursive function to do a depth-first search to count
	 * the number of vertices.
	 * 
	 * @param count
	 * @return
	 */
	private int CountVerticesHelper(Vertex currVertex, Vertex prev, int count)
	{
		Vertex current = currVertex;
		
		// If we've already visited the vertex, skip it.
		if (!current.IsVisited())
		{
			current.Visit(); // Mark the vertex as visited.
			count++; // Increment our counter
			
			if (prev == null)
				predecessors.add(-1);
			else
				predecessors.add(prev.GetName());
			
			dfsVertices.add(current.GetName());
			
			ArrayList<Vertex> next = GetNextVertices(current);
			
			for (int i = 0; i < next.size(); i++)
			{
				count = CountVerticesHelper(next.get(i), currVertex, count);
			}
		}
		
		return count;
	}
	
	/**
	 * GetNextVertices()
	 * 
	 * Gets an ArrayList of the vertices connected to the one passed in.
	 * 
	 * @param current - the vertex whose connected vertices you want to get.
	 * @return
	 */
	private ArrayList<Vertex> GetNextVertices(Vertex current)
	{
		ArrayList<Vertex> vNext = new ArrayList<Vertex>();
		
		// Map of weights by the connected node name.
		HashMap<Integer, Long> edges = current.GetEdges();
		
		for (int i = 0; i < numVertices; i++)
		{
			if (edges.containsKey(i))
			{
				vNext.add(gAdjList.get(i));
			}
		}
		
//		Set<Integer> connected = edges.keySet();
//		
//		Iterator<Integer> cIter = connected.iterator();
//		while (cIter.hasNext())
//		{
//			int vName = cIter.next();
//			//long weight = edges.get(vNext); // don't care about weight here.
//			Vertex vAdd = gAdjList.get(vName);
//			
//			if (!vNext.contains(vAdd))
//				vNext.add(vAdd);
//		}
//		
//		if (sort)
//			vNext = QuickSort(vNext);
		
		return vNext;
	}
	
	/**
	 * QuickSort()
	 * 
	 * Applies a quick sort by name to the given list of vertices.
	 * 
	 * @param vertices - the vertices to be sorted by name.
	 */
	private ArrayList<Vertex> QuickSort(ArrayList<Vertex> vertices)
	{
		if(vertices.size() == 1)
		{
			return vertices;
		}
		
		int mid = (int) Math.ceil((double)vertices.size() / 2);
		Vertex pivot = vertices.get(mid);
		
		ArrayList<Vertex> smaller = new ArrayList<Vertex>();
		ArrayList<Vertex> greater = new ArrayList<Vertex>();
		
		for (int i = 0; i < vertices.size(); i++)
		{
			if (vertices.get(i).GetName() <= pivot.GetName())
			{
				// Skip the middle vertex that we are sorting around.
				if (i == mid)
				{
					continue;
				}
				smaller.add(vertices.get(i));
			}
			else
			{
				greater.add(vertices.get(i));
			}
		}
		
		ArrayList<Vertex> sorted = new ArrayList<Vertex>();
		
		for (int i = 0; i < smaller.size(); i++)
		{
			sorted.add(smaller.get(i));
		}
		
		sorted.add(pivot);
		
		for (int i = 0; i < greater.size(); i++)
		{
			sorted.add(greater.get(i));
		}
		
		return sorted;
	}
	
	/**
	 * ResetVertices()
	 * 
	 * Sets each of the vertices to unvisited.
	 */
	private void ResetVertices()
	{
		for (int i = 0; i < gAdjList.size(); i++)
		{
			gAdjList.get(i).Reset();
		}
	}
	
	/**
	 * ResetGraphs()
	 * 
	 * Resets both the matrix and the adjacency list.
	 */
	private void ResetGraphs()
	{
		// Reset the adjacency list.
		gAdjList = new ArrayList<Vertex>();
		
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
	 * ResetDFSLists()
	 * 
	 * Resets the lists containing data from the DFS.
	 */
	private void ResetDFSLists()
	{
		dfsVertices = new ArrayList<Integer>();
		predecessors = new ArrayList<Integer>();
	}
	
}
