package item;
import java.awt.Color;
import java.util.Random;
/**
 *
 * @author Dimitri
 */
public class Asteroid extends SpaceObject{
  //Variabelen zoals ID, grootte, snelheid,...  
  public static final int SMALL = 0;
  public static final int MEDIUM = 1;
  public static final int LARGE = 2;
  private static final int[] SIZES = { 12, 20, 40 };
  private static final int[] NUM_POINTS = { 8, 10, 12 };
  private static final int[] MIN_SPEED = { 140, 100, 40 };
  private static final int[] MAX_SPEED = { 200, 120, 60 };
  private static final int[] POINTS = { 10, 25, 50 };
  private int type;
  private float spawnTimer;
  private float spawnTime = 2;
  
  public Asteroid(int type, float x, float y)
  {
    super(x, y);
    this.type = type;
    this.points = POINTS[type];
    setShape();
  }
  
  //Vorm geven
  protected void setShape()
  {
    Random r = new Random();
    
    float[] shapex = new float[NUM_POINTS[this.type]];
    float[] shapey = new float[NUM_POINTS[this.type]];
    
    float angle = 0;
    for (int i = 0; i < NUM_POINTS[this.type]; i++)
    {
      int dist = r.nextInt(SIZES[this.type]) + SIZES[this.type];
      
      shapex[i] = ((float)Math.cos(Math.toRadians(angle)) * dist);
      shapey[i] = ((float)Math.sin(Math.toRadians(angle)) * dist);
      angle += 360 / NUM_POINTS[this.type];
    }
    this.rotationSpeed = (r.nextInt(200) - 100);
    
    angle = r.nextInt(360);
    
    int speed = r.nextInt(MAX_SPEED[this.type] - MIN_SPEED[this.type]) + MIN_SPEED[this.type];
    
    this.dx = ((float)Math.cos(Math.toRadians(angle)) * speed);
    this.dy = ((float)Math.sin(Math.toRadians(angle)) * speed);
    
    this.shape = new Polygon(this.x, this.y, shapex, shapey);
    this.shape.setColor(new Color(255, 255, 255, 60));
  }
  
  public void update(float delta)
  {
    this.spawnTimer += delta;
    if (this.spawnTimer > this.spawnTime) {
      this.shape.setColor(Color.WHITE);
    }
    this.x += this.dx * delta;
    this.y += this.dy * delta;
    
    wrap();
    
    this.angle += this.rotationSpeed * delta;
    
    this.shape.setPosition(this.x, this.y);
    this.shape.rotate(this.angle);
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public float getX()
  {
    return this.x;
  }
  
  public float getY()
  {
    return this.y;
  }
  
  public boolean recentlySpawned()
  {
    return this.spawnTimer < this.spawnTime;
  }
}

