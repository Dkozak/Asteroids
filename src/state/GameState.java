package state;
import java.awt.Graphics2D;
/**
 *
 * @author Dimitri
 */
public abstract class GameState
{
  protected GameStateManager gsm;
  
  protected GameState(GameStateManager gsm)
  {
    this.gsm = gsm;
  }
  
  public abstract void init();
  
  public abstract void update(float paramFloat);
  
  public abstract void draw(Graphics2D paramGraphics2D);
}
