package code;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**A JPanel with an image that slowly appears, then plays an audio clip.
 * 
 * @author Eoghan McCarroll
 * */

public class OverriddenPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**Audio player in order to play audio*/
	private AudioPlayer assassin;

	Image myImage = new ImageIcon(getClass().getResource("/wasted.jpg")).getImage().getScaledInstance(900,700,Image.SCALE_SMOOTH);

	
	/**Timer does steps in action performed every 20ms*/
  Timer timer = new Timer(20, this);
  private int sec=0;
  private float alpha = 0f;

  public OverriddenPanel() {
    super();
    assassin=new AudioPlayer("Gta 5 Death.wav");
		
  }
  
  
  /**Used to start the timer from outside this class*/
  public void startTimer(){
	  reset();
	  timer.start();
  }

  /**Used overridden paint method, which paints the picture with an alpha channel which becomes more visible as time goes on*/
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    System.out.println("Im Here");
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    g2d.drawImage(myImage, 0, 0, null);
  }

  /**Increases the value of the alpha param by .01f every step, and if it gets to 1f, it stops the timer.
   * After 10 steps it plays the audio clip.
   */
  public void actionPerformed(ActionEvent e) {
    alpha += 0.01f;
    if (alpha >= 1) {
      alpha = 1;
      timer.stop();
    }
    if(sec==10){
    	assassin.play();
    }
    sec++;
    repaint();
  }
  
  /**Stops the timer and resets sec and alpha to 0, and repaints the Panel.*/
public void reset() {
	timer.stop();
	sec=0;
	alpha=0f;
	repaint();
}
}