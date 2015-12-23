package item;
import java.awt.*;
import java.util.ArrayList;
import main.GameKeys;
import main.Colors;
import main.JukeBox;

/**
 *
 * @author Dimitri
 */
public class Speler extends SpaceObject {
  //Instellingen van de speler
  public static final float ACCELERATION = 400;
  public static final int MAX_SPEED = 600;
  public static final float ROTATIONSPEED = 200;
  public static final float FIRERATE = (float)1.2;
  public static final int BULLETSPEED = 800;
  public static final float SPECIAL_ATTACK_CD = 40;
  public static final int SPECIAL_ATTACK_NUM_BULLETS = 100;
  public static final int NUM_BULLETS_PER_SHOT = 1;
  public static final boolean SPECIAL_ATTACK_ON_DEATH = false;
  public static final float BULLET_LIFETIME = 1;
  private float acceleration;
  private boolean accelerate;
  private float friction;
  private int maxSpeed;
  private float firerate;
  private float shootTimer;
  private int bulletspeed;
  private float bulletLifeTime;
  private int numKogels;
  private final float spawnPosx;
  private final float spawnPosy;
  private int lives;
  public float length;
  private Flame flame;
  private ArrayList<Kogel> bullets = new ArrayList();
  private float specialAttackCD;
  private float specialAttackTimer;
  private float lastDeadTimer;
  private float spawnTime;
  private int numExplosionKogels;
  private boolean specialOnDeath;
  private boolean spawning;
  private float spawnTransparency;
  private int spawnStatus;
  private Rampage rampage;
  
  //Spawnpositie van de speler
  public Speler(float x, float y)
  {
    super(x, y);
    
    this.spawnPosx = x;
    this.spawnPosy = y;
    this.lives = 3;
    this.friction = 50;
    this.lastDeadTimer = 0;
    this.spawnTime = 3;
    this.length = 20;
    this.rampage = new Rampage(5);
    setShape();
  }
  
  //Vorm van het schip maken
  protected void setShape()
  {
    float[] shapex = new float[4];
    float[] shapey = new float[4];
    
    shapex[0] = this.length;
    shapey[0] = 0;
    
    shapex[1] = (-this.length);
    shapey[1] = (-this.length / 2);
    
    shapex[2] = (-this.length * 3 / 5);
    shapey[2] = 0;
    
    shapex[3] = (-this.length);
    shapey[3] = (this.length / 2);
    
    this.shape = new Polygon(this.x, this.y, shapex, shapey);
    this.shape.rotate(this.angle);
    
    this.flame = new Flame(this);
  }
  
  public void update(float delta)
  {
    this.specialAttackTimer += delta;
    
    this.flame.update();
    if ((!this.spawning) && (isAlive()))
    {
      if (this.accelerate)
      {
        this.dx = ((float)(this.dx + Math.cos(Math.toRadians(this.angle)) * this.acceleration * delta));
        this.dy = ((float)(this.dy + Math.sin(Math.toRadians(this.angle)) * this.acceleration * delta));
      }
      float vec = (float)Math.sqrt(this.dx * this.dx + this.dy * this.dy);
      if (vec > 0)
      {
        this.dx -= this.dx / vec * this.friction * delta;
        this.dy -= this.dy / vec * this.friction * delta;
      }
      //Naar links gaan
      if (GameKeys.isDown(1))
      {
        this.angle -= this.rotationSpeed * delta;
        this.shape.rotate(this.angle);
      }
      //Naar rechts gaan
      if (GameKeys.isDown(2))
      {
        this.angle += this.rotationSpeed * delta;
        this.shape.rotate(this.angle);
      }
      checkSpeed();
      
      this.x += this.dx * delta;
      this.y += this.dy * delta;
      
      wrap();
      
      this.shape.setPosition(this.x, this.y);
      
      this.lastDeadTimer += delta;
      this.shootTimer += delta;
      if (this.shootTimer >= 1 / this.firerate)
      {
        shoot();
        this.shootTimer -= 1 / this.firerate;
      }
      this.rampage.update(delta);
      //Kijken of we de speciale attack kunnen uitvoeren als we op spatiebalk drukken
      if ((GameKeys.isPressed(4)) && (this.specialAttackTimer > this.specialAttackCD))
      {
        createKogelExplosion(this.numExplosionKogels);
        this.specialAttackTimer = 0;
      }
    }
    else if (this.spawning)
    {
      this.spawnTransparency += 100 * this.spawnStatus * delta;
      if ((this.spawnStatus == -1) && (this.spawnTransparency <= 0))
      {
        this.spawnTransparency = 0;
        this.spawnStatus = 1;
      }
      else if (this.spawnStatus == 1)
      {
        if (!isAlive())
        {
          this.spawning = false;
          return;
        }
        respawn();
        this.angle = 0;
        this.shape.rotate(this.angle);
        this.dx = 0;
        this.dy = 0;
        if (this.spawnTransparency >= 254)
        {
          this.spawning = false;
          this.shape.setColor(Color.WHITE);
          return;
        }
      }
      this.shape.setColor(new Color(Colors.RED.getRed(), Colors.RED.getGreen(), Colors.RED.getBlue(), (int)this.spawnTransparency));
    }
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
  }
  
