package state;
import animatie.*;
import item.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import main.*;
import savegame.*;
/**
 *
 * @author Dimitri
 */
public class PlayState extends GameState{
  //Variabelen  
  private Speler player;
  private int score;
  private ArrayList<Asteroid> asteroids;
  private ArrayList<Ufo> saucers;
  private ArrayList<Explosie> explosions;
  private ArrayList<TekstAnimatie> textAnimations;
  private Hud hud;
  private float leveltimer;
  private float levelIncreaseTime;
  private int level;
  private float lastAsteroidTimer;
  private float asteroidSpawnTime;
  private float lastUfoTimer;
  private float saucerSpawnTime;
  private int rampageUfoLevelSpawn;
  private SaveGameManager sgm;
  
  public PlayState(GameStateManager gsm, SaveGameManager sgm)
  {
    super(gsm);
    this.sgm = sgm;
  }
  
  public void init()
  {
    JukeBox.stopLoops();
    //Spawnlocatie van de speler instellen
    this.player = new Speler(960, 540);
    //Nieuwe arraylists aanmaken
    this.asteroids = new ArrayList();
    this.saucers = new ArrayList();
    this.explosions = new ArrayList();
    this.textAnimations = new ArrayList();
    
    this.hud = new Hud();
    //Level = 0
    this.leveltimer = 0;
    this.levelIncreaseTime = 15;
    this.level = 1;
    //Het spawnen van asteroids en ufo's
    this.asteroidSpawnTime = 6;
    this.saucerSpawnTime = 20;
    this.lastAsteroidTimer = 0;
    this.lastUfoTimer = 0;
    
    this.score = 0;
    //RampageUfo spawnt in level 10
    this.rampageUfoLevelSpawn = 10 ;
    
    initSpeler();
  }
  
  private void initSpeler()
  {
    //Eigenschappen speler instellen
    this.player.setAcceleration(Speler.ACCELERATION);
    this.player.setKogelspeed(Speler.BULLETSPEED);
    this.player.setNumKogels(Speler.NUM_BULLETS_PER_SHOT);
    this.player.setFirerate(Speler.FIRERATE);
    this.player.setMaxSpeed(Speler.MAX_SPEED);
    this.player.setRotationspeed(Speler.ROTATIONSPEED);
    this.player.setSpecialAttackCD(Speler.SPECIAL_ATTACK_CD);
    this.player.setSpecialAttackNumKogels(Speler.SPECIAL_ATTACK_NUM_BULLETS);
    this.player.setSpecialOnDeath(Speler.SPECIAL_ATTACK_ON_DEATH);
    this.player.setKogelLifeTime(Speler.BULLET_LIFETIME);
  }
  
  public void update(float delta)
  {
    //Als we op Escape drukken dan gaan we naar de vorige state
    if (GameKeys.isPressed(6))
    {
      JukeBox.stopLoops();
      JukeBox.loop("music.wav");
      
      this.gsm.switchState(3);
    }
    //Als we UP ingedrukt houden dan gaan we vooruit
    if (GameKeys.isDown(0)) {
      this.player.accelerate(true);
    } else {
      this.player.accelerate(false);
    }
    this.lastAsteroidTimer += delta;
    this.lastUfoTimer += delta;
    this.leveltimer += delta;
    if (this.leveltimer > this.levelIncreaseTime)
    {
      this.level += 1;
      if (this.level % this.rampageUfoLevelSpawn == 0) {
        this.saucers.add(new RampageUfo(this.player));
      }
      this.leveltimer -= this.levelIncreaseTime;
    }
    if (this.lastAsteroidTimer > this.asteroidSpawnTime)
    {
      for (int i = 0; i < this.level; i++) {
        spawnAsteroid();
      }
      this.lastAsteroidTimer -= this.asteroidSpawnTime;
    }
    if (this.lastUfoTimer > this.saucerSpawnTime)
    {
      for (int i = 0; i < (this.level + (float)0.5) / 2; i++) {
        if (Math.random() < (double)0.5) {
          this.saucers.add(new Ufo(this.player));
        } else {
          this.saucers.add(new GroteUfo());
        }
      }
      this.lastUfoTimer -= this.saucerSpawnTime;
    }
    this.player.update(delta);
    for (int i = 0; i < this.saucers.size(); i++)
    {
      Ufo s = (Ufo)this.saucers.get(i);
      
      s.update(delta);
      if ((s.checkKogelCollision(this.player)) && (!this.player.recentlyDied()))
      {
        hitSpeler();
        if (!this.player.isAlive()) {
          this.textAnimations.add(new TekstAnimatie(s.getX(), s.getY(), "GAME OVER", 2, Colors.RED, 64));
        }
      }
      if (this.player.checkKogelCollision(s))
      {
        s.hit();
        if (s.canBeRemoved())
        {
          this.saucers.remove(i);
          this.explosions.add(new Explosie(s.getX(), s.getY(), 20));
          i--;
          this.textAnimations.add(new TekstAnimatie(s.getX(), s.getY(), "AW!", 2, Colors.RED, 32));
          addScore(s.getPoints());
          this.textAnimations.add(new TekstAnimatie(s.getX(), s.getY() - 20, "+" + s.getPoints(), 1, Colors.YELLOW, 32));
        }
        else
        {
          this.explosions.add(new Explosie(s.getX(), s.getY(), 10));
          this.textAnimations.add(new TekstAnimatie(s.getX(), s.getY(), "Arrrrghhh", 2, Colors.RED, 32));
        }
      }
      else if (s.canBeRemoved())
      {
        this.saucers.remove(i);
        i--;
      }
    }
    for (int i = 0; i < this.asteroids.size(); i++)
    {
      Asteroid a = (Asteroid)this.asteroids.get(i);
      a.update(delta);
      if (this.player.checkKogelCollision(a))
      {
        this.asteroids.remove(i);
        splitAsteroid(a);
        addScore(a.getPoints());
        this.textAnimations.add(new TekstAnimatie(a.getX(), a.getY() - 20, "+" + a.getPoints(), 1, Colors.YELLOW));
      }
      else
      {
        if ((a.intersects(this.player)) && (!a.recentlySpawned()) && (!this.player.recentlyDied())) {
          hitSpeler();
        }
        for (int j = 0; j < this.saucers.size(); j++) {
          if (((Ufo)this.saucers.get(j)).checkKogelCollision(a))
          {
            this.asteroids.remove(i);
            splitAsteroid(a);
            break;
          }
        }
      }
    }
    for (int i = 0; i < this.explosions.size(); i++)
    {
      ((Explosie)this.explosions.get(i)).update(delta);
      if (((Explosie)this.explosions.get(i)).hasFinished())
      {
        this.explosions.remove(i);
        i--;
      }
    }
    for (int i = 0; i < this.textAnimations.size(); i++)
    {
      ((TekstAnimatie)this.textAnimations.get(i)).update(delta);
      if (((TekstAnimatie)this.textAnimations.get(i)).canBeRemoved())
      {
        this.textAnimations.remove(i);
        i--;
      }
    }
    this.hud.update();
    if (this.player.hasFinished()) {
      this.gsm.switchState(3);
    }
  }
  
