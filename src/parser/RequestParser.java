package parser;

import graph.Request;

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
	public static final Pattern REQUEST_PATTERN = Pattern.compile("(([a-zA-Z0-9_]+ [<|>|-]?[ ]*(\\(([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)\\))*[ ]*[a-zA-Z0-9_]+)[ ]*)(&[ ]*(([a-zA-Z0-9_]+ [<|>|-]?[ ]*(\\(([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)\\))*[ ]*[a-zA-Z0-9_]+)[ ]*))*");
	
	/**
	 * this pattern is usefull to get the informations of the differents elements who composed the request 
	 */
	public static final Pattern ELEMENT_PATTERN = Pattern.compile("([a-zA-Z0-9_]+ [<|>|-]?[ ]*(\\(([a-zA-Z0-9_ ]+)=(([a-zA-Z0-9_ ]+)(,[a-zA-Z0-9_ ]+)*)\\))*[ ]*[a-zA-Z0-9_]+)[ ]*");
	
	/**
	 * this matcher store the result of a match of a request with the REQUEST_PATTERN 
	 */
	public static Matcher REQUEST_MATCHER;
	
	/**
	 * @param request, the request give by the user
	 * @return boolean, the result of the matching between the REQUEST_PATTERN and the request, true if the request is well formed, false otherwise
	 */
	public static boolean checkRequest(Request request){
		REQUEST_MATCHER = REQUEST_PATTERN.matcher(request.getTypedRequest());
		return REQUEST_MATCHER.find();
	}

	/**
	 * this methods extract all the filters from the query wrote by the user and store 
	 * all the informations in the Request object.
	 * @param request, the request to execute
	 */
	public static void getFiltersFromRequest(Request request) {
		// TODO Auto-generated method stub
		
	}
}
