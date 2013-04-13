package operation;

import java.util.ArrayList;

import graph.Graph;
import graph.Node;

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

	public abstract ArrayList<Node> execute(String request);
	
	public Graph getGraph(){
		return graph;
	}
	
	public void setGraph(Graph graph){
		this.graph = graph;
	}
}
