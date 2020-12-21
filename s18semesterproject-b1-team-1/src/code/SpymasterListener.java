package code;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Class SpymasterListener is applied to the button called "Spymaster" in the GUI. When the button
 * is pressed, the Spymaster version of the game and the Clue/ Count box if displayed.
 * 
 * @author winnie
 *
 */
public class SpymasterListener implements ActionListener{

	/**25 Locations and its positions on the Board */
	private Location[][] loc;
	
	/**Spymaster's frame that will reveal all the codenames and its Agents */
	private JFrame newFrame;
	
	/**Clue/ Count frame that allows the players to enter in a clue and count */
	private JFrame clueFrame;
	
	/**Number of teams in the game*/
	private int team;
	
	/**
	 * Creates a SpymasterListener.
	 * 
	 * @param loc	25 Locations that are currently being used in the game.
	 * @param newFrame	Spymaster frame that will display once the button is clicked.
	 * @param clueFrame		Frame that allow players to enter in a clue and count.	
	 */
	public SpymasterListener(Location[][] loc, JFrame newFrame, JFrame clueFrame) {
		this.loc = loc;
		this.newFrame = newFrame;
		this.clueFrame = clueFrame;
		this.team = 2;
	}
	
	/**
	 * Creates a SpymasterListener for the 3-Team version.
	 * 
	 * @param loc	25 Locations that are currently being used in the game.
	 * @param newFrame	Spymaster frame that will display once the button is clicked
	 * @param clueFrame		Frame that allow players to enter in a clue and count
	 * @param team		Number of teams that are currently in the game
	 */
	public SpymasterListener(Location[][] loc, JFrame newFrame, JFrame clueFrame, int team) {
		this.loc = loc;
		this.newFrame = newFrame;
		this.clueFrame = clueFrame;
		this.team = team;
	}
	
	/**
	 * Adds the 5x5 panels in the Spymaster frame and displays all the Codenames and Agents with
	 * its corresponding colors. Also displays the Clue/Count frame once the button is clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JPanel main = new JPanel();
		main.setLayout(new GridLayout(5,5));
		if(team == 2) {
			for(int r=0;r<loc.length;r++) {
				for(int c=0;c<loc[r].length;c++) {
					JLabel l = new JLabel();
					l.setHorizontalAlignment(SwingConstants.CENTER);
					setLabelProperties(l);
					l.setText(loc[r][c].getCodename());
					if(loc[r][c].getPerson().getType().equals("Red")) {
						l.setBackground(Color.RED);
					}
					else if(loc[r][c].getPerson().getType().equals("Blue")) {
						l.setBackground(Color.BLUE);
					}
					else if(loc[r][c].getPerson().getType().equals("Assassin")) {
						l.setBackground(Color.BLACK);
						l.setForeground(Color.RED);
					}
					else if(loc[r][c].getPerson().getType().equals("Innocent")) {
						l.setBackground(new java.awt.Color(210, 180, 140));
					}
					main.add(l);
					
				}
			}
		}
		else if(team == 3) {
			for(int r=0;r<loc.length;r++) {
				for(int c=0;c<loc[r].length;c++) {
					JLabel l = new JLabel();
					l.setHorizontalAlignment(SwingConstants.CENTER);
					setLabelProperties(l);
					l.setText(loc[r][c].getCodename());
					if(loc[r][c].getPerson().getType().equals("Red")) {
						l.setBackground(Color.RED);
					}
					else if(loc[r][c].getPerson().getType().equals("Blue")) {
						l.setBackground(Color.BLUE);
					}
					else if(loc[r][c].getPerson().getType().equals("Green")) {
						l.setBackground(Color.GREEN);
					}
					else if(loc[r][c].getPerson().getType().equals("Assassin")) {
						l.setBackground(Color.BLACK);
						l.setForeground(Color.RED);
					}
					else if(loc[r][c].getPerson().getType().equals("Innocent")) {
						l.setBackground(new java.awt.Color(210, 180, 140));
					}
					main.add(l);
					
				}
			}
		}
		newFrame.add(main);
		newFrame.setVisible(true);
		clueFrame.setVisible(true);
		clueFrame.setLocation(50,150);
		newFrame.setSize(550, 550);
		newFrame.setLocation(50, 300);
		newFrame.repaint();
	}
	
	/**
	 * Styles the 25 JLabels created for the Spymaster frame.
	 * 
	 * @param label	JLabel that is being styled
	 */
	public void setLabelProperties(JLabel label) {
		label.setFont(new Font("Courier", Font.BOLD, 18));
		label.setBackground(Color.WHITE);
		label.setForeground(Color.BLACK);
		label.setOpaque(true);
		label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.LIGHT_GRAY));
	}

}
