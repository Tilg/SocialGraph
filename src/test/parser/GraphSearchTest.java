package test.parser;

import static org.junit.Assert.assertEquals;
import graph.Direction;
import graph.Link;
import graph.Node;
import graph.Property;
import graph.Request;

import java.util.ArrayList;

import operation.GraphSearch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.CommandLineParser;

/**
 * class used to test the graphSearch class
 * @author Florent
 */
public class GraphSearchTest {

	@Before
	public void setUp() throws Exception{
		
	}
	
	@After
	public void tearDown() throws Exception{
	}

	/**
	 * this method test a simple request in the graph
	 */
	@Test
	public void testExecuteSimpleCase(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph2.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "1";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "10";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend Carol";
		results = commandLineParser.getOperation().execute(request);
		
		// expected result
		ArrayList<Node> expected = new ArrayList<Node>(2);
		expected.add(commandLineParser.getGraph().getNodes().get("Barbara"));
		expected.add(commandLineParser.getGraph().getNodes().get("Dawn"));
		
		assertEquals(results,expected);
	}
	
	/**
	 * this method test a request composed with a AND '&'
	 */
	@Test
	public void testExecuteCaseWithAND(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph2.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "1";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "10";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend Carol & friend Anna";
		results = commandLineParser.getOperation().execute(request);
		
		// expected result
		ArrayList<Node> expected = new ArrayList<Node>(2);
		expected.add(commandLineParser.getGraph().getNodes().get("Barbara"));
		
		assertEquals(results,expected);
	}
	
	/**
	 * this method test a request composed with a OR '|'
	 */
	@Test
	public void testExecuteCaseWithOR(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph2.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "1";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "10";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend Carol | friend Elizabeth";
		results = commandLineParser.getOperation().execute(request);
		
		// expected result
		ArrayList<Node> expected = new ArrayList<Node>(2);
		expected.add(commandLineParser.getGraph().getNodes().get("Barbara"));
		expected.add(commandLineParser.getGraph().getNodes().get("Dawn"));
		expected.add(commandLineParser.getGraph().getNodes().get("Jill"));
		
		assertEquals(results,expected);
	}
	
	/**
	 * this method test a request with a direction filter
	 */
	@Test
	public void testExecuteDirection(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph2.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "1";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "10";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		ArrayList<Node> results2 = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend < Carol";
		results = commandLineParser.getOperation().execute(request);
		
		// execution of a simple request
		String request2 = "friend > Carol";
		results2 = commandLineParser.getOperation().execute(request2);
		
		// expected result
		ArrayList<Node> expected1 = new ArrayList<Node>(2);
		expected1.add(commandLineParser.getGraph().getNodes().get("Barbara"));

		ArrayList<Node> expected2 = new ArrayList<Node>(2);
		expected2.add(commandLineParser.getGraph().getNodes().get("Dawn"));
		
		assertEquals(results,expected1);
		assertEquals(results2,expected2);
	}
	
	/**
	 * this method test a request with a property filter
	 */
	@Test
	public void testExecuteProperty(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "1";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "10";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend < (since=1999) Carol";
		results = commandLineParser.getOperation().execute(request);
		
		// expected result
		ArrayList<Node> expected1 = new ArrayList<Node>(2);
		expected1.add(commandLineParser.getGraph().getNodes().get("Barbara"));

		assertEquals(results,expected1);
	}
	
	/**
	 * this method test a request with a search level of 3 and uniqueness of 10
	 * ex : the request "friend barbara" became " friend of friend of friend of barbara"
	 * a node car be mark 10 times before beeing ignored
	 */
	@Test
	public void testExecuteSearchLevel(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph2.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "3";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "10";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend Carol";
		results = commandLineParser.getOperation().execute(request);
		
		// expected result
		ArrayList<Node> expected1 = new ArrayList<Node>(2);
		expected1.add(commandLineParser.getGraph().getNodes().get("Barbara"));
		expected1.add(commandLineParser.getGraph().getNodes().get("Dawn"));
		expected1.add(commandLineParser.getGraph().getNodes().get("Jill"));
		expected1.add(commandLineParser.getGraph().getNodes().get("Elizabeth"));

		assertEquals(results,expected1);
	}
	
	/**
	 * this method test a request with a search level of 4 and uniqueness of 2
	 * ex : the request "friend barbara" became " friend of friend of friend of barbara"
	 * a node car be mark only 2 times before beeing ignored
	 */
	@Test
	public void testExecuteUniqueness(){
		// initialisation of a new commande line parser
		CommandLineParser commandLineParser = new CommandLineParser();
		
		String[] args = new String[8];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph2.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "4";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		args[7] = "2";
		
		commandLineParser.parseArguments(args);
		
		((GraphSearch)commandLineParser.getOperation()).setRequest(new Request());
		ArrayList<Node> results = new ArrayList<Node>(1);
		
		// execution of a simple request
		String request = "friend Carol";
		results = commandLineParser.getOperation().execute(request);
		
		// expected result
		ArrayList<Node> expected1 = new ArrayList<Node>(2);
		expected1.add(commandLineParser.getGraph().getNodes().get("Anna")); //anna is the only node return, the other are ignored because they are already visit 2 times 

		// NB: to be more realistic you need to improve the uniqueness level 
		
		assertEquals(results,expected1);
	}
	
	/**
	 * this method test the method keepSomeNode who keep the node in common in 2 node list
	 */
	@Test
	public void testKeepSomeNode(){

		ArrayList<Node> results = new ArrayList<Node>(1);
		
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Node d = new Node("d");
		Node e = new Node("e");
		
		ArrayList<Node> list1 = new ArrayList<Node>(1);
		list1.add(a);
		list1.add(b);
		list1.add(d);
		
		ArrayList<Node> list2 = new ArrayList<Node>(1);
		list2.add(b);
		list2.add(c);
		list2.add(d);
		list2.add(e);
		
		results = GraphSearch.keepSameNode(list1, list2);
		
		ArrayList<Node> expected = new ArrayList<Node>(1);
		expected.add(b);
		expected.add(d);
		
		assertEquals(expected,results);
	}
	
	/**
	 * this method test the contains function who permit to know if a node exist in a list with a node label
	 */
	@Test
	public void testContains(){

		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");

		ArrayList<Node> list1 = new ArrayList<Node>(1);
		list1.add(a);
		list1.add(b);
		list1.add(c);
		
		assertEquals(true,GraphSearch.contains(list1,"a"));
		assertEquals(false,GraphSearch.contains(list1,"d"));
	}
	
	/**
	 * this method test the direction checking method
	 */
	@Test
	public void testCheckDirection(){

		Node a = new Node("a");

		Link link = new Link("friend" , a, Direction.IN);
		
		assertEquals(true,GraphSearch.checkDirection(link, "<"));
		assertEquals(false,GraphSearch.checkDirection(link, ">"));
	}
	
	/**
	 * this method test the property checking method
	 */
	@Test
	public void testCheckProperty(){

		Node a = new Node("a");
		ArrayList<Property> prop =  new ArrayList<Property>(1);
		ArrayList<String> values = new ArrayList<String>(1);
		values.add("2000");
		prop.add(new Property("since",values));

		Link link = new Link("friend" , a, prop,Direction.IN);
		
		assertEquals(true,GraphSearch.checkProperty(link, prop));
		
		ArrayList<Property> prop2 =  new ArrayList<Property>(1);
		ArrayList<String> values2 = new ArrayList<String>(1);
		values2.add("1900");
		prop2.add(new Property("since",values2));
		
		assertEquals(false,GraphSearch.checkProperty(link, prop2));
	}

}
