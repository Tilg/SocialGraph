package parser;

import graph.Graph;
import graph.Node;
import graph.Request;
import graph.Search;

import java.util.ArrayList;
import java.util.Scanner;

import operation.GraphOperation;
import operation.GraphSearch;

/**
 * This class is useful for parsing the command line. This is the main class.
 * 
 * @author Omar
 * @version 0.1
 */
public class CommandLineParser{
	/*
	 * Here is a list of differents arguments which can be entered by the user in the command line
	 */
	/**
	 * The argument "-f" permit to specify a filename
	 */
	public final static String ARGUMENT_FILE_NAME = "-f";
	/**
	 * The argument "-s" permit to specify a the search strategy : it can be "d" for a depth first search or "b" for a breadth first search
	 */
	public final static String ARGUMENT_SEARCH_STRATEGY = "-s";
	public final static String ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST = "d";
	public final static String ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST = "b";
	/**
	 * The argument "-l" permit to specify a search level
	 */
	public final static String ARGUMENT_SEARCH_LEVEL = "-l";
	/**
	 * The argument "-u" is to specify if the search isn't uniqueness
	 */
	public final static String ARGUMENT_UNIQUENESS = "-u";
	/**
	 * This is the string which permit to stop the application when it is entered (as a request)
	 */
	private final static String QUIT_REQUEST = "quit";
	/**
	 * This is the string which permit to see the help message when it is entered (as a request)
	 */
	public final static String HELP_REQUEST = "help";
	/**
	 * Graph on which operations will be executed.
	 */
	private Graph graph;
	/**
	 * Graph operation that will be execute
	 */
	private GraphOperation operation;
	/**
	 * Scanner used to read the request - singleton
	 */
	private Scanner scanner;
	
	public CommandLineParser(){
		super();
	}
	
	/**
	 * This method analyze the arguments in parameter, and builds a graph (from theses parameters)
	 * 
	 * @param args
	 * @return
	 */
	public boolean parseArguments(String[] args){
		boolean parsingArgumentError = false;
		/*
		 Arguments
		 -f [.*]		=> filename
		-s [d | b]	=> search strategy : DEPTH_FIRST (profondeur) or BREADTH_FIRST (largeur)
		-l [0-9]* 	=> max level of search
		-u	[0-9]*	=> uniqueness : number times that a node can be visited
		 */
		String fileName = "";
		Search searchStrategy = Search.DEPTH_FIRST;
		int searchLevel = -1;// by default we search in all the graph
		int uniqueness = 1; // a node cannot be visit more than one time by default
		
		if (args.length == 0){
			System.out.println("There is no argument");
			parsingArgumentError = true;
		}
		
		int i = 0;
		for (i = 0 ; i < (args.length - 1) && !parsingArgumentError ; i++){
			String argument = args[i];
			String nextArgument = args[i + 1];
			
			if (argument.equals(ARGUMENT_FILE_NAME)){
				fileName = nextArgument;
				i++;
			}else if (argument.equals(ARGUMENT_SEARCH_STRATEGY)){
				if (nextArgument.equals(ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST)){
					searchStrategy = Search.BREADTH_FIRST;
				}else if (nextArgument.equals(ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST)){
					searchStrategy = Search.DEPTH_FIRST;
				}else{
					// error argument not allowed
					System.out.println("error argument not allowed '" + nextArgument + "' for '" + argument + "'");
					parsingArgumentError = true;
				}
				i++;
			}else if (argument.equals(ARGUMENT_SEARCH_LEVEL)){
				if (isPositiveInteger(nextArgument)){
					searchLevel = Integer.parseInt(nextArgument);
					i++;
				}else{
					// error : argument value (for search level) not a positive integer
					System.out.println("error argument not allowed '" + nextArgument + "' for '" + argument + "'");
					parsingArgumentError = true;
				}
			}else if (argument.equals(ARGUMENT_UNIQUENESS)){
				if (isPositiveInteger(nextArgument)){
					uniqueness = Integer.parseInt(nextArgument);
					i++;
				}else{
					// error : argument value (for uniqueness) not a positive integer
					System.out.println("error argument not allowed '" + nextArgument + "' for '" + argument + "'");
					parsingArgumentError = true;
				}
			}else{
				// error argument not allowed
				System.out.println("error argument not allowed '" + argument + "'");
				parsingArgumentError = true;
			}
		}
		
		if (i < args.length && !parsingArgumentError){
			String lastArgument = args[i];
			if (lastArgument.equals(ARGUMENT_UNIQUENESS) || lastArgument.equals(ARGUMENT_FILE_NAME)
					|| lastArgument.equals(ARGUMENT_SEARCH_LEVEL) || lastArgument.equals(ARGUMENT_SEARCH_STRATEGY)){
				System.out.println("Missing argument value for '" + lastArgument + "'");
				parsingArgumentError = true;
			}else{
				// error argument not allowed
				System.out.println("error argument not allowed '" + lastArgument + "'");
				parsingArgumentError = true;
			}
		}
		
		if (!parsingArgumentError){
			FileParser parser = new FileParser();
			graph = parser.parseFile(fileName);
			
			if (graph != null){
				operation = new GraphSearch(graph,searchStrategy,searchLevel,uniqueness);
			}
		}
		
		return parsingArgumentError;
	}
	
