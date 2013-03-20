package graph;

import java.util.ArrayList;

/**
 * Properties are linked to one arc.
 * A property contain informations between 2 nodes;
 * 
 * @author Hadrien
 * @version 0.1
 */
public class Property {
	
	protected String label;
	protected ArrayList<String> values;
	
	public Property(String label, ArrayList<String> values) {
		this.label = label;
		this.values = values;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public ArrayList<String> getValues() {
		return values;
	}
	
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
	
	public boolean equals(Object property){
		return this.getLabel().equals(((Property) property).getLabel()) && this.getValues().equals(((Property) property).getValues());
	}
}
