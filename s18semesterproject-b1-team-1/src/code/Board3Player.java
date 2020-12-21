package code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Class Board is used to set up the game board. It is where all the characters and
 * variables in the game are initialized and used.
 * 
 * @author winnie
 *
 */
public class Board3Player {

	/** Holds all the words from a text file */
	private List<String> words;
	
	/** Holds 25 codenames used in the game */
	private List<String> codenames;
	
	/** Grid that holds all 25 locations of the game */
	private Location[][] _locations;
	
	/** Keeps track of whose turn it is */
	//private boolean _isRedTurn; no longer needed in 3p version
	private List<String> _teams;
	private String _turn;
	
	/** Number of codenames related to the clue given by the Spymaster */
	private int _count;
	
	/** Keeps track of the score of each team and which tiles are revealed on the Board */
	private HashMap<String, Integer> _tilesRevealed;
	
	/** Contains all 25 random agents.*/
	private ArrayList<Person> _agents;
	
	
	/**
	 * Creates a new Board. 
	 * words and codenames are initialized. 
	 * _locations is initialized to a 5 by 5 grid.
	 * _agents is initialized to an ArrayList of Persons
	 * _isRedTurn is assigned to true.
	 * _count initialized to zero
	 * _tileRevealed initialized and set the scores to 0.
	 * init() is called to create a list of 25 agents.
	 */
	public Board3Player() {
        words = new ArrayList<String>();
        codenames = new ArrayList<String>();
        _locations = new Location[5][5];
        _agents = new ArrayList<Person>();
        _teams = new ArrayList<String>();
        init();//After initializing the _agents arraylist, we called init() to add in the 25 agents & _teams
        createLocations();
        //_isRedTurn=true;
        _count = 0;
        _turn = "Red";
        _tilesRevealed=new HashMap<String, Integer>();
        _tilesRevealed.put("Red", 0);
        _tilesRevealed.put("Blue", 0);
        _tilesRevealed.put("Green", 0);
        _tilesRevealed.put("Innocent", 0);
        _tilesRevealed.put("Assassin", 0);
	}
	
	/**
	 * Generates a list of Agents (not random).
	 * 
	 */
	public void init(){
		
		_teams.add("Red");
		_teams.add("Blue");
		_teams.add("Green");
		
		
		for(int red=0; red<6; red++) {
			_agents.add(new Person("Red"));
		}
		for(int blue=0; blue<5; blue++) {
			_agents.add(new Person("Blue"));
		}
		for(int green=0; green<5; green++) {
			_agents.add(new Person("Green"));
		}	
		for(int inn=0; inn<7; inn++) {
			_agents.add(new Person("Innocent"));
		}
		
		_agents.add(new Person ("Assassin"));
	    _agents.add(new Person ("Assassin"));


	}
	
