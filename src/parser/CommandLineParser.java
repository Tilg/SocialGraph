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
	private final static String REQUEST_QUIT = "quit";
	private Graph graph;
	
	public CommandLineParser(){
		super();
	}
	
	public boolean parseArguments(String[] args){
		boolean parsingArgumentError = false;
		/*
		 Arguments
		 -f [.*]		=> filename
		-p [l | p]	=> search strategy : DEPTH_FIRST (profondeur) or BREADTH_FIRST (largeur)
		-n [0-9]* 	=> max level of search
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
			
			if (argument.equals("-f")){
				fileName = nextArgument;
				i++;
			}else if (argument.equals("-p")){
				if (nextArgument.equals("l")){
					searchStrategy = Search.BREADTH_FIRST;
				}else if (nextArgument.equals("p")){
					searchStrategy = Search.DEPTH_FIRST;
				}else{
					// error argument not allowed
					System.out.println("error argument not allowed " + nextArgument + " for " + argument);
					parsingArgumentError = true;
				}
				i++;
			}else if (argument.equals("-n")){
				if (isPositiveInteger(nextArgument)){
					searchLevel = Integer.parseInt(nextArgument);
					i++;
				}else{
					// error : argument value (for search level) not a positive integer
					System.out.println("error argument not allowed " + nextArgument + " for " + argument);
					parsingArgumentError = true;
				}
			}else if (argument.equals("-u")){
				uniqueness = false;
			}else{
				// error argument not allowed
				System.out.println("error argument not allowed " + nextArgument);
				parsingArgumentError = true;
			}
		}
		
		if (i < args.length){
			String lastArgument = args[i];
			if (lastArgument.equals("-u")){
				uniqueness = false;
			}else{
				// error argument not allowed
				System.out.println("error argument not allowed " + lastArgument);
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
			System.out.println("Enter your request :");
			String request = SCANNER.next();
			if (request.equals(REQUEST_QUIT)){
				continu = false;
			}else{
				ArrayList<Node> results = graph.executeRequest(request);
				System.out.println("Results : ");
				System.out.println(results);
			}
		}
	}
	
	public static void main(String[] args){
		args = new String[7];
		args[0] = "-f";
		args[1] = "graph.txt";
		args[2] = "-p";
		args[3] = "l";// "p"
		args[4] = "-n";
		args[5] = "3";
		args[6] = "-u";
		
		CommandLineParser parser = new CommandLineParser();
		if (!parser.parseArguments(args)){
			parser.listenRequest();
		}
	}
	
}
