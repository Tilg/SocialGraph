package graph;

import java.util.ArrayList;

/**
 * Properties are linked to one arc. A property contain informations between 2 nodes;
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Property{
	
	/**
	 * Property label e.g.: "Role","Since","Hired"
	 */
	protected String label;

	/**
	 * Values of a property e.g:"[books, movies, tweets]","2005","May 2005"
	 */
	protected ArrayList<String> values;

	/**
	 * Property constructor A property is always composed of a link with a set of attribute
	 * 
	 * @param label
	 * @param values
	 */
	public Property(String label,ArrayList<String> values){
		this.label = label;
		this.values = values;
	}
	
	/**
	 * Property constructor A property is always composed of a link with a set of attribute
	 */
	public Property(){
		values = new ArrayList<String>(1);
	}

	/**
	 * Return the label of a property
	 * 
	 * @return
	 */
	public String getLabel(){
		return label;
	}

	/**
	 * Update the label of a property
	 * 
	 * @param label
	 */
	public void setLabel(String label){
		this.label = label;
	}

	/**
	 * Return all the values of a property
	 * 
	 * @return values
	 */
	public ArrayList<String> getValues(){
		return values;
	}

	/**
	 * Update all the property values
	 * 
	 * @param values
	 */
	public void setValues(ArrayList<String> values){
		this.values = values;
	}

	/**
	 * Prints the property on the basic output
	 */
	@Override
	public String toString() {
		String sout = this.label + "="; 
		int size = this.values.size();

		if(size == 1){
			sout += this.values.get(0);
		} else {
			sout += "["; 
			for(int i = 0; i <(size-1); i++){
				sout += this.values.get(i) + ",";
			}
			sout += values.get(size) + "]";
		}
		return sout;
	}

	/**
	 * This method can be used to compare 2 properties
	 * Returns True is the if the property given in parameter is equal to the instantiated property
	 * 
	 * @param property
	 */
	@Override
	public boolean equals(Object property){
		boolean is_equal = true;
		
		is_equal =  is_equal && this.getLabel().equals(((Property) property).getLabel());
		is_equal =  is_equal && this.getValues().equals(((Property) property).getValues());
		
		return is_equal;
	}
	
}
