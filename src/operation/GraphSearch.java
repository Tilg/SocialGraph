package operation;

import java.util.ArrayList;

import parser.RequestParser;

import graph.Graph;
import graph.Link;
import graph.Node;
import graph.Property;
import graph.Request;
import graph.Search;

/**
 * @author omar & florent
 */
public class GraphSearch extends GraphOperation{
	/**
	 * Search Strategy for node search, insert or delete
	 */
	protected Search searchStrategy;
	/**
	 * Max search level for node search, insert or delete
	 */
	protected int searchLevel;
	/**
	 * uniquenessSearch is at true, if we can pass only one time on the same node.
	 */
	protected int uniquenessSearch;
	/**
	 * The request to execute
	 */
	protected Request request;
	
	public GraphSearch(Graph graph,Search searchStrategy,int searchLevel,int uniquenessSearch){
		super(graph);
		this.searchStrategy = searchStrategy;
		this.searchLevel = searchLevel;
		this.uniquenessSearch = uniquenessSearch;
		request = new Request();
	}
	
	/** this method execute a request on the graph who represent the text file in parameter 
	 * @return Graph, a graph who is a part of the main graph who match with the request
	 **/
	@Override
	public ArrayList<Node> execute(String typedRequest){
		
		ArrayList<Node> resultList= new ArrayList<Node>(1);
		
		if (!RequestParser.checkRequest(typedRequest)) {
			System.out.println("malformed request !\nTo have information on the request format use the command 'request help'");
		}
		else{ //if the request is wellformed
			
			RequestParser.ONE_ELEMENT_MATCHER = RequestParser.ONE_ELEMENT_PATTERN.matcher(typedRequest);
			RequestParser.ONE_ELEMENT_MATCHER.find();
			String matchString = RequestParser.ONE_ELEMENT_MATCHER.group();
			
			if (matchString.length() == typedRequest.length()){//if the request is just the name of a node
				resultList.add(getAllFromNodeName(typedRequest));
			}else{ // if the request is complex
				
				//we parse the request into sub request with the '|' character
				ArrayList<String> elementsList = RequestParser.getElementsFromRequest(typedRequest,'|'); //parsing of the request into sub request on '|' char
				
				for (String subRequest : elementsList) // for each sub request
				{
					RequestParser.getFiltersFromRequest(request,subRequest); //we get the filters from the sub request
					
					resultList.addAll(getResultOfRequest()); // we add the result of the execution of the request to the result list 
					
					request = new Request(); // we reset the request filters
				}
			}
		}

		return resultList;
	}
	
	/**
	 * this method find all the result node for the request in parameter
	 * @return the list of the node in the graph who are selected by the request 
	 */
	public ArrayList<Node> getResultOfRequest(){
		ArrayList<Node> listeRes = new ArrayList<Node>(1);
		
		listeRes.addAll(graph.getNodes().values());
		
		for (int i=0; i<request.getLinkLabelList().size();i++){ // each loop represent the execution of one element of the request, ex :"likes paul & employees > techCo", 2 loops, 1 for "likes paul" and 1 for "employees > techCo"
			listeRes = applyFilters(request.getLinkLabelList().get(i),request.getDirectionList().get(i),request.getPropertyList().get(i),request.getTargetNodeLabelList().get(i),listeRes);
		}
		
		return listeRes;
	}
	
