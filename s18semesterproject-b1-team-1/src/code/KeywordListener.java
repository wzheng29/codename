package code;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;

public class KeywordListener implements KeyListener{

	private HashMap<String,AudioPlayer> cheatMap;
	private LinkedList<JButton> buttons;
	private Board game;
	private GUI gui;
	private Location[][] loc;
	private JLabel count;
	/**
	 * *****These Cheats currently only work when the code is initially ran. They fail to work once
	 * The Main JFrame isFocusable()==false. This happens when new frames pop up or people enter characters into
	 * the clue and count. Due to lack of time this functionality is not fixed, but Could be changed to work if a special key
	 * was used then this could grab the later corresponding characters. I wanted this to work though without any special key required
	 * 
	 * Constructor for listener that listens for keyboard inputs while the JFrame is clicked.
	 * It passes in all the parts of the project that are used when a cheatcode is used.
	 * CheatMap is the map of cheats and their respective sound clips
	 * buttons is all the buttons on the board.
	 * 
	 * @author Eoghan McCarroll
	 * 
	 * @param Board g: Used to access the main code of the project
	 * @param GUI gu: used to access the GUI elements of the project
	 * @param JLabel c: Used to access the count label directly
	 * @param LinkedList<JButton> b: Used in the "redrum" cheat to set buttons color to red and change text
	 * @param HashMap<String,AudioPlayer> cheats: Stores the song files for the cheatcodes
	 * **/
	public KeywordListener(Board g,GUI gu,JLabel c,LinkedList<JButton> b,HashMap<String,AudioPlayer> cheats){

		cheatMap=cheats;
		buttons=b;
		game=g;
		gui=gu;
		count=c;
		loc=game.getLocations();
		
	}
	/*public KeywordListener(Board3Player g,GUI_3Player gu,JLabel c,LinkedList<JButton> b,HashMap<String,AudioPlayer> cheats){

		cheatMap=cheats;
		buttons=b;
		game=g;
		gui=gu;
		count=c;
		loc=game.getLocations();
		
	}*/
	/**
	 * Not Used
	 * 
	 * **/
	@Override
	public void keyPressed(KeyEvent arg0) {

		
	}
	/**
	 * Not Used
	 * 
	 * **/

	@Override
	public void keyReleased(KeyEvent arg0) {

		
	}

	/**
	 * When a key is typed it checks the phrase which the typed key is added to, and if that phrase is equal to one of the 
	 * cheat codes in the cheatmap, then it will perform the cheat. 
	 * "grayskull" plays the grayskull video
	 * "redrum" sets the entire board to redrum and makes the game stuck like that
	 * "help" reveals a friendly tile and dosent decrement the count. its for free
	 * 
	 * @param KeyEvent arg0: The value passed in when a key is typed
	 * **/
	
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		gui.setCheatCodePhrase(gui.getCheatCodePhrase()+arg0.getKeyChar());
		System.out.println("Phrase at Each Step: "+gui.getCheatCodePhrase());
		for(String word:cheatMap.keySet()){
			if(gui.getCheatCodePhrase().equals(word)){
				if(word.equals("grayskull")){
					try {
						Desktop.getDesktop().open(new File(getClass().getResource("/hertz.mp4").getPath()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
				cheatMap.get(word).play();
				}

				if(word.equals("redrum")){
					for(JButton b:buttons){
						b.setText("RED RUM");
						b.setBackground(Color.RED);
					}
					//phrase="";
				}
				if(word.equals("help")){
					for(JButton b:buttons){
						int idx=buttons.indexOf(b);
						int x=0;
						int y=0;
						if(idx<=4){
							y=0;
							x=idx;
						}
						else if(idx<=9){
							y=1;
							x=idx-5;
						}
						else if(idx<=14){
							y=2;
							x=idx-10;
						}
						else if(idx<=19){
							y=3;
							x=idx-15;
						}
						else if(idx<=24){
							y=4;
							x=idx-20;
						}
						if((!loc[y][x].getVisibility())&&loc[y][x].getPerson().getType()=="Red"&&game.get_isRedTurn()){
							b.setBackground(Color.RED);
							game.TileSelected(loc[y][x]);
							b.setText(loc[y][x].getCodename());
							game.setCount(game.getCount()+1);
							count.setText("Count: "+game.getCount());
							if(game.isInWinningState()) {
								gui.winnerExists();	
							}

							break;
						}
						else if((!loc[y][x].getVisibility())&&loc[y][x].getPerson().getType()=="Blue"&&(!game.get_isRedTurn())){
							b.setBackground(Color.BLUE);
							game.TileSelected(loc[y][x]);
							b.setText(loc[y][x].getCodename());
							game.setCount(game.getCount()+1);
							count.setText("Count: "+game.getCount());
							if(game.isInWinningState()) {
								gui.winnerExists();	
							}
							break;
						}
					}
				}
			}
		}
		
		
	}
	
	
	


}
