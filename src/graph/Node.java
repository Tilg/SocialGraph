package graph;

import java.util.ArrayList;

/**
 * Node Class A node is composed of a label and a list of its neighbor
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Node{
	
	/**
	 * Node identifier e.g.: "Barbara","Carol","BigCo"
	 */
	protected String label;
	
	/**
	 * All links starting from the current nodes Direction, link values and destination node are stored here
	 */
	protected ArrayList<Link> links;
	
	/**
	 * the number of times that you can select the node during the search
	 */
	protected int visited;
	
	/**
	 * Creates a node. The identifier (label) is normalized: no spaces, no upperCase
	 * 
	 * @param label
	 * @param links
	 */
	public Node(String label,ArrayList<Link> links){
		this.label = normalize(label);
		this.links = links;
		visited = 0;
	}
	
	/**
	 * Creates a node with no links The identifier (label) is normalized: no spaces, no upperCase
	 * 
	 * @param label
	 */
	public Node(String label){
		this.label = normalize(label);
		links = new ArrayList<Link>();
		visited = 0;
	}
	
	/**
	 * @return the number of time where the node is selected
	 */
	public int getVisited() {
		return visited;
	}

	/**
	 * @param visited, the number of time that the node is visited
	 */
	public void setVisited(int visited) {
		this.visited = visited;
	}

	/**
	 * Returns label of a node
	 * 
	 * @return label
	 */
	public String getLabel(){
		return label;
	}
	
	/**
	 * Update the node label
	 * 
	 * @param label
	 */
	public void setLabel(String label){
		this.label = normalize(label);
	}
	
	/**
	 * Returns all node links
	 * 
	 * @return links
	 */
	public ArrayList<Link> getLinks(){
		return links;
	}
	
	/**
	 * Update all the node links
	 * 
	 * @param links
	 */
	public void setLinks(ArrayList<Link> links){
		this.links = links;
	}
	
	/**
	 * Prints the node label on the basic output
	 */
	@Override
	public String toString(){
		return "Node [" + label + "]";
	}
	
	/**
	 * This method is used to normalize nodes label. All spaces are deleted. The label is in lower case.
	 * 
	 * @param string
	 *            label
	 * @return normalized string label
	 */
	public String normalize(String s){
		String stmp = s.trim();
		stmp = stmp.toLowerCase();
		
		return s;
	}
	
	/**
	 * This method can be used to compare 2 nodes Returns True is the if the node given in parameter is equal to the instantiated node
	 * 
	 * @param node
	 */
	@Override
	public boolean equals(Object node){
		boolean is_equal = true;
		
		is_equal = is_equal && getLabel().equals(((Node)node).getLabel());
		is_equal = is_equal && getLinks().equals(((Node)node).getLinks());
		
		return is_equal;
	}
	
}
