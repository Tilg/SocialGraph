package graph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is representing the graph. It is the main class Only nodes are stored because of the search
 * 
 * @author Hadrien
 * @version 0.1
 */

public class Graph{
	
	/**
	 * Nodes HashMap Very efficient for node search, insert or delete
	 */
	protected HashMap<String,Node> nodes;
	
	/**
	 * Default constructor By default an empty HashMap is created
	 */
	public Graph(){
		nodes = new HashMap<String,Node>();
	}
	
	/**
	 * Constructor using one set of nodes
	 * 
	 * @param nodes
	 */
	public Graph(HashMap<String,Node> nodes){
		this.nodes = nodes;
	}
	
	/**
	 * Return the complete graph This graph is a HashMap of Nodes
	 * 
	 * @return Nodes HashMap
	 */
	public HashMap<String,Node> getNodes(){
		return nodes;
	}
	
	/**
	 * Update the complete graph This graph is a HashMap of nodes
	 * 
	 * @param nodes
	 */
	public void setNodes(HashMap<String,Node> nodes){
		this.nodes = nodes;
	}
	
	/**
	 * Adds a node on the graph
	 * 
	 * @param node
	 *            n
	 */
	public void addNode(Node n){
		String label = n.getLabel();
		nodes.put(label,n);
	}
	
	/**
	 * This method creates a node with a given label This node is added to the graph
	 * 
	 * @param nodeLabel
	 */
	public void addString(String nodeLabel){
		Node n = new Node(nodeLabel);
		addNode(n);
	}

	/**
	 * This method finds and returns a graph node
	 * The node label is given in String format
	 * 
	 * @param nodeLabel
	 */
	public Node getNode(String nodeLabel){
		Node n = nodes.get(nodeLabel);
		if (n == null){
			addString(nodeLabel);
			n = nodes.get(nodeLabel);
		}
		return n;
	}
	
	/**
	 * This method can be used to compare 2 graphs.
	 * Returns TRUE is the if the graph given in parameter is equal to the instantiated graph. 
	 * 
	 * @param link
	 */
	@Override
	public boolean equals(Object graph){
		for(String key : this.getNodes().keySet()) {
	        if(((Graph) graph).getNodes().containsKey(key)) {
	            if(!this.getNodes().get(key).equals(((Graph) graph).getNodes().get(key))) {
	                return false;
	            }
	        } else {
	            return false;
	        }
	    }
	    return this.getNodes().size() == ((Graph) graph).getNodes().size();
	}
	
	/**
	 * This method execute the given request and return a node list
	 * 
	 * @param request
	 * @return TODO : Implement the executeRequest() method
	 */
	public ArrayList<Node> executeRequest(String request){
		return null;
	}
	
	/**
	 * TODO : Implement the method setSearchStrategy()
	 * 
	 * @param search
	 */
	public void setSearchStrategy(Search search){
		
	}
	
	/**
	 * TODO : Implement the method setSearchLevel()
	 * 
	 * @param level
	 */
	public void setSearchLevel(int level){
		
	}
	
	/**
	 * TODO : Implement the method setUniquenessSearch()
	 * 
	 * @param uniqueness
	 */
	public void setUniquenessSearch(boolean uniqueness){
		
	}

}