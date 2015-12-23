package state;
import java.awt.*;
import java.awt.geom.*;
import main.Game;
import main.GameKeys;
import main.Colors;
import main.ControlLabel;
import main.Menu;
import main.MenuItem;
import savegame.SaveGame;
import savegame.SaveGameManager;
/**
 *
 * @author Dimitri
 */
public class LoadSaveGameState extends GameState {
  private SaveGameManager sgm;
  private String title;
  private Color titleColor;
  private Menu menu;
  private ControlLabel cl_del;
  
  public LoadSaveGameState(GameStateManager gsm, SaveGameManager sgm)
  {
    //Controllabels zetten
    super(gsm);
    this.sgm = sgm;
    
    this.title = "Load Game";
    this.titleColor = Colors.BLUE;
    
    this.cl_del = new ControlLabel("DEL", "Delete Savegame");
    
    this.menu = new Menu();
  }
  
  public void init()
  {
    //Menu opvullen
    this.menu.clear();
    for (int i = 0; i < 4; i++) {
      addSavegameToMenu(i);
    }
    MenuItem back = new MenuItem("Back")
    {
      public void select()
      {
        LoadSaveGameState.this.gsm.switchState(0);
      }
    };
    back.setColor(Colors.RED);
    
    this.menu.add(back);
    
    this.menu.init();
  }
  
  private void addSavegameToMenu(final int i)
  {
    this.menu.add(new MenuItem(this.sgm.get(i).getName())
    {
      public void select()
      {
        LoadSaveGameState.this.sgm.select(i, LoadSaveGameState.this.gsm);
      }
    });
  }
  //Savegame verwijderen
  public void update(float delta)
  {
    this.menu.update(delta);
    if (GameKeys.isPressed(7)) {
      if (this.menu.getPosition() < 3)
      {
        this.sgm.get(this.menu.getPosition()).clear();
        init();
      }
    }
  }
  //Fonts instellen en tekenen
  public void draw(Graphics2D g)
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 1920, 1080);
    
    g.setFont(Game.mainGameFont.deriveFont((float)96));
    g.setColor(this.titleColor);
    
    int width = (int)g.getFontMetrics().getStringBounds(this.title, g).getWidth();
    int height = g.getFontMetrics().getHeight() * 2;
    int x = 960 - width / 2;
    
    g.drawString(this.title, x, 200);
    
    this.menu.draw(g, x + width / 2, 200 + height);
    
    this.cl_del.draw(g, 50, 1030);
  }
}

