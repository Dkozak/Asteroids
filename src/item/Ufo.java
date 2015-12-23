package item;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import main.Colors;
import main.JukeBox;
/**
 *
 * @author Dimitri
 */
public class Ufo extends SpaceObject{
  //Variabelen instellen
  protected int size;
  protected SpaceObject target;
  protected ArrayList<Kogel> bullets;
  private int speed;
  protected int lives;
  protected float firerate = (float)0.8;
  private float shootTimer;
  protected int bulletspeed = 800;
  private int pathx;
  
  public Ufo(SpaceObject target)
  {
    //Standaard UFO instellingen
    super(0, 0);
    this.target = target;
    this.speed = 140;
    this.size = 40;
    this.points = 100;
    this.lives = 1;
    setShape();
    
    this.bullets = new ArrayList();
    
    Random r = new Random();
    if (r.nextBoolean())
    {
      this.x = 0;
      this.dx = (this.speed + r.nextInt(this.speed / 2) - this.speed / 4);
      this.pathx = (r.nextInt(300) + 200);
    }
    else
    {
      this.x = 1920;
      this.dx = (-this.speed + r.nextInt(this.speed / 2) - this.speed / 4);
      this.pathx = (1920 - r.nextInt(300) - 200);
    }
    this.dy = this.speed;
    this.y = r.nextInt(1080);
  }
  
  //Vorm geven
  protected void setShape()
  {
    float[] shapex = new float[6];
    float[] shapey = new float[6];
    
    shapex[0] = (-this.size / 2);
    shapey[0] = 0;
    shapex[1] = (-this.size / 4);
    shapey[1] = (-this.size / 4);
    shapex[2] = (this.size / 4);
    shapey[2] = (-this.size / 4);
    shapex[3] = (this.size / 2);
    shapey[3] = 0;
    shapex[4] = (this.size / 4);
    shapey[4] = (this.size / 4);
    shapex[5] = (-this.size / 4);
    shapey[5] = (this.size / 4);
    
    this.shape = new Polygon(this.x, this.y, shapex, shapey);
  }
  
  public void update(float delta)
  {
    move(delta);
    for (int i = 0; i < this.bullets.size(); i++)
    {
      Kogel b = (Kogel)this.bullets.get(i);
      
      b.update(delta);
      if (b.shouldRemove())
      {
        this.bullets.remove(i);
        i--;
      }
    }
    this.shootTimer += delta;
    if (this.shootTimer > 1 / this.firerate)
    {
      shoot();
      this.shootTimer -= 1 / this.firerate;
    }
  }
  //Kogel schieten en muziek spelen
  protected void shoot()
  {
    Random r = new Random();
    
    float angle = (float)Math.toDegrees(Math.atan2(this.target.getY() - this.y, this.target.getX() - this.x));
    angle += r.nextInt(20) - 10;
    this.bullets.add(new Kogel(this.x, this.y, angle, this.bulletspeed, 0.5F));
    
    JukeBox.play("saucer_shoot.wav");
  }
  
  public boolean canBeRemoved()
  {
    if (this.lives < 1) {
      return true;
    }
    if (this.dx > 0) {
      return this.x > 1920 + this.size / 2;
    }
    if (this.dx < 0) {
      return this.x < -this.size / 2;
    }
    return false;
  }
  
  public boolean checkKogelCollision(SpaceObject s)
  {
    for (int i = 0; i < this.bullets.size(); i++) {
      if (((Kogel)this.bullets.get(i)).intersects(s))
      {
        this.bullets.remove(i);
        return true;
      }
    }
    return false;
  }
  
  public void hit()
  {
    this.lives -= 1;
  }
  
  private void move(float delta)
  {
    this.x += this.dx * delta;
    this.y += this.dy * delta;
    if (this.y > 1080) {
      this.y = 0;
    }
    if (((this.dx < 0) && (this.x < this.pathx)) || ((this.dx > 0) && (this.x > this.pathx))) {
      this.dy = 0;
    }
    this.shape.setPosition(this.x, this.y);
  }
  
  public void draw(Graphics2D g)
  {
    super.draw(g);
    
    g.drawLine((int)this.x - this.size / 2, (int)this.y, (int)this.x + this.size / 2, (int)this.y);
    
    g.setColor(Colors.RED);
    for (int i = 0; i < this.bullets.size(); i++) {
      ((Kogel)this.bullets.get(i)).draw(g);
    }
  }
}

