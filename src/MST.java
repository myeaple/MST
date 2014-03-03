/**
 * MST.java
 * 
 * The purpose of this application is to explore the different complexities
 * of various implementations of the Minimum Spanning Tree, as well as to
 * help gain understanding of the difference between theoretical estimates
 * of complexity and real execution times.
 * 
 * @author Michael Yeaple
 */

import java.io.*;

public class MST {
	
	/**
	 * @param args - only arg should be arg[0], the input file name
	 */
	public static void main(String[] args) {
		int numVertices = 0;
		long seed = 0;
		double p = 0.0;
		
		if (args.length != 1)
		{
			System.err.println("Error: Invalid number of parameters provided.");
			System.exit(1);
		}
		
		String inFileName = args[0];
		
		// Read the input file to get the parameters
		int lineNum = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(inFileName));
		
			String line = br.readLine();
			
			
			while (line != null) 
			{
				
				switch(lineNum)
				{
					// Number of vertices in graph
					case(0): 
						numVertices = Integer.parseInt(line);
						break;
						
					// "Random" seed number
					case(1): 
						seed = Long.parseLong(line);
						break;
						
					// Probability any two vertices are connected by an edge
					case(2): 
						p = Double.parseDouble(line);
						break;
				}
				
				// Read next line
				line = br.readLine();
				lineNum++;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			exitWithMessage("Input file not found");
		} catch (NumberFormatException e) {
			// Line number will tell us which message to show.
			switch (lineNum)
			{
				case(0): // Number of vertices
				case(1): // Seed number
					exitWithMessage("n and seed must be integers");
					break;
				
				// Probability
				case(2):
					exitWithMessage("p must be a real number");
					break;
			}
		} catch (IOException e) {
			exitWithError(e);
		}
		
		// Check for other input error conditions
		if (numVertices < 2)
			exitWithMessage("n must be greater than 1");
		
		if (p < 0 || p > 1)
			exitWithMessage("p must be between 0 and 1");
		
		// Build the graph
		Graph g = new Graph(numVertices, seed, p);
		
		System.out.println(String.format("\nTEST: n=%s, seed=%s, p=%s",
					Integer.toString(numVertices),
					Long.toString(seed),
					Double.toString(p))
				);
		
		System.out.println(String.format(
				"Time to generate the graph: %s milliseconds",
				Long.toString(g.getGenerationTime())
				));
		
		g.printAdjacencyMatrix();
		g.printAdjacencyList();
		g.printDFSInfo();
	}
	
	/**
	 * printDivider()
	 * 
	 * Prints a divider for breaking up sections of the program output.
	 */
	private static void printDivider()
	{
		System.out.println("===================================");
	}
	
	/**
	 * exitWithMessage()
	 * 
	 * Exits and prints the message passed in when called.
	 * 
	 * @param message - the message to print upon exiting.
	 */
	private static void exitWithMessage(String message)
	{
		System.out.println(message);
		System.exit(1);
	}
	
	/**
	 * exitWithError()
	 * 
	 * Exits and prints an exception's error message to err when called.
	 * 
	 * @param e - the caught exception.
	 */
	public static void exitWithError(Exception e)
	{
		System.err.println(String.format("Error: {0}", e.getMessage()));
		System.exit(1);
	}

}
