package operation;

import java.util.ArrayList;

import parser.RequestParser;

import graph.Graph;
import graph.Node;
import graph.Request;
import graph.Search;

/**
 * @author omar
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
	protected boolean uniquenessSearch;
	/**
	 * The request to execute
	 */
	protected Request request;
	
	public GraphSearch(Graph graph,Search searchStrategy,int searchLevel,boolean uniquenessSearch){
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
			
			//we parse the request into sub request with the '|' character
			ArrayList<String> elementsList = RequestParser.getElementsFromRequest(typedRequest,'|'); //parsing of the request into sub request on '|' char
			
			for (String subRequest : elementsList) // for each sub request
			{
				RequestParser.getFiltersFromRequest(request,subRequest); //we get the filters from the sub request
				
				resultList.addAll(getResultOfRequest()); // we add the result of the execution of the request to the result list 
				
				request = new Request(); // we reset the request filters
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
		
		//TODO coder le parcours de graph 
		
		return listeRes;
	}
	
	public Search getSearchStrategy(){
		return searchStrategy;
	}
	
	public int getSearchLevel(){
		return searchLevel;
	}
	
	public boolean isUniquenessSearch(){
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
	
	public void setUniquenessSearch(boolean uniquenessSearch){
		this.uniquenessSearch = uniquenessSearch;
	}
	
	public void setRequest(Request request){
		this.request = request;
	}
	
}
