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
	private ArrayList<ArrayList<Property>> propertyList; 
	
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
		propertyList = new ArrayList<ArrayList<Property>>(1);
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
		propertyList = new ArrayList<ArrayList<Property>>(1);
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
	public ArrayList<ArrayList<Property>> getPropertyList() {
		return propertyList;
	}

	/**
	 * @return the list who contains all the source node targeted by the query
	 */
	public ArrayList<String> getTargetNodeLabelList() {
		return targetNodeLabelList;
	}

	/**
	 * this method is used to compare 2 requests
	 */
	@Override
	public boolean equals(Object request){
		boolean is_equal = false;
		
		if ( ((this.linkLabelList.equals(((Request) request).getLinkLabelList()))) && //if all the content of the list and the request are equals
			  (this.directionList.equals(((Request) request).getDirectionList())) && 
			  (this.propertyList.equals(((Request) request).getPropertyList())) && 
			  (this.targetNodeLabelList.equals(((Request) request).getTargetNodeLabelList())) && 
			  (this.typedRequest.equals(((Request) request).getTypedRequest()))){
				is_equal = true;	
			}
				
		return is_equal;
	}

	/**
	 * @param linkLabelList, the list of link label put in the request
	 */
	public void setLinkLabelList(ArrayList<String> linkLabelList) {
		this.linkLabelList = linkLabelList;
	}

	/**
	 * @param directionList, the list of directions put in the request
	 */
	public void setDirectionList(ArrayList<String> directionList) {
		this.directionList = directionList;
	}

	/**
	 * @param propertyList, the list of property put in the request
	 */
	public void setPropertyList(ArrayList<ArrayList<Property>> propertyList) {
		this.propertyList = propertyList;
	}

	/**
	 * @param targetNodeLabelList, the list of node label put in the request
	 */
	public void setTargetNodeLabelList(ArrayList<String> targetNodeLabelList) {
		this.targetNodeLabelList = targetNodeLabelList;
	}
}
