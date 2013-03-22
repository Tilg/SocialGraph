package parser;

import graph.Graph;
import graph.Node;
import graph.Search;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is useful for parsing the command line. This is the main class.
 * 
 * @author Omar
 * @version 0.1
 */
public class CommandLineParser{
	private final static Scanner SCANNER = new Scanner(System.in);
	private final static String QUIT_REQUEST = "quit";
	public final static String HELP_REQUEST = "help";
	private Graph graph;
	
	public final static String ARGUMENT_FILE_NAME = "-f";
	public final static String ARGUMENT_SEARCH_STRATEGY = "-s";
	public final static String ARGUMENT_SEARCH_STRATEGY_DEPTH_FIRST = "d";
	public final static String ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST = "b";
	public final static String ARGUMENT_SEARCH_LEVEL = "-l";
	public final static String ARGUMENT_UNIQUENESS = "-u";
	
	
	public CommandLineParser(){
		super();
		displayWelcomeMessage();
	}
	
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
				graph.setSearchStrategy(searchStrategy);
				graph.setSearchLevel(searchLevel);
				graph.setUniquenessSearch(uniqueness);
			}
		}
		
		return parsingArgumentError;
	}
	
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
	
	public void listenRequest(){
		boolean continu = true;
		while (continu){
			System.out.println("\nEnter your request :");
			String request = SCANNER.next();
			if (request.equals(QUIT_REQUEST)){
				continu = false;
			}else if (request.equals(HELP_REQUEST)){
				displayHelpMessage();
			}else{
				ArrayList<Node> results = graph.executeRequest(request);
				System.out.println("Results : ");
				System.out.println(results);
			}
		}
	}
	
	public Graph getGraph(){
		return graph;
	}
	
	public void displayWelcomeMessage(){
		System.out.println("Welcome to the Graph Search monitor.");
		System.out.println("Your Graph Search connection id is " + (int)(Math.random() * 9 + 1));
		System.out.println("Server version: 0.1 Source distribution\n");
		
		System.out.println("Copyright (c) 2013, CEGT and/or its affiliates. All rights reserved\n");
		
		System.out.println("Graph Search is a registered trademark of CEGT Corporation and/or its");
		System.out.println("affiliates. Other names may be trademarks of their respective owners.");
		
		System.out.println("Type '" + HELP_REQUEST + "' for help.");
	}
	
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
	
	public static void main(String[] args){
		CommandLineParser parser = new CommandLineParser();
		if (!parser.parseArguments(args)){
			parser.listenRequest();
		}
	}
}
