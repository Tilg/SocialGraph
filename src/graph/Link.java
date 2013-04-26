package graph;

import java.util.ArrayList;

/**
 * An arc is an oriented link between 2 nodes. The orientation is given by the attribute "direction"
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Link{
	
	/**
	 * Label describes the nature of the current link e.g. : "friend","employe_of"
	 */
	protected String label;
	
	/**
	 * Destination of the link.
	 */
	protected Node destination;
	
	/**
	 * List of all properties e.g.: "since=1999", "share=[books,movies,tweets]"
	 */
	protected ArrayList<Property> properties;
	
	/**
	 * Direction of a link if the graph is oriented: NONE If the destination node is the start of the link: IN If the destination node is
	 * the end of the link: OUT
	 */
	protected Direction direction;
	
	/**
	 * Constructor of a none oriented link. Only the Label and the destination are needed.
	 * 
	 * @param label
	 * @param destination
	 */
	public Link(String label,Node destination){
		this.label = label;
		this.destination = destination;
		properties = new ArrayList<Property>();
		direction = Direction.NONE;
	}
	
	/**
	 * Constructor of a an oriented link. The Label,the destination and the direction (enum) are needed.
	 * 
	 * @param label
	 * @param destination
	 */
	public Link(String label,Node destination,Direction direction){
		this.label = label;
		this.destination = destination;
		properties = new ArrayList<Property>();
		this.direction = direction;
	}
	
	/**
	 * Construct a link with all data
	 * 
	 * @param label
	 * @param destination
	 * @param properties
	 * @param direction
	 */
	public Link(String label,Node destination,ArrayList<Property> properties,Direction direction){
		this.label = label;
		this.destination = destination;
		this.properties = properties;
		this.direction = direction;
	}
	
	/**
	 * Return label of a link
	 * 
	 * @return label
	 */
	public String getLabel(){
		return label;
	}
	
	/**
	 * Update label of a link
	 * 
	 * @param new link label
	 */
	public void setLabel(String label){
		this.label = label;
	}
	
	/**
	 * Return the destination node
	 * 
	 * @return destination node
	 */
	public Node getDestination(){
		return destination;
	}
	
	/**
	 * Update the destination node
	 * 
	 * @param link
	 *            destination node
	 */
	public void setDestination(Node destination){
		this.destination = destination;
	}
	
	/**
	 * Return a set of properties
	 * 
	 * @return lists of link properties
	 */
	public ArrayList<Property> getProperties(){
		return properties;
	}
	
	/**
	 * Update link properties
	 * 
	 * @param properties
	 */
	public void setProperties(ArrayList<Property> properties){
		this.properties = properties;
	}
	
	/**
	 * Return the direction of a link (if any)
	 * 
	 * @return link
	 */
	public Direction getDirection(){
		return direction;
	}
	
	/**
	 * Update the direction of a link
	 * 
	 * @param direction
	 */
	public void setDirection(Direction direction){
		this.direction = direction;
	}
	
	/**
	 * This method can be used to compare 2 links.
	 * Returns TRUE is the if the link given in parameter is equal to the instantiated link. 
	 * 
	 * @param link
	 */
	@Override
	public boolean equals(Object link){
		boolean is_equal = true;
		
		is_equal = is_equal && getLabel().equals(((Link)link).getLabel());
		is_equal = is_equal && getDestination().getLabel().equals(((Link)link).getDestination().getLabel());
		is_equal = is_equal && getProperties().equals(((Link)link).getProperties());
		is_equal = is_equal && getDirection() == ((Link)link).getDirection(); 
		
		return is_equal;
	}
	
	/**
	 * used to print a link
	 */
	public String toString(){
		
		return label+" "+direction+" "+properties+" "+destination;
	}

	
}