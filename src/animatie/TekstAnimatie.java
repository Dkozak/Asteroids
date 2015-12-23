package animatie;
import java.awt.*;
/**
 *
 * @author Dimitri
 */
//Verschillende tekstanimaties defigneren zodat deze gebruikt kunnen worden in andere delen van het project
public class TekstAnimatie {
   
  private float x;
  private float y;
  private String text;
  private float time;
  private float timer;
  private float speed = 100;
  private Color color;
  private Font font;
  private static final Font DEFAULT_FONT = new Font("Monospaced", 0, 24);
  
  public TekstAnimatie(float x, float y, String text, float time)
  {
    this.x = x;
    this.y = y;
    this.text = text;
    this.time = time;
    this.color = Color.WHITE;
    this.font = DEFAULT_FONT;
  }
  
  public TekstAnimatie(float x, float y, String text, float time, Color color)
  {
    this.x = x;
    this.y = y;
    this.text = text;
    this.time = time;
    this.color = color;
    this.font = DEFAULT_FONT;
  }
  
  public TekstAnimatie(float x, float y, String text, float time, Color color, float size)
  {
    this.x = x;
    this.y = y;
    this.text = text;
    this.time = time;
    this.color = color;
    this.font = DEFAULT_FONT.deriveFont(size);
  }
  
  public TekstAnimatie(float x, float y, String text, float time, Color color, float size, Font font)
  {
    this.x = x;
    this.y = y;
    this.text = text;
    this.time = time;
    this.color = color;
    this.font = font.deriveFont(size);
  }
  
  public void update(float delta)
  {
    this.timer += delta;
    this.y -= this.speed * delta;
  }
  
  public void draw(Graphics2D g)
  {
    int transparency = 255 - (int)(this.timer * 255.0F / this.time);
    if (transparency < 0) {
      transparency = 0;
    }
    g.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), transparency));
    g.setFont(this.font);
    
    int x = (int)(this.x - g.getFontMetrics().getStringBounds(this.text, g).getWidth() / 2.0D);
    
    g.drawString(this.text, x, this.y);
  }
  
  public boolean canBeRemoved()
  {
    return this.timer > this.time;
  }
}
