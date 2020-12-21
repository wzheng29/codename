package code;

/**
 * Instances of the class represents a Location on the game grid. It keeps track of
 * the codename that belongs in each area, if the codename is revealed, and what agent/
 * character is assigned to that area.
 * 
 * @author winnie
 *
 */

public class Location {

	/**The codename that assigned to this location */
	private String _codename;
	
	/**The visibility of the codename (if the codename is revealed or not) */
	private boolean _isVisible;
	
	/**The Person that is assigned to this location */
	private Person _person;
	
	
	/**
	 * Creates a new Location with a specific codename and person.
	 * 
	 * @param codename	String that contains the codename assigned to the location.
	 * @param p		Person that is assigned to the location.
	 */
	public Location(String codename,Person p) {
		_codename=codename;
		_person=p;
		_isVisible=false;
		
	}
	
	/**
	 * Gets the codename at the location
	 * 
	 * @return	String containing the codename.
	 */
	public String getCodename() {
		return _codename;
		
	}
	
	/**
	 * Gets the Person at the location
	 * 
	 * @return	Person at the location.
	 */
	public Person getPerson() {
		return _person;
	}
	
	/**
	 * Gets the visibility of the codename at the location.
	 * 
	 * @return	boolean True if the codename is revealed and False when it is not revealed.
	 */
	public boolean getVisibility() {
		return _isVisible;
	}
	
	/**
	 * Sets visibility of the codename to the new value passed in.
	 * 
	 * @param value	 boolean If the codename is revealed or not.
	 */
	public void setVisible(boolean value) {
		_isVisible=value;
	}
	
	
}
