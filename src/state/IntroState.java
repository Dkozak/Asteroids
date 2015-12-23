package state;
import java.awt.*;
import item.Asteroid;
import java.util.Random;
import main.Game;
import main.JukeBox;
import main.Menu;
import main.MenuItem;
/**
 *
 * @author Dimitri
 */
public class IntroState extends GameState{
 
  private Asteroid[] asteroids;
  private Color titleColor;
  private float r;
  private float g;
  private float b;
  private int[] colorChangeSpeed;
  private int[] colorChangeDirection;
  private Font font;
  private Menu menu;
  
  public IntroState(final GameStateManager gsm)
  {
    super(gsm);
    //font instellen
    this.font = Game.mainGameFont;
    this.asteroids = new Asteroid[10];
    this.titleColor = Color.BLACK;
    
    //variabelen instellen voor kleuren
    this.r = this.titleColor.getRed();
    this.g = this.titleColor.getGreen();
    this.b = this.titleColor.getBlue();
    
    //instellen hoe snel de kleur veranderd
    //40,50,60 => verschillende kleuren ipv wit-zwart
    this.colorChangeSpeed = new int[] { 40, 50, 60 };
    this.colorChangeDirection = new int[] { 1, 1, 1 };
    
    Random r = new Random();
    for (int i = 0; i < this.asteroids.length; i++)
    {
      this.asteroids[i] = new Asteroid(r.nextInt(3), r.nextInt(1920), r.nextInt(1080));
      this.asteroids[i].update(5);
    }
    //Menu opbouwen
    this.menu = new Menu();
    //fontsize voor menuitem instellen
    this.menu.setItemFont(this.font.deriveFont((float)64));
    
    //Menuitem "play" toevoegen en instellen dat hij naar state 2 gaat (loadsavegame)
    this.menu.add(new MenuItem("Play")
    {
      public void select()
      {
        gsm.switchState(2);
      }
    });
    //Menuitem "play" toevoegen en instellen dat hij afsluit
    this.menu.add(new MenuItem("Quit")
    {
      public void select()
      {
        System.exit(0);
      }
    });
    //achergrondmuziek
    JukeBox.loop("music.wav");
  }
  
  public void init()
  {
    this.menu.init();
  }
  //Note: Delta = #ms tussen frames (fps)
  public void update(float delta)
  {
    for (int i = 0; i < this.asteroids.length; i++) {
      this.asteroids[i].update(delta);
    }
    this.menu.update(delta);
    //Titel en menuitems van kleuren laten veranderen
    this.r += this.colorChangeDirection[0] * 1 * this.colorChangeSpeed[0] * delta;
    this.g += this.colorChangeDirection[1] * 1 * this.colorChangeSpeed[1] * delta;
    this.b += this.colorChangeDirection[2] * 1 * this.colorChangeSpeed[2] * delta;
    if (this.r < 0)
    {
      this.r = 0;
      this.colorChangeDirection[0] = (-this.colorChangeDirection[0]);
    }
    else if (this.r > 255)
    {
      this.r = 255;
      this.colorChangeDirection[0] = (-this.colorChangeDirection[0]);
    }
    if (this.g < 0)
    {
      this.g = 0;
      this.colorChangeDirection[1] = (-this.colorChangeDirection[1]);
    }
    else if (this.g > 255)
    {
      this.g = 255;
      this.colorChangeDirection[1] = (-this.colorChangeDirection[1]);
    }
    if (this.b < 0)
    {
      this.b = 0;
      this.colorChangeDirection[2] = (-this.colorChangeDirection[2]);
    }
    else if (this.b > 255)
    {
      this.b = 255;
      this.colorChangeDirection[2] = (-this.colorChangeDirection[2]);
    }
    //Kleur toewijzen aan titel en menu
    this.titleColor = new Color((int)this.r, (int)this.g, (int)this.b);
    this.menu.setItemColor(this.titleColor);
  }
  
  public void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 1920, 1080);
    for (int i = 0; i < this.asteroids.length; i++) {
      this.asteroids[i].draw(g);
    }
    //Titelkleur en size instellen
    g.setColor(this.titleColor);
    g.setFont(this.font.deriveFont((float)256));
    
    //ervoor zorgen dat de tekst in het midden staat
    int fontHeight = g.getFontMetrics().getAscent() * 2;
    int width = (int)g.getFontMetrics().getStringBounds("Diminoids", g).getWidth();
    int y = 540 - fontHeight / 2;
    int x = 960 - width / 2;
    
    g.drawString("Diminoids", x, y);
    g.setFont(this.font.deriveFont((float)25));
    int lengte = (int)g.getFontMetrics().getStringBounds("Made by Dimitri Kozakiewiez", g).getWidth();
    g.drawString("Made by Dimitri Kozakiewiez", 960-lengte/2, y+50);
    
    this.menu.draw(g, x + width / 2, y + fontHeight);
  }
}

