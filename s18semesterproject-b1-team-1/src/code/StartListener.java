package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * Class StartListener is applied to the "Start" menu item in File menu. When the "Start" menu
 * item is selected, the visibility of the frames in GUI is set to false and a new game starts.
 * 
 * @author winnie
 *
 */
public class StartListener implements ActionListener{

	/**GUI currently used for the 2-Team game */
	private GUI gui;
	
	/**GUI currently used for the 3-Team game*/
	private GUI_3Player gui3;
	
	/**Main frame of the GUI */
	private JFrame frame;
	
	/**Spymaster mode's frame */
	private JFrame spymasterFrame;
	
	/**Frame where you provide the clue and count */
	private JFrame clueFrame;
	
	/**Number of teams that are currently playing */
	private int team;
	
	/**
	 * Creates a new StartListener in the 2-Team mode.
	 * 
	 * @param gui	GUI currently used for the game
	 * @param frame		Main JFrame of the game
	 * @param spymasterFrame	JFrame of the Spymaster
	 * @param clueFrame		JFrame of the clue and count
	 * @param team		Number of teams that are currently playing
	 */
	public StartListener(GUI gui, JFrame frame, JFrame spymasterFrame, JFrame clueFrame, int team) {
		this.gui = gui;
		this.frame = frame;
		this.spymasterFrame = spymasterFrame;
		this.clueFrame = clueFrame;
		this.team = team;
	}
	
	/**
	 * Creates a new StartListener in the 3-Team mode.
	 * 
	 * @param gui3	GUI_3Player currently used for the game
	 * @param frame		Main JFrame of the game
	 * @param spymasterFrame	JFrame of the Spymaster
	 * @param clueFrame		JFrame of the clue and count
	 * @param team		Number of teams that are currently playing
	 */
	public StartListener(GUI_3Player gui3, JFrame frame, JFrame spymasterFrame, JFrame clueFrame, int team) {
		this.gui3 = gui3;
		this.frame = frame;
		this.spymasterFrame = spymasterFrame;
		this.clueFrame = clueFrame;
		this.team = team;
	}
	
	/**
	 * Sets all the visibility of the frames to false. Then restarts the game of your choosing.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(team == 2) {
			frame.setVisible(false);
			spymasterFrame.setVisible(false);
			clueFrame.setVisible(false);
			gui.main(null);
		}
		else if(team == 3) {
			frame.setVisible(false);
			spymasterFrame.setVisible(false);
			clueFrame.setVisible(false);
			gui3.main(null);
		}
	}

}
