package graph;

import java.util.HashMap;

/**
 * This class is representing the graph.
 * It is the main class
 * Only nodes are stored because of the search
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Graph {
<<<<<<< HEAD

	/**
	 * Nodes HashMap
	 * Very efficient for node search, insert or delete
	 */
	HashMap<String, Node> nodes;
=======
	
	protected HashMap<String, Node> nodes;
>>>>>>> origin/fileparser

	/**
	 * Default constructor
	 * By default an empty HashMap is created
	 */
	public Graph() {
		this.nodes = new HashMap<String, Node>();
	}

	/**
	 * Constructor using one set of nodes
	 * 
	 * @param nodes
	 */
	public Graph(HashMap<String, Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Return the complete graph 
	 * This graph is a HashMap of Nodes
	 * 
	 * @return Nodes HashMap
	 */
	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	/**
	 * Update the complete graph
	 * This graph is a HashMap of nodes
	 * 
	 * @param nodes
	 */
	public void setNodes(HashMap<String, Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Adds a node on the graph
	 * 
	 * @param node n
	 */
	public void addNode(Node n){
		String label = n.getLabel();
		this.nodes.put(label,n);
	}

	/**
	 * This method creates a node with a given label
	 * This node is added to the graph
	 * 
	 * @param nodeLabel
	 */
	public void addString(String nodeLabel){
		Node n = new Node(nodeLabel);
		this.addNode(n);
	}
<<<<<<< HEAD

=======
	
	public Node getNode(String nodeLabel){
		Node n = nodes.get(nodeLabel);
		if(n == null){
			addString(nodeLabel);
			n = nodes.get(nodeLabel);
		}
		return n;
	}
	
	public boolean equals(Object graph){
		return this.getNodes().equals(((Graph) graph).getNodes());
	}
	
>>>>>>> origin/fileparser
}
