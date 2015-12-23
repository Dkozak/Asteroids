package animatie;
import java.awt.*;
/**
 *
 * @author Dimitri
 */
public class ExpBar {
  private float max;
  private float progress;
  private int width;
  private int height;
  private Color loadedColor;
  private Color loadingColor;
  private Color textColor;
  private String text;
  
  public ExpBar(float max, int width, int height)
  {
    this.max = max;
    this.progress = (float)0.0;
    this.width = width;
    this.height = height;
    this.text = "";
    
    this.loadedColor = new Color(255, 255, 255, 150);
    this.loadingColor = new Color(255, 255, 255, 50);
    this.textColor = Color.BLACK;
  }
  
  public void setProgress(float progress)
  {
    this.progress = progress;
  }
  
  public void setMax(float max)
  {
    this.max = max;
  }
  
  public void setText(String text)
  {
    this.text = text;
  }
  
  public void draw(Graphics2D g, int x, int y)
  {
    int w = (int)(this.progress / this.max * this.width);
    if (w > this.width)
    {
      w = this.width;
      g.setColor(this.loadedColor);
    }
    else
    {
      g.setColor(this.loadingColor);
    }
    g.fillRect(x, y, w, this.height);
    
    g.setColor(this.textColor);
    
    float textX = x + (float)(this.width / 2 - g.getFontMetrics().getStringBounds(this.text, g).getWidth() / 2.0D);
    float textY = y + (this.height / 2 + g.getFontMetrics().getHeight() / 2);
    if (w >= this.width) {
      g.drawString(this.text, textX, textY);
    }
  }
}

