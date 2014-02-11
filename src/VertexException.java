/**
 * VertexException.java
 * 
 * The purpose of this class is to represent an error when working
 * with vertices in the graph.
 * 
 * @author Michael Yeaple
 *
 */

public class VertexException extends Exception {

	public VertexException(){ }
	
	public VertexException(String message)
	{
		super(message);
	}
	
}
