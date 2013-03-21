package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import exception.MalFormedFileException;
import graph.Direction;
import graph.Graph;
import graph.Link;
import graph.Property;
import graph.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.FileParser;

/**
 * File parser test class
 * @author mathieu_canzerini
 *
 */
public class FileParserTest {
	private FileParser fileParser;

	@Before
	public void setUp() throws Exception {
		fileParser = new FileParser();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test(expected=MalFormedFileException.class)
	public void testParseTwoSameLines() throws MalFormedFileException {
		String line = "Barbara--friend-->Elizabeth";
		String sameLine = "Barbara--friend[since=1999]-->Elizabeth";

		Graph graph = new Graph(new HashMap<String, Node>()); 

		fileParser.parseLine(line, graph);
		fileParser.parseLine(sameLine, graph);
	}

	@Test
	public void testParseSmallLine() throws MalFormedFileException{
		String line = "Barbara--friend-->Elizabeth";

		Graph graphExpected = new Graph(new HashMap<String, Node>());

		graphExpected.addString("Barbara");
		graphExpected.addString("Elizabeth");

		graphExpected.getNode("Barbara").getLinks().add(new Link("friend", graphExpected.getNode("Elizabeth"), new ArrayList<Property>(), Direction.OUT));

		graphExpected.getNode("Elizabeth").getLinks().add(new Link("friend", graphExpected.getNode("Barbara"), new ArrayList<Property>(), Direction.IN));

		Graph graphComputed = new Graph(new HashMap<String, Node>()); 
		fileParser.parseLine(line, graphComputed);

		assertEquals(graphExpected, graphComputed);
	}

	/*@Test
	public void testParseBigLine(){
		String line = "Barba ra57--friend_OF[since=1989;share=books,movies,tweets]--Elizabeth";
		Graph graph = new Graph(new HashMap<String, Node>());
	}*/

	@Test
	public void testParseOnlyOneProperty(){
		String stringProperties = "a=d";
		ArrayList<Property> propertiesExpected = new ArrayList<Property>();

		ArrayList<String> valuesExpected = new ArrayList<String>();
		valuesExpected.add("d");
		propertiesExpected.add(new Property("a", valuesExpected));

		ArrayList<Property> propertiesComputed;
		try {
			propertiesComputed = fileParser.parseProperties(stringProperties);
			assertEquals(propertiesExpected, propertiesComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testParseFewProperties(){
		String stringProperties = "a=d;k=msdk";
		ArrayList<Property> propertiesExpected = new ArrayList<Property>();

		ArrayList<String> firstsValuesExpected = new ArrayList<String>();
		firstsValuesExpected.add("d");
		propertiesExpected.add(new Property("a", firstsValuesExpected));

		ArrayList<String> secondsValuesExpected = new ArrayList<String>();
		secondsValuesExpected.add("msdk");
		propertiesExpected.add(new Property("k", secondsValuesExpected));

		ArrayList<Property> propertiesComputed;
		try {
			propertiesComputed = fileParser.parseProperties(stringProperties);
			assertEquals(propertiesExpected, propertiesComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testParseManyProperties(){
		String stringProperties = "a=d;e=f,j,s;f=o,k,k,d,s;k=msdk";
		ArrayList<Property> propertiesExpected = new ArrayList<Property>();

		ArrayList<String> firstsValuesExpected = new ArrayList<String>();
		firstsValuesExpected.add("d");
		propertiesExpected.add(new Property("a", firstsValuesExpected));

		ArrayList<String> secondsValuesExpected = new ArrayList<String>();
		secondsValuesExpected.add("f");
		secondsValuesExpected.add("j");
		secondsValuesExpected.add("s");
		propertiesExpected.add(new Property("e", secondsValuesExpected));

		ArrayList<String> thirdsValuesExpected = new ArrayList<String>();
		thirdsValuesExpected.add("o");
		thirdsValuesExpected.add("k");
		thirdsValuesExpected.add("k");
		thirdsValuesExpected.add("d");
		thirdsValuesExpected.add("s");
		propertiesExpected.add(new Property("f", thirdsValuesExpected));

		ArrayList<String> fourthsValuesExpected = new ArrayList<String>();
		fourthsValuesExpected.add("msdk");
		propertiesExpected.add(new Property("k", fourthsValuesExpected));

		ArrayList<Property> propertiesComputed;
		try {
			propertiesComputed = fileParser.parseProperties(stringProperties);
			assertEquals(propertiesExpected, propertiesComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testParseSmallPropertyName(){
		String stringProperty = "a=x";
		String propertyNameExpected = "a";
		String propertyNameComputed;
		try {
			propertyNameComputed = fileParser.parsePropertyName(stringProperty);
			assertEquals(propertyNameExpected, propertyNameComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testParseBigPropertyName(){
		String stringProperty = "abcdefghijklmopqrstuvwxyz=ksjdksjd,dksjdks,kj";
		String propertyNameExpected = "abcdefghijklmopqrstuvwxyz";
		String propertyNameComputed;
		try {
			propertyNameComputed = fileParser.parsePropertyName(stringProperty);
			assertEquals(propertyNameExpected, propertyNameComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testParseUniquePropertyValues(){
		String stringProperty = "a=xyz";
		String stringPropertyValues = "xyz";
		ArrayList<String> propertyValuesExpected = new ArrayList<String>();
		propertyValuesExpected.add(stringPropertyValues);
		ArrayList<String> propertyValuesComputed;
		try {
			propertyValuesComputed = fileParser.parsePropertyValues(stringProperty);
			assertEquals(propertyValuesExpected,propertyValuesComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		} 
	}

	@Test
	public void testParseMultiplePropertyValues(){
		String stringProperty = "a=w,xy,z";
		ArrayList<String> propertyValuesExpected = new ArrayList<String>();
		propertyValuesExpected.add("w");
		propertyValuesExpected.add("xy");
		propertyValuesExpected.add("z");
		ArrayList<String> propertyValuesComputed;
		try {
			propertyValuesComputed = fileParser.parsePropertyValues(stringProperty);
			assertEquals(propertyValuesExpected,propertyValuesComputed);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testGetDirectionLeft(){
		Direction direction;
		String toLeft = "<--";
		String toNowhere = "--";
		try {
			direction = fileParser.getDirection(toLeft, toNowhere);
			assertEquals(Direction.IN,direction);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testGetDirectionRight(){
		Direction direction;
		String toRight = "-->";
		String toNowhere = "--";
		try {
			direction = fileParser.getDirection(toNowhere, toRight);
			assertEquals(Direction.OUT,direction);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testGetDirectionNone(){
		Direction direction;
		String toNowhere = "--";
		try {
			direction = fileParser.getDirection(toNowhere, toNowhere);
			assertEquals(Direction.NONE,direction);
		} catch (MalFormedFileException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testGetReverseDirectionOfIN(){
		Direction direction ;
		direction = fileParser.getReverseDirection(Direction.IN);
		assertEquals(Direction.OUT,direction);
	}

	@Test
	public void testGetReverseDirectionOfOUT(){
		Direction direction ;
		direction = fileParser.getReverseDirection(Direction.OUT);
		assertEquals(Direction.IN,direction);
	}

	@Test
	public void testGetReverseDirectionOfNONE(){
		Direction direction ;
		direction = fileParser.getReverseDirection(Direction.NONE);
		assertEquals(Direction.NONE,direction);
	}
}
