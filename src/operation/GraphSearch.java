package operation;

import java.util.HashMap;

import parser.RequestParser;

import exception.MalFormedRequestException;

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
	 * @throws MalFormedRequestException 
	 **/
	@Override
	public Graph execute() throws MalFormedRequestException{
		Graph resultGraph = null;
		HashMap<String,Node> resultTable = new HashMap<String,Node>();
		
		if (request.getTypedRequest().equals("*")){//if the request is *, the result graph is the entire input graph
			resultGraph = graph;
		}
		else{
			if (!RequestParser.checkRequest(request)) {
				throw new MalFormedRequestException();
			}
			else{ //if the request is wellformed
				
				//TODO ( Ajouter des test pour tester le parseur de requete )
				
				//codage de la recuperation des données dans la requete
				RequestParser.getFiltersFromRequest(request);
				
				//codage de l'exploration du graphe avec les filtres stockés dans la requete
			}
		}

		return resultGraph;
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
