package graph;

import java.util.ArrayList;

/**
 * Node Class
 * A node is composed of a label and a list of its neighbor
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Node {

	/**
	 * Node identifier
	 * e.g.: "Barbara","Carol","BigCo"
	 */
	protected String label;
	
	/**
	 * All links starting from the current nodes
	 * Direction, link values and destination node are stored here 
	 */
	protected ArrayList<Link> links;

	/**
	 * Creates a node.
	 * The identifier (label) is normalized: no spaces, no upperCase
	 * 
	 * @param label
	 * @param links
	 */
	public Node(String label, ArrayList<Link> links) {
		this.label = normalize(label);
		this.links = links;
	}
	
	/**
	 * Creates a node with no links
	 * The identifier (label) is normalized: no spaces, no upperCase
	 * 
	 * @param label
	 */
	public Node(String label) {
		this.label = normalize(label);
		this.links = new ArrayList<Link>();
	}

	/**
	 * Returns label of a node
	 * 
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Update the node label
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = normalize(label);
	}

	/**
	 * Returns all node links
	 * 
	 * @return links
	 */
	public ArrayList<Link> getLinks() {
		return this.links;
	}

	/**
	 * Update all the node links
	 * 
	 * @param links
	 */
	public void setLinks(ArrayList<Link> links) {
		this.links = links;
	}
	
	/**
	 * Prints the node label on the basic output 
	 */
	@Override
	public String toString() {
		return "Node [" + label + "]";
	}

	/**
	 * This method is used to normalize nodes label.
	 * All spaces are deleted. The label is in lower case.
	 * 
	 * @param string label
	 * @return normalized string label
	 */
	public String normalize(String s){
		String stmp = s.trim();
		stmp = stmp.toLowerCase();
		
		return s;
	}
	
	public boolean equals(Object node){
		return this.getLabel().equals(((Node) node).getLabel()) && this.getLinks().equals(((Node) node).getLinks());
	}
	
}
