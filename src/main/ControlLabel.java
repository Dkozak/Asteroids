package main;
import java.awt.*;
/**
 *
 * @author Dimitri
 */
//Knoppen als toetsen laten zien
public class ControlLabel
{
  private String buttonText;
  private String description;
  private int size;
  private Font font;
  
  public ControlLabel(String buttonText, String description)
  {
    this.buttonText = buttonText;
    this.description = description;
    this.size = 48;
    this.font = new Font("Lucida Sans Regular", 20, 20);
  }
  
  public void draw(Graphics2D g, int x, int y)
  {
    g.setFont(this.font);
    g.setColor(Color.WHITE);
    g.fillRoundRect(x - this.size / 2, y - this.size / 2, this.size, this.size, 10, 10);
    
    int bx = x - (int)g.getFontMetrics().getStringBounds(this.buttonText, g).getWidth() / 2;
    int by = y + g.getFontMetrics().getDescent();
    
    g.setColor(Color.DARK_GRAY);
    g.drawString(this.buttonText, bx, by);
    
    g.setColor(Color.LIGHT_GRAY);
    g.drawString(this.description, x + this.size, by);
  }
}