  public void hit()
  {
    this.lives -= 1;
    if (this.specialOnDeath) {
      createKogelExplosion(this.numExplosionKogels);
    }
    this.spawning = true;
    this.spawnStatus = -1;
    this.spawnTransparency = 255;
    this.lastDeadTimer = 0;
    
    JukeBox.play("player_hit.wav");
  }
  
  private void respawn()
  {
    this.x = this.spawnPosx;
    this.y = this.spawnPosy;
    this.shape.setPosition(this.x, this.y);
  }
  
  private void shoot()
  {
    float shootangle = this.angle - (this.numKogels - 1) * 2;
    for (int i = 0; i < this.numKogels; i++)
    {
      this.bullets.add(new Kogel(this.x, this.y, shootangle, this.bulletspeed));
      shootangle += 4;
    }
    JukeBox.play("player_shoot.wav");
  }
  
  public boolean recentlyDied()
  {
    return this.lastDeadTimer < this.spawnTime;
  }
  
  private void checkSpeed()
  {
    float speed = (float)Math.sqrt(this.dx * this.dx + this.dy * this.dy);
    if (speed > this.maxSpeed)
    {
      this.dx = (this.dx / speed * this.maxSpeed);
      this.dy = (this.dy / speed * this.maxSpeed);
    }
  }
  
