package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class ExitListener is applied to the "End" menu item in the File menu. When the "End" menu item 
 * in the File menu is selected, the Game closes and ends.
 * 
 * @author winnie
 *
 */
public class ExitListener implements ActionListener{

	/**
	 * The game ends.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

}
