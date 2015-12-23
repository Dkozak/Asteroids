package item;
import java.util.ArrayList;
import main.Colors;
import main.JukeBox;
/**
 *
 * @author Dimitri
 */
public class RampageUfo extends Ufo{
  private int numBullets;
  private Speler player;
  
  public RampageUfo(Speler target)
  {
    //Eigenschappen van de rampage ufo
    super(target);
    this.player = target;
    this.firerate = (float)1.2;
    this.numBullets = 3;
    this.lives = 2;
    this.size = 100;
    this.points = 500;
    
    setShape();
    this.shape.setColor(Colors.RED);
  }
  
  protected void shoot()
  {
    //Instellingen kogel
    float angle = (float)Math.toDegrees(Math.atan2(this.target.getY() - this.y, this.target.getX() - this.x));
    float shootangle = angle - (this.numBullets - 1) * 2;
    //3 kogels vuren
    for (int i = 0; i < this.numBullets; i++)
    {
      this.bullets.add(new Kogel(this.x, this.y, shootangle, this.bulletspeed));
      shootangle += 4;
    }
    JukeBox.play("saucer_shoot.wav");
  }
  
  public void hit()
  {
    super.hit();
    if (this.lives < 1) {
      this.player.activateRampage();
    }
  }
}
