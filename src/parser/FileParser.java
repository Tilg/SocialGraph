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

import exception.MalFormedFileException;

import graph.Direction;
import graph.Link;
import graph.Node;
import graph.Graph;
import graph.Property;;


/**
 * TODO : throw MalFormedFileException
 * This class is useful for parsing a text file 
 * and return a graph
 * @author mathieu_canzerini
 * @version 0.1
 */

public class FileParser {
	public static final Pattern LINE_PATTERN = Pattern.compile("^([^-<]+)(--|<--)([a-zA-Z0-9_]+)((\\[([a-zA-Z0-9_ ]+))(=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*))((;([a-zA-Z0-9_ ])+)(=[a-zA-Z0-9_ ]+(,[a-zA-Z0-9_ ]+)*))*\\])?(-->|--)([^-]+)");
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
	public static final int LINK_PROPERTIES_GROUP = 4;
	public static final int SECOND_LINK_DIRECTION_GROUP = 16;
	public static final int SECOND_NODE_LABEL_GROUP = 17;
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
			graph = null;
		}
		return graph;
	}

	/**
	 * parse text line and update the graph
	 * @param line the parsed file line  
	 * @param graph the graph to construct
	 */
	public void parseLine(String line, Graph graph){
		try {
			LINE_MATCHER = LINE_PATTERN.matcher(line);
			boolean wellFormedFile = true;

			// matches the line with the pattern
			wellFormedFile = LINE_MATCHER.find();

			if (!wellFormedFile) {
				throw new MalFormedFileException();
			}

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

			if (firstNodeLabel == null || stringFirstLinkDirection == null || linkLabel == null 
					|| stringSecondLinkDirection == null || secondNodeLabel == null) {
				throw new MalFormedFileException();
			}
			
			Link firstLink, secondLink;
			Node firstNode, secondNode;
			ArrayList<Property> properties = new ArrayList<Property>();
			Direction firstLinkDirection, secondLinkDirection;

			// if link has properties
			if(linkProperties != null){
				// get the list of link properties
				properties = parseProperties(linkProperties);
			}

			//set the two links directions 
			firstLinkDirection = getDirection(stringFirstLinkDirection, stringSecondLinkDirection);
			secondLinkDirection = getReverseDirection(firstLinkDirection);

			// first node construction
			// if the node already exists
			if (graph.getNodes().containsKey(firstNodeLabel)){
				//firstNode = graph.getNodes().get(firstNodeLabel);
				firstNode = graph.getNode(firstNodeLabel);
			} else {
				// if the node does not already exist	
				//firstNode = new Node(firstNodeLabel); // Waiting for Hadrien modification of the Graph Class Methods
				//graph.addNode(firstNode);
				graph.addString(firstNodeLabel);
				firstNode = graph.getNode(firstNodeLabel);
			}

			// second node construction
			// if the node already exists
			if (graph.getNodes().containsKey(secondNodeLabel)){
				//secondNode = graph.getNodes().get(secondNodeLabel);
				secondNode = graph.getNode(secondNodeLabel);
			} else {
				// if the node does not already exist	
				//secondNode = new Node(secondNodeLabel); // Waiting for Hadrien modification of the Graph Class Methods
				//graph.addNode(secondNode);
				graph.addString(secondNodeLabel);
				secondNode = graph.getNode(secondNodeLabel);
			}

			firstLink = new Link(linkLabel, secondNode, properties, firstLinkDirection);
			secondLink = new Link(linkLabel, firstNode, properties, secondLinkDirection);

			firstNode.getLinks().add(firstLink);
			secondNode.getLinks().add(secondLink);
		} catch (MalFormedFileException e) {
			graph = null;
		}
	}

	/**
	 * parse properties
	 * @param properties 
	 * @return an array list of the properties
	 * @throws MalFormedFileException 
	 */
	public ArrayList<Property> parseProperties(String stringProperties) throws MalFormedFileException{
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
		
		if (properties.isEmpty()) {
			throw new MalFormedFileException();
		}
		return properties;
	}

	/**
	 * parse property name
	 * @param properties 
	 * @return an array list of the property name
	 * @throws MalFormedFileException 
	 */
	public String parsePropertyName(String stringProperty) throws MalFormedFileException{
		PROPERTY_NAME_MATCHER = PROPERTY_NAME_PATTERN.matcher(stringProperty);
		boolean wellFormedFile;
		// get the name of the property
		wellFormedFile = PROPERTY_NAME_MATCHER.find();
		String propertyName = PROPERTY_NAME_MATCHER.group(PROPERTY_NAME_GROUP);
		
		if (!wellFormedFile || propertyName == null) {
			throw new MalFormedFileException();
		}
		return propertyName;
	}

	/**
	 * parse property values
	 * @param properties 
	 * @return an array list of the property values
	 * @throws MalFormedFileException 
	 */
	public ArrayList<String> parsePropertyValues(String stringProperty) throws MalFormedFileException{
		PROPERTY_VALUES_MATCHER = PROPERTY_VALUES_PATTERN.matcher(stringProperty);
		boolean wellFormedFile;
		// get the values of the property (example : a,b,c)
		wellFormedFile = PROPERTY_VALUES_MATCHER.find();
		String propertyValues = PROPERTY_VALUES_MATCHER.group(PROPERTY_VALUES_GROUP);
		
		// split with ',' and add the different values in the array list
		ArrayList<String> values = new ArrayList<String>(Arrays.asList(propertyValues.split(",")));

		if (!wellFormedFile || propertyValues == null) {
			throw new MalFormedFileException();
		}
		return values;
	}

	/**
	 * get a direction using the two String directions
	 * @param stringFirstLinkDirection first read direction (can be <-- or --)
	 * @param stringSecondLinkDirection second read direction (can be -- or -->)
	 * @param firstLinkDirection the first link Direction (IN, OUT or NONO)
	 * @param secondLinkDirection the second link Direction (IN, OUT or NONO)
	 * @return IN, OUT, or NONE
	 * @throws MalFormedFileException 
	 */
	public Direction getDirection(String stringFirstLinkDirection, String stringSecondLinkDirection) throws MalFormedFileException {
		Direction direction;
		// if file is not well formed 
		if(stringFirstLinkDirection.equals("<--") && stringSecondLinkDirection.equals("-->")){
			throw new MalFormedFileException();
		}
		
		// if it is an input link
		if (stringFirstLinkDirection.equals("<--")) {
			direction = Direction.IN;
		} else if(stringSecondLinkDirection.equals("-->")){
			// if it is an output link
			direction = Direction.OUT;
		} else {
			// if it is an input/output link
			direction = Direction.NONE;
		}
		return direction;
	}

	/**
	 * get the reverse direction of the parameter
	 * @param direction the direction to reverse
	 * @return IN, OUT, or NONE
	 */
	public Direction getReverseDirection(Direction direction) {
		Direction reverseDirection;
		// if it is an input link
		if (direction == Direction.OUT) {
			reverseDirection = Direction.IN;
		} else if(direction == Direction.IN){
			// if it is an output link
			reverseDirection = Direction.OUT;
		} else {
			// if it is an input/output link
			reverseDirection = Direction.NONE;
		}
		return reverseDirection;
	}
}
