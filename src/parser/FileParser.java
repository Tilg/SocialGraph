package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graph.Direction;
import graph.Link;
import graph.Node;
import graph.Graph;
import graph.Property;;


/**
 * TODO : throw MalFormedFileException
 * TODO : Write Tests
 * This class is useful for parsing a text file 
 * and return a graph
 * @author mathieu_canzerini
 * @version 0.1
 */

public class FileParser {
	public static final Pattern LINE_PATTERN = Pattern.compile("^([^-]+)(--|<--)([a-zA-Z0-9_]+)((\\[([a-zA-Z0-9_ ]+))(=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*))((;([a-zA-Z0-9_ ])+)(=[a-zA-Z0-9_ ]+(,[a-zA-Z0-9_ ]+)*))*\\])?(-->|--)([^-]+)");
	public static final Pattern PROPERTIES_PATTERN = Pattern.compile("([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)");
	public static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("([a-zA-Z0-9_ ]+)=");
	public static final Pattern PROPERTY_VALUES_PATTERN = Pattern.compile("=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)");
	public static Matcher LINE_MATCHER;
	public static Matcher PROPERTIES_MATCHER;
	public static Matcher PROPERTY_NAME_MATCHER;
	public static Matcher PROPERTY_VALUES_MATCHER;
	public static final int FIRST_NODE_LABEL_GROUP = 1;
	public static final int FIRST_LINK_DIRECTION_GROUP = 2;
	public static final int LINK_LABEL_GROUP = 3;
	public static final int LINK_PROPERTIES_GROUP = 5;
	public static final int SECOND_LINK_DIRECTION_GROUP = 17;
	public static final int SECOND_NODE_LABEL_GROUP = 18;
	public static final int PROPERTY_NAME_GROUP = 1;
	public static final int PROPERTY_VALUES_GROUP = 1;

	/**
	 * parse a text file 
	 * and return a graph
	 * @param fileName the file name which represents the graph
	 * @return the graph represented by the file
	 */