	/**
	 * Reads a given text file containing a list of possible codenames. Each word(line) is added
	 * into words.
	 * 
	 * @param filename	Path of the file that contains a list of possible codenames. 
	 */
	public void read(String filename) {
		
		try{
			words = new ArrayList<String>();
            for(String line : Files.readAllLines(Paths.get(filename))){
                words.add(line);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
	}
	
	/**
	 * Generates a list of 25 random codenames from a list of possible codenames(words).
	 * 
	 * @param filename	Path of the file that contains a list of possible codenames.
	 * @author Winnie, Sally
	 */
	public void codename(String filename){
		read(filename);
		Collections.shuffle(words);//shuffle the List of words
		for(int i=0; i<25; i++) {
		    String word = words.get(i);
		    word = dealwithdupes(word);
			codenames.add(word);//adding 25 words into the List
		}
	}
	
	/**
	 * Adds only non-duplicated words from shuffled word list into codenames list
	 * 
	 * @param w String word from words list
	 * @return word that is not in the codenames list
	 * @author Sally Leung
	 */
	private String dealwithdupes(String w) {
	  if (codenames.contains(w) == false) {
	    return w;
	  }
	  else {
	    int idx = (int) ((Math.random() * (words.size() - 1) - 25)) + 24;
        String w1 = words.get(idx); //random word from index (24 to words.size() - 1)
        return dealwithdupes(w1); // make sure this random word isn't a dupe too
	  }
	}

	
	/**
	 * Shuffles a list of 25 characters(agents) with different roles in the game.
	 * 
	 * @return A List of Person instances
	 */
	public ArrayList<Person> shuffle_Agents() {				
		Collections.shuffle(_agents);
		return _agents;
	}
	
	/**
	 * Assigns the grid(_locations) with a random codename and a random Person(agent/character).
	 * 
	 */
	public void createLocations(){
		int a = 0; //used to grab variables 0-24 in both codenames and _agents as we continue through the grid
		codename("GameWords.txt");
		//System.out.println("Codenames :" + codenames);
		//System.out.println("Agents: "+_agents);
		//System.out.println(codenames.size());
		//System.out.println(_agents.size());
		//Shuffles the list of 25 agents.
		shuffle_Agents();
	
		//Fills in each box on the 'grid' with a location containing a random codename and random agent
		for(int i=0;i<5;i++) {
			for(int j=0; j<5; j++) {
				_locations[i][j] = new Location(codenames.get(a), _agents.get(a));
				a++;
				//System.out.println(a);
			}
		}
		
	}
	
	/**
	 * Verifies if the clue given by the Spymaster is legal or illegal. If the clue is equal to a codename that is
	 * not revealed, it is an illegal clue and the team forfeits their turn. If the clue is more than one word, it
	 * is an illegel clue. If the clue is equal to a codename that is already revealed, the clue is legal and the
	 * team continues their turn. If the clue does not equal to any of the codenames, the clue is legal and the team continues their turn.
	 * 
	 * @param hint	String that contains the clue
	 * @return True if the clue is legal. False if the clue is illegal
	 */
	public boolean clue(String hint) {
		HashMap<String,Boolean> p = new HashMap<>();
		//fills a HashMap with the codename paired with its visibility(if the codename is revealed or not)
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				Location l = _locations[i][j];
				p.put(l.getCodename(), l.getVisibility());
			}
		}
		
		//if the clue is a codename and it is revealed then it is a legal clue
		if(p.containsKey(hint) && p.get(hint)) {
			return true;
		}
		//if the clue is a codename but it is not revealed then it is an illegal clue
		else if(p.containsKey(hint) && !(p.get(hint))) {
			nextTurn();
			return false;
		}
		//if the clue is more than one word
		else if(hint.contains(" ")) {
			//_isRedTurn = !_isRedTurn;//forfeit turn
			return false;
		}
		//if the clue is not equal to codename it is a legal clue
		else {
			return true;
		}
		
	}
	/**
	 * When a Tile is Selected this method searches for the given Location on the Board.
	 * Before comparing the given Tile, it checks that the @param is not null. 
	 * After it finds the Tile it checks that the tile hasn't been already revealed
	 * If it isn't revealed, it completes the following steps: It sets the Visibiltiy to true and 
	 * decrements the count and changes the current turn if the count==0. Finally it updates _tilesRevealed.
	 * 
	 * @author Eoghan McCarroll, Sally Leung
	 * @param Location passed in, to be found on board
	 * @return True if the Tile contains current teams agent. False otherwise.
	 */
	public boolean TileSelected(Location t1) {
		for(int r=0;r<getLocations().length;r++) {
			for(int c=0;c<getLocations()[r].length;c++) {
			  Location boardtile = this.getLocations()[r][c];
				if(boardtile !=null && boardtile.equals(t1) && boardtile.getVisibility()==false) {
				  boardtile.setVisible(true);
				  if(boardtile.getPerson().getType().equals("Red")) {
				    _tilesRevealed.put("Red", _tilesRevealed.get("Red")+1);
				    if(_turn.equals("Red")) {
				      setCount(getCount()-1);
				      if(getCount()==0){nextTurn();}
				      return true; //was current teams agent
				      }
				    else {
				      setCount(0);
				      //set_isRedTurn(true);// old way, going to have a problem prob due to select listener
				      nextTurn();
				      return false; //was not our agent
				      }
					}
				  else if(boardtile.getPerson().getType().equals("Blue")) {
						_tilesRevealed.put("Blue", _tilesRevealed.get("Blue")+1);
						if(_turn.equals("Blue")) {
							setCount(getCount()-1);
							if(getCount()==0){nextTurn();} //4-11-18 Had to add this line to fix functionality-Eoghan
							return true; //was our agent
						}
						else {
							setCount(0);
							nextTurn();
							//System.out.println(_turn);
							return false; // was not our agent
						}
					}
				  else if(boardtile.getPerson().getType().equals("Green")) {
						_tilesRevealed.put("Green", _tilesRevealed.get("Green")+1);
						if(_turn.equals("Green")) {
							setCount(getCount()-1);
							if(getCount()==0){nextTurn();} //4-11-18 Had to add this line to fix functionality-Eoghan
							return true; //was our agent
						}
						else {
							setCount(0);
							nextTurn();
							return false; // was not our agent
						}
					}
				  else if(boardtile.getPerson().getType().equals("Innocent")) {
						_tilesRevealed.put("Innocent", _tilesRevealed.get("Innocent")+1);
						setCount(0);
						nextTurn();
						return false; //was not our agent
					}
				  else if(boardtile.getPerson().getType().equals("Assassin")) {
						_tilesRevealed.put("Assassin", _tilesRevealed.get("Assassin")+1);
						setCount(0);
						///System.out.println(_turn);
	                    int idx=_teams.indexOf(_turn);
	                    ///System.out.println("Index of turn :" + idx);
	                    nextTurn();//Added Line 4-13-18 to fix when game is over**Updated 4-26-18
	                    _teams.remove(idx);
	                    ///System.out.println(_teams.remove(idx));//Removes the team that found the assassin from the group of teams.
						//if(idx==0) {
						//	_turn=_teams.get(_teams.size()-1);
						//}
						//else {_turn=_teams.get(idx-1);}
	                    //nextTurn();//Added Line 4-13-18 to fix when game is over**Updated 4-26-18

						return false; //was not our agent
					}
					
				}
				
			}
		}
		return false; //was not our agent
	}
	/**isInWinningState() checks if there is a potential winner this turn. 
	 * It checks if red, blue, Green or assassin is max'd, then return true.
	 * Else Return False.
	 * 
	 * @author Eoghan McCarroll, Sally Leung
	 * @return True if either Red, Blue, Green, or Assassin reached their Winning Value. False otherwise.
	 */
	public boolean isInWinningState() {
		int redscore = _tilesRevealed.get("Red");
		int bluescore = _tilesRevealed.get("Blue");
		int greenscore= _tilesRevealed.get("Green");
		int assassin = _tilesRevealed.get("Assassin");
		if (redscore == 6) {
		  return true; //red reached their win state
		}
		else if (bluescore == 5) {
		  return true; //blue reached their win state
		}
		else if (greenscore == 5) {
			  return true; //blue reached their win state
			}
		else if (assassin == 2) {
		  return true; //The Assassins have been revealed so their is a win state
		}
		return false; //There is not currently a win state
	}

