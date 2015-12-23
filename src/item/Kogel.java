package item;
import java.awt.Graphics2D;
/**
 *
 * @author Dimitri
 */
public class Kogel extends SpaceObject{
  private int speed;
  private float lifetime;
  private float lifetimer;
  
  public Kogel(float x, float y, float angle, int speed)
  {
   //Tijd en snelheid van de kogel
    super(x, y);
    this.speed = speed;
    this.lifetime = (float)1.2;
    this.dx = ((float)Math.cos(Math.toRadians(angle)) * speed);
    this.dy = ((float)Math.sin(Math.toRadians(angle)) * speed);
  }
  
  public Kogel(float x, float y, float angle, int speed, float lifetime)
  {
    super(x, y);
    
    this.speed = speed;
    this.lifetime = lifetime;
    this.dx = ((float)Math.cos(Math.toRadians(angle)) * speed);
    this.dy = ((float)Math.sin(Math.toRadians(angle)) * speed);
  }
  
  protected void setShape()
  {
    float[] shapex = new float[1];
    float[] shapey = new float[1];
    
    this.shape = new Polygon(this.x, this.y, shapex, shapey);
  }
  
  public void update(float delta)
  {
    this.lifetimer += delta;
    
    this.x += this.dx * delta;
    this.y += this.dy * delta;
    
    wrap();
    
    this.shape.setPosition(this.x, this.y);
  }
  
  public boolean shouldRemove()
  {
    return this.lifetimer > this.lifetime;
  }
  
  public void draw(Graphics2D g)
  {
    g.fillOval((int)this.x - 2, (int)this.y - 2, 4, 4);
  }
}
