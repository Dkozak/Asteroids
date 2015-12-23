package item;
import java.awt.*;
import java.awt.geom.*;
/**
 *
 * @author Dimitri
 */
public class Polygon {
  private float x;
  private float y;
  private float[] shapex;
  private float[] shapey;
  private final float[] pointsx;
  private final float[] pointsy;
  private float angle;
  private Color color;
  private float scalex;
  private float scaley;
  
  public Polygon(float x, float y, float[] shapex, float[] shapey)
  {
    this.shapex = shapex;
    this.shapey = shapey;
    this.pointsx = new float[shapex.length];
    this.pointsy = new float[shapey.length];
    
    this.scalex = 1;
    this.scaley = 1;
    
    this.color = Color.WHITE;
    for (int i = 0; i < shapex.length; i++)
    {
      this.pointsx[i] = shapex[i];
      this.pointsy[i] = shapey[i];
    }
    this.x = x;
    this.y = y;
    this.angle = 0;
    updateShape();
  }
  
  public void rotate(float angle)
  {
    this.angle = angle;
    updateShape();
  }
  
  private void updateShape()
  {
    AffineTransform at = new AffineTransform();
    at.setToRotation(Math.toRadians(this.angle));
    for (int i = 0; i < this.pointsx.length; i++)
    {
      Point2D p = new Point2D.Float(this.pointsx[i] * this.scalex, this.pointsy[i] * this.scaley);
      
      p = at.transform(p, null);
      
      this.shapex[i] = (this.x + (float)p.getX());
      this.shapey[i] = (this.y + (float)p.getY());
    }
  }
  
  public void draw(Graphics2D g)
  {
    g.setColor(this.color);
    
    int i = 0;
    for (int j = this.shapex.length - 1; i < this.shapex.length; j = i++) {
      g.drawLine((int)this.shapex[i], (int)this.shapey[i], (int)this.shapex[j], (int)this.shapey[j]);
    }
  }
  
  public void draw(Graphics2D g, int x, int y)
  {
    g.setColor(this.color);
    
    int i = 0;
    for (int j = this.shapex.length - 1; i < this.shapex.length; j = i++) {
      g.drawLine((int)(this.shapex[i] - this.x) + x, (int)(this.shapey[i] - this.y) + y, (int)(this.shapex[j] - this.x) + x, (int)(this.shapey[j] - this.y) + y);
    }
  }
  
  public void scale(float x, float y)
  {
    this.scalex = x;
    this.scaley = y;
    updateShape();
  }
  
  public void setPosition(float x, float y)
  {
    this.x = x;
    this.y = y;
    updateShape();
  }
  
  public boolean contains(float x, float y)
  {
    boolean b = false;
    int i = 0;
    for (int j = this.shapex.length - 1; i < this.shapex.length; j = i++) {
      if ((this.shapey[i] > y ? 1 : 0) != (this.shapey[j] > y ? 1 : 0)) {
        if (x < (this.shapex[j] - this.shapex[i]) * (y - this.shapey[i]) / (this.shapey[j] - this.shapey[i]) + this.shapex[i]) {
          b = !b;
        }
      }
    }
    return b;
  }
  
  public boolean intersects(Polygon s)
  {
    for (int i = 0; i < this.shapex.length; i++) {
      if (s.contains(this.shapex[i], this.shapey[i])) {
        return true;
      }
    }
    return false;
  }
  
  public void setColor(Color color)
  {
    this.color = color;
  }
  
  public float getX()
  {
    return this.x;
  }
  
  public float getY()
  {
    return this.y;
  }
}

