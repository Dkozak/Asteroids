package main;
import java.awt.*;
import java.awt.geom.*;
/**
 *
 * @author Dimitri
 */
public abstract class MenuItem
{
  private String name;
  private int width;
  private int height;
  private Color color;
  
  public MenuItem(String name)
  {
    this.name = name;
    this.color = Color.WHITE;
  }
  
  public void draw(Graphics2D g, int x, int y)
  {
    g.setColor(this.color);
    this.width = ((int)g.getFontMetrics().getStringBounds(this.name, g).getWidth());
    this.height = (g.getFontMetrics().getHeight() * 2);
    g.drawString(this.name, x - this.width / 2, y);
  }
  
  public abstract void select();
  
  public int getWidth()
  {
    return this.width;
  }
  
  public int getHeight()
  {
    return this.height;
  }
  
  public void setColor(Color color)
  {
    this.color = color;
  }
}

