package parser;

import graph.Property;
import graph.Request;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to parse and check the validity of a request
 * @author Florent
 */
public class RequestParser{
	/**
	 * this patern match a well formed request like : [ linkName [<|-|>] [propertyName = value1[,value2,value3]*]* nodeName ] & ...
	 * exemple : friend - carole & boyfriend > (since = 1989)(look = beautiful) natacha 
	 * we match the node who are friend with carole and who are the beautiful boyfriend of natacha since 1989
	 */
	public static final Pattern REQUEST_PATTERN = Pattern.compile("[ ]*(([a-zA-Z0-9_]+ [<|>|-]?[ ]*(\\(([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)\\)[ ]*)*[ ]*[a-zA-Z0-9_]+)[ ]*)(&[ ]*(([a-zA-Z0-9_]+ [<|>|-]?[ ]*(\\(([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)\\)[ ]*)*[ ]*[a-zA-Z0-9_]+)[ ]*))*");
	/**
	 * this matcher store the result of a match of a request with the REQUEST_PATTERN 
	 */
	public static Matcher REQUEST_MATCHER;
	
	/**
	 * this pattern is useful to get the informations of the different elements who composed the request 
	 */
	public static final Pattern ELEMENT_PATTERN = Pattern.compile("[ ]*(([a-zA-Z0-9_]+) ([<|>|-]?)[ ]*(\\(([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)\\)[ ]*)*[ ]*([a-zA-Z0-9_]+))[ ]*");
	/**
	 * this matcher allows to get easily the informations contains in a request
	 */
	public static Matcher ELEMENT_MATCHER;
	/**
	 * this int is is group number of the link label in the regular expression who represent the request, useful to get it from the request
	 */
	public static final int LINK_LABEL_GROUP_NUMBER = 2;
	/**
	 * this int is is group number of the direction in the regular expression who represent the request, useful to get it from the request
	 */
	public static final int DIRECTION_GROUP_NUMBER = 3;
	/**
	 * this int is is group number of the node label in the regular expression who represent the request, useful to get it from the request
	 */
	public static final int NODE_LABEL_GROUP_NUMBER = 9;
	/**
	 * this int is is group number of the label in the regular expression who represent the property, useful to get it from the request
	 */
	public static final int PROPERTY_LABEL_GROUP_NUMBER = 1;
	/**
	 * this int is is group number of the values in the regular expression who represent the property, useful to get it from the request
	 */
	public static final int PROPERTY_VALUES_GROUP_NUMBER = 2;
	/**
	 * this matcher allows to get easily the property contains in a request
	 */
	public static Matcher PROPERTY_MATCHER;
	/**
	 * this patern is used to get the different group of link property in the element
	 */
	public static final Pattern PROPERTIES_PATTERN = Pattern.compile("([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)");
	
	/**
	 * @param request, the request give by the user
	 * @return boolean, the result of the matching between the REQUEST_PATTERN and the request, true if the request is well formed, false otherwise
	 */
	public static boolean checkRequest(Request request){
		REQUEST_MATCHER = REQUEST_PATTERN.matcher(request.getTypedRequest());
System.out.println("*****");
System.out.println(REQUEST_MATCHER.group());
System.out.println("*****");
		return REQUEST_MATCHER.find();
	}

	/**
	 * this methods extract all the filters from the query wrote by the user and store all the informations in the Request object.
	 * @param request, the request to execute
	 */
	public static void getFiltersFromRequest(Request request) {
		
		ArrayList<String> elementsList = getElementsFromRequest(request); //parsing of the request into sub request
		
		for ( String element : elementsList){ // for each element
System.out.println("element : "+element);
System.out.println(ELEMENT_PATTERN.toString());
			ELEMENT_MATCHER = ELEMENT_PATTERN.matcher(element);
System.out.println(ELEMENT_MATCHER.group());
			String linkLabelString = ELEMENT_MATCHER.group(LINK_LABEL_GROUP_NUMBER);
System.out.println("linkLabelString : "+linkLabelString);
			linkLabelString = linkLabelString.trim();
			request.getLinkLabelList().add(linkLabelString); // we add to the link_label_List each linkLabel Match in the different elements
			
			String directionString = ELEMENT_MATCHER.group(DIRECTION_GROUP_NUMBER);
			directionString = directionString.trim();
			request.getDirectionList().add(directionString); // same thing for direction list, if the direction wasn't specify, null is add to the list
			
			String nodeLabelString = ELEMENT_MATCHER.group(NODE_LABEL_GROUP_NUMBER);
			nodeLabelString = nodeLabelString.trim();
			request.getTargetNodeLabelList().add(nodeLabelString);// same thing for node list
			
			PROPERTY_MATCHER = PROPERTIES_PATTERN.matcher(element);
			
			if (!PROPERTY_MATCHER.find()){ // if we don't match property 
				request.getPropertyList().add(null);
			}
			else{ // if we have property to get
				ArrayList<Property> tmpPropertyList = new ArrayList<Property>(1);
				
				Property tmpProperty;
				ArrayList<String> allValues;
				
				while (PROPERTY_MATCHER.find()) {// while there is property
					
					tmpProperty = new Property();
					allValues = new ArrayList<String>(1);
					
					tmpProperty.setLabel(PROPERTY_MATCHER.group(PROPERTY_LABEL_GROUP_NUMBER).trim());// i set the property label

					String propertyValues = PROPERTY_MATCHER.group(PROPERTY_VALUES_GROUP_NUMBER);
					
					if (propertyValues.indexOf(',')==-1){//if you don't have a coma, you have only 1 value 
						allValues.add(propertyValues.trim());
					}
					else{// we parse all the values
						
						while (propertyValues.indexOf(',')!=-1 )
						{
							allValues.add(propertyValues.substring(0, propertyValues.indexOf(',')));
							propertyValues = propertyValues.substring(propertyValues.indexOf(',')+1, propertyValues.length());
						}
						allValues.add(propertyValues); 
					}
					tmpProperty.setValues(allValues);//i set the property values
					
					tmpPropertyList.add(tmpProperty);// i add the property to the list of property of the request
				}
				
				request.getPropertyList().add(tmpPropertyList); //i add the property to the list of all property found in the request
			}
		}
	}
	
	/**
	 * this method parse a request into his differents composant
	 * exemple : friend > carol & employee > (since = 1989) techCO parse the request into a list of 2 elements : [0] friend > carol, [1] employee > (since = 1989) techCO
	 * @param request , the request to parse
	 * @return, the arraylist who contains all the element
	 */
	public static ArrayList<String> getElementsFromRequest(Request request)
	{
		ArrayList<String> res = new ArrayList<String>(1);
		String requestToParse = request.getTypedRequest();
		
		if (requestToParse.indexOf('&')==-1){ //if the request don't contains the & character, it's not a request composed with different elements
			res.add(requestToParse.trim()); 
		}
		else{//if the request is compose by differents elements separate by the & character
			
			while (requestToParse.indexOf('&')!=-1 ){
				res.add(requestToParse.substring(0, requestToParse.indexOf('&')).trim());
				requestToParse = requestToParse.substring(requestToParse.indexOf('&')+1, requestToParse.length());
			}
			res.add(requestToParse.trim()); 
		}
		
		return res;
	}
}
