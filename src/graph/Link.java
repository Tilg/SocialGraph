package graph;

import java.util.ArrayList;

/**
 * An arc is an oriented link between 2 nodes.
 * The orientation is given by the attribute "direction"
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Link {

	protected String label;
	protected Node destination;
	protected ArrayList<Property> properties; 
	protected Direction direction;
	
	public Link(String label, Node destination, ArrayList<Property> properties,
			Direction direction) {
		this.label = label;
		this.destination = destination;
		this.properties = properties;
		this.direction = direction;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	} 
	
	public boolean equals(Object link){
		return this.getLabel().equals(((Link) link).getLabel()) //&& this.getDestination() == ((Link) link).getDestination()
				&& this.getProperties().equals(((Link) link).getProperties()) && this.getDirection() == ((Link) link).getDirection();
	}
}
