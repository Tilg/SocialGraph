package parser;

import exception.MalFormedRequestException;
import graph.Graph;
import graph.Request;
import graph.Search;

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
	 * This is the string which permit to see the request help message when it is entered (as a request)
	 */
	public final static String REQUEST_HELP_REQUEST = "request help";
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
		displayWelcomeMessage();
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
		-u				=> if this parameter is present, uniqueness = false
		 */
		String fileName = "";
		Search searchStrategy = Search.DEPTH_FIRST;
		int searchLevel = -1;// by default we search in all the graph
		boolean uniqueness = true; // a node cannot be visit more than one time by default
		
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
				uniqueness = false;
			}else{
				// error argument not allowed
				System.out.println("error argument not allowed '" + nextArgument + "'");
				parsingArgumentError = true;
			}
		}
		
		if (i < args.length){
			String lastArgument = args[i];
			if (lastArgument.equals(ARGUMENT_UNIQUENESS)){
				uniqueness = false;
			}else if (lastArgument.equals(ARGUMENT_FILE_NAME) || lastArgument.equals(ARGUMENT_SEARCH_LEVEL)
					|| lastArgument.equals(ARGUMENT_SEARCH_STRATEGY)){
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
			String request = scanner.next();
			if (request.equals(QUIT_REQUEST)){
				continu = false;
			}else if (request.equals(HELP_REQUEST)){
				displayHelpMessage();
			}else if (request.equals(REQUEST_HELP_REQUEST)){
				displayRequestHelpMessage();
			}else{
				((GraphSearch)operation).setRequest(new Request(request));
				Graph results = new Graph();
				try {
					results = ((GraphSearch)operation).execute();
				} catch (MalFormedRequestException e) {
					System.out.println("your request is not well formed, type '" + REQUEST_HELP_REQUEST + "' for help.");
					e.printStackTrace();
				}
				System.out.println("Results : ");
				System.out.println(results);
			}
		}
		scanner.close();
	}
	
	/**
	 * This method display a welcome message
	 * TODO bufferiser la chaine et l'afficher avec un seul println car c'est super long d'écrire a l'écran et le prog perd le process sur une demande d'ecriture ( remarque florent )
	 */
	public void displayWelcomeMessage(){
		System.out.println("Welcome to the Graph Search monitor.");
		System.out.println("Your Graph Search connection id is " + (int)(Math.random() * 9 + 1));
		System.out.println("Server version: 0.1 Source distribution\n");
		
		System.out.println("Copyright (c) 2013, CEGT and/or its affiliates. All rights reserved\n");
		
		System.out.println("Graph Search is a registered trademark of CEGT Corporation and/or its");
		System.out.println("affiliates. Other names may be trademarks of their respective owners.");
		
		System.out.println("Type '" + HELP_REQUEST + "' for help.");
	}
	
	/**
	 * This method display an help message
	 * TODO bufferiser la chaine et l'afficher avec un seul println car c'est super long d'écrire a l'écran et le prog perd le process sur une demande d'ecriture ( remarque florent )
	 */
	public void displayHelpMessage(){
		System.out.println("\nCommand line arguments : ");
		System.out.println(ARGUMENT_FILE_NAME + "\t<filename>\tFile path to the graph");
		System.out.println(ARGUMENT_SEARCH_STRATEGY + "\t<" + ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST + "> or <"
				+ ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST + ">\tSearch Strategy ('" + ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST
				+ "' for breadth first search, or '" + ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST + "' for depth first search)");
		System.out.println(ARGUMENT_SEARCH_LEVEL + "\t<level>\t\tMax search level (positive integer)");
		System.out.println(ARGUMENT_UNIQUENESS
				+ "\t\t\t\tUniqueness (if this parameter is present, a node can be passed several time during a search.)");
		System.out.println(QUIT_REQUEST + " \t\t\t\tQuit Graph Search monitor");
	}
	
	/**
	 * This method display a request help message
	 */
	public void displayRequestHelpMessage(){
		System.out.println("\nTo make a well formed request, you need to have : \n\t- nameOfTheLink\n\t- linkOrientation (optional) [<|>|-]\n\t- [parameterName = value[,parameterName2 = value2]*]* (optional)\n\t- nodeLabel\n\nYou can combine request with '&'\n\tExemple : 'friend > paul & employee - (since = 1989) techCo'\n\nYou can also see the entire graph with the request '*'");
	}
	
	/**
	 * This is the main method of this application
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		CommandLineParser parser = new CommandLineParser();
		if (!parser.parseArguments(args)){
			parser.listenRequest();
		}
	}
}
