package graph;

import java.util.ArrayList;

/**
 * this class represent a request on the graph by the user
 * @author Florent
 */
public class Request {


	/**
	 * the text of the request typed by the user
	 */
	private String typedRequest;
	
	/**
	 * the list who contains all the label of the link filters used in the query
	 */
	private ArrayList<String> linkLabelList; 
	
	/**
	 * the list who contains all the directions of the link filters used in the query
	 */
	private ArrayList<String> directionList;
	
	/**
	 * the list who contains all the property of the link filters used in the query
	 */
	private ArrayList<Property> propertyList; 
	
	/**
	 * the list who contains all the source node targeted by the query
	 */
	private ArrayList<String> targetNodeLabelList;

	/**
	 * constructor who initialized the list
	 */
	public Request()
	{
		linkLabelList = new ArrayList<String>(1);
		directionList = new ArrayList<String>(1);
		propertyList = new ArrayList<Property>(1);
		targetNodeLabelList =  new ArrayList<String>(1);
	}
	
	/**
	 * constructor who initialized the list and the query typed by the user
	 * @param typedQuery, the query typed by the user
	 */
	public Request(String typedQuery)
	{
		typedRequest = typedQuery;
		linkLabelList = new ArrayList<String>(1);
		directionList = new ArrayList<String>(1);
		propertyList = new ArrayList<Property>(1);
		targetNodeLabelList =  new ArrayList<String>(1);
	}
	
	/**
	 * @return the request type by the user
	 */
	public String getTypedRequest() {
		return typedRequest;
	}

	/**
	 * @param typedRequest, the request type by the user
	 */
	public void setTypedRequest(String typedRequest) {
		this.typedRequest = typedRequest;
	} 
	
	/**
	 * @return ArrayList<String>, the list who contains all the label of the link wrote in the query
	 */
	public ArrayList<String> getLinkLabelList() {
		return linkLabelList;
	}

	/**
	 * @return ArrayList<String> the list who contains all the directions of the link filters used in the query
	 */
	public ArrayList<String> getDirectionList() {
		return directionList;
	}

	/**
	 * @return the list who contains all the property of the link filters used in the query
	 */
	public ArrayList<Property> getPropertyList() {
		return propertyList;
	}

	/**
	 * @return the list who contains all the source node targeted by the query
	 */
	public ArrayList<String> getTargetNodeLabelList() {
		return targetNodeLabelList;
	}

	

}
