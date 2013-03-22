package test.parser;

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
		args[0] = "-f";
		args[1] = "graph.txt";
		args[2] = "-p";
		args[3] = "l";// "p"
		args[4] = "-n";
		args[5] = "3";
		args[6] = "-u";
		
		commandLineParser.parseArguments(args);
	}
	
	
	@Test
	public void testIsPositiveInteger(){
		
	}
}