  private void spawnAsteroid()
  {
    Random r = new Random();
    
    int x = r.nextInt(1920);
    int y = r.nextInt(1080);
    
    this.asteroids.add(new Asteroid(2, x, y));
    this.explosions.add(new Explosie(x, y, 20));
  }
  //Als een speler geraakt is doe: 
  private void hitSpeler()
  {
    if (this.player.isAlive())
    {
      this.player.hit();
      
      this.textAnimations.add(new TekstAnimatie(this.player.getX(), this.player.getY(), "-1", 2, Colors.RED, 32, Game.mainGameFont));
      if (!this.player.isAlive()) {
        dispose();
      }
    }
  }
  //Einde van het spel
  private void dispose()
  {
    this.sgm.checkScore(this.sgm.getCurrent().getName(), this.score);
    this.sgm.getCurrent().save();
    JukeBox.stopLoops();
    JukeBox.loop("music.wav");
  }
  //Asteroid splitsen
  private void splitAsteroid(Asteroid a)
  {
    int numParticles = 20 * (int)Math.pow(a.getType(), 2.0D) + 10;
    
    this.explosions.add(new Explosie(a.getX(), a.getY(), numParticles));
    if (a.getType() == 2)
    {
      this.textAnimations.add(new TekstAnimatie(a.getX(), a.getY() + 40, "KaBooom", 0.7F, Colors.RED, 28));
      JukeBox.play("asteroid_explosion_large.wav");
    }
    else if (a.getType() == 1)
    {
      JukeBox.play("asteroid_explosion_medium.wav");
    }
    else if (a.getType() <= 0)
    {
      JukeBox.play("asteroid_explosion_small.wav");
      return;
    }
    for (int i = 0; i < 2; i++) {
      this.asteroids.add(new Asteroid(a.getType() - 1, a.getX(), a.getY()));
    }
  }
  
  public void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 1920, 1080);
    
    g.setColor(Color.WHITE);
    this.player.draw(g);
    for (int i = 0; i < this.asteroids.size(); i++) {
      ((Asteroid)this.asteroids.get(i)).draw(g);
    }
    for (int i = 0; i < this.saucers.size(); i++) {
      ((Ufo)this.saucers.get(i)).draw(g);
    }
    for (int i = 0; i < this.explosions.size(); i++) {
      ((Explosie)this.explosions.get(i)).draw(g);
    }
    for (int i = 0; i < this.textAnimations.size(); i++) {
      ((TekstAnimatie)this.textAnimations.get(i)).draw(g);
    }
    this.hud.draw(g);
  }
  
  private void addScore(int i)
  {
    if (!this.player.hasFinished()) {
      this.score += i;
    }
  }
  //HUD rechts boven configureren
  private class Hud
  {
    item.Polygon playerShape;
    ExpBar specialProgressBar;
    
    public Hud()
    {
      //Aantal levens en expbar laten zien
      this.playerShape = new Speler(0, 0).getShape();
      this.playerShape.rotate(270);
      this.specialProgressBar = new ExpBar(PlayState.this.player.getSpecialAttackCD(), 240, 20);
      this.specialProgressBar.setText("Press Space!");
      update();
    }
    
    public void update()
    {
      this.specialProgressBar.setProgress(PlayState.this.player.getSpecialAttackTimer());
      this.specialProgressBar.setMax(PlayState.this.player.getSpecialAttackCD());
    }
    
    public void draw(Graphics2D g)
    {
      g.setFont(Game.mainGameFont.deriveFont((float)32));   
      g.setColor(Colors.YELLOW);
      g.drawString("Score: " + PlayState.this.score, 20, 100);
      for (int i = 0; i < PlayState.this.player.getLives(); i++) {
        this.playerShape.draw(g, 30 + i * 32, 30);
      }
      g.setFont(Game.mainGameFont.deriveFont((float)14));
      this.specialProgressBar.draw(g, 20, 120);
    }
  }
}