  public void draw(Graphics2D g)
  {
    super.draw(g);
    if ((recentlyDied()) && (!this.spawning)) {
      g.setColor(Colors.WHITE_TRANSPARENT_20);
    }
    for (int i = -(int)this.length; i < (int)this.length; i++) {
      for (int k = -(int)this.length; k < (int)this.length; k++) {
        if (this.shape.contains(this.x + i, this.y + k)) {
          g.fillRect((int)(this.x + i), (int)(this.y + k), 1, 1);
        }
      }
    }
    if ((this.accelerate) && (!this.spawning)) {
      this.flame.draw(g);
    }
    g.setColor(Color.WHITE);
    for (int i = 0; i < this.bullets.size(); i++) {
      ((Kogel)this.bullets.get(i)).draw(g);
    }
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
  
  private void createKogelExplosion(int num)
  {
    for (int i = 0; i < num; i++) {
      this.bullets.add(new Kogel(this.x, this.y, i * 360 / num, this.bulletspeed, this.bulletLifeTime));
    }
    JukeBox.play("special_attack.wav");
  }
  
  public void accelerate(boolean b)
  {
    this.accelerate = b;
  }
  
  public void stop()
  {
    this.dx = 0;
    this.dy = 0;
    accelerate(false);
  }
  
  public void activateRampage()
  {
    this.rampage.activate();
  }
  
  public void setPosition(float x, float y)
  {
    super.setPosition(x, y);
    this.flame.update();
  }
  
  public boolean hasFinished()
  {
    return (!isAlive()) && (!this.spawning) && (this.bullets.size() <= 0);
  }
  
  public int getLives()
  {
    return this.lives;
  }
  
  public float getSpecialAttackTimer()
  {
    return this.specialAttackTimer;
  }
  
  public float getSpecialAttackCD()
  {
    return this.specialAttackCD;
  }
  
  public boolean isAlive()
  {
    return this.lives > 0;
  }
  
  public void setAcceleration(float f)
  {
    this.acceleration = f;
  }
  
  public void setMaxSpeed(int f)
  {
    this.maxSpeed = f;
  }
  
  public void setRotationspeed(float f)
  {
    this.rotationSpeed = f;
  }
  
  public void setFirerate(float f)
  {
    this.firerate = f;
  }
  
  public void setKogelspeed(int f)
  {
    this.bulletspeed = f;
  }
  
  public void setSpecialAttackCD(float f)
  {
    this.specialAttackCD = f;
  }
  
  public void setSpecialAttackNumKogels(int f)
  {
    this.numExplosionKogels = f;
  }
  
  public void setNumKogels(int f)
  {
    this.numKogels = f;
  }
  
  public void setSpecialOnDeath(boolean b)
  {
    this.specialOnDeath = b;
  }
  
  public float getKogelLifeTime()
  {
    return this.bulletLifeTime;
  }
  
  public void setKogelLifeTime(float f)
  {
    this.bulletLifeTime = f;
  }
  
  public boolean isSpawning()
  {
    return this.spawning;
  }
  
  public int getLength()
  {
    return (int)this.length;
  }
  
  private class Flame
  {
    private Speler player;
    private Polygon shape;
    
    public Flame(Speler player)
    {
      this.player = player;
      init();
    }
    
    private void init()
    {
      float[] shapex = new float[3];
      float[] shapey = new float[3];
      
      shapex[0] = (-this.player.length / 5);
      shapey[0] = (-this.player.length / 5);
      
      shapex[1] = (-this.player.length / 5);
      shapey[1] = (this.player.length / 5);
      
      shapex[2] = (this.player.length / 3);
      shapey[2] = 0;
      
      this.shape = new Polygon(0, 0, shapex, shapey);
      update();
    }
    
    public void update()
    {
      float x = this.player.getX() - (float)Math.cos(Math.toRadians(this.player.getAngle())) * this.player.length;
      float y = this.player.getY() - (float)Math.sin(Math.toRadians(this.player.getAngle())) * this.player.length;
      
      this.shape.setPosition(x, y);
      this.shape.rotate(this.player.getAngle() + 180);
    }
    
    public void draw(Graphics2D g)
    {
      this.shape.draw(g);
    }
  }
  
  private class Rampage
  {
    private boolean active;
    private float time;
    private float timer;
    private int preNumKogels;
    private float preFirerate;
    
    public Rampage(float time)
    {
      this.time = time;
    }
    
    public void update(float delta)
    {
      if (this.active)
      {
        this.timer += delta;
        if (this.timer > this.time)
        {
          this.timer = 0;
          this.active = false;
          Speler.this.numKogels = this.preNumKogels;
          Speler.this.firerate = this.preFirerate;
          Speler.this.shape.setColor(Color.WHITE);
          Speler.this.flame.shape.setColor(Color.WHITE);
        }
      }
    }
    
    public void activate()
    {
      this.active = true;
      this.preNumKogels = Speler.this.numKogels;
      this.preFirerate = Speler.this.firerate;
      Speler.this.shape.setColor(Colors.RED);
      Speler.this.flame.shape.setColor(Colors.RED);
      
      Speler.this.numKogels = 5;
      Speler.this.firerate = 10;
    }
  }
}

