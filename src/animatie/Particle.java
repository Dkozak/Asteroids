package animatie;
import java.awt.*;
import java.util.Random;
/**
 *
 * @author Dimitri
 */
public class Particle
{
  private float x;
  private float y;
  private float dx;
  private float dy;
  private int size;
  private Color color;
  private float speed;
  private float friction = 150;
  private boolean finished;
  private int transparency = 255;
  
  public Particle(float x, float y, float angle)
  {
    this.x = x;
    this.y = y;
    this.size = 2;
    
    Random r = new Random();
    this.speed = (r.nextInt(100) + 300);
    
    this.dx = ((float)Math.cos(Math.toRadians(angle)) * this.speed);
    this.dy = ((float)Math.sin(Math.toRadians(angle)) * this.speed);
    
    int rg = r.nextInt(55) + 200;
    
    this.color = new Color(rg, rg, r.nextInt(200));
  }
  
  public void update(double delta)
  {
    this.x = ((float)(this.x + this.dx * delta));
    this.y = ((float)(this.y + this.dy * delta));
    
    float vec = (float)Math.sqrt(this.dx * this.dx + this.dy * this.dy);
    this.transparency = ((int)(255 * (vec / this.speed)));
    if (this.transparency > 255) {
      this.transparency = 255;
    } else if (this.transparency < 0) {
      this.transparency = 0;
    }
    if (vec > 1.0F)
    {
      this.dx = ((float)(this.dx - this.dx / vec * this.friction * delta));
      this.dy = ((float)(this.dy - this.dy / vec * this.friction * delta));
    }
    else
    {
      this.finished = true;
    }
  }
  
  public boolean hasFinished()
  {
    return this.finished;
  }
  
  public void draw(Graphics2D g)
  {
    g.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.transparency));
    
    g.fillRect((int)this.x, (int)this.y, this.size, this.size);
  }
}

