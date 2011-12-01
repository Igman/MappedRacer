package Beans;

/**
 * Class for the item object. TODO: Is this the best way to pass an object
 * as an array and have it mapped to the model class...?
 */
public class ItemObj {
	private String location;
	private int type;
	private int value;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}