	public Graph parseFile(String fileName) {
		// graph initialization with an empty hash map 
		Graph graph = new Graph(new HashMap<String, Node>()); // better an empty constructor

		try {
			// Construction of the fileInputStream with the file name
			FileInputStream fileInputStream = new FileInputStream(fileName);

			// Construction of the bufferedReader with the fileInputStream
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

			String line;

			// line by line file process
			while ((line = bufferedReader.readLine()) != null) {
				parseLine(line, graph);
			}

			bufferedReader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return graph;
	}

	/**
	 * parse text line and update the graph
	 * @param line the parsed file line  
	 * @param graph the graph to construct
	 */
	public void parseLine(String line, Graph graph){
		LINE_MATCHER = LINE_PATTERN.matcher(line);

		// get the first node label by parsing the line
		String firstNodeLabel = LINE_MATCHER.group(FIRST_NODE_LABEL_GROUP);

		// get the first link direction by parsing the line
		String stringFirstLinkDirection = LINE_MATCHER.group(FIRST_LINK_DIRECTION_GROUP);

		// get the link label by parsing the line
		String linkLabel = LINE_MATCHER.group(LINK_LABEL_GROUP);

		// get the link properties label by parsing the line
		String linkProperties = LINE_MATCHER.group(LINK_PROPERTIES_GROUP);

		// get the second link direction by parsing the line
		String stringSecondLinkDirection = LINE_MATCHER.group(SECOND_LINK_DIRECTION_GROUP);

		// get the second node label by parsing the line
		String secondNodeLabel = LINE_MATCHER.group(SECOND_NODE_LABEL_GROUP);


		Link firstLink, secondLink;
		Node firstNode, secondNode;
		ArrayList<Property> properties = new ArrayList<Property>();
		Direction firstLinkDirection = null, secondLinkDirection = null;

		// if link has properties
		if(linkProperties != null){
			// get the list of link properties
			properties = parseProperties(linkProperties);
		}

		//set the two links directions 
		setDirections(stringFirstLinkDirection, stringSecondLinkDirection, firstLinkDirection, secondLinkDirection); // Maybe does'nt work

		// first node construction
		// if the node already exists
		if (graph.getNodes().containsKey(firstNodeLabel)){
			firstNode = graph.getNodes().get(firstNodeLabel);
		} else {
			// if the node does not already exist	
			firstNode = new Node(firstNodeLabel); // Waiting for Hadrien modification of the Graph Class Methods
			graph.addNode(firstNode);
		}

		// second node construction
		// if the node already exists
		if (graph.getNodes().containsKey(secondNodeLabel)){
			secondNode = graph.getNodes().get(secondNodeLabel);
		} else {
			// if the node does not already exist	
			secondNode = new Node(secondNodeLabel); // Waiting for Hadrien modification of the Graph Class Methods
			graph.addNode(secondNode);
		}

		firstLink = new Link(linkLabel, secondNode, properties, firstLinkDirection);
		secondLink = new Link(linkLabel, firstNode, properties, secondLinkDirection);

		firstNode.getLinks().add(firstLink);
		secondNode.getLinks().add(secondLink);

	}

	/**
	 * parse properties
	 * @param properties 
	 * @return an array list of the properties
	 */
	public ArrayList<Property> parseProperties(String stringProperties){
		ArrayList<Property> properties = new ArrayList<Property>();
		PROPERTIES_MATCHER = PROPERTIES_PATTERN.matcher(stringProperties);
		String stringProperty;
		String propertyName;
		ArrayList<String> propertyValues;
		Property property;

		// while there is property
		while (PROPERTIES_MATCHER.find()) {
			// get the property ( name + value(s) )
			stringProperty = PROPERTIES_MATCHER.group();

			// get the name of property by parsing stringProperty
			propertyName = parsePropertyName(stringProperty);

			// get the property list of values by parsing stringProperty
			propertyValues = parsePropertyValues(stringProperty);

			// instantiation of a new property object with name and list of values
			property = new Property(propertyName, propertyValues);
			properties.add(property);
		}

		return properties;
	}

	/**
	 * parse property name
	 * @param properties 
	 * @return an array list of the property name
	 */
	public String parsePropertyName(String stringProperty){
		PROPERTY_NAME_MATCHER = PROPERTY_NAME_PATTERN.matcher(stringProperty);

		// get the name of the property
		String propertyName = PROPERTY_NAME_MATCHER.group(PROPERTY_NAME_GROUP);
		return propertyName;
	}

	/**
	 * parse property values
	 * @param properties 
	 * @return an array list of the property values
	 */
	public ArrayList<String> parsePropertyValues(String stringProperty){
		PROPERTY_VALUES_MATCHER = PROPERTY_NAME_PATTERN.matcher(stringProperty);

		// get the values of the property (example : a,b,c)
		String propertyValues = PROPERTY_VALUES_MATCHER.group(PROPERTY_VALUES_GROUP);

		// split with ',' and add the different values in the array list
		ArrayList<String> values = new ArrayList<String>(Arrays.asList(propertyValues.split(",")));

		return values;
	}

	/**
	 * set the two links directions
	 * @param stringFirstLinkDirection first read direction (can be <-- or --)
	 * @param stringSecondLinkDirection second read direction (can be -- or -->)
	 * @param firstLinkDirection the first link Direction (IN, OUT or NONO)
	 * @param secondLinkDirection the second link Direction (IN, OUT or NONO)
	 */
	public void setDirections(String stringFirstLinkDirection, String stringSecondLinkDirection, Direction firstLinkDirection, Direction secondLinkDirection){
		// if it is an input link
		if (stringFirstLinkDirection.equals("<--")) {
			firstLinkDirection = Direction.IN;
			secondLinkDirection = Direction.OUT;
		} else if(stringSecondLinkDirection.equals("-->")){
			// if it is an output link
			firstLinkDirection = Direction.OUT;
			secondLinkDirection = Direction.IN;
		} else {
			// if it is an input/output link
			firstLinkDirection = Direction.NONE;
			secondLinkDirection = Direction.NONE;
		}
	}
}
