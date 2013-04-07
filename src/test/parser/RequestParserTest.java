package test.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import graph.Property;
import graph.Request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.RequestParser;


/**
 * class used to test the request parser
 * @author Florent
 */
public class RequestParserTest {

	@Before
	public void setUp() throws Exception{
	}
	
	@After
	public void tearDown() throws Exception{
	}
	
	/**
	 * this method test check if the checkRequest method of request parser correctly match the request
	 */
	@Test
	public void testCheckRequest(){

		String request = "friend > paul & employee - (since = 1989)(role = chief) techCo & like < (film = oz,water,taz) cinema";
		
		assertEquals(true,RequestParser.checkRequest(request));
	}
	
	/**
	 * this method test check if the getElementsFromRequest method of request parser correctly parse the request on the '&' char
	 */
	@Test
	public void testParseRequest1(){
		String request = "friend > paul & employee - (since = 1989) techCo & aaa < BBB";
		
		ArrayList<String> elementsListRes = RequestParser.getElementsFromRequest(request,'&');

		ArrayList<String> elementsListCorrect = new ArrayList<String>(1);
		elementsListCorrect.add("friend > paul");
		elementsListCorrect.add("employee - (since = 1989) techCo");
		elementsListCorrect.add("aaa < BBB");

		assertEquals(elementsListRes,elementsListCorrect);
	}
	
	/**
	 * this method test check if the getElementsFromRequest method of request parser correctly parse the request on the '|' char
	 */
	@Test
	public void testParseRequest2(){
		String request = "friend > paul | employee - (since = 1989) techCo & aaa < BBB";
		
		ArrayList<String> elementsListRes = RequestParser.getElementsFromRequest(request,'|');

		ArrayList<String> elementsListCorrect = new ArrayList<String>(1);
		elementsListCorrect.add("friend > paul");
		elementsListCorrect.add("employee - (since = 1989) techCo & aaa < BBB");

		assertEquals(elementsListRes,elementsListCorrect);
	}
	
	/**
	 * this method test check if the getFiltersFromRequest method of request parser correctly get all the filters from the request
	 */
	@Test
	public void testMatching1(){
		
		String typeRequest = "friend > paul & employee - (since = 1989)(role = chief) techCo & like < (film = oz,water,taz) cinema";
		Request request = new Request();
		RequestParser.getFiltersFromRequest(request,typeRequest);
		
		Request request2 = new Request();

		//list of link tags
		ArrayList<String> linkTagsList = new ArrayList<String>(1);
		linkTagsList.add("friend");
		linkTagsList.add("employee");
		linkTagsList.add("like");
		
		//list of directions
		ArrayList<String> directionList = new ArrayList<String>(1);
		directionList.add(">");
		directionList.add("-");
		directionList.add("<");
		
		//list of property
		ArrayList<ArrayList<Property>> propertyListOfAllPropertyList = new ArrayList<ArrayList<Property>>(1);
		
		//THE FIRST PART
		propertyListOfAllPropertyList.add(new ArrayList<Property>(1));//the first part of the request doesn't have any property
		
		//THE SECOND PART
		ArrayList<Property> propertyList1 = new ArrayList<Property>(1);
		
		ArrayList<String> value1 = new ArrayList<String>(); //(since = 1989)
		value1.add("1989");
		propertyList1.add(new Property("since",value1));
		
		ArrayList<String> value2 = new ArrayList<String>(); //(role = chief)
		value2.add("chief");
		propertyList1.add(new Property("role",value2));
		
		propertyListOfAllPropertyList.add(propertyList1);
		
		//THE THIRD PART
		ArrayList<Property> propertyList2 = new ArrayList<Property>(1);
		
		ArrayList<String> value3 = new ArrayList<String>(); //(film = oz,water,taz)
		value3.add("oz");
		value3.add("water");
		value3.add("taz");
		propertyList2.add(new Property("film",value3));
		
		propertyListOfAllPropertyList.add(propertyList2);
		
		//list of node label
		ArrayList<String> nodeTagList = new ArrayList<String>(1);
		nodeTagList.add("paul");
		nodeTagList.add("techCo");
		nodeTagList.add("cinema");
		
		//we finish the request by setting the list
		request2.setLinkLabelList(linkTagsList);
		request2.setDirectionList(directionList);
		request2.setPropertyList(propertyListOfAllPropertyList);
		request2.setTargetNodeLabelList(nodeTagList);
		
		assertEquals(true,request.equals(request2));
	}
	
	/**
	 * this method test check if the request where * is used are well match
	 */
	@Test
	public void testMatching2(){
		String typeRequest = "florent * & * > kad & * < (test =aaa) jaques & * *";
		Request request = new Request();
		RequestParser.getFiltersFromRequest(request,typeRequest);
		
		Request request2 = new Request();
		
		//list of link tags
		ArrayList<String> linkTagsList = new ArrayList<String>(1);
		linkTagsList.add("florent");
		linkTagsList.add("*");
		linkTagsList.add("*");
		linkTagsList.add("*");
		
		//list of directions
		ArrayList<String> directionList = new ArrayList<String>(1);
		directionList.add("-");
		directionList.add(">");
		directionList.add("<");
		directionList.add("-");
		
		//list of property
		ArrayList<ArrayList<Property>> propertyListOfAllPropertyList = new ArrayList<ArrayList<Property>>(1);
		
		propertyListOfAllPropertyList.add(new ArrayList<Property>(1));
		propertyListOfAllPropertyList.add(new ArrayList<Property>(1));
		
		ArrayList<Property> propertyList = new ArrayList<Property>(1);
		ArrayList<String> value1 = new ArrayList<String>(); //(test= aaa)
		value1.add("aaa");
		propertyList.add(new Property("test",value1));
		propertyListOfAllPropertyList.add(propertyList);
		
		propertyListOfAllPropertyList.add(new ArrayList<Property>(1));
		
		//list of node label
		ArrayList<String> nodeTagList = new ArrayList<String>(1);
		nodeTagList.add("*");
		nodeTagList.add("kad");
		nodeTagList.add("jaques");
		nodeTagList.add("*");
		
		//we finish the request by setting the list
		request2.setLinkLabelList(linkTagsList);
		request2.setDirectionList(directionList);
		request2.setPropertyList(propertyListOfAllPropertyList);
		request2.setTargetNodeLabelList(nodeTagList);
		
		assertEquals(true,request.equals(request2));
	}
	
}