	/**
	 * this method return an arraylist of node contains in the list in parameter, who match with the filters in parameters
	 * @param link, the label of the link use as a filter
	 * @param direction, the direction filter
	 * @param propertyList, the arrayList of property filter
	 * @param targetNode, the label of the node target by the element of the request
	 * @param nodeListe, the node who contains all the node already filtered
	 * @return listeRes, the list of the node filtered
	 */
	private ArrayList<Node> applyFilters(String link, String direction,
			ArrayList<Property> propertyList, String targetNode,
			ArrayList<Node> nodeListe) {
		
		ArrayList<Node> listeRes = new ArrayList<Node>(1);
		ArrayList<Node> tmp = new ArrayList<Node>(1); // list used to handle the searchStrategy
		ArrayList<Node> tmp2 = new ArrayList<Node>(1);
		boolean first=true;// a flag used to stop the while when i=0
		String currentNodeLabel="";
		
		for (int i =0; i< searchLevel;i++){ // loop on the search level, ex : friend of the friend of natasha, 1rst loop "friend natasha", 2nd loop "friend *" 
				
			if (i==0){// if we are in the first loop we just execute the request without the searchStrategy
				currentNodeLabel = targetNode; //the current node is the node used in the request
			}else{
				tmp2=new ArrayList<Node>(tmp); //we create a new list
				tmp.clear();//we drop all the element of tmplist to add new element of this loop
			}
			
			while(tmp2.size()>0 || first){
				
				currentNodeLabel = tmp2.get(0).getLabel();//the current node is the first node of the temporaly list
				tmp2.remove(0); //we delete the node from the list
				
				if (direction.equals(">")){ // simple case, just get the node in the table
					
					if (i == searchLevel-1){ //if we are in the last loop
						if (searchStrategy == Search.DEPTH_FIRST){ // if we used the depth_first search
							listeRes.addAll(0,getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));//we add in head
						}else{ // we used the breadth_first
							listeRes.addAll(getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));//we add in queue
						}
						
					}else{//if its not the last loop we don't have the result node but just temp note
						if (searchStrategy == Search.DEPTH_FIRST){
							tmp.addAll(0,getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));
						}else{
							tmp.addAll(getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));
						}
					}
					
					
				}else if (direction.equals("<")){// complex case, get the node in the link of all the node in the table
					
					if (i == searchLevel-1){ 
						if (searchStrategy == Search.DEPTH_FIRST){
							listeRes.addAll(0,getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}else{
							listeRes.addAll(getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}
							
					}else{
						if (searchStrategy == Search.DEPTH_FIRST){
							tmp.addAll(0,getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}else{
							tmp.addAll(getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}
					}
					
					
				}else{ // "-" case
					
					if (i == searchLevel-1){ //if we are in the last loop
						if (searchStrategy == Search.DEPTH_FIRST){
							listeRes.addAll(0,getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));
							listeRes.addAll(0,getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}else{
							listeRes.addAll(getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));
							listeRes.addAll(getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}
						
					}else{
						if (searchStrategy == Search.DEPTH_FIRST){
							tmp.addAll(0,getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));
							tmp.addAll(0,getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}else{
							tmp.addAll(getNodeOutCase(link,propertyList,currentNodeLabel,nodeListe));
							tmp.addAll(getNodeInCase(link,propertyList,currentNodeLabel,nodeListe));
						}
					}	
				}
				
				if (first){ // we put the flag to false a the end of the first loop
					first = false;
				}
			}
			
				
		}
		return listeRes;
	}

	private ArrayList<Node> getNodeOutCase(String link, ArrayList<Property> propertyList, String currentNodeLabel, ArrayList<Node> nodeList) {
		ArrayList<Node> listeRes = new ArrayList<Node>(1);
		ArrayList<Link> linksListTmp = null;
		
		for ( Node curentNode : nodeList){ //for each node in the nodeList in parameter
			
			if (curentNode.getLabel().equals(currentNodeLabel)){ //if the label of the current node match with the node in param
				linksListTmp = curentNode.getLinks(); 
				
				for (Link linkTmp : linksListTmp){ // for each link of this node
					
					if (linkTmp.getLabel().equals(link) && linkTmp.getProperties().equals(propertyList)){	// we check the link label & we check the property list
						
						int numberOfVisit = graph.getNodes().get(linkTmp.getDestination().getLabel()).getVisited();// we check the uniqueSearch Level
						if (numberOfVisit<uniquenessSearch){ // if the node is visited less times that the uniquenessSearchLevel
							
							if (searchStrategy == Search.DEPTH_FIRST){
								listeRes.add(0, linkTmp.getDestination());
							}else{
								listeRes.add(linkTmp.getDestination());
							}
							
							linkTmp.getDestination().setVisited(linkTmp.getDestination().getVisited()+1); // update of the visited flag
						}
					}
				}
			}		
		}
		
		return listeRes;
	}
	
	private ArrayList<Node> getNodeInCase(String link, ArrayList<Property> propertyList, String currentNodeLabel, ArrayList<Node> nodeList) {
		ArrayList<Node> listeRes = new ArrayList<Node>(1);
		ArrayList<Link> linksListTmp = null;
		
		for ( Node curentNode : nodeList){ //for each node in the nodeList in parameter
			
			linksListTmp = curentNode.getLinks(); 
			for (Link linkTmp : linksListTmp){ // for each link of this node
			
				if (linkTmp.getDestination().getLabel().equals(currentNodeLabel)){ //if the label of the destination node in the link match with the node in param
					
					if (linkTmp.getLabel().equals(link) && linkTmp.getProperties().equals(propertyList)){	// we check the link label & we check the property list
						
						int numberOfVisit = graph.getNodes().get(curentNode.getLabel()).getVisited();// we check the uniqueSearch Level
						if (numberOfVisit<uniquenessSearch){ // if the node is visited less times that the uniquenessSearchLevel
							
							if (searchStrategy == Search.DEPTH_FIRST){
								listeRes.add(0, curentNode);
							}else{
								listeRes.add(curentNode);
							}
							
							curentNode.setVisited(linkTmp.getDestination().getVisited()+1); // update of the visited flag
						}
					}
				}
			}
					
		}
		
		
		return listeRes;
	}

	/**
	 * this method take a node label and return all the information about this node stored in the graph
	 * @param nodeLabel, the label of the node research
	 * @return the node in the tabe with the label in parameters
	 */
	private Node getAllFromNodeName(String nodeLabel) {
		Node resNode = new Node("");
		
		if (graph.getNodes().containsKey(nodeLabel)){
			resNode =graph.getNodes().get(nodeLabel); 
		}
		
		return resNode;
	}
	
	public Search getSearchStrategy(){
		return searchStrategy;
	}
	
	public int getSearchLevel(){
		return searchLevel;
	}
	
	public int isUniquenessSearch(){
		return uniquenessSearch;
	}
	
	public Request getRequest(){
		return request;
	}
	
	public void setSearchStrategy(Search searchStrategy){
		this.searchStrategy = searchStrategy;
	}
	
	public void setSearchLevel(int searchLevel){
		this.searchLevel = searchLevel;
	}
	
	public void setUniquenessSearch(int uniquenessSearch){
		this.uniquenessSearch = uniquenessSearch;
	}
	
	public void setRequest(Request request){
		this.request = request;
	}
	
}
