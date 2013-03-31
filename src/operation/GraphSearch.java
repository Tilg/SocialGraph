package operation;

import graph.Graph;
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
	protected String request;
	
	public GraphSearch(Graph graph,Search searchStrategy,int searchLevel,boolean uniquenessSearch){
		super(graph);
		this.searchStrategy = searchStrategy;
		this.searchLevel = searchLevel;
		this.uniquenessSearch = uniquenessSearch;
		request = "";
	}
	
	@Override
	public Graph execute(){
		return null;
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
	
	public String getRequest(){
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
	
	public void setRequest(String request){
		this.request = request;
	}
	
}
