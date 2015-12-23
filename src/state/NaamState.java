package state;
import java.awt.*;
import java.awt.geom.*;
import main.Game;
import main.GameKeys;
import main.Colors;
import main.JukeBox;
import main.ControlLabel;
import savegame.SaveGameManager;
/**
 *
 * @author Dimitri
 */
public class NaamState extends GameState{
  //Variabelen zoals max lengte naam en titels  
  public static final int NUM_CHARS = 10;
  private SaveGameManager sgm;
  private char[] name;
  private int position;
  private ControlLabel cl_enter;
  private ControlLabel cl_esc;
  public boolean fout = false;
  
  public NaamState(GameStateManager gsm, SaveGameManager sgm)
  {
    super(gsm);
    this.sgm = sgm;
    //Knoppen zetten, Icoon en bijschrift
    this.cl_enter = new ControlLabel("\u21b2", "Accept");
    this.cl_esc = new ControlLabel("ESC", "Cancel");
  }
  
  public void init()
  {
    this.name = new char[10];
    for (int i = 0; i < this.name.length; i++) {
      this.name[i] = ' ';
    }
    this.position = 0;
  }
  
  public void update(float delta)
  {
      //enter
    if (GameKeys.isPressed(5) && this.name[this.position] != ' ')
    {
      this.sgm.createNew(this.sgm.getCurrent(), new String(this.name).trim());
      //Play geluid afspelen
      JukeBox.play("play.wav");
      //Naar de loadscreenstate gaan
      this.gsm.switchState(2);
    }
    else if(GameKeys.isPressed(5) && this.name[this.position] == ' ')
    {
        this.fout = true;
        
    }
    //pijl down letter kiezen
    if (GameKeys.isPressed(3))
    {
      if (this.name[this.position] == ' ')
      {
        this.name[this.position] = 'z';
      }
      else if (this.name[this.position] <= 'a')
      {
        this.name[this.position] = ' ';
      }
      else
      {
        char[] arrc = this.name;
        int n = this.position;
        arrc[n] = (char)(arrc[n] - '\u0001');
      }
      //geluid afspelen
      JukeBox.play("menu_change.wav");
    }
    //pijl up en letter kiezen
    if (GameKeys.isPressed(0))
    {
      if (this.name[this.position] == ' ')
      {
        this.name[this.position] = 'a';
      }
      else if (this.name[this.position] >= 'z')
      {
        this.name[this.position] = ' ';
      }
      else
      {
        char[] arrc = this.name;
        int n = this.position;
        arrc[n] = (char)(arrc[n] + '\u0001');
      }
      //geluid afspelen
      JukeBox.play("menu_change.wav");
    }
    //pijl rechts en positie naar links gaan
    if (GameKeys.isPressed(2))
    {
      if ((this.name[this.position] != ' ') && (this.position < 9)) {
        this.position += 1;
      }
      //geluid afspelen
      JukeBox.play("menu_select.wav");
    }
    //pijl links en positie naar rechts gaan
    if (GameKeys.isPressed(1))
    {
      if (this.position > 0) {
        this.position -= 1;
      }
      //geluid afspelen
      JukeBox.play("menu_select.wav");
    }
    //Back en terug naar loadsavegame state.
    if (GameKeys.isPressed(6)) {
      this.gsm.switchState(2);
    }
  }
  
  public void draw(Graphics2D g)
  {
     if (this.fout=true)
     {
        g.setFont(Game.mainGameFont.deriveFont((float)25));
        g.setColor(Colors.RED);
        int lengte = (int)g.getFontMetrics().getStringBounds("Je naam mag niet leeg zijn!", g).getWidth();
        g.drawString("Je naam mag niet leeg zijn!", 960-lengte/2,300);
     }
    //Titel in het blauw 'vul je naam in'  
    g.setFont(Game.mainGameFont.deriveFont((float)96));
    g.setColor(Colors.BLUE);
    int width = (int)g.getFontMetrics().getStringBounds("Vul je naam in", g).getWidth();
    int x = 960 - width / 2;
    
    g.drawString("Vul je naam in", x, 200);
    
    g.setFont(Game.mainGameFont.deriveFont((float)70));
    
    int charWidth = g.getFontMetrics().getMaxAdvance();
    int charHeight = g.getFontMetrics().getHeight();
    x = 960 - this.name.length * charWidth / 2;
    int y = 540;
    for (int i = 0; i < this.name.length; i++)
    {
      if (i == this.position)
      {
        //Huidige positie onderlijnen met groen
        g.setColor(Colors.GREEN);
        g.fillRect(x - 3, y + charHeight / 2 - 4, charWidth - 6, 7);
      }
      else
      {
        //andere posities in het wit
        g.setColor(Color.WHITE);
        g.fillRect(x - 3, y + charHeight / 2, charWidth - 6, 5);
      }
      g.setColor(Color.WHITE);
      
      x += charWidth;
    }
    x = 960 - this.name.length * charWidth / 2;
    
    g.drawChars(this.name, 0, this.name.length, x, y);

    this.cl_enter.draw(g, 50, 1030);
    this.cl_esc.draw(g, 250, 1030);
  }
}

