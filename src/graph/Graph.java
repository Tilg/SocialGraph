package graph;

import java.util.HashMap;

/**
 * This class is representing the graph.
 * It is the main class
 * Only nodes are stored beacause of the search
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Graph {
	
	HashMap<String, Node> nodes;

	public Graph(HashMap<String, Node> nodes) {
		this.nodes = nodes;
	}

	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	public void setNodes(HashMap<String, Node> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(Node n){
		String label = n.getLabel();
		this.nodes.put(label,n);
	}
	
	public void addString(String nodeLabel){
		Node n = new Node(nodeLabel);
		this.addNode(n);
	}
	
}
