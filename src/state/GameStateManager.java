package state;
import java.awt.*;
import savegame.SaveGameManager;
/**
 *
 * @author Dimitri
 */
public class GameStateManager
{
  //variabelen (Volgorde) instellen
  public static final int INTRO = 0;
  public static final int PLAY = 1;
  public static final int LOADSAVEGAME = 2;
  public static final int PREPARE = 3;
  public static final int CHOOSENAME = 4;
  private GameState[] gameStates;
  private int current;
  private SaveGameManager sgm;
  private StateSwitcher switcher;
  private int fps;
  
  public GameStateManager()
  {
    this.sgm = new SaveGameManager();
    this.switcher = new StateSwitcher();
    initGameStates();
  }
  
  private void initGameStates()
  {
    //gamestates instellen
    this.gameStates = new GameState[5];
    this.gameStates[1] = new PlayState(this, this.sgm);
    this.gameStates[2] = new LoadSaveGameState(this, this.sgm);
    this.gameStates[3] = new PrepareState(this, this.sgm);
    this.gameStates[0] = new IntroState(this);
    this.gameStates[4] = new NaamState(this, this.sgm);
    
    setState(0);
  }
  
  public void switchState(int i)
  {
    this.switcher.switchState(i);
  }
  
  //Naar een andere staat gaan
  private void setState(int i)
  {
    this.gameStates[i].init();
    this.current = i;
  }
  
  public void update(float delta)
  {
    this.fps = ((int)(60 / delta));
    if (this.switcher.isActive()) {
      this.switcher.update(delta);
    }
    this.gameStates[this.current].update(delta);
  }
  
  public void draw(Graphics2D g)
  {
    this.gameStates[this.current].draw(g);
    if (this.switcher.isActive()) {
      this.switcher.draw(g);
    }
  }
  
  private class StateSwitcher
  {
    private boolean active;
    private int nextState;
    private int speed = 300;
    private Color color;
    private int dir;
    private int transparency;
    
    private StateSwitcher() {}
    
    public void switchState(int state)
    {
      this.nextState = state;
      this.color = new Color(0, 0, 0, this.transparency);
      this.active = true;
      this.dir = 1;
    }
    
    public void update(float delta)
    {
      this.transparency = ((int)(this.transparency + this.speed * this.dir * delta));
      if (this.transparency > 255)
      {
        this.transparency = 255;
        this.dir = -1;
        GameStateManager.this.setState(this.nextState);
      }
      else if (this.transparency < 0)
      {
        this.transparency = 0;
        this.active = false;
      }
      this.color = new Color(0, 0, 0, this.transparency);
    }
    
    public void draw(Graphics2D g)
    {
      g.setColor(this.color);
      g.fillRect(0, 0, 1920, 1080);
    }
    
    public boolean isActive()
    {
      return this.active;
    }
  }
}

