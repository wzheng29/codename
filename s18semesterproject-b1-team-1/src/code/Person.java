package code;

/**
 * Instances of this class are used to represent a character role in the game. It 
 * store the roles (Red Agent, Blue Agent, Bystanders, and Assassin) of each character
 * in the game.
 * 
 * @author winnie
 *
 */

public class Person {

	/**Type of role the character has*/
	private String _role;
	
	/**
	 * Creates a new Person with an assigned role
	 * @param type	The role assigned to each Person that is being created
	 */
	public Person(String type) {
		_role=type;
		
	}
	
	/**
	 * Gets the role of the Person.
	 * @return	String containing the Person's role
	 */
	public String getType() {
		//temp method. Unsure if we would like to keep later for implementation.
		return _role;
	}
	
}

