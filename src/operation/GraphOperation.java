package operation;

import exception.MalFormedRequestException;
import graph.Graph;

/**
 * @author omar
 */
public abstract class GraphOperation{
	protected Graph graph;
	
	protected GraphOperation(Graph graph){
		super();
		this.graph = graph;
	}
	
	/**
	 * this abstract method execute the operation on the graph (in attribut) and return a graph as the result
	 * 
	 * @return Graph
	 * @throws MalFormedRequestException 
	 */
	public abstract Graph execute() throws MalFormedRequestException;
	
	public Graph getGraph(){
		return graph;
	}
	
	public void setGraph(Graph graph){
		this.graph = graph;
	}
}
