package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Class GUI creates the GUI for the game
 * 
 * @author Winnie, Eoghan, Sally
 *
 */


public class GUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	/** Board that is currently used for the game*/
	private Board game;
	
	/** Main Frame of the game and the Frame that tells whether a clue is accepted*/
	private JFrame frame,clueacceptedframe;
	
	/** Frame of the Spymaster*/
	private JFrame spymasterFrame;
	
	/** Frame that will allow the players to enter in a clue and count*/
	private JFrame giveclueframe;
	
	/** Location of each codename and agent*/
	private Location[][] _locations;
	
	/** ProgressBar of the Red Team*/
	private JProgressBar progressRed;
	
	/** ProgressBar of the Blue Team*/
	private JProgressBar progressBlue;

	/** Labels that keep track of the clue, count and whos turn it is*/
	private JLabel tLabel,clueLabel,countLabel;
	
	/** Buttons that are on the 5x5 grid*/
	private LinkedList<JButton> buttons;
	
	/** Audio that are used in the game*/
	private HashMap<String,AudioPlayer> cheatcodes;
	
	/** String of cheat code*/
	private String cheatCodePhrase;
	
	/** Timer that resets cheat code*/
	private Timer phraseReset;

	/**A regular JPanel but with overridden method for easter egg :)
	 * This Panel is the panel that holds everything. */
	private OverriddenPanel _panelThatHoldsMainAndSide;
	
	/** JButton at bottom to open the spymaster mode.
	 * Also the end turn button to manually end the turn. They are here so we can enable/disable it during the game*/
	private JButton spymastermode;
	private JButton endTurnButton;
	
	
	/**
	 * Creates a new GUI that will set up the game and initialize any components being used in the game.
	 * 
	 */
	public GUI() {
		//HashMap of all the cheatcodes and their Audio Files.
		cheatcodes=new HashMap<String,AudioPlayer>();
		cheatcodes.put("grayskull", new AudioPlayer("By The Power of Grayskull.wav"));
		cheatcodes.put("help", new AudioPlayer("Ding Sound Effect.wav"));
		cheatCodePhrase="";//Stores the keyvalues entered during the timer
		
		buttons=new LinkedList<JButton>();//used to enable and disable buttons
		frame = new JFrame("CODENAME 2-Teams");
		frame.setFocusable(true);//used to gain priority for keyboard inputs
		
		//Every 6 seconds, it resets the cheatCodePhrase so that a new cheat code could be entered.
		phraseReset=new Timer(6000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Phrase is: "+cheatCodePhrase);
				cheatCodePhrase="";
				System.out.println("refresh");
			}
		});
		phraseReset.start();//starts the timer
		spymasterFrame = new JFrame("SPYMASTER MODE");
		giveclueframe = new JFrame("Give a clue");
		game=new Board();
		_locations=game.getLocations();
        clueLabel=new JLabel("Clue: ");
        countLabel=new JLabel("Count: ");
        clueacceptedframe = new JFrame("Clue accepted");
        
        //Listens for keyboard inputs.
        frame.addKeyListener(new KeywordListener(game,this,clueLabel,buttons,cheatcodes));
		initGUI(); //sets up the rest of the GUI
		
	}
	
	/**
	 * Create and runs the GUI
	 * 
	 * @param args
	 */
	public static void main(String [] args) {
		new GUI();
	}
	
	/**
	 * Sets up the main GUI of the game. Creates a File menu that restarts and ends the game.
	 * Creates ProgressBars that will update the progress of each team. Creates the 5x5 grid.
	 * 
	 */
	public void initGUI() {
		
		_panelThatHoldsMainAndSide = new OverriddenPanel(); //The "Main" Panel. Holds everything in the main frame
		_panelThatHoldsMainAndSide.setLayout(new BoxLayout(_panelThatHoldsMainAndSide, BoxLayout.X_AXIS));
		
		JPanel _mainPanel = new JPanel();
		_mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
		
		JPanel _menuPanel = new JPanel();
		_menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//MENU--------------------------------------------------------
		JMenu menu = new JMenu("File");
		
		JMenuItem two = new JMenuItem("2-Teams");
		two.addActionListener(new StartListener(this, frame, spymasterFrame, giveclueframe, 2));
		setMenuItemProperties(two);
		menu.add(two);
		
		JMenuItem three = new JMenuItem("3-Teams");
		three.addActionListener(new StartListener(this, frame, spymasterFrame, giveclueframe,3));
		setMenuItemProperties(three);
		menu.add(three);
		
		JMenuItem end = new JMenuItem("End");
		end.addActionListener(new ExitListener());
		setMenuItemProperties(end);
		menu.add(end);
		
		JMenuBar menuBar = new JMenuBar();
		setMenuProperties(menu);
		menuBar.add(menu);
		_menuPanel.add(menuBar);
		
		_mainPanel.add(_menuPanel);
		
		// Sally - TURN & PROGRESS BAR----------------------------------------------
		
        JPanel whose_turn = new JPanel();
        whose_turn.setLayout(new BoxLayout(whose_turn, BoxLayout.Y_AXIS));
        whose_turn.setAlignmentX(CENTER_ALIGNMENT); 
        String teamturn="";

        if(game.get_isRedTurn()) {teamturn="Red";}
        else teamturn="Blue";
        tLabel = new JLabel("Turn: "+teamturn);
        tLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        
        progressRed = new JProgressBar(0,9);
        progressBlue = new JProgressBar(0,8);
        progressRed.setMaximumSize(new Dimension(350, 10));
        progressRed.setStringPainted(true);
        progressRed.setBorderPainted(true);
        progressRed.setVisible(true);
        progressBlue.setVisible(false);
        progressBlue.setMaximumSize(new Dimension(350, 10));
        progressBlue.setStringPainted(true);
        progressBlue.setBorderPainted(true);
        
        int red_score = game.get_tilesRevealed().get("Red");
        progressRed.setValue(red_score); 
        int blue_score = game.get_tilesRevealed().get("Blue");
        progressBlue.setValue(blue_score); 
        
        
        whose_turn.add(tLabel);
        whose_turn.add(progressRed);
        whose_turn.add(progressBlue);

        _mainPanel.add(whose_turn);
        
		//GRID---------------------------------------------------------
        //Creates the grid in a 5x5 of buttons, and when buttons are created they have a listener attached which 
        //grabs everything that it needs to modify when a button is pressed. it also adds the newly created button to
        // a linked list of buttons.
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(5,5));
		//Adding 25 button to grid panel
		for(int r=0;r<_locations.length;r++) {
			for(int c=0;c<_locations[r].length;c++) {
				JButton b = new JButton();
				setButtonProperties(b);
				b.setText(_locations[r][c].getCodename()+" \n["+r+","+c+"]");
				b.addActionListener(new SelectListener(this,game,b,_locations[r][c],countLabel,clueacceptedframe, progressBlue, progressRed, 2));
				b.setEnabled(false);
				buttons.push(b);
				grid.add(b);
				
			}
		}
		_mainPanel.add(grid);
			
		
        //Sally - PLAYER/SPYMASTER MODE-----------------------------------------
		
        JPanel mode = new JPanel();
        mode.setLayout(new FlowLayout());
        JLabel modelabel = new JLabel("Mode: ");
        
        spymastermode = new JButton("Spymaster");
        setModeButtonProperties(spymastermode);
        spymastermode.addActionListener(new SpymasterListener(_locations, spymasterFrame, giveclueframe));
        
        mode.add(modelabel);
        mode.add(spymastermode);
        _mainPanel.add(mode);
        
        informTurn();
        giveClue();
        
        frame.add(_mainPanel);
        frame.setSize(700, 700);
        
        //Clue, Count, End Turn--------------------------------------------------
        JPanel sideDetails= new JPanel();
        sideDetails.setLayout(new BoxLayout(sideDetails, BoxLayout.Y_AXIS));
        sideDetails.setBorder(BorderFactory.createRaisedBevelBorder());
        endTurnButton=new JButton("End Turn");
        endTurnButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				game.set_isRedTurn(!game.get_isRedTurn());
				endTurn();
			}
        });

        sideDetails.add(clueLabel);
        sideDetails.add(countLabel);
        sideDetails.add(endTurnButton);           
        
        _panelThatHoldsMainAndSide.add(_mainPanel);
        _panelThatHoldsMainAndSide.add(sideDetails);
        //----------------
		frame.add(_panelThatHoldsMainAndSide);
		frame.setSize(900, 700);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.repaint();
	}
	
	/** 
	 * Changes the GUI Elements that need to be changed once a turn is over. Does not actually change the turn itself
	 * It changes the progress bars to the correct team
	 * Sets the Turn label to show to proper team
	 * Disables all the buttons
	 * 
	 * **/
	
	public void endTurn() {
		

		//here we should update UI Contents to reflect the current team.
		//System.out.println("It is "+game.get_isRedTurn()+" That it is the Reds turn");
      String teamturn="";
      if(game.get_isRedTurn()) {
        progressBlue.setVisible(false);
        progressRed.setVisible(true);
        teamturn="Red";}
      else {
        progressRed.setVisible(false);
        progressBlue.setVisible(true);
        teamturn="Blue";
      }
      tLabel.setText("Turn: "+teamturn);
     // System.out.println(teamturn);
      for(JButton bu:buttons){
          bu.setEnabled(false);
      }
      spymastermode.setEnabled(true);
      informTurn();        
    

		
	}
	//Sally - Turn popup
    public void informTurn() {
      String t = "";
      if (game.get_isRedTurn()) {
        t = "red";
      }
      else { 
        t = "blue"; 
      }
      JFrame informturnframe = new JFrame();
      JPanel informturnpanel = new JPanel();
      informturnpanel.setLayout(new BoxLayout(informturnpanel, BoxLayout.Y_AXIS));
      
      JLabel turn = new JLabel("It is " + t + " team's turn.");
      turn.setAlignmentX(CENTER_ALIGNMENT);
      JLabel spymasterprompt = new JLabel("The spymaster for the " + t + " team");
      spymasterprompt.setAlignmentX(CENTER_ALIGNMENT);
      JLabel spymasterprompt2 = new JLabel("can submit a clue using spymaster mode.");
      spymasterprompt2.setAlignmentX(CENTER_ALIGNMENT);
      JButton gotit = new JButton("Got it, thanks!");
      gotit.setAlignmentX(CENTER_ALIGNMENT);
      
      informturnpanel.add(turn);
      informturnpanel.add(spymasterprompt);
      informturnpanel.add(spymasterprompt2);
      informturnpanel.add(gotit);
      
      gotit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          informturnframe.dispose();
        }
      });
      
      informturnframe.add(informturnpanel);
      informturnframe.setAlwaysOnTop(true);
      informturnframe.setLocationRelativeTo(null);
      informturnframe.setVisible(true);
      informturnframe.setSize(new Dimension(300, 150));
    }
	
	//Sally - CLUE --------------------------------------------------------
	
	public void giveClue() {
	  JPanel cluepanel = new JPanel();
	  cluepanel.setLayout(new BoxLayout(cluepanel, BoxLayout.Y_AXIS)); 
      JLabel cluelabel = new JLabel("Clue: ");
      JLabel countlabel = new JLabel("Count: ");
      JTextField clue = new JTextField();
      JTextField count = new JTextField();
      JButton submitclue = new JButton("Submit");

      cluepanel.add(cluelabel);
      cluepanel.add(clue);
      cluepanel.add(countlabel);
      cluepanel.add(count);
      cluepanel.add(submitclue);
      
      submitclue.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	boolean turn=game.get_isRedTurn();
          boolean cluevalid = game.clue(clue.getText().toUpperCase());
          boolean countvalid = false;
          int countint =-1;
          if(count.getText()!=null){
        	  try{
        		  countint = Integer.parseInt(count.getText());
        	  }
        	  catch(Exception ex){  
        	  }
          }
          if (game.get_isRedTurn()&&(countint >= 0 && countint <= 9)) {
            countvalid = true;
          }
          else if(!game.get_isRedTurn()&&(countint >= 0 && countint <= 8)) {
              countvalid = true;
            }
          // a meme
          if (clue.getText().toUpperCase().equals("HERTZ")) {
            List<String> quotelist = new ArrayList<String>();
            quotelist.add("baby don't hertz me, don't hertz me");
            quotelist.add("serious question need help professor hertz how many hawaiian shirts do you own?");
            quotelist.add("roses are red\nviolets are blue\nunexpected '{'\non line 32");
            quotelist.add("to meme or not to meme... that is the question");
            JFrame hertzframe = new JFrame("cse116 spring '18 piazza quotes");
            JPanel hertzpanel = new JPanel();
            int rand = (int) (Math.random() * (quotelist.size()-1));
            String quote = quotelist.get(rand);
            JLabel hertzlabel = new JLabel(quote);
            hertzpanel.add(hertzlabel);
            hertzframe.add(hertzpanel);
            hertzframe.setSize(new Dimension(550, 210));
            hertzframe.setLocationRelativeTo(null);
            hertzframe.setAlwaysOnTop(true);
            hertzframe.setVisible(true);
          }
          if ((cluevalid) && (countvalid)) {
            giveclueframe.dispose();
            JFrame clueacceptedframe = new JFrame("Clue accepted");
            JPanel clueacceptedpanel = new JPanel();
            JLabel accepted = new JLabel("Clue and count accepted!");
            clueacceptedpanel.add(accepted);
            clueacceptedframe.add(clueacceptedpanel);
            clueacceptedframe.setVisible(true);
            clueacceptedframe.setAlwaysOnTop(true);
            clueacceptedframe.setSize(new Dimension(550, 210));
            spymasterFrame.setVisible(false);
        	clueLabel.setText("Clue: "+clue.getText().toUpperCase());
			game.setCount(Integer.parseInt(count.getText())+1);
			countLabel.setText("Count: "+game.getCount());
			for(JButton bu:buttons){
				bu.setEnabled(true);
			}
			spymastermode.setEnabled(false);
          }
          else if(turn!=game.get_isRedTurn()){
        	  giveclueframe.dispose();
        	  spymasterFrame.setVisible(false);
        	  endTurn();
          }
          else {
            JFrame cluerejectedframe = new JFrame("Clue rejected");
            JPanel cluerejectedpanel = new JPanel();
            JLabel rejected = new JLabel("Clue or count invalid. Please check again.");
            JLabel rejected2 = new JLabel("Clue cannot be non-revealed agent's name and can only be one word.");
            JLabel rejected3 = new JLabel("If red team count must be >=0 and <=9.");
            JLabel rejected4 = new JLabel("If blue team count must be >=0 and <=8.");
            cluerejectedpanel.add(rejected);
            cluerejectedpanel.add(rejected2);
            cluerejectedpanel.add(rejected3);
            cluerejectedpanel.add(rejected4);
            cluerejectedframe.add(cluerejectedpanel);
            cluerejectedframe.setVisible(true);
            cluerejectedframe.setAlwaysOnTop(true);
            cluerejectedframe.setSize(new Dimension(550, 210));
          }
        }
      });
      
      giveclueframe.add(cluepanel);
      giveclueframe.setAlwaysOnTop(true);
      giveclueframe.setLocationRelativeTo(null);
      //giveclueframe.setVisible(true);
      giveclueframe.setSize(new Dimension(550, 210));
      
    }    
	  
    
    //Winnie - Check is there is a winner. If there is, there will be a popup message.
    public void winnerExists() {
        if(((game.whoWonAssassinRevealed()!=null&&game.whoWonAssassinRevealed().equals("Red")) || game.isInWinningState()) && game.get_isRedTurn()) {
     	   spymastermode.setEnabled(false);
          	endTurnButton.setEnabled(false);
        	for(JButton bu:buttons){
            	bu.setEnabled(false);
            	color(bu);
            }
            JOptionPane.showMessageDialog(frame,
                    "Red Team Won",
                    "WINNER",
                    JOptionPane.PLAIN_MESSAGE);
        }else if(((game.whoWonAssassinRevealed()!=null&&game.whoWonAssassinRevealed().equals("Blue")) || game.isInWinningState()) && !game.get_isRedTurn()) {
      	   spymastermode.setEnabled(false);
         	endTurnButton.setEnabled(false);
        	for(JButton bu:buttons){
            	bu.setEnabled(false);
            	color(bu);
            }
            JOptionPane.showMessageDialog(frame,
                    "Blue Team Won",
                    "WINNER",
                    JOptionPane.PLAIN_MESSAGE);
       }
        
    }
    public void color(JButton b) {
		Timer t = new Timer(20, new ActionListener() {
			Random r = new Random();
			@Override
			public void actionPerformed(ActionEvent e) {
				b.setBackground(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
			}
		});
		t.start();
    }
    
    
    /**  
     * Used to set the cheatcodephrase
     * used to update the current cheatcodephrase with the 
     * 
     * 
     * **/
    public void setCheatCodePhrase(String a){
    	cheatCodePhrase=a;
    }
    public String getCheatCodePhrase(){
    	return cheatCodePhrase;
    }
    
    //PROPERTIES---------------------------------------------------------------
  	
	// Sally - Button properties of mode buttons
	public void setModeButtonProperties(JButton button) {
	  button.setFont(new Font("Helvetica", Font.PLAIN, 16));
	}

	public void setButtonProperties(JButton button) {
		button.setFont(new Font("Helvetica", Font.BOLD, 18));
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);
		button.setPreferredSize(new Dimension(100,100));
		button.setOpaque(true);
		button.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.LIGHT_GRAY));
	}
	public void setLabelProperties(JLabel label) {
		label.setFont(new Font("Courier", Font.BOLD, 44));
		label.setBackground(Color.WHITE);
		label.setForeground(Color.BLACK);
		label.setOpaque(true);
		label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.LIGHT_GRAY));
	}
	public void setMenuProperties(JMenu menu) {
		menu.setFont(new Font("Courier", Font.BOLD, 44));
		menu.setBackground(Color.WHITE);
		menu.setForeground(Color.BLACK);
		menu.setOpaque(true);
		menu.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.LIGHT_GRAY));
	}
	public void setMenuItemProperties(JMenuItem menu) {
		menu.setFont(new Font("Courier", Font.BOLD, 44));
		menu.setBackground(Color.WHITE);
		menu.setForeground(Color.BLACK);
		menu.setOpaque(true);
		menu.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.LIGHT_GRAY));
	}
}
