package graph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is representing the graph. It is the main class Only nodes are stored because of the search
 * 
 * @author Hadrien
 * @version 0.1
 */
<<<<<<< HEAD
public class Graph {

=======
public class Graph{
	
>>>>>>> origin/develop
	/**
	 * Nodes HashMap Very efficient for node search, insert or delete
	 */
<<<<<<< HEAD
	protected HashMap<String, Node> nodes;


=======
	protected HashMap<String,Node> nodes;
	
	
>>>>>>> origin/develop
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
<<<<<<< HEAD

=======
	
>>>>>>> origin/develop
	public Node getNode(String nodeLabel){
		Node n = nodes.get(nodeLabel);
		if (n == null){
			addString(nodeLabel);
			n = nodes.get(nodeLabel);
		}
		return n;
	}
	
	@Override
	public boolean equals(Object graph){
		return getNodes().equals(((Graph)graph).getNodes());
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
<<<<<<< HEAD

=======
	
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
>>>>>>> origin/develop
}
