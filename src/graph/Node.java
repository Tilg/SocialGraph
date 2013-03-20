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

	protected String label;
	protected ArrayList<Link> links;

	public Node(String label, ArrayList<Link> links) {
		this.label = normalize(label);
		this.links = links;
	}
	
	public Node(String label) {
		this.label = normalize(label);
		this.links = new ArrayList<Link>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = normalize(label);
	}

	public ArrayList<Link> getLinks() {
		return this.links;
	}

	public void setLinks(ArrayList<Link> links) {
		this.links = links;
	}
	
	public String normalize(String s){
		String stmp = s.trim();
		stmp = stmp.toLowerCase();
		
		return s;
	}
}
