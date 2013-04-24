package test.parser;

import static org.junit.Assert.assertEquals;
import graph.Node;
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
}
