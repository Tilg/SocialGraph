package operation;

import graph.Graph;
import graph.Link;
import graph.Node;
import graph.Property;
import graph.Request;
import graph.Search;

import java.util.ArrayList;

import parser.RequestParser;

/**
 * @author omar & florent
 */
public class GraphSearch extends GraphOperation{
	
	/**
	 * this boolean can be set to true if you want to see all the step of the search algorithme
	 */
	protected final static boolean SHOW_ALGORITHME = false;
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
					
					ArrayList<Node> result = getResultOfRequest();
					
					for ( Node node : result){
						if (!contains(resultList, node.getLabel())){
							resultList.add(node);
						}
					}
					
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
	public static ArrayList<Node> keepSameNode(ArrayList<Node> list1, ArrayList<Node> list2) {
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
	public ArrayList<Node> applyFilters(String link, String direction,
			ArrayList<Property> propertyList, String targetNode,
			ArrayList<Node> nodeListe) {
		
		ArrayList<Node> listeRes = new ArrayList<Node>(1);// the list return with all the node mathcing with the request
		ArrayList<ArrayList<Node>> tmp = new ArrayList<ArrayList<Node>>(1); // tmp list used to explore the graph
		for (int i=0; i<searchLevel;i++){
			tmp.add(new ArrayList<Node>(1));
		}
		String currentNodeLabel="";
		int currentLevel = 0; // used to memorise the level where we are during the research
		boolean end = false;
		
		Node currentNode = getNodeFromList(nodeListe,targetNode);

		if (currentNode != null){ // if the node in the request exist in the graph
			
			currentNode.setVisited(currentNode.getVisited()+1);// we update the number of visit of the node
			tmp.get(0).add(currentNode); //the current node is the node used in the request is add to the level 0 of tmp list
			
			while(! end){ // while all the list are not empty
				
				currentNodeLabel = tmp.get(currentLevel).get(0).getLabel();//the current node is the first node of the list of the level where we are	
				tmp.get(currentLevel).remove(0);//we delete the current node from his tmp list
				getNodeFromFilters(link,direction,propertyList,currentNodeLabel,nodeListe,tmp,listeRes,currentLevel);
				
				//we update the current level
				if ( searchStrategy == Search.BREADTH_FIRST){ //largeur
					if (tmp.get(currentLevel).size() == 0){
						currentLevel ++;
					}
				}else{ //profondeur
					if (currentLevel == (searchLevel-1)){ // if we are at the last level of tmp list and that this last list is empty
						if ( tmp.get(currentLevel).size() == 0){
							currentLevel--;
						}
					}
					else{ 
						if (tmp.get(currentLevel+1).size() > 0){ // if the next list is not empty, we go down in the shaft
							currentLevel++;
						}
						else{ // if the next list of tmp is empty
							if (tmp.get(currentLevel).size() == 0){ //if the next list and the current list are empty, we go up in the tree
								currentLevel--;
							}
						}
					}
				}
				
				// we check that all the list are not empty, if they are, we have finish the research
				end = true; 
				for (ArrayList<Node> list : tmp){
					if ( list.size()>0){
						end = false;
						break;
					}
				}
				
				// this case occurs during a depth search when we finish a branch and that we need to up 2 level in a same time
				if (searchStrategy == Search.DEPTH_FIRST && !end){ //if we have another node to examine but the current list is empty 
					while(tmp.get(currentLevel).size() == 0){ // while we are in a empty list of tmp
						currentLevel--;
					}
				}
				
				if (SHOW_ALGORITHME){
					System.out.println();
					System.out.println(tmp);
					System.out.println("***");
					System.out.println(listeRes);
					System.out.println("***");
					System.out.println();
				}
			}
		}
		
		return listeRes;
	}


	/** this method apply the link label filter,the direction filter, property filter and handle the uniqueness param.
	 *  
	 * @param link, the label of the link in the request
	 * @param direction , the direction filter
	 * @param tmp , the list of list of node already find
	 * @param listeRes, the result list  
	 * @param propertyList, the property list specify in the request
	 * @param currentNodeLabel, the label of the node on which we apply filters
	 * @param nodeList, the node list in which we check for nodes who match with the request
	 * @param currentLevel, the level where we are actualy exploring in the graph
	 */
	public void getNodeFromFilters(String link, String direction, ArrayList<Property> propertyList, String currentNodeLabel, ArrayList<Node> nodeList, ArrayList<ArrayList<Node>> tmp, ArrayList<Node> listeRes, int currentLevel) {
		
		ArrayList<Link> linksListTmp = null;
		ArrayList<Node> nodeResultList = new ArrayList<Node>(1);
		
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
							
							if (currentLevel == searchLevel-1){ //if we are in the last level, we add the node in the result list 
								if ( ! contains(listeRes, targetNode.getLabel())){
									listeRes.add(targetNode);
								}
							}else{
								if ( searchStrategy == Search.DEPTH_FIRST){ //profondeur
									nodeResultList.add(targetNode); // we add the node find in the next level of the tmp list at the beginning of the list
								}else{//largeur
									tmp.get(currentLevel+1).add(targetNode); // we add the node find in the next level of the tmp list at the end of the list
								}
							}	
							
							targetNode.setVisited(linkTmp.getDestination().getVisited()+1); // update of the visited flag
						}
					}
				}
			}		
		}
		
		if ( currentLevel < searchLevel-1 && searchStrategy == Search.DEPTH_FIRST){ //profondeur
			tmp.get(currentLevel+1).addAll(0, nodeResultList); // we add the node find in the next level of the tmp list at the beginning of the list
		}
	}

	/**
	 * this method is used to find a node in a node list using the label of the node
	 * @param nodeListe, the list of node
	 * @param targetNode, the label of the searched node
	 * @return the node who have the label in param if he is in the list, null otherwise
	 */
	public Node getNodeFromList(ArrayList<Node> nodeListe, String targetNode) {
		Node res =null;
		for (Node nodeTmp : nodeListe){
			if (nodeTmp.getLabel().equals(targetNode)){
				res = nodeTmp;
			}
		}
		
		return res;
	}


	/**
	 * this method check if a node with the label specify in parameter already exist in the list
	 * @param listeRes, the list of node
	 * @param label, the label of the node that we want to check the existance in the list
	 * @return res, true if a node with the label label already exist in the list, false otherwise
	 */
	public static boolean contains(ArrayList<Node> listeRes, String label) {
		
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
	public static boolean checkProperty(Link linkTmp, ArrayList<Property> propertyList) {
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
	public static boolean checkDirection(Link linkTmp, String direction) {
		
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
	public Node getAllFromNodeName(String nodeLabel) {
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
