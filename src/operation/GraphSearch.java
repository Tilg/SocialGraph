package operation;

import graph.Graph;
import graph.Link;
import graph.Node;
import graph.Property;
import graph.Request;
import graph.Search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import parser.RequestParser;

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
	
	/**
	 * @param graph, the principal structure
	 * @param searchStrategy, the search strategy in the graph
	 * @param searchLevel, level of inception (ex: level 2 with "friend annie", return the friends of the friend of annie) 
	 * @param uniquenessSearch, the number of time that we can select a node
	 */
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
		Set<Node> set; // we used a SET to remove the object in double in the list
		
		if (!RequestParser.checkRequest(typedRequest)) {
			System.out.println("malformed request !\nTo have information on the request format use the command 'request help'");
		}
		else{ //if the request is wellformed
			
			//we reset the number of visit of each node of the graph to have a clean graph for each request typed by the user
			for (Node nodetmp : graph.getNodes().values()){
				nodetmp.setVisited(0);
			}
			
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
					
					set = new HashSet<Node>() ; 
					set.addAll(resultList) ;
					resultList = new ArrayList<Node>(set) ;
					
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
		ArrayList<ArrayList<Node>> resultList =  new ArrayList<ArrayList<Node>>(1);
		ArrayList<Node> res = new ArrayList<Node>(1);
		
		ArrayList<Node> graphNodeList = new ArrayList<Node>(1);
		graphNodeList.addAll(graph.getNodes().values());
		
		for (int i=0; i<request.getLinkLabelList().size();i++){ // each loop represent the execution of one element of the request, ex :"likes paul & employees > techCo", 2 loops, 1 for "likes paul" and 1 for "employees > techCo"
			
			for (Node nodetmp : graph.getNodes().values()){
				nodetmp.setVisited(0);
			}
			
			resultList.add(applyFilters(request.getLinkLabelList().get(i),request.getDirectionList().get(i),request.getPropertyList().get(i),request.getTargetNodeLabelList().get(i),graphNodeList));
		}
		
		// we get the common elements of all the list
		res = resultList.get(0);
		if (resultList.size()>1){
			for (int j=1;j<resultList.size();j++){ // for each list of result
				res = keepSameNode(res,resultList.get(j));
			}
		}

		return res;
	}
	
	/**
	 * This method return the common node of the two list in parameter
	 * @param list1, the first list of node
	 * @param list2, the second list of node
	 * @return res, the list of common nodes
	 */
	private ArrayList<Node> keepSameNode(ArrayList<Node> list1, ArrayList<Node> list2) {
		ArrayList<Node> res = new ArrayList<Node>(1);
		
		for (Node tmpNode1 : list1){ // for each node of the first list
			
			for (Node tmpNode2 : list2){ // for each node of the second list
				if (tmpNode1.equals(tmpNode2)){
					res.add(tmpNode1);
				}
			}
		}
		
		return res; 
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
		String currentNodeLabel="";
		
		for (int i =0; i< searchLevel;i++){ // loop on the search level, ex : friend of the friend of natasha, 1rst loop "friend natasha", 2nd loop "friend *" 
				
			System.out.println(tmp);//TODO
			System.out.println("***");
			for ( Node a : tmp){
				System.out.println(a.getVisited());
			}
			System.out.println("***");
			
			if (i==0){// if we are in the first loop we just execute the request without the searchStrategy
				
				Node currentNode = getNodeFromList(nodeListe,targetNode);
				
				if (currentNode != null){
					currentNode.setVisited(currentNode.getVisited()+1);// we update the number of visit of the node
					tmp2.add(currentNode); //the current node is the node used in the request
				}
			}else{
				tmp2=new ArrayList<Node>(tmp); //we create a new list
				tmp.clear();//we drop all the element of tmplist to add new element of this loop
			}
			
			while(tmp2.size()>0){
				
				currentNodeLabel = tmp2.get(0).getLabel();//the current node is the first node of the temporaly list
				tmp2.remove(0); //we delete the node from the list
					
				if (i == searchLevel-1){ //if the search level is >1, we make a recurtial filtering. if i ==searchLevel-1, it's the last time that we make the recursivity
					
					ArrayList<Node> filtredList = getNodeFromFilters(link,direction,propertyList,currentNodeLabel,nodeListe,tmp);
					
					if (searchStrategy == Search.DEPTH_FIRST){
						listeRes.addAll(0,filtredList);
					}else{
						listeRes.addAll(filtredList);
					}
						
				}else{
					if (searchStrategy == Search.DEPTH_FIRST){
						tmp.addAll(0,getNodeFromFilters(link,direction,propertyList,currentNodeLabel,nodeListe,tmp));
					}else{
						tmp.addAll(getNodeFromFilters(link,direction,propertyList,currentNodeLabel,nodeListe,tmp));
					}
				}
			}
			
			Set<Node> set = new HashSet<Node>() ; 
			set.addAll(tmp) ;
			tmp = new ArrayList<Node>(set) ;
			
		}
		return listeRes;
	}

	/**
	 * this method is used to find a node in a node list using the label of the node
	 * @param nodeListe, the list of node
	 * @param targetNode, the label of the searched node
	 * @return the node who have the label in param if he is in the list, null otherwise
	 */
	private Node getNodeFromList(ArrayList<Node> nodeListe, String targetNode) {
		Node res =null;
		for (Node nodeTmp : nodeListe){
			if (nodeTmp.getLabel().equals(targetNode)){
				res = nodeTmp;
			}
		}
		
		return res;
	}

	/** this method apply the link label filter,the direction filter, property filter and handle the uniqueness param
	 *  
	 * @param link, the label of the link in the request
	 * @param direction , the direction filter
	 * @param loopNodeList , the list of node already find during the loop
	 * @param propertyList, the property list specify in the request
	 * @param currentNodeLabel, the label of the node on which we apply filters
	 * @param nodeList, the node list in which we check for nodes who match with the request
	 * @return
	 */
	private ArrayList<Node> getNodeFromFilters(String link, String direction, ArrayList<Property> propertyList, String currentNodeLabel, ArrayList<Node> nodeList, ArrayList<Node> loopNodeList) {
		ArrayList<Node> listeRes = new ArrayList<Node>(1);
		ArrayList<Link> linksListTmp = null;
		
		for ( Node curentNode : nodeList){ //for each node in the nodeList in parameter
			
			if (curentNode.getLabel().equals(currentNodeLabel)){ //if the label of the current node match with the node in param
				linksListTmp = curentNode.getLinks(); 
				
				for (Link linkTmp : linksListTmp){ // for each link of this node
					
					boolean linkTest = linkTmp.getLabel().equals(link);
					boolean propertyTest = checkProperty(linkTmp,propertyList);
					boolean directionTest = checkDirection(linkTmp,direction);
					if (linkTest && propertyTest && directionTest){	// we check the link label, the direction & we check the property list
						
						int numberOfVisit = graph.getNodes().get(linkTmp.getDestination().getLabel()).getVisited();// we check the uniqueSearch Level
						if (numberOfVisit<uniquenessSearch){ // if the node is visited less times that the uniquenessSearchLevel
							
							Node targetNode = graph.getNode(linkTmp.getDestination().getLabel());
							
							if (!contains(loopNodeList,targetNode.getLabel())){ // if the node is not in the listRes
							
								if (searchStrategy == Search.DEPTH_FIRST){
									listeRes.add(0,targetNode );
								}else{
									listeRes.add(targetNode);
								}
								
								targetNode.setVisited(linkTmp.getDestination().getVisited()+1); // update of the visited flag
							}
						}
					}
				}
			}		
		}
		
		return listeRes;
	}


	/**
	 * this method check if a node with the label specify in parameter already exist in the list
	 * @param listeRes, the list of node
	 * @param label, the label of the node that we want to check the existance in the list
	 * @return res, true if a node with the label label already exist in the list, false otherwise
	 */
	private boolean contains(ArrayList<Node> listeRes, String label) {
		
		boolean res = false;
		
		for ( Node node : listeRes){
			if ( label.equals(node.getLabel())){
				res = true;
				break;
			}
		}
		
		return res;
	}

	/**
	 * this method check if the propertyList in param match with the propertyList of the link in param
	 * @param linkTmp, the link to examine
	 * @param propertyList, the propertyList that we wan
	 * @return boolean, true if the direction match with the direction of the link, else otherwise
	 */
	private boolean checkProperty(Link linkTmp, ArrayList<Property> propertyList) {
		boolean res = false;
		if (propertyList.size()==0){ // if we don't have a property filter in the request, the list of property in the link don't matter
			res = true;
		}else{
			if (linkTmp.getProperties().equals(propertyList)){ // if the 2 list are equals
				res = true;
			}
		}

		return res;
	}

	/**
	 * this method check if the direction in param match with the direction of the link in param
	 * @param linkTmp, the link to examine
	 * @param direction, the direction that we wan
	 * @return boolean, true if the direction match with the direction of the link, else otherwise
	 */
	private boolean checkDirection(Link linkTmp, String direction) {
		
		boolean res = false;
		String linkDirection = linkTmp.getDirection().toString();
		
		if (linkDirection.equals("IN") && direction.equals("<")){
			res = true;
		}else if(linkDirection.equals("OUT") && direction.equals(">")){
			res = true;
		}else if(direction.equals("-")){
			res = true;
		}
		
		return res;
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
