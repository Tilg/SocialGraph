package test.parser;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.CommandLineParser;


/**
 * Command Line Parser test class
 * 
 * @author Omar
 * @version 0.1
 */
public class CommandLineParserTest{
	CommandLineParser commandLineParser;
	
	@Before
	public void setUp() throws Exception{
		commandLineParser = new CommandLineParser();
	}
	
	@After
	public void tearDown() throws Exception{
	}
	
	
	@Test
	public void testParseArguments(){
		String[] args = new String[7];
		args[0] = CommandLineParser.ARGUMENT_FILE_NAME;
		args[1] = "graph.txt";
		args[2] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY;
		args[3] = CommandLineParser.ARGUMENT_SEARCH_STRATEGY_BREADTH_FIRST;
		args[4] = CommandLineParser.ARGUMENT_SEARCH_LEVEL;
		args[5] = "3";
		args[6] = CommandLineParser.ARGUMENT_UNIQUENESS;
		
		boolean parsingState = commandLineParser.parseArguments(args);
		
		assertEquals(false,parsingState);
	}
	
	
	@Test
	public void testIsPositiveInteger(){
		assertEquals(true,commandLineParser.isPositiveInteger("3"));
		assertEquals(false,commandLineParser.isPositiveInteger("0"));
		assertEquals(false,commandLineParser.isPositiveInteger("-5"));
	}
}