	/**
	 * This method test the string in parameter and return true if this string is a positive integer, otherwise return false
	 * 
	 * @param str
	 *            the string to test
	 * @return boolean
	 */
	public boolean isPositiveInteger(String str){
		boolean isPositiveInteger = false;
		try{
			int number = Integer.parseInt(str);
			if (number > 0){
				isPositiveInteger = true;
			}
		}catch (Exception e){
		}
		return isPositiveInteger;
	}
	
	/**
	 * This method is waiting a request from the user, execute it and display the result until the user enters "quit"
	 */
	public void listenRequest(){
		if (scanner == null){
			scanner = new Scanner(System.in);
		}
		boolean continu = true;
		while (continu){
			System.out.println("\nEnter your request :");
			String request = scanner.nextLine();
			if (request.equals(QUIT_REQUEST)){
				continu = false;
			}else if (request.equals(HELP_REQUEST)){
				displayRequestHelpMessage();
			}else{
				
				((GraphSearch)operation).setRequest(new Request());
				ArrayList<Node> results = new ArrayList<Node>(1);
				
				results = ((GraphSearch)operation).execute(request);
				
				System.out.println("Results : ");
				System.out.println(results);
			}
		}
		scanner.close();
	}
	
	/**
	 * This method display a welcome message
	 */
	public void displayWelcomeMessage(){
		System.out.println("Welcome to the Graph Search monitor.\n" + "Your Graph Search connection id is " + (int)(Math.random() * 9 + 1)
				+ "\n" + "Server version: 0.1 Source distribution\n" + "Copyright (c) 2013, CEGT and/or its affiliates. All rights reserved\n"
				+ "Graph Search is a registered trademark of CEGT Corporation and/or its"
				+ "affiliates. Other names may be trademarks of their respective owners." + "Type '" + HELP_REQUEST + "' for help.");
	}
	
	/**
	 * This method display an help message
	 */
	public void displayHelpMessage(){
		System.out.println("\nCommand line arguments : \n" + ARGUMENT_FILE_NAME + "\t<filename>\tFile path to the graph\n"
				+ ARGUMENT_SEARCH_STRATEGY + "\t<" + ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST + "> or <" + ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST
				+ ">\tSearch Strategy ('" + ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST + "' for breadth first search, or '"
				+ ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST + "' for depth first search)\n" + ARGUMENT_SEARCH_LEVEL
				+ "\t<level>\t\tMax search level (positive integer)\n" + ARGUMENT_UNIQUENESS
				+ "\t<level>\t\tUniqueness (positive integer), number times that a node can be visited\n");
	}
	
	/**
	 * This method display the request help message o
	 */
	public void displayRequestHelpMessage(){
		System.out.println("\nTo make a well formed request, you need to have : \n"
				+ "\t- nameOfTheLink\n\t- linkOrientation (optional) [<|>|-]\n"
				+ "\t- [parameterName = value[,parameterName2 = value2]*]* (optional)\n" + "\t- nodeLabel\n\n"
				+ "You can combine request with '&' or '|'\n" + "\tExemple : 'friend > paul & employee - (since = 1989) techCo'\n\n"
				+ "'*' can replace any link tag or node name\n"
				+ "You can also see the entire graph with the request '*'\n\nSpecial requests :\n\t" + HELP_REQUEST + " \t\t\t\tShow help\n\t"
				+ QUIT_REQUEST + " \t\t\t\tQuit Graph Search monitor");
	}
	
	/**
	 * This is the main method of this application
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		CommandLineParser parser = new CommandLineParser();
		if (!parser.parseArguments(args)){
			parser.displayWelcomeMessage();
			parser.listenRequest();
		}else{
			parser.displayHelpMessage();
		}
	}
	
	/**
	 * @return operation, the GraphOperation of this CommandeLineParser
	 */
	public GraphOperation getOperation() {
		return operation;
	}

	/**
	 * @return graph, the Graph with all the node and link
	 */
	public Graph getGraph() {
		return graph;
	}
	
}
