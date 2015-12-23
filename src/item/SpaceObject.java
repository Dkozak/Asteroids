package item;
import java.awt.Graphics2D;
/**
 *
 * @author Dimitri
 */
public abstract class SpaceObject
{
  protected float x;
  protected float y;
  protected float dx;
  protected float dy;
  protected Polygon shape;
  protected int points;
  protected float angle;
  protected float rotationSpeed = 180;
  
  protected abstract void setShape();
  
  public abstract void update(float paramFloat);
  
  protected SpaceObject(float x, float y)
  {
    this.x = x;
    this.y = y;
    setShape();
  }
  
  public void wrap()
  {
    if (this.x < 0) {
      this.x = 1920;
    } else if (this.x > 1920) {
      this.x = 0;
    }
    if (this.y < 0) {
      this.y = 1080;
    } else if (this.y > 1080) {
      this.y = 0;
    }
  }
  
  public boolean intersects(SpaceObject o)
  {
    return this.shape.intersects(o.getShape());
  }
  
  public void draw(Graphics2D g)
  {
    this.shape.draw(g);
  }
  
  public void setPosition(float x, float y)
  {
    this.x = x;
    this.y = y;
    this.shape.setPosition(x, y);
  }
  
  public Polygon getShape()
  {
    return this.shape;
  }
  
  public float getAngle()
  {
    return this.angle;
  }
  
  public float getX()
  {
    return this.x;
  }
  
  public float getY()
  {
    return this.y;
  }
  
  public int getPoints()
  {
    return this.points;
  }
}

