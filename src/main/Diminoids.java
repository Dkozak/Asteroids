package main;
import javax.swing.JFrame;
/**
 *
 * @author Dimitri
 */
public class Diminoids {
    
 public static void main(String[] args)
  {
    //JFrame maken en instellen  
    JFrame frame = new JFrame("Diminoids");
    //Exit game als op kruisje wordt gedrukt
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Volledig scherm gebruiken
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    Game game = new Game();
    game.init();
    frame.add(game, "Center");
    frame.pack();
    frame.setVisible(true);
  }
}

