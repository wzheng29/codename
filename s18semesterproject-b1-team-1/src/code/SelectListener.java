package code;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Class SelectListener is applied to each of the buttons on the 5x5 grid. The Listener changes the color 
 * of the buttons once it is clicked. It also keeps track of who is winning the game and which teams turn it is.
 * 
 * @author winnie
 *
 */
public class SelectListener implements ActionListener{
	
	/** GUI that is currently used by the game*/
	private GUI gui;
	
	/** GUI_3Player that is currently used by the game*/
	private GUI_3Player gui3;
	
	/** Board that is currently used by the game*/
	private Board game;
	
	/** Board that is currently used by the game8*/
	private Board3Player game3;
	
	/** Button that is being clicked*/
	private JButton button;
	
	/** Location that is at the button*/
	private Location loc;
	
	/** Label that keeps trach of the count*/
	private JLabel count;
	
	/** Frame that tells whether a clue is valid*/
	private JFrame cluevalidframe;
	
	/** Play audio when assassin is found*/
	private AudioPlayer assassin;
	
	/** ProgressBar for Blue team*/
	private JProgressBar progressBlue;
	
	/** ProgressBar for Red team*/
	private JProgressBar progressRed;
	
	/** ProgressBar for Green team*/
	private JProgressBar progressGreen;
	
	/** Number of teams playing*/
	private int team;
	
	/**A regular JPanel but with overridden method for easter egg :)
	 * This Panel is the panel that holds everything. */
	private OverriddenPanel _panelThatHoldsMainAndSide;
	
	/**
	 * Creates a new SelectListener with all the components that will be altered with the button is pressed.
	 * 
	 * @param gui	GUI that is currently used by the game
	 * @param g		Board that is currently used by the game
	 * @param b		Button that is being clicked
	 * @param a		Location that is at that button
	 * @param countL	Label that keeps track of the count
	 * @param cluevalid		Frame that tells whether a clue is valid
	 * @param pb	ProgressBar that keeps track of how close the Blue team is to winning
	 * @param pr	PrograssBar that keeps track of how close the Red team is to winning
	 */
	public SelectListener(GUI gui,Board g,JButton b,Location a,JLabel countL,JFrame cluevalid, JProgressBar pb, JProgressBar pr,int team) {
		this.gui=gui;
		game=g;
		button = b;
		loc=a;
		count=countL;
		cluevalidframe=cluevalid;
		progressBlue = pb;
		progressRed = pr;
		this.team = team;
	}
	
	/**
	 * Creates a new SelectListener with all the components that will be altered with the button is pressed.
	 * 
	 * @param gui3	GUI_3Player that is currently used by the game
	 * @param g		Board3Player that is currently used by the game
	 * @param b		Button that is being clicked
	 * @param a		Location that is at that button
	 * @param countL	Label that keeps track of the count
	 * @param cluevalid		Frame that tells whether a clue is valid
	 * @param pb	ProgressBar that keeps track of how close the Blue team is to winning
	 * @param pr	PrograssBar that keeps track of how close the Red team is to winning
	 */
	public SelectListener(GUI_3Player gui3, Board3Player g,JButton b,Location a,JLabel countL,JFrame cluevalid, JProgressBar pb, JProgressBar pr,JProgressBar pg, int team,OverriddenPanel p) {
		this.gui3 = gui3;
		game3=g;
		button = b;
		loc=a;
		count=countL;
		cluevalidframe=cluevalid;
		progressBlue = pb;
		progressRed = pr;
		progressGreen = pg;
		this.team = team;
		_panelThatHoldsMainAndSide=p;
	}
	
	/**
	 * For all the buttons on the 5x5 grid, a random codename is assigned with its given agent.
	 * The color of the button will change once it is pressed. It will also check if each team is 
	 * in a winning state. Lastly, it will update the progress bar as needed.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(team == 2) {
			cluevalidframe.dispose();
			game.TileSelected(loc);
			count.setText("Count: "+game.getCount());
	
			if(loc.getPerson().getType().equals("Red")) {
				button.setBackground(Color.RED);
			}
			else if(loc.getPerson().getType().equals("Blue")) {
				button.setBackground(Color.BLUE);
			}
			else if(loc.getPerson().getType().equals("Assassin")) {
				button.setBackground(Color.BLACK);
				button.setForeground(Color.RED);
				assassin=new AudioPlayer("Gta 5 Death.wav");
				assassin.play();
			}
			else if(loc.getPerson().getType().equals("Innocent")) {
				button.setBackground(new java.awt.Color(210, 180, 140));
				assassin=new AudioPlayer("myLeg.wav");
				assassin.play();
			}
	
			button.setText(loc.getCodename());
			if(game.isInWinningState()) {
				gui.winnerExists();	
			}
			else if(game.getCount()==0){
				gui.endTurn();
			}
			
			
	        int red_score = game.get_tilesRevealed().get("Red");
	        progressRed.setValue(red_score); 
	        int blue_score = game.get_tilesRevealed().get("Blue");
	        progressBlue.setValue(blue_score); 
		}
		else if(team == 3) {
			cluevalidframe.dispose();
			game3.TileSelected(loc);
			count.setText("Count: "+game3.getCount());
	
			if(loc.getPerson().getType().equals("Red")) {
				button.setBackground(Color.RED);
			}
			else if(loc.getPerson().getType().equals("Blue")) {
				button.setBackground(Color.BLUE);
			}
			else if(loc.getPerson().getType().equals("Green")) {
				button.setBackground(Color.GREEN);
			}
			else if(loc.getPerson().getType().equals("Assassin")) {
				button.setBackground(Color.BLACK);
				button.setForeground(Color.RED);
				if(game3.get_tilesRevealed().get("Assassin")!=2){
				_panelThatHoldsMainAndSide.startTimer();
				}
			}
			else if(loc.getPerson().getType().equals("Innocent")) {
				button.setBackground(new java.awt.Color(210, 180, 140));
				assassin=new AudioPlayer("myLeg.wav");
				assassin.play();
			}
	
			button.setText(loc.getCodename());
			if(game3.isInWinningState()) {
				gui3.winnerExists();	
			}
			else if(game3.getCount()==0){
				gui3.endTurn();
			}
			
			
	        int red_score = game3.get_tilesRevealed().get("Red");
	        progressRed.setValue(red_score); 
	        int blue_score = game3.get_tilesRevealed().get("Blue");
	        progressBlue.setValue(blue_score); 
	        int green_score = game3.get_tilesRevealed().get("Green");
	        progressGreen.setValue(green_score);
		}
	}

}