	/**In the case that the Assassin's Location was revealed,
	 * the method returns a String of the team who won.
	 * If no one won, return null.
	 * 
	 * @author Eoghan McCarroll
	 * @return The name of the team who won if the Assassin was revealed. If no team won, return null.
	 */
	
	public String whoWonAssassinRevealed() {
		if(_tilesRevealed.get("Assassin")==2) {
			return _teams.get(0); //returns the only team still able to play;
		}
		return null; //The assassin hasnt been revealed yet so null
	}
	
	/**When this is called, it sets the _turn variable equal to the next team in the order specified
	 * If it reaches the last index of the list, it goes back to the front.
	 * 
	 * @author Eoghan McCarroll
	 * @return The name of the team who won if the Assassin was revealed. If no team won, return null.
	 */	
	public void nextTurn(){
		int i=_teams.indexOf(_turn);
		if(i==_teams.size()-1) {
			_turn=_teams.get(0);
		}
		else {
			_turn=_teams.get(i+1);
		}
		
	}
	
	/*
	
	 * Changes the value of _isRedTurn to the value passed in
	 * 
	 * @param value	new boolean value for _isRedTurn

	public void set_isRedTurn(boolean value) {
		_isRedTurn=value;
	}

	
	 * Getter method of _isRedTurn
	 * 
	 * @return	boolean value of _isRedTurn();
	 
	public boolean get_isRedTurn() {
		return _isRedTurn;	
	}
	*/
	
	/**
	 * getter method of _turn
	 * @return string value of _turn
	 */
	public String getTurn() {
	  return _turn;
	}
	
	/**
	 * setter method of _turn
	 * @param String t
	 */
	public void setTurn(String t) {
	      _turn = t;
	    }
	
	/**
	 * Getter method of get_tilesRevealed
	 * 
	 * @return	HashMap of tiles (codename) and its visibility
	 */
	public HashMap<String, Integer> get_tilesRevealed(){
		return _tilesRevealed;
	}
		
	/**
	 * Sets _tilesRevealed to the new value that is passed in.
	 * 
	 * @param map	Updated HashMap of the score
	 */
	public void set_tilesRevealed(HashMap<String,Integer> map) {
		_tilesRevealed = map;
	}
	
	/**
	 * Getter method for get_agents
	 * 
	 * @return	ArrayList containing the agents
	 */
	public ArrayList<Person> get_agents(){
		return _agents;
	}
	
	/**
	 * Getter method for words
	 * 
	 * @return ArrayList of possible words to be used as codenames.
	 */
	public ArrayList<String> get_words(){
		return (ArrayList<String>) words;
	}
	
	/**
	 * Sets count to new value that is passed in
	 * 
	 * @param i	new int value of count
	 */
	public void setCount(int i) {
		_count=i;
		
	}
	
	/**
	 * Getter method for _count
	 * 
	 * @return	int value of count
	 */
	public int getCount() {
		
		return _count;
	}
	
	/**
	 * Getter method of codenames.
	 * 
	 * @return	List of Strings containing the 25 randomly generated codenames
	 */
	public List<String> getCodenames(){
		return codenames;
	}
	
	/**
	 * Setter methods of codenames.
	 * 
	 * @param newCodename
	 */
	public void setCodenames(List<String> newCodename) {
		this.codenames = newCodename;
	}
	
	/**
	 * Getter method of _locations.
	 * 
	 * @return	Location 2D array filled with 25 locations
	 */
	public Location[][] getLocations(){
		return _locations;
	}
	/**
	 * Setter method of _locations.
	 * 
	 * @param Location 2D array
	 */
	public void setLocations(Location[][] a){
		_locations=a;
	}
	public Location getLocation(String codename) {
		Location a=null;
		
		for(int i=0;i<_locations.length;i++) {
			for(int j=0;j<_locations[i].length;j++) {
				if(_locations[i][j].getCodename().equals(codename)) {
					a= _locations[i][j];
				}
			}
		}
		//comment
		
		return a;
	}
}
